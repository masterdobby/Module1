package com.smartgroup.module1;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.ActivityNotFoundException;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.nfc.Tag;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.util.HashMap;

/**
 * Created by Rahul Yadav on 3/29/2016.
 */

public class UserProfile extends AppCompatActivity implements View.OnClickListener {

    private TextView tvUsername;
    private EditText edCarNumber;
    private Button bQR, bSubmit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.user_activity);
        tvUsername = (TextView) findViewById(R.id.textViewUsername);
        edCarNumber = (EditText) findViewById(R.id.editTextCarNumber);
        bQR = (Button) findViewById(R.id.buttonQR);
        bSubmit = (Button) findViewById(R.id.buttonSubmit);
        bQR.setOnClickListener(this);
        bSubmit.setOnClickListener(this);
        Intent intent = getIntent();
        String username = intent.getStringExtra(Config.USER_NAME);
        tvUsername.setText("Welcome " + username);
    }

    @Override
    public void onClick(View v) {
        if (v == bQR) {
            try {
                Intent intent = new Intent(Config.ACTION_SCAN);
                intent.putExtra("SCAN_MODE", "QR_CODE_MODE");
                startActivityForResult(intent, 0);
            } catch (ActivityNotFoundException e) {
                showDialog(UserProfile.this, "No Scanner Found", "Download a scanner code activity?",
                        "Yes", "No").show();
            }
        } else if (v == bSubmit) {
            String carNumber = edCarNumber.getText().toString().trim().toUpperCase();
            if (carNumber.length() == 0) {
                Toast.makeText(UserProfile.this, "Car Number field cannot be empty.",
                        Toast.LENGTH_LONG).show();
                return;
            }
            checkCarNumber(carNumber);
        }
    }

    private void checkCarNumber(final String carNumber) {

        class CarNumberClass extends AsyncTask<String,Void,String> {

            ProgressDialog loading;

            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                loading = ProgressDialog.show(UserProfile.this,"Please Wait...",null,true,true);
            }

            @Override
            protected void onPostExecute(String s) {
                super.onPostExecute(s);
                loading.dismiss();
                if (s.equalsIgnoreCase("success")) {
                    startQualityCheck(carNumber);
                } else {
                    Toast.makeText(UserProfile.this,"Invalid Car Number.",Toast.LENGTH_LONG).show();
                }
            }

            @Override
            protected String doInBackground(String... params) {
                //Log.v(Config.TAG, "Size: " + params.length);
                HashMap<String,String> data = new HashMap<>();
                data.put("carNumber", params[0]);
                RegisterUser ruc = new RegisterUser();
                String result = ruc.sendPostRequest(Config.CAR_URL,data);
                return result;
            }
        }
        CarNumberClass carNumberClass = new CarNumberClass();
        carNumberClass.execute(carNumber);
    }

    //alert dialog for downloadDialog
    private AlertDialog showDialog(final Activity act, CharSequence title, CharSequence message, CharSequence buttonYes, CharSequence buttonNo) {
        AlertDialog.Builder downloadDialog = new AlertDialog.Builder(act);
        downloadDialog.setTitle(title);
        downloadDialog.setMessage(message);
        downloadDialog.setPositiveButton(buttonYes, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialogInterface, int i) {
                Uri uri = Uri.parse("market://search?q=pname:" + "com.google.zxing.client.android");
                Intent intent = new Intent(Intent.ACTION_VIEW, uri);
                try {
                    act.startActivity(intent);
                } catch (ActivityNotFoundException e) {
                }
            }
        });
        downloadDialog.setNegativeButton(buttonNo, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialogInterface, int i) {
            }
        });
        return downloadDialog.show();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent intent) {
        if (requestCode == 0) {
            if (resultCode == RESULT_OK) {
                //get the extras that are returned from the intent
                String contents = intent.getStringExtra("SCAN_RESULT");
                //String format = intent.getStringExtra("SCAN_RESULT_FORMAT");
                //Toast.makeText(this, "Content:" + contents + " Format:" + format, Toast.LENGTH_LONG).show();
                startQualityCheck(contents);
            }
        } else if (requestCode == 1) {
            if (resultCode == RESULT_OK) {
                edCarNumber.setText("");
            }
        }
    }

    private void startQualityCheck(String s) {
        Intent intent = new Intent(UserProfile.this, QualityCheck.class);
        intent.putExtra(Config.CAR_NUMBER, s);
        startActivityForResult(intent, 1);
    }
}
