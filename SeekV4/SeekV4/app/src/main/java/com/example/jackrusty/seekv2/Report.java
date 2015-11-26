package com.example.jackrusty.seekv2;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Spinner;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;


public class Report extends Activity {
    EditText nameField;
    String pid;

    private ProgressDialog pDialog;
    Spinner feedbackSpinner ;
    Spinner SpinnerType ;
    EditText feedbackField ;
   // CheckBox responseCheckbox ;

    JSONParser jsonParser = new JSONParser();

    // url to create new product
    private static String url_report = "http://172.22.32.197/androidhive/create_report.php";
    private static final String TAG_SUCCESS = "success";
    private static final String TAG_PID = "pid";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getActionBar().setDisplayHomeAsUpEnabled(true);
//        Intent i = getIntent();
//        // getting product id (pid) from intent
//        pid = i.getStringExtra(TAG_PID);
        setContentView(R.layout.activity_report);
        nameField = (EditText) findViewById(R.id.EditTextName);
        feedbackSpinner = (Spinner) findViewById(R.id.SpinnerFeedbackType);
        SpinnerType = (Spinner) findViewById(R.id.SpinnerType);
        feedbackField = (EditText) findViewById(R.id.EditTextFeedbackBody);
       // responseCheckbox = (CheckBox) findViewById(R.id.CheckBoxResponse);
        // Do click handling here
        Button btnLinkToRegister = (Button) findViewById(R.id.ButtonSendFeedback);

        btnLinkToRegister.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                // creating new product in background thread
                new CreateNewReport().execute();
            }
        });

    }

    class CreateNewReport extends AsyncTask<String, String, String> {

        /**
         * Before starting background thread Show Progress Dialog
         * */
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog = new ProgressDialog(Report.this);
            pDialog.setMessage("Creating Report..");
            pDialog.setIndeterminate(false);
            pDialog.setCancelable(true);
            pDialog.show();

        }

        /**
         * Creating product
         * */
        protected String doInBackground(String... args) {
            String name = nameField.getText().toString();
            String Category = feedbackSpinner.getSelectedItem().toString();
            String Type = SpinnerType.getSelectedItem().toString();
            String Description = feedbackField.getText().toString();
//            String UID = pid;
            Log.d("Category", Category);

           // boolean Type = responseCheckbox.isChecked();
            // Building Parameters
            List<NameValuePair> params = new ArrayList<>();
            params.add(new BasicNameValuePair("name", name));
            params.add(new BasicNameValuePair("Category", Category));
            params.add(new BasicNameValuePair("Type", Type));
            params.add(new BasicNameValuePair("Description", Description));
//            params.add(new BasicNameValuePair("UID", UID));
            //params.add(new BasicNameValuePair("Type", String.valueOf(Type)));


            // getting JSON Object
            // Note that create product url accepts POST method
            JSONObject json = jsonParser.makeHttpRequest(url_report,
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
//                    i.putExtra(TAG_PID, pid);

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
