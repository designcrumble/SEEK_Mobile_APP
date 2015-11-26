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
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;


public class Claim extends Activity {
    EditText nameField;
    String rid;

    private ProgressDialog pDialog;
    EditText feedbackField ;

    JSONParser jsonParser = new JSONParser();

    // url to create new Claim
    private static String url_claim = "http://172.22.11.16/androidhive/claim_report.php";
    private static final String TAG_SUCCESS = "success";
    private static final String TAG_RPID = "rid";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getActionBar().setDisplayHomeAsUpEnabled(true);
        Intent i = getIntent();
        // getting product id (rid) from intent
        rid = i.getStringExtra(TAG_RPID);
        setContentView(R.layout.activity_claim);
        nameField = (EditText) findViewById(R.id.EditTextName);
        feedbackField = (EditText) findViewById(R.id.EditTextFeedbackBody);

        Button btnLinkToRegister = (Button) findViewById(R.id.ButtonSendFeedback);

        btnLinkToRegister.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                // creating new Claim in background thread
                new CreateNewClaim().execute();
            }
        });

    }

    class CreateNewClaim extends AsyncTask<String, String, String> {

        /**
         * Before starting background thread Show Progress Dialog
         * */
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog = new ProgressDialog(Claim.this);
            pDialog.setMessage("Claiming Report..");
            pDialog.setIndeterminate(false);
            pDialog.setCancelable(true);
            pDialog.show();

        }

        /**
         * Creating claim
         * */
        protected String doInBackground(String... args) {
            String name = nameField.getText().toString();
            String Description = feedbackField.getText().toString();

            // Building Parameters
            List<NameValuePair> params = new ArrayList<>();
            params.add(new BasicNameValuePair("name", name));
            params.add(new BasicNameValuePair("Description", Description));
            params.add(new BasicNameValuePair("rid", rid));

            // getting JSON Object
            // Note that claim url accepts POST method
            JSONObject json = jsonParser.makeHttpRequest(url_claim,
                    "POST", params);

            // check log cat for response
            Log.d("Create Response", json.toString());

            // check for success tag
            try {
                int success = json.getInt(TAG_SUCCESS);

                if (success == 1) {
                    // successfully created claim
                    Intent i = new Intent(getApplicationContext(), Newsfeed.class);
                    startActivity(i);
                    finish();
                } else {
                    // failed to create claim
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
