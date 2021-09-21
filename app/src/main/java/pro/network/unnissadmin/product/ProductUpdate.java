package pro.network.unnissadmin.product;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.deishelon.roundedbottomsheet.RoundedBottomSheetDialog;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;
import com.google.gson.Gson;
import com.weiwangcn.betterspinner.library.material.MaterialBetterSpinner;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.mime.content.FileBody;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import pro.network.unnissadmin.R;
import pro.network.unnissadmin.app.ActivityMediaOnline;
import pro.network.unnissadmin.app.AndroidMultiPartEntity;
import pro.network.unnissadmin.app.AppController;
import pro.network.unnissadmin.app.Appconfig;
import pro.network.unnissadmin.app.Imageutils;

import static pro.network.unnissadmin.app.Appconfig.CATEGORIES_GET_ALL;
import static pro.network.unnissadmin.app.Appconfig.CATEGORY;
import static pro.network.unnissadmin.app.Appconfig.PRODUCT_DELETE;
import static pro.network.unnissadmin.app.Appconfig.PRODUCT_GET_ID;
import static pro.network.unnissadmin.app.Appconfig.PRODUCT_SIZE;
import static pro.network.unnissadmin.app.Appconfig.PRODUCT_UPDATE;
import static pro.network.unnissadmin.app.Appconfig.QTY_TYPE;

/**
 * Created by user_1 on 11-07-2018.
 */

public class ProductUpdate extends AppCompatActivity implements Imageutils.ImageAttachmentListener, ImageClick {


