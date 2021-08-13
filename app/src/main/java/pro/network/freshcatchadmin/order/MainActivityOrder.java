package pro.network.freshcatchadmin.order;

import android.app.ProgressDialog;
import android.app.SearchManager;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.FileProvider;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.deishelon.roundedbottomsheet.RoundedBottomSheetDialog;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.google.gson.Gson;
import com.itextpdf.text.Document;
import com.itextpdf.text.PageSize;
import com.itextpdf.text.pdf.PdfWriter;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import pro.network.freshcatchadmin.R;
import pro.network.freshcatchadmin.app.AppController;
import pro.network.freshcatchadmin.app.Appconfig;
import pro.network.freshcatchadmin.app.HeaderFooterPageEvent;
import pro.network.freshcatchadmin.app.PdfConfig;
import pro.network.freshcatchadmin.product.Product;

import static pro.network.freshcatchadmin.app.Appconfig.ORDER_CHANGE_STATUS;
import static pro.network.freshcatchadmin.app.Appconfig.ORDER_GET_ALL;
import static pro.network.freshcatchadmin.app.Appconfig.UPDATE_WALLET;

public class MainActivityOrder extends AppCompatActivity implements OrderAdapter.ContactsAdapterListener, StatusListener {
    private static final String TAG = MainActivityOrder.class.getSimpleName();
    ProgressDialog progressDialog;
    Button loadMore;
    int offset = 0;
    LinkedHashMap<String, String> stringStringMap = new LinkedHashMap<>();
    private RecyclerView recyclerView;
    private List<Order> orderList;
    private OrderAdapter mAdapter;
    private SearchView searchView;
    private OrderAdapter deliverAdapter;
    private ArrayList<Order> deliveredList;
    private RecyclerView recycler_view_delivered;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mainorder);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        progressDialog = new ProgressDialog(this);
        progressDialog.setCancelable(false);

        // toolbar fancy stuff
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle(R.string.order);

        recyclerView = findViewById(R.id.recycler_view);
        orderList = new ArrayList<>();
        mAdapter = new OrderAdapter(this, orderList, this, this);

        recycler_view_delivered = findViewById(R.id.recycler_view_delivered);
        deliveredList = new ArrayList<>();
        deliverAdapter = new OrderAdapter(this, deliveredList, this, this);
        loadMore = findViewById(R.id.loadMore);

        // white background notification bar
        whiteNotificationBar(recyclerView);


        recyclerView.setItemAnimator(new DefaultItemAnimator());
        final LinearLayoutManager addManager1 = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(addManager1);
        recyclerView.setAdapter(mAdapter);

        recycler_view_delivered.setItemAnimator(new DefaultItemAnimator());
        final LinearLayoutManager deliManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        recycler_view_delivered.setLayoutManager(deliManager);
        recycler_view_delivered.setAdapter(deliverAdapter);

        loadMore.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                fetchContacts();
            }
        });
    }

    private void fetchContacts() {
        String tag_string_req = "req_register";
        progressDialog.setMessage("Processing ...");
        showDialog();
        StringRequest strReq = new StringRequest(Request.Method.POST,
                ORDER_GET_ALL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.d("Register Response: ", response);

                try {
                    JSONObject jObj = new JSONObject(response);
                    int success = jObj.getInt("success");
                    if (success == 1) {
                        JSONArray jsonArray = jObj.getJSONArray("data");
                        if (offset == 0) {
                            orderList = new ArrayList<>();
                            deliveredList = new ArrayList<>();
                        }
                        offset = offset + 1;
                        for (int i = 0; i < jsonArray.length(); i++) {
                            try {
                                JSONObject jsonObject = jsonArray.getJSONObject(i);
                                Order order = new Order();
                                order.setId(jsonObject.getString("id"));
                                order.setPrice(jsonObject.getString("price"));
                                order.setQuantity(jsonObject.getString("quantity"));
                                order.setStatus(jsonObject.getString("status"));
                                order.setItems(jsonObject.getString("items"));
                                order.setName(jsonObject.getString("name"));
                                order.setPhone(jsonObject.getString("phone"));
                                order.setAddress(jsonObject.getString("address"));
                                order.setReson(jsonObject.getString("reason"));
                                order.setAddressOrg(jsonObject.has("addressOrg") ?
                                        jsonObject.getString("addressOrg") : "NA");
                                order.setComments(jsonObject.has("comments") ?
                                        jsonObject.getString("comments") : "NA");
                                order.setPayment(jsonObject.getString("payment"));
                                order.setPaymentId(jsonObject.getString("paymentId"));
                                order.setToPincode(jsonObject.has("toPincode") ?
                                        jsonObject.getString("toPincode") : "NA");
                                order.setDelivery(jsonObject.has("delivery") ?
                                        jsonObject.getString("delivery") : "NA");
                                order.setDeliveryTime(jsonObject.has("deliveryTime") ?
                                        jsonObject.getString("deliveryTime") : "NA");
                                order.setGrandCost(jsonObject.getString("grandCost"));
                                order.setShipCost(jsonObject.getString("shipCost"));
                                order.setAmount(jsonObject.has("amount") ?
                                        jsonObject.getString("amount") : "NA");
                                order.setCreatedon(jsonObject.getString("createdon"));
                                order.setUser(jsonObject.getString("user"));
                                order.setCashback(jsonObject.getString("cashback"));
                                order.setTotalAmt(jsonObject.getString("totalAmt"));

                                ObjectMapper mapper = new ObjectMapper();
                                Object listBeans = new Gson().fromJson(jsonObject.getString("items"),
                                        Object.class);
                                ArrayList<Product> accountList = mapper.convertValue(
                                        listBeans,
                                        new TypeReference<ArrayList<Product>>() {
                                        }
                                );
                                order.setProductBeans(accountList);
                                if (order.getStatus().equalsIgnoreCase("ordered")) {
                                    orderList.add(order);
                                } else {
                                    deliveredList.add(order);
                                }
                            } catch (Exception e) {
                                Log.e("xxxxxxxx", e.toString());
                            }
                        }
                        mAdapter.notifyData(orderList);
                        deliverAdapter.notifyData(deliveredList);
                        getSupportActionBar().setSubtitle("Orders - " + orderList.size());

                    } else {
                        Toast.makeText(getApplication(), jObj.getString("message"), Toast.LENGTH_SHORT).show();
                    }
                } catch (JSONException e) {
                    Toast.makeText(getApplication(), "Some Network Error.Try after some time", Toast.LENGTH_SHORT).show();

                }
                hideDialog();

            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                hideDialog();
                // Log.e("Registration Error: ", error.getMessage());
                Toast.makeText(getApplication(),
                        "Some Network Error.Try after some time", Toast.LENGTH_LONG).show();
            }
        }) {
            protected Map<String, String> getParams() {
                HashMap localHashMap = new HashMap();
                localHashMap.put("offset", offset * 10 + "");
                if (getIntent().getStringExtra("status") != null) {
                    localHashMap.put("status", getIntent().getStringExtra("status"));
                }
                return localHashMap;
            }
        };
        strReq.setRetryPolicy(new DefaultRetryPolicy(
                50000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        strReq.setRetryPolicy(Appconfig.getPolicy());
        AppController.getInstance().addToRequestQueue(strReq, tag_string_req);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);

        // Associate searchable configuration with the SearchView
        SearchManager searchManager = (SearchManager) getSystemService(Context.SEARCH_SERVICE);
        searchView = (SearchView) menu.findItem(R.id.action_search)
                .getActionView();
        searchView.setSearchableInfo(searchManager
                .getSearchableInfo(getComponentName()));
        searchView.setMaxWidth(Integer.MAX_VALUE);

        // listening to search query text change
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                // filter recycler view when query submitted
                mAdapter.getFilter().filter(query);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String query) {
                // filter recycler view when text is changed
                mAdapter.getFilter().filter(query);
                return false;
            }
        });
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == android.R.id.home) {
            finish();
        }
        //noinspection SimplifiableIfStatement
        if (id == R.id.action_search) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        // close search view on back button pressed
        if (!searchView.isIconified()) {
            searchView.setIconified(true);
            return;
        }
        super.onBackPressed();
    }

    private void whiteNotificationBar(View view) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            int flags = view.getSystemUiVisibility();
            flags |= View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR;
            view.setSystemUiVisibility(flags);
            getWindow().setStatusBarColor(Color.WHITE);
        }
    }

    @Override
    public void onContactSelected(Order contact) {
       /* Intent intent = new Intent(MainActivityOrder.this, ProductUpdate.class);
        intent.putExtra("data", contact);
        startActivity(intent);*/
    }

    @Override
    protected void onStart() {
        super.onStart();
        fetchContacts();

    }

    @Override
    public void onDeliveredClick(String id) {
        statusChange(id, "Delivered", "Delivered by admin");
    }

    @Override
    public void onWhatsAppClick(String phone) {
        try {

            Intent intent = new Intent(Intent.ACTION_VIEW);
            intent.setData(Uri.parse("http://api.whatsapp.com/send?phone=91" + phone
                    + "&text=" + "Hi"));
            intent.setPackage("com.whatsapp.w4b");
            startActivity(intent);
        } catch (ActivityNotFoundException e) {
            Intent intent = new Intent(Intent.ACTION_VIEW);
            intent.setData(Uri.parse("http://api.whatsapp.com/send?phone=91" + phone
                    + "&text=" + "Hi"));
            intent.setPackage("com.whatsapp");
            startActivity(intent);
        }
    }

    @Override
    public void onCallClick(String phone) {
        Intent intent = new Intent(Intent.ACTION_DIAL);
        intent.setData(Uri.parse("tel:" + phone));
        startActivity(intent);
    }

    @Override
    public void onCancelClick(final String id) {

        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(MainActivityOrder.this);
        LayoutInflater inflater = MainActivityOrder.this.getLayoutInflater();
        final View dialogView = inflater.inflate(R.layout.alert_dialog, null);
        TextView title = dialogView.findViewById(R.id.title);
        final TextInputEditText reason = dialogView.findViewById(R.id.address);


        title.setText("* Do you want to cancel this order? If yes Order will be canceled.");
        dialogBuilder.setTitle("Alert")
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        if (reason.getText().toString().length() > 0) {
                            statusChange(id, "canceled", reason.getText().toString());
                            dialog.cancel();
                        } else {
                            Toast.makeText(getApplicationContext(), "Enter valid reason", Toast.LENGTH_SHORT).show();
                        }
                    }
                })
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.cancel();
                    }
                });
        dialogBuilder.setView(dialogView);
        final AlertDialog b = dialogBuilder.create();
        b.setCancelable(false);
        b.show();
    }

    @Override
    public void onInvoice(Order order) {
        printFunction(order);
    }

    @Override
    public void onProduct(Order order) {
        getToOrderPage(order);
    }

    @Override
    public void onWallet(Order order) {
        showCashBack(order);
    }


    private void getToOrderPage(Order orderId) {
        Intent intent = new Intent(MainActivityOrder.this, SingleOrderPage.class);
        intent.putExtra("data", orderId);
        intent.putExtra("from", "payment");
        startActivity(intent);
        finish();
    }

    private void statusChange(final String id, final String status, final String reason) {
        String tag_string_req = "req_register";
        showDialog();
        StringRequest strReq = new StringRequest(Request.Method.POST,
                ORDER_CHANGE_STATUS, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.d("Register Response: ", response);
                try {
                    JSONObject jObj = new JSONObject(response);
                    int success = jObj.getInt("success");
                    if (success == 1) {
                        offset = 0;
                        fetchContacts();
                    } else {
                        Toast.makeText(getApplication(), jObj.getString("message"), Toast.LENGTH_SHORT).show();
                    }
                } catch (JSONException e) {
                    Log.e("xxxxxxxxxxx", e.toString());
                    Toast.makeText(getApplication(), "Some Network Error.Try after some time", Toast.LENGTH_SHORT).show();
                }

            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e("Registration Error: ", error.getMessage());
                Toast.makeText(getApplication(),
                        "Some Network Error.Try after some time", Toast.LENGTH_LONG).show();
            }
        }) {
            protected Map<String, String> getParams() {
                HashMap localHashMap = new HashMap();
                localHashMap.put("id", id);
                localHashMap.put("status", status);
                localHashMap.put("reason", reason);
                return localHashMap;
            }
        };
        strReq.setRetryPolicy(Appconfig.getPolicy());
        AppController.getInstance().addToRequestQueue(strReq, tag_string_req);
    }

    public void printFunction(Order order) {
        try {
            String path = getExternalCacheDir().getPath() + "/PDF";
            File dir = new File(path);
            if (!dir.exists())
                dir.mkdirs();
            Log.d("PDFCreator", "PDF Path: " + path);
            File file = new File(dir, stringStringMap.get("Order Id") + "_" + System.currentTimeMillis() + ".pdf");
            if (file.exists()) {
                file.delete();
            }
            FileOutputStream fOut = new FileOutputStream(file);


            Document document = new Document(PageSize.A4, 30, 28, 40, 119);
            PdfWriter pdfWriter = PdfWriter.getInstance(document, fOut);

            document.open();
            PdfConfig.addMetaData(document);
            HeaderFooterPageEvent event = new HeaderFooterPageEvent();
            pdfWriter.setPageEvent(event);
            PdfConfig.addContent(document, order, MainActivityOrder.this);
            document.close();

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                Uri uri = FileProvider.getUriForFile(this, getPackageName() + ".provider", file);
                Intent intent = new Intent(Intent.ACTION_VIEW);
                intent.setData(uri);
                intent.setFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
                startActivity(intent);
            } else {
                Intent target = new Intent(Intent.ACTION_VIEW);
                target.setDataAndType(Uri.fromFile(file), "application/pdf");
                Intent intent = Intent.createChooser(target, "Open File");
                startActivity(intent);
            }

        } catch (Error | Exception e) {
            Toast.makeText(getApplicationContext(), e.toString(), Toast.LENGTH_LONG).show();
            e.printStackTrace();
        }

        hideDialog();
    }

    private void showDialog() {
        if (!progressDialog.isShowing())
            progressDialog.show();
    }

    private void hideDialog() {
        if (progressDialog.isShowing())
            progressDialog.dismiss();
    }

    private void showCashBack(Order order) {
        final RoundedBottomSheetDialog mBottomSheetDialog = new RoundedBottomSheetDialog(MainActivityOrder.this);
        LayoutInflater inflater = MainActivityOrder.this.getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.bottom_amount_layout, null);

        TextInputLayout reviewTxt = dialogView.findViewById(R.id.walletTxt);
        TextInputEditText walletEdit = dialogView.findViewById(R.id.wallet);
        final Button submit = dialogView.findViewById(R.id.submit);

        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (walletEdit.getText().toString().length() <= 0) {
                    Toast.makeText(MainActivityOrder.this, "Enter Valid Cashback", Toast.LENGTH_LONG).show();
                    return;
                } else {
                    updateWallet(order.getUser(), walletEdit.getText().toString(), order.id, mBottomSheetDialog);
                }
            }
        });
        mBottomSheetDialog.setContentView(dialogView);
        walletEdit.requestFocus();
        mBottomSheetDialog.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
        mBottomSheetDialog.setOnShowListener(new DialogInterface.OnShowListener() {
            @Override
            public void onShow(final DialogInterface dialog) {
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        RoundedBottomSheetDialog d = (RoundedBottomSheetDialog) dialog;
                        FrameLayout bottomSheet = d.findViewById(R.id.design_bottom_sheet);
                        BottomSheetBehavior bottomSheetBehavior = BottomSheetBehavior.from(bottomSheet);
                        bottomSheetBehavior.setState(BottomSheetBehavior.STATE_EXPANDED);
                    }
                }, 0);
            }
        });
        mBottomSheetDialog.show();
    }


    private void updateWallet(final String userId, final String wallet, final String orderId, RoundedBottomSheetDialog mBottomSheetDialog) {
        String tag_string_req = "req_register";
        showDialog();
        StringRequest strReq = new StringRequest(Request.Method.POST,
                UPDATE_WALLET, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                hideDialog();
                Log.d("Register Response: ", response);
                try {
                    JSONObject jObj = new JSONObject(response);
                    Toast.makeText(getApplication(), jObj.getString("message"), Toast.LENGTH_SHORT).show();
                    int success = jObj.getInt("success");
                    if (success == 1) {
                        if (mBottomSheetDialog != null) {
                            mBottomSheetDialog.cancel();
                        }
                        offset = 0;
                        fetchContacts();
                    }
                } catch (JSONException e) {
                    Log.e("xxxxxxxxxxx", e.toString());
                    Toast.makeText(getApplication(), "Some Network Error.Try after some time", Toast.LENGTH_SHORT).show();
                }

            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                hideDialog();
                Log.e("Registration Error: ", error.getMessage());
                Toast.makeText(getApplication(),
                        "Some Network Error.Try after some time", Toast.LENGTH_LONG).show();
            }
        }) {
            protected Map<String, String> getParams() {
                HashMap localHashMap = new HashMap();
                localHashMap.put("userId", userId);
                localHashMap.put("amt", wallet);
                localHashMap.put("orderId", orderId);
                return localHashMap;
            }
        };
        strReq.setRetryPolicy(Appconfig.getPolicy());
        AppController.getInstance().addToRequestQueue(strReq, tag_string_req);
    }


}
