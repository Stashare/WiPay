package ke.co.stashare.wipay.ui;

import android.annotation.TargetApi;
import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.net.wifi.ScanResult;
import android.net.wifi.WifiManager;
import android.os.Build;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.RequiresApi;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.util.Pair;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.transition.Slide;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.gson.Gson;
import com.yqritc.recyclerviewflexibledivider.HorizontalDividerItemDecoration;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import ke.co.stashare.wipay.Adapters.RecyclerAdapter;
import ke.co.stashare.wipay.Adapters.SpinnerAdapter;
import ke.co.stashare.wipay.Adapters.ThreeStrongAdapter;
import ke.co.stashare.wipay.R;
import ke.co.stashare.wipay.helper.SharedPrefManager;
import ke.co.stashare.wipay.helper.User;
import ke.co.stashare.wipay.model.AddLevel;
import ke.co.stashare.wipay.model.CheckWifiExistance;
import ke.co.stashare.wipay.model.Elements;
import ke.co.stashare.wipay.model.HotSpotDetails;
import ke.co.stashare.wipay.model.Hotspots;
import ke.co.stashare.wipay.model.OldWifi;
import ke.co.stashare.wipay.model.PayMethList;
import ke.co.stashare.wipay.model.SimulateClass;
import ke.co.stashare.wipay.model.WooiLEVEL;
import ke.co.stashare.wipay.registration.Config;
import ke.co.stashare.wipay.registration.MyApplication;

/**
 * Created by Ken Wainaina on 31/03/2017.
 */

public class TheMainClass extends AppCompatActivity implements ThreeStrongAdapter.Callbacks{

    private List<Elements> mainList;

    private List<Elements> mainList2 = new ArrayList<>();
    private Spinner mSpinner;
    private List<Elements> dummyList = new ArrayList<>();
    private List<Elements> dummyList2 = new ArrayList<>();
    private RecyclerView recyclerView;
    Toolbar mToolbar;
    private ThreeStrongAdapter elementsAdapter;
    private List<String> pay_methods;
    String method;

    RecyclerAdapter adapter2;
    TextView empty;
    ProgressDialog dialog;
    private static final String TAG = "TAG";
    String wifi_name = null;
    String payB;
    String amount;
    String password;

