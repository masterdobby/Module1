package com.smartgroup.module1;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Rahul Yadav on 4/1/2016.
 */
public class QualityCheck extends AppCompatActivity implements View.OnClickListener {

    private HashMap<String, String> data, answer;
    private String carNumber, Exterior = "NA", Interior = "NA", Odour = "NA";
    private Button qcSubmit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.quality_check);
        qcSubmit = new Button(this);
        qcSubmit.setText("Submit");
        qcSubmit.setOnClickListener(this);
        Intent intent = getIntent();
        carNumber = intent.getStringExtra(Config.CAR_NUMBER);
        setTitle("Quality Check: " + carNumber);
        qualityCheck(carNumber);
    }

    @Override
    public void onClick(View v) {
        for(final Map.Entry<String, String> entry : data.entrySet()) {
            if (answer.get(entry.getKey()).equals("0")) {
                Toast.makeText(QualityCheck.this,"Please select an option for " + entry.getKey() + ".",
                        Toast.LENGTH_LONG).show();
                return;
            }
        }
        insertQC();
    }

    private void qualityCheck(String carNumber) {

        class QCDetailsClass extends AsyncTask<String,Void,String> {

            ProgressDialog loading;

            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                loading = ProgressDialog.show(QualityCheck.this,"Please Wait...",null,true,true);
            }

            @Override
            protected void onPostExecute(String s) {
                super.onPostExecute(s);
                loading.dismiss();
                //Log.v(Config.TAG, "Result: " + s);
                showQC(s);
            }

            @Override
            protected String doInBackground(String... params) {
                //Log.v(Config, "Size: " + params.length);
                HashMap<String,String> data = new HashMap<>();
                data.put("carNumber", params[0]);
                RegisterUser ruc = new RegisterUser();
                String result = ruc.sendPostRequest(Config.QC_URL,data);
                return result;
            }
        }
        QCDetailsClass qcDetailsClass = new QCDetailsClass();
        qcDetailsClass.execute(carNumber);
    }

    private void insertQC() {

        class InsertQCDetailsClass extends AsyncTask<String,Void,String> {

            ProgressDialog loading;

            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                loading = ProgressDialog.show(QualityCheck.this,"Please Wait...",null,true,true);
            }

            @Override
            protected void onPostExecute(String s) {
                super.onPostExecute(s);
                loading.dismiss();
                if (s.equalsIgnoreCase("success")) {
                    Toast.makeText(QualityCheck.this,"Quality Check details uploaded successfully.",Toast.LENGTH_LONG).show();
                    finish();
                } else {
                    Toast.makeText(QualityCheck.this,"Quality Check details could not be uploaded.",Toast.LENGTH_LONG).show();
                }
            }

            @Override
            protected String doInBackground(String... params) {
                //Log.v(Config, "Size: " + params.length);
                HashMap<String,String> data = new HashMap<>();
                data.put("carNumber", carNumber);
                data.put(Config.KEY_EXTERIOR, Exterior);
                data.put(Config.KEY_INTERIOR, Interior);
                data.put(Config.KEY_ODOUR, Odour);
                RegisterUser ruc = new RegisterUser();
                String result = ruc.sendPostRequest(Config.INSERT_QC_URL,data);
                return result;
            }
        }
        InsertQCDetailsClass insertQCDetailsClass = new InsertQCDetailsClass();
        insertQCDetailsClass.execute(carNumber, Exterior, Interior, Odour);
    }

    private void showQC(String res) {
        try {
            JSONObject jsonObject = new JSONObject(res);
            final JSONArray result = jsonObject.getJSONArray(Config.JSON_ARRAY);
            JSONObject qcData = result.getJSONObject(0);
            data = new HashMap<>();
            answer = new HashMap<>();
            data.put(Config.KEY_INTERIOR, qcData.getString(Config.KEY_INTERIOR));
            data.put(Config.KEY_EXTERIOR, qcData.getString(Config.KEY_EXTERIOR));
            data.put(Config.KEY_ODOUR, qcData.getString(Config.KEY_ODOUR));
            /*data.put("Smell", "1");
            data.put("Fell", "1");
            data.put("Hell", "1");*/
            //Log.v(Config.TAG, "Interior:\t" + interior + "\nExterior:\t" + exterior + "\nOdour:\t" + odour);
            int count = 1;
            LinearLayout linearLayout = (LinearLayout) findViewById(R.id.qc_layout);
            for(final Map.Entry<String, String> entry : data.entrySet()) {
                Log.v(Config.TAG, entry.getKey() + ": " + entry.getValue() + "\n");
                if (entry.getValue().equals("1")) {
                    RadioGroup radioGroup = new RadioGroup(this);
                    //radioGroup.setId(count);
                    RadioGroup.LayoutParams layoutParams = new RadioGroup.LayoutParams(
                            RadioGroup.LayoutParams.MATCH_PARENT,
                            RadioGroup.LayoutParams.WRAP_CONTENT
                    );
                    TextView textView = new TextView(this);
                    textView.setText(entry.getKey() + ":");
                    radioGroup.addView(textView, layoutParams);
                    answer.put(entry.getKey(), "0");
                    for (int i = 1; i <= 3; i++) {
                        final String qcFactor = Config.QC_FACTORS[i];
                        RadioButton radioButton = new RadioButton(this);
                        radioButton.setId(count * i);
                        radioButton.setText(qcFactor);
                        radioGroup.addView(radioButton);
                        radioButton.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                answer.put(entry.getKey(), "1");
                                if (entry.getKey().equals(Config.KEY_EXTERIOR)) {
                                    Exterior = qcFactor;
                                } else if (entry.getKey().equals(Config.KEY_INTERIOR)) {
                                    Interior = qcFactor;
                                } else if (entry.getKey().equals(Config.KEY_ODOUR)) {
                                    Odour = qcFactor;
                                }
                            }
                        });
                    }
                    linearLayout.addView(radioGroup, layoutParams);
                }
                count++;
            }
            linearLayout.addView(qcSubmit);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}
