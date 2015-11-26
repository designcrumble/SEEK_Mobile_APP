package com.example.jackrusty.seekv2;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Updated on 31-10-2015.
 */
public class SignIn extends Activity {

    private ProgressDialog pDialog;
    Button btnLinkToRegister;

    JSONParser jsonParser = new JSONParser();
    EditText inputName;
    EditText inputPass;
    String pid;
    // url to create new product
    private static String url_login = "http://172.22.32.197/androidhive/login.php";



    // JSON Node names
    private static final String TAG_SUCCESS = "success";
    private static final String TAG_PID = "pid";
//    private static final String TAG_PRODUCTS = "products";

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getActionBar().setDisplayHomeAsUpEnabled(true);
        setContentView(R.layout.sign_in);

        // Link to Register Screen

//        btnLinkToRegister.setOnClickListener(new View.OnClickListener() {
//
//            public void onClick(View view) {
//                Intent i = new Intent(getApplicationContext(),
//                        Register.class);
//                startActivity(i);
//                finish();
//            }
//        });

        // Edit Text
        inputName = (EditText) findViewById(R.id.inputName);
        inputPass = (EditText) findViewById(R.id.inputPass);

        // Create button
        Button btnCreateProduct = (Button) findViewById(R.id.btnCreateProduct);
        Button btnLinkToRegister = (Button) findViewById(R.id.btnLinkToRegisterScreen);

        //Create Preloader
//        spinner=(ProgressBar)findViewById(R.id.progressBar);
//        spinner.setVisibility(View.GONE);

        // button click event
        btnLinkToRegister.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                // Launching Sign In activity
                Intent i = new Intent(getApplicationContext(), Register.class);
                startActivity(i);

            }
        });

        // button click event
        btnCreateProduct.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {

                new Credentials().execute();
            }
        });
    }

    /**
     * Background Async Task to Create new product
     */
    class Credentials extends AsyncTask<String, String, String> {

        /**
         * Before starting background thread Show Progress Dialog
         */
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog = new ProgressDialog(SignIn.this);
            pDialog.setMessage("Logging in");
            pDialog.setIndeterminate(false);
            pDialog.setCancelable(true);
            pDialog.show();

        }

        /**
         * Creating product
         */
        protected String doInBackground(String... args) {

            String name = inputName.getText().toString();
            String pass = inputPass.getText().toString();
            // Building Parameters
            List<NameValuePair> params = new ArrayList<>();
            params.add(new BasicNameValuePair("name", name));
            params.add(new BasicNameValuePair("pass", pass));

            // getting JSON Object
            // Note that create product url accepts POST method
            JSONObject json = jsonParser.makeHttpRequest(url_login,
                    "GET", params);

            // check log cat fro response
            Log.d("Create Response", json.toString());

            // check for success tag
            try {
                int success = json.getInt(TAG_SUCCESS);

                if (success == 1) {
                    pid=json.getString(TAG_PID);
//                    JSONArray productObj = json
//                           .getJSONArray(TAG_PRODUCTS); // JSON Array
//
//                    // get first product object from JSON Array
//                    JSONObject product = productObj.getJSONObject(0);
//                    pid=product.getString(TAG_PID);
                    Log.d("UID", pid);
                    Intent in = new Intent(getApplicationContext(),
                            Newsfeed.class);
                    // sending pid to next activity
                    in.putExtra(TAG_PID, pid);

                    // starting new activity and expecting some response back
                    startActivity(in);
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
            return json.toString();
        }

        /**
         * After completing background task Dismiss the progress dialog
         **/
        protected void onPostExecute(String file_url) {
            // dismiss the dialog once done
            pDialog.dismiss();

        }

    }
}
//    @Override
//    public boolean onOptionsItemSelected(MenuItem item) {
//        return super.onOptionsItemSelected(item);
//        case android.R.id.home:
//        this.finish();
//        return true;
//    }



