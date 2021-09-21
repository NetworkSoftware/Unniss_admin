package pro.network.unnissadmin;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;

import java.util.HashMap;
import java.util.Map;

import pro.network.unnissadmin.app.AppController;
import pro.network.unnissadmin.app.Appconfig;

public class Push_Notification extends AppCompatActivity {

    EditText title, sub_title;
    TextView submit;
    private ProgressDialog pDialog;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_push_notification);
        title = findViewById(R.id.notify_title);
        sub_title = findViewById(R.id.sub_title);
        pDialog=new ProgressDialog(this);
        pDialog.setCancelable(false);
        submit = findViewById(R.id.submit);
        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (title.getText().toString().length() <= 0) {
                    title.setError("Select the Category");
                } else if (sub_title.getText().toString().length() <= 0) {
                    sub_title.setError("Select the Brand");
                } else {
                    sendNotification(title.getText().toString(), sub_title.getText().toString());
                }
            }
        });
    }

    private void sendNotification(String title, String sub_title) {
        String tag_string_req = "req_register";
        pDialog.setMessage("Pushing...");
        showDialog();
        StringRequest strReq = new StringRequest(Request.Method.POST,
                Appconfig.NOTIFY, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.d("Register Response: ", response);
                Toast.makeText(getApplication(), "Notify Success", Toast.LENGTH_SHORT).show();
                hideDialog();
                finish();
            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                hideDialog();
                finish();
            }
        }) {
            protected Map<String, String> getParams() {
                HashMap localHashMap = new HashMap();
                localHashMap.put("title", title);
                localHashMap.put("subtitle", sub_title);
                return localHashMap;
            }
        };

        strReq.setRetryPolicy(Appconfig.getPolicy());
        AppController.getInstance().addToRequestQueue(strReq, tag_string_req);
    }

    private void showDialog() {
        if (!pDialog.isShowing())
            pDialog.show();
    }

    private void hideDialog() {
        if (pDialog.isShowing())
            pDialog.dismiss();
    }

}
