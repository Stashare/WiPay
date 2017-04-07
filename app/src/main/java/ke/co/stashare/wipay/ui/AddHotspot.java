package ke.co.stashare.wipay.ui;

import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.net.wifi.ScanResult;
import android.net.wifi.WifiManager;
import android.os.Build;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.design.widget.Snackbar;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Objects;

import ke.co.stashare.wipay.Adapters.RecyclerAdapter;
import ke.co.stashare.wipay.R;
import ke.co.stashare.wipay.helper.User;
import ke.co.stashare.wipay.model.HotSpotDetails;
import ke.co.stashare.wipay.model.Hotspots;
import ke.co.stashare.wipay.model.OldWifi;

/**
 * Created by Ken Wainaina on 01/04/2017.
 */

public class AddHotspot extends AppCompatActivity {

    List<HotSpotDetails> hotspot;

    //our database reference object
    DatabaseReference databaseArtists;

    Toolbar mToolbar;
    RecyclerView recyclerView;
    RecyclerAdapter adapter;
    TextView empty;
    ProgressDialog dialog;
    private static final String TAG = "TAG";
    String wifi_name = null;


    List<ScanResult> temp_result;
    List<ScanResult> results;
    List<ScanResult> old_results;
    DividerItemDecoration mDividerItemDecoration;

