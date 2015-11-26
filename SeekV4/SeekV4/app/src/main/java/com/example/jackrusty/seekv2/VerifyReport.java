package com.example.jackrusty.seekv2;

/**
 * Created by jack.rusty on 26-10-2015.
 */
import java.util.ArrayList;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class VerifyReport extends Activity {

    EditText txtName;
    EditText txtCategory;
    EditText txtDescription;
    EditText txtType;
    Button btnApprove;
    Button btnReject;

    String rid;

    // Progress Dialog
    private ProgressDialog pDialog;

    // JSON parser class
    JSONParser jsonParser = new JSONParser();

    // single product url
    private static final String url_product_detials = "http://172.22.32.197/androidhive/get_report_details.php";

    // url to update product
    private static final String url_approve = "http://172.22.32.197/androidhive/approve_report.php";

    // url to delete product
    private static final String url_reject = "http://172.22.32.197/androidhive/reject_report.php";

    // JSON Node names
    private static final String TAG_SUCCESS = "success";
    private static final String TAG_STATUS = "status";
    private static final String TAG_PRODUCT = "product";
    private static final String TAG_RPID = "rid";
    private static final String TAG_NAME = "name";
    private static final String TAG_CATEGORY = "category";
    private static final String TAG_TYPE = "type";
    private static final String TAG_DESCRIPTION = "description";

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.verify_report);

        // save button
        btnApprove = (Button) findViewById(R.id.btnSave);
        btnReject = (Button) findViewById(R.id.btnDelete);

        Intent i = getIntent();
        // getting product id (pid) from intent
        rid = i.getStringExtra(TAG_RPID);

        // Getting complete product details in background thread
        new GetReportDetails().execute();

        // save button click event
        btnApprove.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {
                // starting background task to update product
                new ApproveThisReport().execute();
            }
        });

        // Delete button click event
        btnReject.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {
                // deleting product in background thread
                new DeleteProduct().execute();
            }
        });

    }

    class GetReportDetails extends AsyncTask<String, String, String> {

        /**
         * Before starting background thread Show Progress Dialog
         * */
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog = new ProgressDialog(VerifyReport.this);
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
            // Note that product details url will use GET request
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
                            // product with this pid found
                            // Edit Text
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

    /**
     * Background Async Task to  Save product Details
     * */
    class ApproveThisReport extends AsyncTask<String, String, String> {

        /**
         * Before starting background thread Show Progress Dialog
         * */
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog = new ProgressDialog(VerifyReport.this);
            pDialog.setMessage("Approving the report ...");
            pDialog.setIndeterminate(false);
            pDialog.setCancelable(true);
            pDialog.show();
        }

        /**
         * Saving product
         * */
        protected String doInBackground(String... args) {

            // getting updated data from EditTexts
            String status = "1";

            // Building Parameters
            List<NameValuePair> params = new ArrayList<>();
            params.add(new BasicNameValuePair(TAG_STATUS, status));
            params.add(new BasicNameValuePair(TAG_RPID, rid));
            // sending modified data through http request
            // Notice that update product url accepts POST method
            JSONObject json = jsonParser.makeHttpRequest(url_approve,
                    "POST", params);
            Log.d("update Response", json.toString());
            // check json success tag
            try {
                int success = json.getInt(TAG_SUCCESS);

                if (success == 1) {
                    // successfully updated
                    Intent i = new Intent(getApplicationContext(), Newsfeed_Admin.class);
                    startActivity(i);
                } else {
                    // failed to update product
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
            // dismiss the dialog once product uupdated
            pDialog.dismiss();
        }
    }

    /*****************************************************************
     * Background Async Task to Delete Product
     * */
    class DeleteProduct extends AsyncTask<String, String, String> {

        /**
         * Before starting background thread Show Progress Dialog
         * */
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog = new ProgressDialog(VerifyReport.this);
            pDialog.setMessage("Deleting Product...");
            pDialog.setIndeterminate(false);
            pDialog.setCancelable(true);
            pDialog.show();
        }

        /**
         * Deleting product
         * */
        protected String doInBackground(String... args) {
            List<NameValuePair> params = new ArrayList<>();
            params.add(new BasicNameValuePair("rid", rid));

            // getting product details by making HTTP request
            JSONObject json = jsonParser.makeHttpRequest(
                    url_reject, "POST", params);

            // check your log for json response
            Log.d("Delete Product", json.toString());
            // Check for success tag
            int success;
            try {
                // Building Parameters


                // json success tag
                success = json.getInt(TAG_SUCCESS);
                if (success == 1) {
                    Intent i = new Intent(getApplicationContext(), Newsfeed_Admin.class);
                    startActivity(i);
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
            // dismiss the dialog once product deleted
            pDialog.dismiss();

        }

    }
}