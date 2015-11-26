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

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by jack.rusty on 31-10-2015.
 */
public class ViewItemDetails extends Activity {
    EditText txtName;
    EditText txtCategory;
    EditText txtType;
    EditText txtDescription;
    Button btnBack;
    Button btnClaim;
    String rid;
    // Progress Dialog
    private ProgressDialog pDialog;

    // JSON parser class
    JSONParser jsonParser = new JSONParser();

    // single report url
    private static final String url_product_detials = "http://172.22.11.16/androidhive/get_report_details.php";

    // JSON Node names
    private static final String TAG_SUCCESS = "success";
    private static final String TAG_PRODUCT = "product";
    private static final String TAG_RPID = "rid";
    private static final String TAG_NAME = "name";
    private static final String TAG_CATEGORY = "category";
    private static final String TAG_TYPE = "type";
    private static final String TAG_DESCRIPTION = "description";

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.view_details);

        btnBack = (Button) findViewById(R.id.btnBack);
        btnClaim = (Button) findViewById(R.id.btnClaim);
        // getting product details from intent
        Intent i = getIntent();
        // getting product id (rid) from intent
        rid = i.getStringExtra(TAG_RPID);

        // Getting complete report details in background thread
        new GetReportDetails().execute();

        // Back button click event
        btnBack.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {
                // deleting product in background thread
                Intent i = new Intent(getApplicationContext(), Newsfeed.class);
                startActivity(i);
            }
        });
        // Claim button click event
        btnClaim.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {
                // deleting product in background thread
                Intent i = new Intent(getApplicationContext(), Claim.class);
                i.putExtra(TAG_RPID, rid);
                startActivity(i);
            }
        });
    }

    /**
     * Background Async Task to Get complete report details
     * */
    class GetReportDetails extends AsyncTask<String, String, String> {
        /**
         * Before starting background thread Show Progress Dialog
         * */
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog = new ProgressDialog(ViewItemDetails.this);
            pDialog.setMessage("Loading Report details. Please wait...");
            pDialog.setIndeterminate(false);
            pDialog.setCancelable(true);
            pDialog.show();
        }
        /**
         * Getting product details in background thread
         * */
        protected String doInBackground(String... args) {
            List<NameValuePair> params = new ArrayList<>();
            params.add(new BasicNameValuePair("rid", rid));
            // getting product details by making HTTP request
            // Note that report details url will use GET request
            final JSONObject json = jsonParser.makeHttpRequest(
                    url_product_detials, "GET", params);

            // check your log for json response
            Log.d("Single Report Details", json.toString());
            // updating UI from Background Thread
            runOnUiThread(new Runnable() {
                public void run() {
                    // Check for success tag
                    int success;
                    try {
                        // json success tag
                        success = json.getInt(TAG_SUCCESS);
                        if (success == 1) {
                            // successfully received product details
                            JSONArray productObj = json
                                    .getJSONArray(TAG_PRODUCT); // JSON Array

                            // get first product object from JSON Array
                            JSONObject product = productObj.getJSONObject(0);

                            txtName = (EditText) findViewById(R.id.inputName);
                            txtCategory = (EditText) findViewById(R.id.inputCategory);
                            txtType = (EditText) findViewById(R.id.inputType);
                            txtDescription = (EditText) findViewById(R.id.inputDescription);

                            // display product data in EditText
                            txtName.setText(product.getString(TAG_NAME));
                            txtCategory.setText(product.getString(TAG_CATEGORY));
                            txtType.setText(product.getString(TAG_TYPE));
                            txtDescription.setText(product.getString(TAG_DESCRIPTION));

                        }else{
                            // product with pid not found
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            });

            return json.toString();
        }

        /**
         * After completing background task Dismiss the progress dialog
         * **/
        protected void onPostExecute(String file_url) {
            // dismiss the dialog once got all details
            pDialog.dismiss();
        }
    }


}