    String mpesa1;
    String orange1;
    String airtel1;
    String equity1;
    String building1;
    String county1;
    String location1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_hotspot);


        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            getWindow().setStatusBarColor(getResources().getColor(R.color.type_message_dark));
        }

        //getting the reference of artists node
        databaseArtists = FirebaseDatabase.getInstance().getReference("Registered_Hotspots");

        hotspot= new ArrayList<>();

        /*
         * Listing 16-14: Accessing the Wi-Fi Manager
         */
        String service = Context.WIFI_SERVICE;
        final WifiManager wifi = (WifiManager)getSystemService(service);

        /*
         * Listing 16-15: Monitoring and changing Wi-Fi state
         */
        if (!wifi.isWifiEnabled())
            if (wifi.getWifiState() != WifiManager.WIFI_STATE_ENABLING)
                wifi.setWifiEnabled(true);



        mToolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(mToolbar);

        final ActionBar actionBar = getSupportActionBar();
        assert actionBar != null;
        actionBar.setDisplayShowTitleEnabled(true);

        recyclerView=(RecyclerView)findViewById(R.id.my_recycler_view);
        empty=(TextView)findViewById(R.id.empty_view);

        dialog = new ProgressDialog(AddHotspot.this);
        dialog.setMessage("Scanning, please wait...");
        dialog.show();

        temp_result= new ArrayList<>();


        adapter=new RecyclerAdapter(temp_result, new RecyclerAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(ScanResult item) {
                wifi_name= item.SSID;



                Intent openAddHotspot = new Intent(AddHotspot.this,
                        HotspotAddPage.class);

                openAddHotspot.putExtra("Wifi", wifi_name);
                startActivity(openAddHotspot);



            }
        },new RecyclerAdapter.OnItemLongClickListener(){
            @Override
            public void onItemLongClicked(ScanResult item) {
                wifi_name= item.SSID;

               // updatePaybill();

            }


        });

        recyclerView.setAdapter(adapter);



        recyclerView.setHasFixedSize(true);

        LinearLayoutManager llm = new LinearLayoutManager(this);

        recyclerView.setLayoutManager(llm);

        mDividerItemDecoration = new DividerItemDecoration(recyclerView.getContext(),
                llm.getOrientation());
        recyclerView.addItemDecoration(mDividerItemDecoration);



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
                        return (lhs.level > rhs.level ? -1 : (lhs.level == rhs.level ? 0 : 1));
                    }
                };


                Collections.sort(results, comparator);


                if (old_results == null) {

                    temp_result.clear();

                    if (results.size() < 3) {


                        for (int i = 0; i < results.size(); i++) {
                            temp_result.add(i, results.get(i));
                            User user = new User(temp_result);

                            save_User_To_Shared_Prefs(getApplicationContext(), user);

                        }

                    } else {
                        for (int i = 0; i < 3; i++) {
                            temp_result.add(i, results.get(i));

                            User user = new User(temp_result);

                            save_User_To_Shared_Prefs(getApplicationContext(), user);

                        }
                    }


                } else {

                    User get_saved_wifiList = get_User_From_Shared_Prefs(getApplicationContext());

                    old_results = get_saved_wifiList.getResults();


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

                            if (results.size() < 3) {


                                for (int i = 0; i < results.size(); i++) {
                                    temp_result.add(i, results.get(i));
                                    User user = new User(temp_result);

                                    save_User_To_Shared_Prefs(getApplicationContext(), user);

                                }

                            } else {
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

                if(!temp_result.isEmpty()) {

                    dialog.dismiss();
                    adapter.notifyDataSetChanged();
                    recyclerView.setVisibility(View.VISIBLE);
                    empty.setVisibility(View.GONE);
                }
                else
                {
                    dialog.dismiss();
                    adapter.notifyDataSetChanged();
                    recyclerView.setVisibility(View.GONE);
                    empty.setVisibility(View.VISIBLE);
                }




            }
        }, new IntentFilter(WifiManager.SCAN_RESULTS_AVAILABLE_ACTION));

        // Initiate a scan.
        wifi.startScan();

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

    public static User get_User_From_Shared_Prefs(Context context) {

        SharedPreferences appSharedPrefs = PreferenceManager
                .getDefaultSharedPreferences(context.getApplicationContext());
        Gson gson = new Gson();
        String json = appSharedPrefs.getString("user", "");


        User user = gson.fromJson(json, User.class);
        return user;
    }


    private void addPaybill(){

        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(this);
        LayoutInflater inflater = getLayoutInflater();
        final View dialogView = inflater.inflate(R.layout.add_biz, null);
        dialogBuilder.setView(dialogView);

        final EditText mpesa = (EditText) dialogView.findViewById(R.id.mpesa);
        final EditText orange = (EditText) dialogView.findViewById(R.id.orange);
        final EditText airtel = (EditText) dialogView.findViewById(R.id.airtel);
        final EditText equity = (EditText) dialogView.findViewById(R.id.equity_account);
        final EditText building = (EditText) dialogView.findViewById(R.id.situated_at);
        final EditText county = (EditText) dialogView.findViewById(R.id.county);
        final EditText location = (EditText) dialogView.findViewById(R.id.location);
        final Button buttonAdd = (Button)dialogView.findViewById(R.id.buttonAdd);

        dialogBuilder.setTitle(wifi_name);

        final AlertDialog b = dialogBuilder.create();
        b.show();


        buttonAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mpesa1 = mpesa.getText().toString().trim();
                orange1  = orange.getText().toString().trim();
                airtel1 = airtel.getText().toString().trim();
                equity1 = equity.getText().toString().trim();
                building1 = building.getText().toString().trim();
                county1 = county.getText().toString().trim();
                location1 = location.getText().toString().trim();

                if (!TextUtils.isEmpty(mpesa1) &&!TextUtils.isEmpty(orange1) &&!TextUtils.isEmpty(airtel1) &&!TextUtils.isEmpty(equity1)
                        &&!TextUtils.isEmpty(building1) &&!TextUtils.isEmpty(county1) &&!TextUtils.isEmpty(location1)) {
                    addDetails();
                    b.dismiss();
                }

                else{
                    Snackbar snackbar = Snackbar.make(view, "Please fill in all the details", Snackbar.LENGTH_LONG)
                            .setAction("Action", null);
                    View sbView = snackbar.getView();
                    sbView.setBackgroundColor(ContextCompat.getColor(AddHotspot.this, R.color.type_message_dark));
                    snackbar.show();
                }
            }
        });


    }



    private void addDetails(){

            //getting a unique id using push().getKey() method
            //it will create a unique id and we will use it as the Primary Key for our Artist
            String id = databaseArtists.push().getKey();

         String loc = building1 + " "+ location1 + " "+ county1+ " County";
        String url="";

           //creating an HotspotDetail Object

          HotSpotDetails hotSpotDetails= new HotSpotDetails(id,wifi_name,mpesa1,equity1,airtel1,orange1,loc,url);


            //Saving the Artist
            databaseArtists.child(id).setValue(hotSpotDetails);

            //displaying a success toast
            Toast.makeText(this, "Hotspot added", Toast.LENGTH_LONG).show();

    }

}
