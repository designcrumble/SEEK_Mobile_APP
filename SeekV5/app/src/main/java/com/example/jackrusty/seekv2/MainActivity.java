package com.example.jackrusty.seekv2;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.view.Window;
import android.widget.Button;

public class MainActivity extends Activity{

    Button btnViewProducts;
    Button btnNewProduct;
    Button btnNewAdmin;
    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        return false;
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().requestFeature(Window.FEATURE_ACTION_BAR);
        getActionBar().hide();

        setContentView(R.layout.main_screen);
        // Buttons
        btnViewProducts = (Button) findViewById(R.id.btnViewProducts);
        btnNewProduct = (Button) findViewById(R.id.btnCreateProduct);
        btnNewAdmin = (Button) findViewById(R.id.btnNewAdmin);

        btnViewProducts.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                // Launching Sign In activity
                Intent i = new Intent(getApplicationContext(), SignIn.class);
                startActivity(i);

            }
        });

        btnNewProduct.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                // Launching Register Activity
                Intent i = new Intent(getApplicationContext(), Register.class);
                startActivity(i);

            }
        });

        btnNewAdmin.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                // Launching Admin SignIn Activity
                Intent i = new Intent(getApplicationContext(), AdminSignIn.class);
                startActivity(i);

            }
        });
    }
}