package com.smartgroup.module1;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.smartgroup.module1.Config;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener {

    private EditText editTextUserName;
    private EditText editTextPassword;
    private Button buttonLogin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        SharedPreferences settings = getSharedPreferences(Config.PREFS_NAME, 0);
        boolean hasLoggedIn = settings.getBoolean("hasLoggedIn", false);
        if (hasLoggedIn) {
            String username = settings.getString("username", "");
            Intent intent = new Intent(LoginActivity.this,UserProfile.class);
            //intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            intent.putExtra(Config.USER_NAME, username);
            startActivity(intent);
            finish();
        }
        editTextUserName = (EditText) findViewById(R.id.username);
        editTextPassword = (EditText) findViewById(R.id.password);
        buttonLogin = (Button) findViewById(R.id.buttonUserLogin);
        buttonLogin.setOnClickListener(this);
    }


    private void login(){
        String username = editTextUserName.getText().toString().trim();
        String password = editTextPassword.getText().toString().trim();
        if (username.length() != 10) {
            Toast.makeText(LoginActivity.this,"Mobile Number should be of 10 digits.",Toast.LENGTH_LONG).show();
            return;
        }
        if (password.length() != 4) {
            Toast.makeText(LoginActivity.this,"PIN should be of 4 digits.",Toast.LENGTH_LONG).show();
            return;
        }
        userLogin(username, password);
    }

    private void userLogin(final String username, final String password){

        class UserLoginClass extends AsyncTask<String,Void,String>{

            ProgressDialog loading;

            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                loading = ProgressDialog.show(LoginActivity.this,"Please Wait...",null,true,true);
            }

            @Override
            protected void onPostExecute(String s) {
                super.onPostExecute(s);
                loading.dismiss();
                if (s.equalsIgnoreCase("success")) {
                    SharedPreferences settings = getSharedPreferences(Config.PREFS_NAME, 0);
                    SharedPreferences.Editor editor = settings.edit();
                    editor.putBoolean("hasLoggedIn", true);
                    editor.putString("username", username);
                    editor.commit();
                    Intent intent = new Intent(LoginActivity.this,UserProfile.class);
                    intent.putExtra(Config.USER_NAME, username);
                    startActivity(intent);
                    finish();
                } else {
                    Toast.makeText(LoginActivity.this,"Invalid Mobile Number or PIN.",Toast.LENGTH_LONG).show();
                }
            }

            @Override
            protected String doInBackground(String... params) {
                HashMap<String,String> data = new HashMap<>();
                data.put("username",params[0]);
                data.put("password",params[1]);
                RegisterUser ruc = new RegisterUser();
                String result = ruc.sendPostRequest(Config.LOGIN_URL,data);
                return result;
            }
        }
        UserLoginClass ulc = new UserLoginClass();
        ulc.execute(username,password);
    }

    @Override
    public void onClick(View v) {
        if (v == buttonLogin) {
            login();
        }
    }
}