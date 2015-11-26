package com.example.jackrusty.login;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
//import android.view.Menu;
//import android.view.MenuItem;
import android.view.Menu;
import android.view.View;
import android.view.Window;
import android.widget.Button;

import com.parse.Parse;
import com.parse.ParseInstallation;
import com.parse.PushService;

public class MainActivity extends Activity{

    Button btnViewProducts;
    Button btnNewProduct;

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        return false;
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        getWindow().requestFeature(Window.FEATURE_ACTION_BAR);
        getActionBar().hide();

        Parse.initialize(this, "dK8wwag7b58CZj982grz4rAXwgX0aY4VyeixVprc", "lzA1Ql0PY2cVpzdLnD0VnywhqtD9AYJMsAfHIPxV");
        ParseInstallation.getCurrentInstallation().saveInBackground();

        setContentView(R.layout.main_screen);



//        PushService.setDefaultPushCallback(this, MainActivity.class);
//
//        ParseInstallation.getCurrentInstallation().saveInBackground();

        // Buttons
        btnViewProducts = (Button) findViewById(R.id.btnViewProducts);
        btnNewProduct = (Button) findViewById(R.id.btnCreateProduct);

        // view products click event
        btnViewProducts.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                // Launching Sign In activity
                Intent i = new Intent(getApplicationContext(), SignIn.class);
                startActivity(i);

            }
        });

        // view products click event
        btnNewProduct.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                // Launching Register Activity

                Intent i = new Intent(getApplicationContext(), Register.class);
                startActivity(i);

            }
        });
    }
}