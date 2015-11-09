package com.example.jackrusty.login;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Updated on 31-10-2015.
 */
public class SignIn extends Activity {

    private ProgressDialog pDialog;
    Button btnLinkToRegister = (Button) findViewById(R.id.btnLinkToRegisterScreen);

    JSONParser jsonParser = new JSONParser();
    EditText inputName;
    EditText inputPass;

    // url to create new product
    private static String url_login = "http://172.22.11.181/androidhive/login.php";



    // JSON Node names
    private static final String TAG_SUCCESS = "success";

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.sign_in);

        // Link to Register Screen

        btnLinkToRegister.setOnClickListener(new View.OnClickListener() {

            public void onClick(View view) {
                Intent i = new Intent(getApplicationContext(),
                        Register.class);
                startActivity(i);
                finish();
            }
        });

        // Edit Text
        inputName = (EditText) findViewById(R.id.inputName);
        inputPass = (EditText) findViewById(R.id.inputPass);

        // Create button
        Button btnCreateProduct = (Button) findViewById(R.id.btnCreateProduct);

        // button click event
        btnCreateProduct.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                // creating new product in background thread
                new Credentials().execute();
            }
        });
    }

    /**
     * Background Async Task to Create new product
     * */
    class Credentials extends AsyncTask<String, String, String> {

        /**
         * Before starting background thread Show Progress Dialog
         * */
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
         * */
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
                    "POST", params);

            // check log cat fro response
            Log.d("Create Response", json.toString());

            // check for success tag
            try {
                int success = json.getInt(TAG_SUCCESS);

                if (success == 1) {
                    // successfully created product
                    Intent i = new Intent(getApplicationContext(), Newsfeed.class);
                    startActivity(i);

                    // closing this screen
                    finish();
                } else {
                    // failed to create product
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }

            return json.toString();
        }

        /**
         * After completing background task Dismiss the progress dialog
         * **/
        protected void onPostExecute(String file_url) {
            // dismiss the dialog once done
            pDialog.dismiss();

        }

    }



}


