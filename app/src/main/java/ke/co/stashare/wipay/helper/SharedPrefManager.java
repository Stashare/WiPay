package ke.co.stashare.wipay.helper;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * Created by Ken Wainaina on 22/02/2017.
 */

public class SharedPrefManager {

    private static final String SHARED_PREF_NAME = "WipaySharedPref";
    private static final String SIGNAL = "signal";
    private static final String ERROR = "error";
    private static final String PAYMENT_METHOD = "payment_method";
    private static final String CURRENT_GITHAA = "current_time";
    private static final String STORE_PAYMETH = "paymeth";

    private static SharedPrefManager mInstance;

    private static Context mCtx;

    private SharedPrefManager(Context context) {
        mCtx = context;
    }

    public static synchronized SharedPrefManager getInstance(Context context) {
        if (mInstance == null) {
            mInstance = new SharedPrefManager(context);
        }
        return mInstance;
    }

    //this method will save the Strongest Signal to shared preferences
    public boolean saveStrongestSignal(String token){
        SharedPreferences sharedPreferences = mCtx.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(SIGNAL, token);
        editor.apply();
        return true;
    }

    //this method will fetch the device token from shared preferences
    public String getStrongestSignal(){
        SharedPreferences sharedPreferences = mCtx.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
        return  sharedPreferences.getString(SIGNAL, null);
    }

    //this method will save the payment method to shared preferences
    public boolean savePaymethod(String methodpay){
        SharedPreferences sharedPreferences = mCtx.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(PAYMENT_METHOD, methodpay);
        editor.apply();
        return true;
    }

    //this method will fetch the payment methodfrom shared preferences
    public String getPaymentMethod(){
        SharedPreferences sharedPreferences = mCtx.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
        return  sharedPreferences.getString(PAYMENT_METHOD, null);
    }

    //this method will save the payment method to shared preferences
    public boolean saveError(String error){
        SharedPreferences sharedPreferences = mCtx.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(ERROR, error);
        editor.apply();
        return true;
    }

    //this method will fetch the payment methodfrom shared preferences
    public String getError(){
        SharedPreferences sharedPreferences = mCtx.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
        return  sharedPreferences.getString(ERROR, null);
    }

    //this method will save the payment method to shared preferences
    public boolean saveCurTime(long cur){
        SharedPreferences sharedPreferences = mCtx.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putLong(CURRENT_GITHAA, cur);
        editor.apply();
        return true;
    }

    final long currenttym =  System.currentTimeMillis();

    //this method will fetch the payment methodfrom shared preferences
    public long getCurrenttym(){
        SharedPreferences sharedPreferences = mCtx.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
        return  sharedPreferences.getLong(CURRENT_GITHAA, currenttym);
    }

    //this method will save the payment method to shared preferences
    public boolean savePaymeth(String paymeth){
        SharedPreferences sharedPreferences = mCtx.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(STORE_PAYMETH, paymeth);
        editor.apply();
        return true;
    }


    //this method will fetch the payment methodfrom shared preferences
    public String getPaymeth(){
        SharedPreferences sharedPreferences = mCtx.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
        return  sharedPreferences.getString(STORE_PAYMETH, null);
    }

}


//final String token = SharedPrefManager.getInstance(this).getDeviceToken();

//SharedPrefManager.getInstance(getApplicationContext()).saveDeviceToken(token);







