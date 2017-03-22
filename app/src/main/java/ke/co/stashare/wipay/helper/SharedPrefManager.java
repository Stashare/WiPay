package ke.co.stashare.wipay.helper;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * Created by Ken Wainaina on 22/02/2017.
 */

public class SharedPrefManager {

    private static final String SHARED_PREF_NAME = "WipaySharedPref";
    private static final String SIGNAL = "signal";

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


}
