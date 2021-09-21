package pro.network.unnissadmin;

import android.content.Intent;
import android.os.Bundle;
import android.text.Html;
import android.view.View;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import pro.network.unnissadmin.banner.MainActivityBanner;
import pro.network.unnissadmin.categories.MainActivityCategories;
import pro.network.unnissadmin.coupon.MainActivityCoupon;
import pro.network.unnissadmin.news.NewsRegister;
import pro.network.unnissadmin.order.MainActivityOrder;
import pro.network.unnissadmin.pincode.MainActivityPincode;
import pro.network.unnissadmin.product.MainActivityProduct;

public class NaviActivity extends AppCompatActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_navi);
      //  getSupportActionBar().setDisplayHomeAsUpEnabled(true);
     //   getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setTitle(Html.fromHtml("<font color='#000000'>Unniss Admin</font>"));
        CardView catrgories = findViewById(R.id.catrgories);
        catrgories.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent io = new Intent(NaviActivity.this, MainActivityCategories.class);
                startActivity(io);
            }
        });

        CardView news = findViewById(R.id.news);
        news.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent io = new Intent(NaviActivity.this, NewsRegister.class);
                startActivity(io);
            }
        });
        CardView stock = findViewById(R.id.stock);
        stock.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent io = new Intent(NaviActivity.this, MainActivityProduct.class);
                startActivity(io);

            }
        });
        CardView pincode = findViewById(R.id.pincode);
        pincode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent io = new Intent(NaviActivity.this, MainActivityPincode.class);
                startActivity(io);

            }
        });
        CardView notification = (CardView) findViewById(R.id.notification);
        notification.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent io = new Intent(NaviActivity.this, Push_Notification.class);
                startActivity(io);

            }
        });
        CardView banner = findViewById(R.id.banner);
        banner.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent io = new Intent(NaviActivity.this, MainActivityBanner.class);
                startActivity(io);

            }
        });
        CardView coupon = findViewById(R.id.coupon);
        coupon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent io = new Intent(NaviActivity.this, MainActivityCoupon.class);
                startActivity(io);
            }
        });

        CardView order = findViewById(R.id.orders);
        order.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                navOrderPage("processing");
            }
        });

        CardView canceled = findViewById(R.id.canceled);
        canceled.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                navOrderPage("cancelled");
            }
        });
        CardView delivered = findViewById(R.id.delivered);
        delivered.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                navOrderPage("completed");
            }
        });

    }

    private void navOrderPage(String status) {
        Intent io = new Intent(NaviActivity.this, MainActivityOrder.class);
        io.putExtra("status", status);
        startActivity(io);
    }
}
