package com.smartgroup.module1;

/**
 * Created by Rahul Yadav on 4/10/2016.
 */
public class Config {

    /* LoginActivity Config */
    public static final String LOGIN_URL = "http://192.168.43.237/web1/login.php";
    public static final String TAG = "com.smartgroup.module1";
    public static final String PREFS_NAME = "MyPrefsFile";
    public static final String USER_NAME = "USER_NAME";
    //public static final String PASSWORD = "PASSWORD";

    /* UserProfile Config */
    public static final String CAR_URL = "http://192.168.43.237/web1/car.php";
    public static final String ACTION_SCAN = "com.google.zxing.client.android.SCAN";
    public static final String CAR_NUMBER = "CAR_NUMBER";
    public static final String JSON_ARRAY = "result";

    /* QualityCheck Config */
    public static final String QC_URL = "http://192.168.43.237/web1/qc.php";
    public static final String INSERT_QC_URL = "http://192.168.43.237/web1/insert_qc.php";
    public static final String KEY_INTERIOR = "Interior";
    public static final String KEY_EXTERIOR = "Exterior";
    public static final String KEY_ODOUR = "Odour";
    public static final String[] QC_FACTORS = { "", "Good", "Average", "Poor" };
}