    private final String[] STOCKUPDATE = new String[]{
            "Instock", "outofstock",
    };
    private final String[] BESTSELLING = new String[]{
            "Best Selling",
    };
    private final String[] PRICEDROP = new String[]{
            "Price Drop",
    };
    private final Set<String> delete_size = new HashSet<>();
    public MaterialButton addSize;
    EditText brand;
    EditText model;
    EditText price;
    EditText ram;
    ArrayList<Size> sizes = new ArrayList<>();
    SizeAdapter sizeAdapter;
    EditText fabric, ideal, occasion, fit, color, closure, pocket, pattern, rise, origin;
    EditText rom, description;
    EditText rqty;
    AddImageAdapter maddImageAdapter;
    MaterialBetterSpinner category;
    MaterialBetterSpinner bestselling;
    MaterialBetterSpinner pricedrop;
    MaterialBetterSpinner stock_update;
    String studentId = null;
    TextView submit;
    Imageutils imageutils;
    ImageView image_placeholder, image_wallpaper;
    CardView itemsAdd;
    private RecyclerView sizelist;
    private MaterialBetterSpinner rqtyType;
    private ProgressDialog pDialog;
    private RecyclerView imagelist;
    private ArrayList<String> samplesList = new ArrayList<>();
    private String imageUrl = "";
    private Map<String, String> idCatMap = new HashMap<>();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.stock_register);
        imageutils = new Imageutils(this);

        pDialog = new ProgressDialog(this);
        pDialog.setCancelable(false);

        sizelist = findViewById(R.id.sizelist);
        sizes = new ArrayList<>();
        addSize = findViewById(R.id.addSize);
        addSize.setText("Update");
        addSize.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showSizeBottom(-1);
            }
        });
        sizeAdapter = new SizeAdapter(this, sizes, new ImageClick() {
            @Override
            public void onImageClick(int position) {
                showSizeBottom(position);
            }

            @Override
            public void onDeleteClick(int position) {
                if (sizes.get(position).getId() != null && sizes.get(position).getId().length() > 0) {
                    delete_size.add(sizes.get(position).getId());
                }
                sizes.remove(position);
                sizeAdapter.notifyData(sizes);
            }
        }, true);
        final LinearLayoutManager sizeManager = new LinearLayoutManager(
                this, LinearLayoutManager.HORIZONTAL, false);
        sizelist.setLayoutManager(sizeManager);
        sizelist.setAdapter(sizeAdapter);
        itemsAdd = (CardView) findViewById(R.id.itemsAdd);
        ImageView image_wallpaper = (ImageView) findViewById(R.id.image_wallpaper);
        image_wallpaper.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                imageutils.imagepicker(1);
            }
        });

        samplesList = new ArrayList<>();
        imagelist = (RecyclerView) findViewById(R.id.imagelist);
        maddImageAdapter = new AddImageAdapter(this, samplesList, this);
        final LinearLayoutManager addManager1 = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
        imagelist.setLayoutManager(addManager1);
        imagelist.setAdapter(maddImageAdapter);
        category = (MaterialBetterSpinner) findViewById(R.id.category);


        model = (EditText) findViewById(R.id.model);
        price = (EditText) findViewById(R.id.price);
        ram = (EditText) findViewById(R.id.ram);
        rom = (EditText) findViewById(R.id.rom);
        description = findViewById(R.id.description);
        fabric = findViewById(R.id.fabric);
        ideal = findViewById(R.id.ideal);
        occasion = findViewById(R.id.occasion);
        fit = findViewById(R.id.fit);
        color = findViewById(R.id.color);
        closure = findViewById(R.id.closure);
        pocket = findViewById(R.id.pocket);
        pattern = findViewById(R.id.pattern);
        rise = findViewById(R.id.rise);
        origin = findViewById(R.id.origin);
        bestselling = findViewById(R.id.bestselling);
        pricedrop = findViewById(R.id.pricedrop);
        ArrayAdapter<String> sellingAdapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_dropdown_item_1line, BESTSELLING);
        bestselling.setAdapter(sellingAdapter);
        bestselling.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            }
        });
        ArrayAdapter<String> priceAdapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_dropdown_item_1line, PRICEDROP);
        pricedrop.setAdapter(priceAdapter);
        pricedrop.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            }
        });


        stock_update = (MaterialBetterSpinner) findViewById(R.id.stock_update);

        ArrayAdapter<String> stockAdapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_dropdown_item_1line, STOCKUPDATE);
        stock_update.setAdapter(stockAdapter);
        stock_update.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            }
        });
        brand = findViewById(R.id.brand);
        ArrayAdapter<String> categoryAdapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_dropdown_item_1line, CATEGORY);
        category.setAdapter(categoryAdapter);
        category.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                ArrayAdapter<String> brandAdapter = new ArrayAdapter<String>(ProductUpdate.this,
                        android.R.layout.simple_dropdown_item_1line, Appconfig.getSubCatFromCat(CATEGORY[position]));

            }
        });

        rqtyType = findViewById(R.id.rqtyType);
        ArrayAdapter<String> rqtyTypeAdapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_dropdown_item_1line, QTY_TYPE);
        rqtyType.setAdapter(rqtyTypeAdapter);
        rqty = findViewById(R.id.rqty);
        submit = (TextView) findViewById(R.id.submit);
        submit.setText("UPDATE");
        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (category.getText().toString().length() <= 0) {
                    category.setError("Select the Category");
                } else if (brand.getText().toString().length() <= 0) {
                    brand.setError("Enter the Brand");
                } else if (model.getText().toString().length() <= 0) {
                    model.setError("Enter the Model");
                } else if (rqty.getText().toString().length() <= 0) {
                    rqty.setError("Enter the Quantity");
                } else if (fabric.getText().toString().length() <= 0) {
                    fabric.setError("Enter the Fabric");
                } else if (ideal.getText().toString().length() <= 0) {
                    ideal.setError("Enter the Ideal");
                } else if (occasion.getText().toString().length() <= 0) {
                    occasion.setError("Enter the Occasion");
                } else if (fit.getText().toString().length() <= 0) {
                    fit.setError("Enter the Fit");
                } else if (color.getText().toString().length() <= 0) {
                    color.setError("Enter the Color");
                } else if (closure.getText().toString().length() <= 0) {
                    closure.setError("Enter the Closure");
                } else if (pocket.getText().toString().length() <= 0) {
                    pocket.setError("Enter the Pocket");
                } else if (pattern.getText().toString().length() <= 0) {
                    pattern.setError("Enter the Pattern");
                } else if (rise.getText().toString().length() <= 0) {
                    rise.setError("Enter the Rise");
                } else if (origin.getText().toString().length() <= 0) {
                    origin.setError("Enter the Origin");
                } /*else if (price.getText().toString().length() <= 0) {
                    price.setError("Enter the Price");
                }*/ else if (stock_update.getText().toString().length() <= 0) {
                    stock_update.setError("Select the Sold or Not");
                } else if (samplesList.size() <= 0) {
                    Toast.makeText(getApplicationContext(), "Upload the Images!", Toast.LENGTH_SHORT).show();
                } else {

                    registerUser();
                }
            }
        });

        if (getIntent().getBooleanExtra("isServer", false)) {
            getProductById(getIntent().getStringExtra("id"));
        } else {
            Product contact = (Product) getIntent().getSerializableExtra("data");
            doAssignAllValues(contact);
        }
        getAllCategories();
    }

    private void doAssignAllValues(Product contact) {
        try {
            category.setText(contact.categoryId);
            brand.setText(Html.fromHtml(contact.brand));
            model.setText(contact.model);
            //  price.setText(contact.price);
            ram.setText(contact.ram);
            rom.setText(contact.rom);
            rqty.setText(contact.rqty);
            rqtyType.setText(contact.rqtyType);
            description.setText(Html.fromHtml(contact.description));
            fabric.setText(contact.fabric);
            ideal.setText(contact.ideal);
            occasion.setText(contact.occasion);
            fit.setText(contact.fit);
            color.setText(contact.color);
            closure.setText(contact.closure);
            pocket.setText(contact.pocket);
            pattern.setText(contact.pattern);
            rise.setText(contact.rise);
            price.setText(contact.price);
            origin.setText(contact.origin);
            bestselling.setText(contact.bestselling);
            pricedrop.setText(contact.pricedrop);
            studentId = contact.id;
            stock_update.setText(contact.stock_update);
            rqtyType.setText(contact.rqtyType);
            imageUrl = contact.image;
            if (imageUrl == null) {
                imageUrl = "";
            } else {
                samplesList = new Gson().fromJson(imageUrl, (Type) List.class);
            }
            if (contact.getSize() == null || contact.getSize().equalsIgnoreCase("null")) {
                sizes = new ArrayList<>();
            } else {
                try {
                    ObjectMapper mapper = new ObjectMapper();
                    Object listBeans = new Gson().fromJson(contact.getSize(),
                            Object.class);
                    sizes = mapper.convertValue(
                            listBeans,
                            new TypeReference<ArrayList<Size>>() {
                            }
                    );
                } catch (Exception e) {
                    Log.e("xxxxxxxxxx", e.toString());
                }
            }
            if (sizes == null) {
                sizes = new ArrayList<>();
            }
            sizeAdapter.notifyData(sizes);
            maddImageAdapter.notifyData(samplesList);

        } catch (Exception e) {
            Log.e("xxxxxxxxxxx", e.toString());
        }
    }

    private void showSizeBottom(int position) {
        final RoundedBottomSheetDialog mBottomSheetDialog = new RoundedBottomSheetDialog(ProductUpdate.this);
        LayoutInflater inflater = ProductUpdate.this.getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.bottom_size_layout, null);
        final TextInputEditText size = dialogView.findViewById(R.id.size);
        final TextInputEditText size_price = dialogView.findViewById(R.id.size_price);
        final Button submit = dialogView.findViewById(R.id.submit);

        if (position >= 0) {
            Size sizetemp = sizes.get(position);
            size.setText(sizetemp.getSize());
            size_price.setText(sizetemp.getSize_price());
            size.setEnabled(false);
        }
        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (size_price.getText().toString().length() <= 0) {
                    Toast.makeText(getApplicationContext(), "Enter Valid Price", Toast.LENGTH_LONG).show();
                    return;
                }
                if (position >= 0) {
                    sizes.get(position).setSize_price(size_price.getText().toString());
                } else {
                    sizes.add(new Size("", size.getText().toString(), size_price.getText().toString()));
                }
                sizeAdapter.notifyData(sizes);
                mBottomSheetDialog.cancel();
            }
        });
        size.requestFocus();

        mBottomSheetDialog.setContentView(dialogView);
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

    private void registerUser() {
        String tag_string_req = "req_register";
        pDialog.setMessage("Updateing ...");
        showDialog();
        StringRequest strReq = new StringRequest(Request.Method.PUT,
                PRODUCT_UPDATE, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.d("Register Response: ", response);
                hideDialog();
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    boolean success = jsonObject.getBoolean("success");
                    String msg = jsonObject.getString("message");
                    if (success) {
                        finish();
                    }
                    Toast.makeText(getApplicationContext(), msg, Toast.LENGTH_SHORT).show();
                } catch (JSONException e) {
                    Toast.makeText(getApplicationContext(), "Some Network Error.Try after some time", Toast.LENGTH_SHORT).show();

                }

            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e("Registration Error: ", error.getMessage());
                Toast.makeText(getApplicationContext(),
                        "Some Network Error.Try after some time", Toast.LENGTH_LONG).show();
                hideDialog();
            }
        }) {
            protected Map<String, String> getParams() {
                HashMap localHashMap = new HashMap();
                localHashMap.put("category", idCatMap.get(category.getText().toString()));
                localHashMap.put("subcategory", brand.getText().toString());
                localHashMap.put("name", model.getText().toString());
                localHashMap.put("price", sizes.get(0).getSize_price());
                localHashMap.put("rqty", rqty.getText().toString());
                localHashMap.put("rqtyType", rqtyType.getText().toString());
                localHashMap.put("fabric", fabric.getText().toString());
                localHashMap.put("ideal", ideal.getText().toString());
                localHashMap.put("occasion", occasion.getText().toString());
                localHashMap.put("fit", fit.getText().toString());
                localHashMap.put("color", color.getText().toString());
                localHashMap.put("size", new Gson().toJson(sizes));
                localHashMap.put("closure", closure.getText().toString());
                localHashMap.put("pocket", pocket.getText().toString());
                localHashMap.put("pattern", pattern.getText().toString());
                localHashMap.put("rise", rise.getText().toString());
                localHashMap.put("origin", origin.getText().toString());
                localHashMap.put("bestselling", bestselling.getText().toString());
                localHashMap.put("pricedrop", pricedrop.getText().toString());
                localHashMap.put("stock_status", stock_update.getText().toString());
                localHashMap.put("id", studentId);
                for (int i = 0; i < samplesList.size(); i++) {
                    localHashMap.put("image[" + i + "]", samplesList.get(i));
                }
                localHashMap.put("description", description.getText().toString());
                localHashMap.put("dSize", new Gson().toJson(new ArrayList<>(delete_size)));
                return localHashMap;
            }
        };
        strReq.setRetryPolicy(Appconfig.getPolicy());
        AppController.getInstance().addToRequestQueue(strReq);
    }

    private void getAllCategories() {
        String tag_string_req = "req_register";
        pDialog.setMessage("Loading...");
        showDialog();
        StringRequest strReq = new StringRequest(Request.Method.GET,
                CATEGORIES_GET_ALL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                hideDialog();
                try {
                    JSONObject jObj = new JSONObject(response);
                    int success = jObj.getInt("success");

                    if (success == 1) {
                        JSONArray jsonArray = jObj.getJSONArray("data");
                        CATEGORY = new String[jsonArray.length()];
                        idCatMap = new HashMap<>();
                        for (int i = 0; i < jsonArray.length(); i++) {
                            CATEGORY[i] = jsonArray.getJSONObject(i).getString("title");
                            idCatMap.put(jsonArray.getJSONObject(i).getString("title"),
                                    jsonArray.getJSONObject(i).getString("id"));
                        }
                        ArrayAdapter<String> titleAdapter = new ArrayAdapter<String>(ProductUpdate.this,
                                android.R.layout.simple_dropdown_item_1line, CATEGORY);
                        category.setAdapter(titleAdapter);
                        fetchSize();
                    }
                } catch (JSONException e) {
                }
            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                hideDialog();
            }
        }) {
            protected Map<String, String> getParams() {
                HashMap localHashMap = new HashMap();
                return localHashMap;
            }
        };
        strReq.setRetryPolicy(Appconfig.getPolicy());
        AppController.getInstance().addToRequestQueue(strReq, tag_string_req);
    }

    private void fetchSize() {
        String tag_string_req = "req_register";
        pDialog.setMessage("Processing ...");
        showDialog();
        StringRequest strReq = new StringRequest(Request.Method.GET,
                PRODUCT_SIZE + "?id=" + studentId, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                hideDialog();
                Log.d("Register Response: ", response);
                try {
                    JSONObject jObj = new JSONObject(response);
                    int success = jObj.getInt("success");

                    if (success == 1) {
                        JSONArray jsonArray = jObj.getJSONArray("data");
                        sizes = new ArrayList<>();
                        for (int i = 0; i < jsonArray.length(); i++) {
                            JSONObject jsonObject = jsonArray.getJSONObject(i);
                            Size product = new Size();
                            product.setId(jsonObject.getString("id"));
                            product.setSize(jsonObject.getString("size"));
                            product.setSize_price(jsonObject.getString("size_price"));
                            sizes.add(product);
                        }
                        sizeAdapter.notifyData(sizes);
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
                Toast.makeText(getApplication(),
                        "Some Network Error.Try after some time", Toast.LENGTH_LONG).show();
            }
        }) {
            protected Map<String, String> getParams() {
                HashMap localHashMap = new HashMap();
                return localHashMap;
            }
        };
        strReq.setRetryPolicy(Appconfig.getPolicy());
        AppController.getInstance().addToRequestQueue(strReq, tag_string_req);
    }

    private void deleteUser() {
        String tag_string_req = "req_register";
        pDialog.setMessage("Processing ...");
        showDialog();
        StringRequest strReq = new StringRequest(Request.Method.DELETE,
                PRODUCT_DELETE + "?id=" + studentId, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.d("Register Response: ", response);
                hideDialog();
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    boolean success = jsonObject.getBoolean("success");
                    String msg = jsonObject.getString("message");
                    if (success) {
                        finish();
                    }
                    Toast.makeText(getApplicationContext(), msg, Toast.LENGTH_SHORT).show();
                } catch (JSONException e) {
                    Toast.makeText(getApplicationContext(), "Some Network Error.Try after some time", Toast.LENGTH_SHORT).show();

                }

            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e("Registration Error: ", error.getMessage());
                Toast.makeText(getApplicationContext(),
                        "Some Network Error.Try after some time", Toast.LENGTH_LONG).show();
                hideDialog();
            }
        }) {
            protected Map<String, String> getParams() {
                HashMap localHashMap = new HashMap();
                localHashMap.put("id", studentId);
                return localHashMap;
            }
        };
        strReq.setRetryPolicy(Appconfig.getPolicy());
        AppController.getInstance().addToRequestQueue(strReq);
    }


    private void showDialog() {
        if (!pDialog.isShowing())
            pDialog.show();

    }

    private void hideDialog() {
        if (pDialog.isShowing())
            pDialog.dismiss();
    }


    @Override
    protected void onPause() {
        super.onPause();
        hideDialog();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        imageutils.request_permission_result(requestCode, permissions, grantResults);
    }

    @Override
    public void image_attachment(int from, String filename, Bitmap file, Uri uri) {
        String path = getCacheDir().getPath() + File.separator + "ImageAttach" + File.separator;
        File storedFile = imageutils.createImage(file, filename, path, false);
        pDialog.setMessage("Uploading...");
        showDialog();
        new UploadFileToServer().execute(Appconfig.compressImage(storedFile.getPath(), ProductUpdate.this));
    }

    @Override
    public void onImageClick(int position) {

        Intent localIntent = new Intent(ProductUpdate.this, ActivityMediaOnline.class);
        localIntent.putExtra("filePath", samplesList.get(position));
        localIntent.putExtra("isImage", true);
        startActivity(localIntent);
    }


    @Override
    public void onDeleteClick(int position) {
        samplesList.remove(position);
        maddImageAdapter.notifyData(samplesList);
    }


    @Override
    public void onPointerCaptureChanged(boolean hasCapture) {

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        imageutils.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu options from the res/menu/menu_editor.xml file.
        // This adds menu items to the app bar.
        getMenuInflater().inflate(R.menu.menu_delete, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // User clicked on a menu option in the app bar overflow menu
        switch (item.getItemId()) {
            case R.id.action_delete:
                AlertDialog diaBox = AskOption();
                diaBox.show();
                return true;
            case android.R.id.home:
                finish();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private AlertDialog AskOption() {
        AlertDialog myQuittingDialogBox = new AlertDialog.Builder(this)
                // set message, title, and icon
                .setTitle("Delete")
                .setMessage("Do you want to Delete")
                .setIcon(R.drawable.ic_delete_black_24dp)

                .setPositiveButton("Confirm", new DialogInterface.OnClickListener() {

                    public void onClick(DialogInterface dialog, int whichButton) {
                        //your deleting code
                        dialog.dismiss();
                        deleteUser();
                    }

                })
                .setNegativeButton("cancel", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {

                        dialog.dismiss();

                    }
                })
                .create();

        return myQuittingDialogBox;
    }

    private void getProductById(final String id) {
        String tag_string_req = "req_register";
        pDialog.setMessage("Processing ...");
        showDialog();
        StringRequest strReq = new StringRequest(Request.Method.GET,
                PRODUCT_GET_ID + "?id=" + (int) Float.parseFloat(id), new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                hideDialog();
                Log.d("Register Response: ", response);
                try {
                    JSONObject jObj = new JSONObject(response);
                    int success = jObj.getInt("success");

                    if (success == 1) {
                        JSONObject jsonObject = jObj.getJSONObject("data");
                        Product product = new Product();
                        product.setId(jsonObject.getString("id"));
                        product.setBrand(jsonObject.getString("subcategory"));
                        product.setCategory(jsonObject.getString("category"));
                        product.setCategoryId(jsonObject.getString("categoryName"));
                        product.setStock_update(jsonObject.getString("stock_status"));
                        product.setDescription(jsonObject.getString("description"));
                        product.setFabric(jsonObject.getString("fabric"));
                        product.setIdeal(jsonObject.getString("ideal"));
                        product.setOccasion(jsonObject.getString("occasion"));
                        product.setFit(jsonObject.getString("fit"));
                        product.setColor(jsonObject.getString("color"));
                        product.setSize(jsonObject.getString("size"));
                        product.setClosure(jsonObject.getString("closure"));
                        product.setPocket(jsonObject.getString("pocket"));
                        product.setPattern(jsonObject.getString("pattern"));
                        product.setRise(jsonObject.getString("rise"));
                        product.setOrigin(jsonObject.getString("origin"));
                        product.setBestselling(jsonObject.getString("bestselling"));
                        product.setPricedrop(jsonObject.getString("pricedrop"));
                        product.setPrice(jsonObject.getString("price"));
                        if (!jsonObject.isNull("rqtyType")) {
                            product.setRqtyType(jsonObject.getString("rqtyType"));
                        }
                        if (!jsonObject.isNull("rqty")) {
                            product.setRqty(jsonObject.getString("rqty"));
                        }
                        product.setModel(jsonObject.getString("name"));
                        product.setImage(jsonObject.getString("image"));
                        doAssignAllValues(product);
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
                return localHashMap;
            }
        };
        strReq.setRetryPolicy(Appconfig.getPolicy());

        AppController.getInstance().addToRequestQueue(strReq, tag_string_req);
    }

    private class UploadFileToServer extends AsyncTask<String, Integer, String> {
        public long totalSize = 0;
        String filepath;

        @Override
        protected void onPreExecute() {
            // setting progress bar to zero

            super.onPreExecute();

        }

        @Override
        protected void onProgressUpdate(Integer... progress) {
            pDialog.setMessage("Uploading..." + (progress[0]));
        }

        @Override
        protected String doInBackground(String... params) {
            filepath = params[0];
            return uploadFile();
        }

        @SuppressWarnings("deprecation")
        private String uploadFile() {
            String responseString = null;

            HttpClient httpclient = new DefaultHttpClient();
            HttpPost httppost = new HttpPost(Appconfig.URL_IMAGE_UPLOAD);
            try {
                AndroidMultiPartEntity entity = new AndroidMultiPartEntity(
                        new AndroidMultiPartEntity.ProgressListener() {

                            @Override
                            public void transferred(long num) {
                                publishProgress((int) ((num / (float) totalSize) * 100));
                            }
                        });

                File sourceFile = new File(filepath);
                // Adding file data to http body
                entity.addPart("image", new FileBody(sourceFile));

                totalSize = entity.getContentLength();
                httppost.setEntity(entity);

                // Making server call
                HttpResponse response = httpclient.execute(httppost);
                HttpEntity r_entity = response.getEntity();

                int statusCode = response.getStatusLine().getStatusCode();
                if (statusCode == 200) {
                    // Server response
                    responseString = EntityUtils.toString(r_entity);

                } else {
                    responseString = "Error occurred! Http Status Code: "
                            + statusCode;

                }

            } catch (ClientProtocolException e) {
                responseString = e.toString();
            } catch (IOException e) {
                responseString = e.toString();
            }

            return responseString;

        }

        @Override
        protected void onPostExecute(String result) {
            Log.e("Response from server: ", result);
            try {
                JSONObject jsonObject = new JSONObject(result);
                if (!jsonObject.getBoolean("error")) {
                    imageUrl = Appconfig.ip + "/images/" + imageutils.getfilename_from_path(filepath);
                    samplesList.add(imageUrl);
                    maddImageAdapter.notifyData(samplesList);
                } else {
                    imageUrl = null;
                }
                Toast.makeText(getApplicationContext(), jsonObject.getString("message"), Toast.LENGTH_SHORT).show();

            } catch (Error | Exception e) {
                Toast.makeText(getApplicationContext(), "Image not uploaded", Toast.LENGTH_SHORT).show();
            }
            hideDialog();
            // showing the server response in an alert dialog
            //showAlert(result);


            super.onPostExecute(result);
        }

    }

    @Override
    protected void onDestroy() {
        if(pDialog!=null){
            hideDialog();
        }
        super.onDestroy();
    }
}



