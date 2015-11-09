package com.example.jackrusty.login;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
//import android.view.Menu;
//import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

public class MainActivity extends Activity{

    Button btnViewProducts;
    Button btnNewProduct;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_screen);

        // Buttons
        btnViewProducts = (Button) findViewById(R.id.btnViewProducts);
        btnNewProduct = (Button) findViewById(R.id.btnCreateProduct);

        // view products click event
        btnViewProducts.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                // Launching All products Activity
                Intent i = new Intent(getApplicationContext(), SignIn.class);
                startActivity(i);

            }
        });

        // view products click event
        btnNewProduct.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                // Launching create new product activity
                Intent i = new Intent(getApplicationContext(), Register.class);
                startActivity(i);

            }
        });
    }
}