    List<ScanResult> temp_result;
    List<ScanResult> results;
    List<ScanResult> old_results;
    DividerItemDecoration mDividerItemDecoration;
    List<Hotspots> checkSpots;
    List<ScanResult> finalOutput;
    List<ScanResult> old_list;
    List<ScanResult>temp_final;
    List<AddLevel>temp_final2;
    List<Elements> checkWifi;
    List<WooiLEVEL> checki;
    List<Integer> rst;
    //our database reference object
    DatabaseReference databasePaybills;
    JSONArray biz;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_load_more);


        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            getWindow().setStatusBarColor(getResources().getColor(R.color.type_message_dark));
        }

        //getting the reference of artists node
        databasePaybills = FirebaseDatabase.getInstance().getReference("Registered_Hotspots");

        //Accessing the Wi-Fi Manager

        String service = Context.WIFI_SERVICE;
        final WifiManager wifi = (WifiManager)getSystemService(service);


          //Monitoring and changing Wi-Fi state

        if (!wifi.isWifiEnabled())
            if (wifi.getWifiState() != WifiManager.WIFI_STATE_ENABLING)
                wifi.setWifiEnabled(true);


        mToolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(mToolbar);

        final ActionBar actionBar = getSupportActionBar();
        assert actionBar != null;
        actionBar.setTitle("");
        actionBar.setDisplayShowTitleEnabled(true);

        mainList = new ArrayList<>();


        PayMethList get_payMethList = get_paymethlist(getApplicationContext());

        pay_methods = get_payMethList.getResults();

        //Log.d("TAG_A", String.valueOf(pay_methods));

        mSpinner=(Spinner)findViewById(R.id.spinner_rss);


        SpinnerAdapter adapter = new SpinnerAdapter(actionBar.getThemedContext(), pay_methods);
        mSpinner.setAdapter(adapter);


        if(Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP)
        {
            mSpinner.setDropDownVerticalOffset(-116);
        }


        mSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {


                final String choice = SharedPrefManager.getInstance(TheMainClass.this).getPaymeth();

                //Log.d("TAG_CHOICE", String.valueOf(choice));

               String first = pay_methods.get(0);

                if(!Objects.equals(first, pay_methods.get(i))) {
                    method = pay_methods.get(i);

                    SharedPrefManager.getInstance(getApplicationContext()).savePaymeth(convertStringToDBColumns(method));

                    final String choice2 = SharedPrefManager.getInstance(TheMainClass.this).getPaymeth();

                    //Log.d("TAG_CHOICE_AFTER", String.valueOf(choice2));

                    String output = "Payment method changed to: " + method;

                    Snackbar snackbar = Snackbar.make(view, output, Snackbar.LENGTH_LONG)
                            .setAction("Action", null);
                    View sbView = snackbar.getView();
                    sbView.setBackgroundColor(ContextCompat.getColor(TheMainClass.this, R.color.type_message_dark));
                    snackbar.show();
                }else{

                    SharedPrefManager.getInstance(getApplicationContext()).savePaymeth(convertStringToDBColumns(first));

                    final String choice2 = SharedPrefManager.getInstance(TheMainClass.this).getPaymeth();

                    //Log.d("TAG_CHOICE_AF", String.valueOf(choice2));

                }

            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });




        empty = (TextView)findViewById(R.id.empty_view);

        dialog = new ProgressDialog(TheMainClass.this);
        dialog.setMessage("Scanning, please wait...");
        dialog.show();

        temp_result= new ArrayList<>();

        checki= new ArrayList<>();
        checkWifi = new ArrayList<>();

        //list to store artists
        checkSpots = new ArrayList<>();

        finalOutput= new ArrayList<>();

        temp_final= new ArrayList<>();
        rst = new ArrayList<>();



        recyclerView = (RecyclerView) findViewById(R.id.my_recycler_view);

        recyclerView.setHasFixedSize(true);

        LinearLayoutManager llm = new LinearLayoutManager(this);

        recyclerView.setLayoutManager(llm);

        /*
        adapter2 =new RecyclerAdapter(finalOutput, new RecyclerAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(ScanResult item) {
                wifi_name= item.SSID;


            }
        },new RecyclerAdapter.OnItemLongClickListener(){
            @Override
            public void onItemLongClicked(ScanResult item) {
                wifi_name= item.SSID;

            }


        });

        //recyclerView.setAdapter(adapter2);

**/



     /*   SimulateClass get_simu=get_simulatedList(getApplicationContext());

        mainList = get_simu.getResults();
*/


        /*
        // added all elements to main list
        for(int i = 0; i<3; ++i) {
            Elements elements = new Elements();
            elements.setLogo(R.drawable.placeholder);
            elements.setTitle("Java House "+i);
            elements.setDesc("Kipro Centre - Westlands, Nairobi " +i);

            mainList2.add(elements);
        }
        **/

        Log.d("TAG_mainList", String.valueOf(checkWifi.size()));

        if(checkWifi.size()<=1){

            // now only add number of elements for the first show i.e. 1
            for(int i = 0; i<checkWifi.size(); ++i) {
                dummyList.add(checkWifi.get(i));
            }
            elementsAdapter = new ThreeStrongAdapter(TheMainClass.this,dummyList);
            elementsAdapter.setCallback(this);
            elementsAdapter.setWithFirstElement(true);
            elementsAdapter.setWithFooter(false); //enabling footer to show
            recyclerView.setAdapter(elementsAdapter);
        }
        else {
            // now only add number of elements for the first show i.e. 1
            for(int i = 0; i<1; ++i) {
                dummyList.add(checkWifi.get(i));
            }
            elementsAdapter = new ThreeStrongAdapter(TheMainClass.this,dummyList);
            elementsAdapter.setCallback(this);
            elementsAdapter.setWithFirstElement(true);
            elementsAdapter.setWithFooter(true); //enabling footer to show
            recyclerView.setAdapter(elementsAdapter);
        }





                /*
           Conducting a scan for Wi-Fi access points
         */
        // Register a broadcast receiver that listens for scan results.
        registerReceiver(new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {

                results = wifi.getScanResults();



                Comparator<ScanResult> comparator = new Comparator<ScanResult>() {
                    @Override
                    public int compare(ScanResult lhs, ScanResult rhs) {
                        return (lhs.level >rhs.level ? -1 : (lhs.level==rhs.level ? 0 : 1));
                    }
                };


                Collections.sort(results, comparator);

                // Loop old_results items
                for (ScanResult res : results) {
                    rst.add(res.level);
                }

                Log.d("TAG_LEVEL", String.valueOf(rst));


                if(old_results == null){

                    temp_result.clear();

                    if(results.size()< 3) {


                        for (int i = 0; i<results.size(); i++) {
                            temp_result.add(i, results.get(i));
                            User user = new User(temp_result);

                            save_User_To_Shared_Prefs(getApplicationContext(), user);

                        }

                    }else {
                        for (int i = 0; i < 3; i++) {
                            temp_result.add(i, results.get(i));

                            User user = new User(temp_result);

                            save_User_To_Shared_Prefs(getApplicationContext(), user);

                        }
                    }



                }
                else {

                    User get_saved_wifiList= get_User_From_Shared_Prefs(getApplicationContext());

                    old_results= get_saved_wifiList.getResults();


                    // Loop old_results items
                    for (ScanResult person2 : old_results) {
                        // Loop original scan results items
                        boolean found = false;
                        for (ScanResult person1 : results) {
                            if (Objects.equals(person2.SSID, person1.SSID)) {
                                found = true;
                            }
                        }
                        if (!found) {

                            temp_result.clear();

                            if(results.size()< 3) {


                                for (int i = 0; i<results.size(); i++) {
                                    temp_result.add(i, results.get(i));
                                    User user = new User(temp_result);

                                    save_User_To_Shared_Prefs(getApplicationContext(), user);

                                }

                            }else {
                                for (int i = 0; i < 3; i++) {
                                    temp_result.add(i, results.get(i));

                                    User user = new User(temp_result);

                                    save_User_To_Shared_Prefs(getApplicationContext(), user);

                                }
                            }


                        }
                    }

                }

                Log.d(TAG, String.valueOf(temp_result));

                for (final ScanResult reslt : temp_result) {

                    final String ssd = reslt.SSID;


                    StringRequest strReq = new StringRequest(Request.Method.POST,
                            Config.URL_CHECKBIZ, new Response.Listener<String>() {

                        //iterating through all the nodes
                        ScanResult output_signal;

                        @Override
                        public void onResponse(String response) {
                            Log.d(TAG, response);

                            try {

                                JSONObject responseObj = new JSONObject(response);

                                JSONArray jArray = responseObj.getJSONArray("biz");

                                Log.d("TAG2", String.valueOf(jArray.length()));

                                // Parsing json object response
                                // response will be a json object
                                boolean error = responseObj.getBoolean("error");
                                String message = responseObj.getString("message");


                              /*  // checking for error, if not error SMS is initiated
                                // device should receive it shortly
                                if (!error) {

                                    dialog.dismiss();





                                    output_signal = reslt;

                                    temp_final.add(output_signal);


                                } else {
                                    dialog.dismiss();
                                    Toast.makeText(getApplicationContext(),
                                            "Error: " + message,
                                            Toast.LENGTH_LONG).show();
                                }

*/
                                // hiding the progress bar
                                //progressBar2.setVisibility(View.GONE);

                            } catch (JSONException e) {
                                Toast.makeText(getApplicationContext(),
                                        "Error: " + e.getMessage(),
                                        Toast.LENGTH_LONG).show();

                                //progressBar2.setVisibility(View.GONE);
                            }

                        }
                    }, new Response.ErrorListener() {

                        @Override
                        public void onErrorResponse(VolleyError error) {
                            Log.e(TAG, "Error: " + error.getMessage());
                            Toast.makeText(getApplicationContext(),
                                    error.getMessage(), Toast.LENGTH_SHORT).show();
                            //progressBar2.setVisibility(View.GONE);
                        }
                    }) {

                        /**
                         * Passing user parameters to our server
                         * @return
                         */
                        @Override
                        protected Map<String, String> getParams() {
                            Map<String, String> params = new HashMap<String, String>();
                            params.put("hotspotName", ssd);

                            Log.e(TAG, "Posting params: " + params.toString());

                            return params;
                        }

                    };

                    // Adding request to request queue
                    MyApplication.getInstance().addToRequestQueue(strReq);




                   /* final Query firebaseRef = FirebaseDatabase.getInstance().getReference("Registered_Hotspots")
                            .orderByChild("hotspotName").equalTo(ssd);

                    //attaching value event listener
                    firebaseRef.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {

                            dialog.dismiss();

                            //iterating through all the nodes
                            ScanResult output_signal;

                            //clearing the previous list

                            checki.clear();
                            if (dataSnapshot.exists()) {

                                output_signal = reslt;

                                temp_final.add(output_signal);

                                int lev = output_signal.level;
                                //iterating through all the nodes
                                for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {

                                    HotSpotDetails hotSpotDetails = postSnapshot.getValue(HotSpotDetails.class);

                                    String taskTitle = postSnapshot.getValue(String.class);

                                    Log.d("TAG_TASKTITLE", String.valueOf(taskTitle));



                                }


                                *//*
                                (String hotspotId, String hotspotName, String mpesa_paybill,
                                        String equity_acc, String airtel_paybill, String orange_paybill,String location,String url )
                                (String hotspotId, String hotspotName, String mpesa_paybill, String equity_acc,
                                        String airtel_paybill, String orange_paybill, String location, String url, int level)

                               **//*
                                //AddLevel addLevel= new AddLevel(checkWifi.get(0),checkWifi.get(1),checkWifi.get(2),checkWifi.get(3),checkWifi.get(4),checkWifi.get(5),checkWifi.get(6),checkWifi.get(7),lev);




                                //check whether finalOutput contain an item with the same ssid as output_signal.ssid
                            }
                        }


                        @Override
                        public void onCancelled(DatabaseError databaseError) {

                            dialog.dismiss();
                        }
                    });

*/                }

                Log.d("TAG_CHECKWIFI", String.valueOf(checkWifi));


                //checkWifi.clear();

                Comparator<ScanResult> comp = new Comparator<ScanResult>() {
                    @Override
                    public int compare(ScanResult lhs, ScanResult rhs) {
                        return (lhs.level >rhs.level ? -1 : (lhs.level==rhs.level ? 0 : 1));
                    }
                };


                Collections.sort(temp_final, comp);


                if(old_list == null){

                    finalOutput.clear();

                    for (int i = 0; i<temp_final.size(); i++) {

                        finalOutput.add(i, temp_final.get(i));

                    }

                    OldWifi oldWifi = new OldWifi(finalOutput);

                    save_old(getApplicationContext(), oldWifi);



                }
                else{

                    OldWifi get_old = get_old(getApplicationContext());

                    old_list = get_old.getResults();



                    // Loop old_results items
                    for (ScanResult person2 : old_list) {
                        // Loop original scan results items
                        boolean found = false;
                        for (ScanResult person1 : temp_final) {
                            if (Objects.equals(person2.SSID, person1.SSID)) {
                                found = true;
                            }
                        }
                        if (!found) {

                            for (int i = 0; i<temp_final.size(); i++) {

                                finalOutput.add(i, temp_final.get(i));

                            }

                            OldWifi oldWifi = new OldWifi(finalOutput);

                            save_old(getApplicationContext(), oldWifi);
                        }

                    }


                }
                /*

                for (final ScanResult reslt : finalOutput) {

                    final String ssd = reslt.SSID;

                    final Query firebaseRef = FirebaseDatabase.getInstance().getReference("Registered_Hotspots")
                            .orderByChild("hotspotName").equalTo(ssd);

                    //attaching value event listener
                    firebaseRef.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {


                            if (dataSnapshot.exists()) {

                                //iterating through all the nodes
                                for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {

                                    HotSpotDetails hotSpotDetails = postSnapshot.getValue(HotSpotDetails.class);

                                    //adding the existing hotspot to the list
                                    mainList.add(hotSpotDetails);
                                }


                            }
                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {

                            dialog.dismiss();
                        }
                    });

                }

                Log.d("TAG_CHECKWIFI", String.valueOf(mainList));

                **/

                if(!checkWifi.isEmpty()) {

                    //dialog.dismiss();
                    elementsAdapter.notifyDataSetChanged();
                    recyclerView.setVisibility(View.VISIBLE);
                    empty.setVisibility(View.GONE);
                }
                else
                {
                    //dialog.dismiss();
                    elementsAdapter.notifyDataSetChanged();
                    recyclerView.setVisibility(View.GONE);
                    empty.setVisibility(View.VISIBLE);
                }


                Log.d("TAG_OLD_FULL", String.valueOf(finalOutput));

                temp_final.clear();



            }
        }, new IntentFilter(WifiManager.SCAN_RESULTS_AVAILABLE_ACTION));

        // Initiate a scan.
        wifi.startScan();


    }




    public static String convertStringToDBColumns(String type) {
        if (type == null) { return null; }
        if (type.equalsIgnoreCase("M-PESA")) {
            return "mpesa_paybill";
        } else if (type.equalsIgnoreCase("EQUITEL")) {
            return "equity_acc";
        } else if (type.equalsIgnoreCase("AIRTEL MONEY")) {
            return "airtel_paybill";
        }
        else {
            return "orange_paybill";
        }

    }



    @Override
    public void onClickLoadMore() {
        elementsAdapter.setWithFooter(false); // hide footer

        // now add remaining elements
        for(int i = 1; i< checkWifi.size(); ++i) {
            dummyList.add( checkWifi.get(i));
        }

        recyclerView.addItemDecoration(new HorizontalDividerItemDecoration.Builder(this)
                .marginResId(R.dimen.leftmargin, R.dimen.rightmargin)
                .build());

        elementsAdapter.notifyDataSetChanged(); // more elements will be added
    }

    public static PayMethList get_paymethlist(Context context) {

        SharedPreferences appSharedPrefs = PreferenceManager
                .getDefaultSharedPreferences(context.getApplicationContext());
        Gson gson = new Gson();
        String json = appSharedPrefs.getString("payMeth", "");


        PayMethList payMethList = gson.fromJson(json, PayMethList.class);
        return payMethList;
    }


    public static void save_User_To_Shared_Prefs(Context context, User _USER) {
        SharedPreferences appSharedPrefs = PreferenceManager
                .getDefaultSharedPreferences(context.getApplicationContext());
        SharedPreferences.Editor prefsEditor = appSharedPrefs.edit();
        Gson gson = new Gson();
        String json = gson.toJson(_USER);
        prefsEditor.putString("user", json);
        prefsEditor.apply();

    }

    public static void save_old(Context context, OldWifi _oldwifi) {
        SharedPreferences appSharedPrefs = PreferenceManager
                .getDefaultSharedPreferences(context.getApplicationContext());
        SharedPreferences.Editor prefsEditor = appSharedPrefs.edit();
        Gson gson = new Gson();
        String json = gson.toJson(_oldwifi);
        prefsEditor.putString("old", json);
        prefsEditor.apply();

    }

    public static User get_User_From_Shared_Prefs(Context context) {

        SharedPreferences appSharedPrefs = PreferenceManager
                .getDefaultSharedPreferences(context.getApplicationContext());
        Gson gson = new Gson();
        String json = appSharedPrefs.getString("user", "");


        User user = gson.fromJson(json, User.class);
        return user;
    }

    public static OldWifi get_old(Context context) {

        SharedPreferences appSharedPrefs = PreferenceManager
                .getDefaultSharedPreferences(context.getApplicationContext());
        Gson gson = new Gson();
        String json = appSharedPrefs.getString("old", "");


        OldWifi oldWifi = gson.fromJson(json, OldWifi.class);
        return oldWifi;
    }

    public static SimulateClass get_simulatedList(Context context) {

        SharedPreferences appSharedPrefs = PreferenceManager
                .getDefaultSharedPreferences(context.getApplicationContext());
        Gson gson = new Gson();
        String json = appSharedPrefs.getString("simulation", "");


        SimulateClass simulateClass = gson.fromJson(json, SimulateClass.class);
        return simulateClass;
    }
}


