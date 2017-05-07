package ke.co.stashare.wipay.ui;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.transition.Slide;
import android.util.Log;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.RadioButton;
import android.widget.Toast;

import com.google.gson.Gson;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;

import ke.co.stashare.wipay.R;
import ke.co.stashare.wipay.helper.SharedPrefManager;
import ke.co.stashare.wipay.model.OldWifi;
import ke.co.stashare.wipay.model.PayMethList;


/**
 * Created by Ken Wainaina on 15/03/2017.
 */

public class Main extends AppCompatActivity {

    String mpesa = null;
    String airtel = null;
    String equitel = null;
    String orange = null;

    List<String> paymeth;
    ArrayList<String> temp_paymeth;

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.intro);

        paymeth= new ArrayList<>();

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            getWindow().setStatusBarColor(getResources().getColor(R.color.type_message_dark));
        }

        mpesa= "mpesa_paybill";

        SharedPrefManager.getInstance(getApplicationContext()).savePaymeth(mpesa);

        paymeth.add("M-PESA");
        paymeth.add("EQUITEL");


        PayMethList payMethList = new PayMethList(paymeth);

        save_paymeth_list(getApplicationContext(), payMethList);

        Log.d("TAG", String.valueOf(paymeth));


        //setupWindowAnimations();

        //setupToolbar();
    }


    public void addHotspotClick(View view) {

        startActivity(new Intent(Main.this, AddHotspot.class));
    }


    public void radioButtonClicked(View view) {


        boolean checked = ((RadioButton) view).isChecked();


        // This check which radio button was clicked
        switch (view.getId()) {
            case R.id.radioButton1:
                if (checked)
                    mpesa= "mpesa_paybill";

                SharedPrefManager.getInstance(getApplicationContext()).savePaymeth(mpesa);

                paymeth.clear();

                paymeth.add("M-PESA");
                paymeth.add("EQUITEL");

                PayMethList payMethList = new PayMethList(paymeth);

                save_paymeth_list(getApplicationContext(), payMethList);

                 //Do something when radio button is clicked
                    //Toast.makeText(getApplicationContext(), mpesa, Toast.LENGTH_SHORT).show();

                break;

            case R.id.radioButton2:

                paymeth.clear();

                equitel="equity_acc";

                SharedPrefManager.getInstance(getApplicationContext()).savePaymeth(equitel);

                paymeth.add("EQUITEL");
                paymeth.add("M-PESA");


                payMethList = new PayMethList(paymeth);

                save_paymeth_list(getApplicationContext(), payMethList);
                //Do something when radio button is clicked
                //Toast.makeText(getApplicationContext(), equitel, Toast.LENGTH_SHORT).show();
                break;



    }
        PayMethList get_payMethList = get_paymethlist(getApplicationContext());

        paymeth = get_payMethList.getResults();

        Log.d("TAG_B", String.valueOf(paymeth));

}



    public void nextButton(View view) {


        Intent openHomepage = new Intent(Main.this, TheMainClass.class);

        startActivity(openHomepage);

    }

    public static void save_paymeth_list(Context context, PayMethList _payMethList) {
        SharedPreferences appSharedPrefs = PreferenceManager
                .getDefaultSharedPreferences(context.getApplicationContext());
        SharedPreferences.Editor prefsEditor = appSharedPrefs.edit();
        Gson gson = new Gson();
        String json = gson.toJson(_payMethList);
        prefsEditor.putString("payMeth", json);
        prefsEditor.apply();

    }

    public static PayMethList get_paymethlist(Context context) {

        SharedPreferences appSharedPrefs = PreferenceManager
                .getDefaultSharedPreferences(context.getApplicationContext());
        Gson gson = new Gson();
        String json = appSharedPrefs.getString("payMeth", "");


        PayMethList payMethList = gson.fromJson(json, PayMethList.class);
        return payMethList;
    }

    /*
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_finish, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_refresh) {
            //refresh();
        }

        return super.onOptionsItemSelected(item);

    }
    **/

}
