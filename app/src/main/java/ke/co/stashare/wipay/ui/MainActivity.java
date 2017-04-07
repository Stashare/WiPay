package ke.co.stashare.wipay.ui;

import android.app.ProgressDialog;
import android.app.SearchManager;
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
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.gson.Gson;
import com.scottyab.showhidepasswordedittext.ShowHidePasswordEditText;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Objects;

import ke.co.stashare.wipay.Adapters.RecyclerAdapter;
import ke.co.stashare.wipay.R;
import ke.co.stashare.wipay.model.OldWifi;
import ke.co.stashare.wipay.helper.User;
import ke.co.stashare.wipay.model.Hotspots;


public class MainActivity extends AppCompatActivity {
    Toolbar mToolbar;
    RecyclerView recyclerView;
    RecyclerAdapter adapter;
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

    //view objects
    EditText editTextPaybill;

    Button buttonAddPaybill;

    //our database reference object
    DatabaseReference databasePaybills;
    int index=0;
    String ssd;
    String first_tym;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        /*
        setTheme(R.style.AppThemeC);
        getWindow().getDecorView().setSystemUiVisibility(
                View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION);
                **/


        setContentView(R.layout.activity_main);

        //getting the reference of artists node
        databasePaybills = FirebaseDatabase.getInstance().getReference("Registered_Hotspots");



        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            getWindow().setStatusBarColor(getResources().getColor(R.color.type_message_dark));
        }


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

        dialog = new ProgressDialog(MainActivity.this);
        dialog.setMessage("Scanning, please wait...");
        dialog.show();

        temp_result= new ArrayList<>();

        //list to store artists
        checkSpots = new ArrayList<>();

        finalOutput= new ArrayList<>();

        temp_final= new ArrayList<>();


        adapter=new RecyclerAdapter(finalOutput, new RecyclerAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(ScanResult item) {
                wifi_name= item.SSID;
                promptPayBill();
                //onSearchClick();
                //Toast.makeText(MainActivity.this, wifi_name, Toast.LENGTH_LONG).show();


            }
        },new RecyclerAdapter.OnItemLongClickListener(){
            @Override
            public void onItemLongClicked(ScanResult item) {
                wifi_name= item.SSID;
                //String onlongclck="Long clicked " + wifi_name;
                //Toast.makeText(MainActivity.this, onlongclck, Toast.LENGTH_LONG).show();

               //checkHotspotExistence(wifi_name);

                showPaybillDialog();

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
                        return (lhs.level >rhs.level ? -1 : (lhs.level==rhs.level ? 0 : 1));
                    }
                };


                Collections.sort(results, comparator);



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

                    final Query firebaseRef = FirebaseDatabase.getInstance().getReference("paybills")
                            .orderByChild("hotspotName").equalTo(ssd);

                    //attaching value event listener
                    firebaseRef.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {

                            //iterating through all the nodes
                            ScanResult output_signal = null;


                            if (dataSnapshot.exists()) {
                                output_signal = reslt;

                                temp_final.add(output_signal);


                                //check whether finalOutput contain an item with the same ssid as output_signal.ssid
                            }
                        }


                        @Override
                        public void onCancelled(DatabaseError databaseError) {

                        }
                    });

                }



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



                if(!finalOutput.isEmpty()) {

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


                Log.d("TAG_OLD_FULL", String.valueOf(finalOutput));

                temp_final.clear();



            }
        }, new IntentFilter(WifiManager.SCAN_RESULTS_AVAILABLE_ACTION));

        // Initiate a scan.
        wifi.startScan();

    }


    private void updatePaybillDialog() {

        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(this);
        LayoutInflater inflater = getLayoutInflater();
        final View dialogView = inflater.inflate(R.layout.update_dialog, null);
        dialogBuilder.setView(dialogView);

        final EditText editTextName = (EditText) dialogView.findViewById(R.id.editTextPaybill);
        final Button buttonAdd = (Button) dialogView.findViewById(R.id.buttonAddPaybill);
        final Button buttonCancel = (Button) dialogView.findViewById(R.id.buttonCancel);

        dialogBuilder.setTitle(wifi_name);
        final AlertDialog b = dialogBuilder.create();
        b.show();


        buttonAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                payB = editTextName.getText().toString().trim();
                //String genre = spinnerGenre.getSelectedItem().toString();

                Log.d(TAG, payB);
                if (!TextUtils.isEmpty(payB)) {
                    updatePaybill(wifi_name);
                    b.dismiss();
                }
            }
        });


        buttonCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                b.dismiss();

                /*
                * we will code this method to delete the artist
                * */

            }
        });
    }

    private void promptPayBill(){

        String paybill_test= "765456";
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(this);
        LayoutInflater inflater = getLayoutInflater();
        final View dialogView = inflater.inflate(R.layout.amount_pin, null);
        dialogBuilder.setView(dialogView);

        final ImageView notes=(ImageView)dialogView.findViewById(R.id.notes);
        final ImageView lock =(ImageView)dialogView.findViewById(R.id.lock);
        final EditText editTextAmount = (EditText) dialogView.findViewById(R.id.amount);
        final ShowHidePasswordEditText showHidePasswordEditText = (ShowHidePasswordEditText)dialogView.findViewById(R.id.pin);
        final Button buttonSend = (Button) dialogView.findViewById(R.id.send);
        final Button buttonCancel=(Button) dialogView.findViewById(R.id.cancel);
        final AlertDialog b = dialogBuilder.create();
        b.setCanceledOnTouchOutside(false);
        b.show();

        /*
        showHidePasswordEditText.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if(hasFocus){
                    lock.setColorFilter(getResources().getColor(R.color.red), android.graphics.PorterDuff.Mode.MULTIPLY);

                }else{
                    lock.setColorFilter(getResources().getColor(R.color.button_grey), android.graphics.PorterDuff.Mode.MULTIPLY);


                }
            }
        });

        **/

        buttonSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                amount = editTextAmount.getText().toString().trim();
                //String genre = spinnerGenre.getSelectedItem().toString();
                password = showHidePasswordEditText.getText().toString().trim();

                if (!TextUtils.isEmpty(amount) && !TextUtils.isEmpty(password) ) {

                    Toast.makeText(MainActivity.this, amount + " "+ password, Toast.LENGTH_LONG).show();

                    b.dismiss();
                }
                else{
                    Toast.makeText(MainActivity.this, "Please fill in all the fields", Toast.LENGTH_LONG).show();

                }
            }
        });


        buttonCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                b.dismiss();

                /*
                * we will code this method to delete the artist
                * */

            }
        });
    }



    private void showPaybillDialog() {

        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(this);
        LayoutInflater inflater = getLayoutInflater();
        final View dialogView = inflater.inflate(R.layout.update_dialog, null);
        dialogBuilder.setView(dialogView);

        final EditText editTextName = (EditText) dialogView.findViewById(R.id.editTextPaybill);
        final Button buttonAdd = (Button) dialogView.findViewById(R.id.buttonAddPaybill);
        final Button buttonCancel = (Button) dialogView.findViewById(R.id.buttonCancel);

        dialogBuilder.setTitle(wifi_name);
        final AlertDialog b = dialogBuilder.create();
        b.show();


        buttonAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                payB = editTextName.getText().toString().trim();
                //String genre = spinnerGenre.getSelectedItem().toString();

                Log.d(TAG, payB);
                if (!TextUtils.isEmpty(payB)) {
                    addPaybill();
                    b.dismiss();
                }
            }
        });


        buttonCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                b.dismiss();

                /*
                * we will code this method to delete the artist
                * */

            }
        });
    }

    private void updatePaybill(final String hotspot){


        databasePaybills.child("hotspotName").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    // TODO: handle the case where the data already exists
                    String output= hotspot + " exists in the database";

                    Toast.makeText(MainActivity.this, output, Toast.LENGTH_LONG).show();

                }
                else {
                    // TODO: handle the case where the data does not yet exist
                    String output= hotspot + " does not exists in the database";

                    Toast.makeText(MainActivity.this, output, Toast.LENGTH_LONG).show();

                }
            }


            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

    }

    private void checkHotspotExistence(final String wifiN){
        Query firebaseRef = FirebaseDatabase.getInstance().getReference("paybills").orderByChild("hotspotName").equalTo(wifiN);;


        //attaching value event listener
        firebaseRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                String output= wifiN + " already exists in the database";
                String outpt= wifiN + " not found";
                //iterating through all the nodes

                if (dataSnapshot.exists()) {
                        // User Exists
                    Toast.makeText(MainActivity.this,"exists",Toast.LENGTH_LONG).show();
                    }
                    else {
                    Toast.makeText(MainActivity.this,"dont exists",Toast.LENGTH_LONG).show();
                }


                }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });


    }

    private void addPaybill() {

        //checking if the value is provided
        if (wifi_name!=null) {

            //getting a unique id using push().getKey() method
            //it will create a unique id and we will use it as the Primary Key for our Artist
            String id = databasePaybills.push().getKey();

            //creating an Artist Object
            Hotspots hotspots = new Hotspots(id, wifi_name,payB);

            //Saving the Artist
            databasePaybills.child(id).setValue(hotspots);

            //displaying a success toast
            Toast.makeText(this, "Hotspot added to the database", Toast.LENGTH_LONG).show();
        } else {
            //if the value is not given displaying a toast
            Toast.makeText(this, "process failed,try adding the hotspot again", Toast.LENGTH_LONG).show();
        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
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

    public void refresh() {

        dialog = new ProgressDialog(MainActivity.this);
        dialog.setMessage("Refreshing...");
        dialog.show();
        /**
         * Listing 16-14: Accessing the Wi-Fi Manager
         */
        String service = Context.WIFI_SERVICE;
        final WifiManager wifi = (WifiManager)getSystemService(service);

        /**
         * Listing 16-15: Monitoring and changing Wi-Fi state
         */
        if (!wifi.isWifiEnabled())
            if (wifi.getWifiState() != WifiManager.WIFI_STATE_ENABLING)
                wifi.setWifiEnabled(true);

        registerReceiver(new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {

                results = wifi.getScanResults();


                old_results = null;


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

                for (final ScanResult reslt : temp_result) {

                    final String ssd = reslt.SSID;

                    final Query firebaseRef = FirebaseDatabase.getInstance().getReference("paybills")
                            .orderByChild("hotspotName").equalTo(ssd);

                    //attaching value event listener
                    firebaseRef.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {

                            //iterating through all the nodes
                            ScanResult output_signal = null;


                            if (dataSnapshot.exists()) {
                                output_signal = reslt;

                                temp_final.add(output_signal);


                                //check whether finalOutput contain an item with the same ssid as output_signal.ssid
                            }
                        }


                        @Override
                        public void onCancelled(DatabaseError databaseError) {

                        }
                    });

                }

                /*

                if(!temp_final.isEmpty()) {

                    finalOutput.clear();

                    for (int i = 0; i<temp_final.size(); i++) {

                        finalOutput.add(i, temp_final.get(i));

                    }

                }
                else
                {
                    finalOutput.clear();

                }

                temp_final.clear();
                **/

                if(!temp_final.isEmpty()){

                    dialog.dismiss();
                    adapter.notifyDataSetChanged();
                    recyclerView.setVisibility(View.VISIBLE);
                    empty.setVisibility(View.GONE);

                }else {

                    dialog.dismiss();
                    adapter.notifyDataSetChanged();
                    recyclerView.setVisibility(View.GONE);
                    empty.setVisibility(View.VISIBLE);
                }

                Log.d("TAG_OLD_FULL", String.valueOf(temp_final));



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


    public void onSearchClick() {
        try {
            Intent intent = new Intent(Intent.ACTION_WEB_SEARCH);
            intent.putExtra(SearchManager.QUERY, wifi_name);
            startActivity(intent);
        } catch (Exception e) {
            Toast.makeText(MainActivity.this, "Connection failed", Toast.LENGTH_LONG).show();

            // TODO: handle exception

        }
    }

}


