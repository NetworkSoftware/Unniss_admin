package pro.network.unnissadmin.product;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
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
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.deishelon.roundedbottomsheet.RoundedBottomSheetDialog;
import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
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
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import pro.network.unnissadmin.R;
import pro.network.unnissadmin.app.ActivityMediaOnline;
import pro.network.unnissadmin.app.AndroidMultiPartEntity;
import pro.network.unnissadmin.app.AppController;
import pro.network.unnissadmin.app.Appconfig;
import pro.network.unnissadmin.app.Imageutils;

import static pro.network.unnissadmin.app.Appconfig.CATEGORIES_GET_ALL;
import static pro.network.unnissadmin.app.Appconfig.PRODUCT_CREATE;
import static pro.network.unnissadmin.app.Appconfig.QTY_TYPE;

/**
 * Created by user_1 on 11-07-2018.
 */

public class ProductRegister extends AppCompatActivity implements Imageutils.ImageAttachmentListener, ImageClick {

    public static String[] CATEGORY = new String[]{
    };
    private final String[] STOCKUPDATE = new String[]{
            "instock", "outofstock",
    };
    private final String[] BESTSELLING = new String[]{
            "Best Selling",
    };
    private final String[] PRICEDROP = new String[]{
            "Price Drop",
    };
    public MaterialButton addSize;
    AddImageAdapter maddImageAdapter;
    ArrayList<Size> sizes = new ArrayList<>();
    SizeAdapter sizeAdapter;
    EditText brand;
    EditText model, fabric, ideal, occasion, fit, color, closure, pocket, pattern, rise, origin;
    EditText price;
    EditText ram;
    EditText rom, description;
    MaterialBetterSpinner category;
    MaterialBetterSpinner bestselling;
    MaterialBetterSpinner pricedrop;
    MaterialBetterSpinner stock_update;
    String studentId = null;
    Imageutils imageutils;
    ImageView image_placeholder, image_wallpaper;
    CardView itemsAdd;
    EditText rqty;
    TextView submit;
    private RecyclerView sizelist;
    private RecyclerView imagelist;
    private ArrayList<String> samplesList = new ArrayList<>();
    private ProgressDialog pDialog;
    private String imageUrl = "";
    private MaterialBetterSpinner rqtyType;
    private Map<String, String> idCatMap = new HashMap<>();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.stock_register);
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
        addSize = findViewById(R.id.addSize);
        sizelist = findViewById(R.id.sizelist);
        sizes = new ArrayList<>();
        addSize.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showSizeBottom();
            }
        });
        sizeAdapter = new SizeAdapter(this, sizes, new ImageClick() {
            @Override
            public void onImageClick(int position) {

            }

            @Override
            public void onDeleteClick(int position) {
                sizes.remove(position);
                sizeAdapter.notifyData(sizes);
            }
        }, true);
        final LinearLayoutManager sizeManager = new LinearLayoutManager(
                this, LinearLayoutManager.HORIZONTAL, false);
        sizelist.setLayoutManager(sizeManager);
        sizelist.setAdapter(sizeAdapter);
        ArrayAdapter<String> sellingAdapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_dropdown_item_1line, BESTSELLING);
        bestselling.setAdapter(sellingAdapter);
        bestselling.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            }
        });
        pricedrop = findViewById(R.id.pricedrop);
        ArrayAdapter<String> priceAdapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_dropdown_item_1line, PRICEDROP);
        pricedrop.setAdapter(priceAdapter);
        pricedrop.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            }
        });
        imageutils = new Imageutils(this);

        getSupportActionBar().setTitle("Stock Register");

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

        ArrayAdapter<String> titleAdapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_dropdown_item_1line, CATEGORY);
        category.setAdapter(titleAdapter);
        category.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                ArrayAdapter<String> brandAdapter = new ArrayAdapter<String>(ProductRegister.this,
                        android.R.layout.simple_dropdown_item_1line, Appconfig.getSubCatFromCat(CATEGORY[position]));
                category.setAdapter(brandAdapter);
                category.setThreshold(1);
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


        pDialog = new ProgressDialog(this);
        pDialog.setCancelable(false);

        model = (EditText) findViewById(R.id.model);
        price = (EditText) findViewById(R.id.price);
        ram = (EditText) findViewById(R.id.ram);
        rom = (EditText) findViewById(R.id.rom);
        description = findViewById(R.id.description);
        rqty = findViewById(R.id.rqty);
        rqtyType = findViewById(R.id.rqtyType);
        ArrayAdapter<String> rqtyTypeAdapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_dropdown_item_1line, QTY_TYPE);
        rqtyType.setAdapter(rqtyTypeAdapter);

        submit = (TextView) findViewById(R.id.submit);

        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (category.getText().toString().length() <= 0) {
                    category.setError("Select the Category");
                } else if (brand.getText().toString().length() <= 0) {
                    brand.setError("Select the Brand");
                } else if (model.getText().toString().length() <= 0) {
                    model.setError("Enter the Model");
                }/* else if (price.getText().toString().length() <= 0) {
                    price.setError("Enter the Price");
                } */else if (rqty.getText().toString().length() <= 0) {
                    rqty.setError("Enter the Rqty");
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
                } else if (stock_update.getText().toString().length() <= 0) {
                    stock_update.setError("Select the Sold or Not");
                } else if (samplesList.size() <= 0) {
                    Toast.makeText(getApplicationContext(), "Upload the Images!", Toast.LENGTH_SHORT).show();
                } else {

                    registerUser();
                }

            }
        });

        getAllCategories();
    }

    private void showSizeBottom() {
        final RoundedBottomSheetDialog mBottomSheetDialog = new RoundedBottomSheetDialog(ProductRegister.this);
        LayoutInflater inflater = ProductRegister.this.getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.bottom_size_layout, null);

        final TextInputLayout sizeTxt = dialogView.findViewById(R.id.sizeLayoutTxt);
        final TextInputEditText size = dialogView.findViewById(R.id.size);
        final TextInputEditText size_price = dialogView.findViewById(R.id.size_price);

        final Button submit = dialogView.findViewById(R.id.submit);
        sizeTxt.setVisibility(View.VISIBLE);
        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (size.getText().toString().length() <= 0) {
                    Toast.makeText(getApplicationContext(), "Enter Valid Size", Toast.LENGTH_LONG).show();
                    return;
                } else if (size_price.getText().toString().length() <= 0) {
                    Toast.makeText(getApplicationContext(), "Enter Valid Price", Toast.LENGTH_LONG).show();
                    return;
                }
                sizes.add(new Size("", size.getText().toString(), size_price.getText().toString()));
                sizeAdapter.notifyData(sizes);
                mBottomSheetDialog.cancel();
            }
        });
        size.requestFocus();
        size_price.requestFocus();

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
        pDialog.setMessage("Createing ...");
        showDialog();
        StringRequest strReq = new StringRequest(Request.Method.POST,
                PRODUCT_CREATE, new Response.Listener<String>() {
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
                localHashMap.put("price",sizes.get(0).getSize_price());
                localHashMap.put("rqty", rqty.getText().toString());
                localHashMap.put("rqtyType", rqtyType.getText().toString());
                for (int i = 0; i < samplesList.size(); i++) {
                    localHashMap.put("image[" + i + "]", samplesList.get(i));
                }

                localHashMap.put("description", description.getText().toString());
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

    private void getAllCategories() {
        String tag_string_req = "req_register";
        StringRequest strReq = new StringRequest(Request.Method.GET,
                CATEGORIES_GET_ALL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
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
                        ArrayAdapter<String> titleAdapter = new ArrayAdapter<String>(ProductRegister.this,
                                android.R.layout.simple_dropdown_item_1line, CATEGORY);
                        category.setAdapter(titleAdapter);
                    }
                } catch (JSONException e) {
                }
            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
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

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        imageutils.request_permission_result(requestCode, permissions, grantResults);
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    @Override
    public void image_attachment(int from, String filename, Bitmap file, Uri uri) {
        String path = getCacheDir().getPath() + File.separator + "ImageAttach" + File.separator;
        File storedFile = imageutils.createImage(file, filename, path, false);
        pDialog.setMessage("Uploading...");
        showDialog();
        new UploadFileToServer().execute(Appconfig.compressImage(storedFile.getPath(), ProductRegister.this));

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        imageutils.onActivityResult(requestCode, resultCode, data);
        super.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public void onImageClick(int position) {
        Intent localIntent = new Intent(ProductRegister.this, ActivityMediaOnline.class);
        localIntent.putExtra("filePath", samplesList.get(position));
        localIntent.putExtra("isImage", true);
        startActivity(localIntent);
    }

    @Override
    public void onDeleteClick(int position) {
        samplesList.remove(position);
        maddImageAdapter.notifyData(samplesList);
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

}
