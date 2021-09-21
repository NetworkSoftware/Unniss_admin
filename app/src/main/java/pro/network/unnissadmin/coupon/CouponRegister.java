package pro.network.unnissadmin.coupon;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.weiwangcn.betterspinner.library.material.MaterialBetterSpinner;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import pro.network.unnissadmin.R;
import pro.network.unnissadmin.app.AppController;
import pro.network.unnissadmin.app.Appconfig;

import static pro.network.unnissadmin.app.Appconfig.COUPON;


/**
 * Created by user_1 on 11-07-2018.
 */

public class CouponRegister extends AppCompatActivity {


    private final String[] STOCKUPDATE = new String[]{
            "0", "1",
    };
    EditText coupon;
    EditText amt;
    EditText description;
   // MaterialBetterSpinner status;
    TextView submit;
    EditText percentage,minimumOrder;
    RadioButton rupeesBtn, percentBtn;
    private ProgressDialog pDialog;
    private CheckBox isNotify;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.coupon_register);

        getSupportActionBar().setTitle("Coupon Register");

        final LinearLayoutManager addManager1 = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);


        pDialog = new ProgressDialog(this);
        pDialog.setCancelable(false);

        isNotify=findViewById(R.id.isNotify);

        amt = findViewById(R.id.amt);
        description = findViewById(R.id.description);

        coupon = findViewById(R.id.coupon);
        rupeesBtn = findViewById(R.id.rupeesBtn);
        percentBtn = findViewById(R.id.percentBtn);
        percentage = findViewById(R.id.percentage);
        minimumOrder=findViewById(R.id.minimumOrder);

        rupeesBtn.setChecked(true);

        rupeesBtn.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    percentage.setVisibility(View.GONE);
                    rupeesBtn.setChecked(true);
                    percentBtn.setChecked(false);
                } else {
                    rupeesBtn.setChecked(false);
                    percentBtn.setChecked(true);
                    percentage.setVisibility(View.VISIBLE);
                }
            }
        });

        percentBtn.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    percentage.setVisibility(View.VISIBLE);
                    rupeesBtn.setChecked(false);
                    percentBtn.setChecked(true);
                } else {
                    rupeesBtn.setChecked(true);
                    percentBtn.setChecked(false);
                    percentage.setVisibility(View.GONE);
                }
            }
        });

        submit = findViewById(R.id.submit);

        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (amt.getText().toString().length() <= 0) {
                    amt.setError("Select the Amount");
                } else if (description.getText().toString().length() <= 0) {
                    description.setError("Enter the Description");
                } else if (coupon.getText().toString().length() <= 0) {
                    coupon.setError("Enter the Coupon");
                }/* else if (status.getText().toString().length() <= 0) {
                    status.setError("Enter the Status");
                }*/ else if (minimumOrder.getText().toString().length() <= 0) {
                    minimumOrder.setError("Enter the Minimum order value");
                } else if (percentBtn.isChecked() && percentage.getText().toString().length() <= 0) {
                    percentage.setError("Enter the percentage");
                } else {

                    registerUser();
                }

            }
        });
        //status = findViewById(R.id.status);

        ArrayAdapter<String> stockAdapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_dropdown_item_1line, STOCKUPDATE);
    /*    status.setAdapter(stockAdapter);
        status.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            }
        });*/


    }

    private void registerUser() {
        String tag_string_req = "req_register";
        pDialog.setMessage("Createing ...");
        showDialog();
        StringRequest strReq = new StringRequest(Request.Method.POST,
                COUPON, new Response.Listener<String>() {
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
                     /*   if (isNotify.isChecked()) {
                            final String descrip = description.getText().toString();
                           *//* Appconfig.sendNotification(coupon.getText().toString().toUpperCase()
                                    , descrip.length() > 30 ? descrip.substring(0, 29) + "..." :
                                            descrip, pDialog, CouponRegister.this);*//*

                        }*/
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
                localHashMap.put("description", description.getText().toString());
                localHashMap.put("amount", amt.getText().toString());
             // localHashMap.put("status", status.getText().toString());
                localHashMap.put("isPercent", percentBtn.isChecked() ? "1" : "0");
                localHashMap.put("percentage", percentage.getText().toString());
                localHashMap.put("code", coupon.getText().toString());
                localHashMap.put("minimum_amount", minimumOrder.getText().toString());
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


}
