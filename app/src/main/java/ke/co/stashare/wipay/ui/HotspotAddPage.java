package ke.co.stashare.wipay.ui;

import android.app.ProgressDialog;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.net.Uri;
import android.net.wifi.ScanResult;
import android.os.Build;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.webkit.MimeTypeMap;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.google.gson.Gson;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import ke.co.stashare.wipay.Adapters.RecyclerAdapter;
import ke.co.stashare.wipay.Adapters.ThreeStrongAdapter;
import ke.co.stashare.wipay.R;
import ke.co.stashare.wipay.helper.Constants;
import ke.co.stashare.wipay.helper.User;
import ke.co.stashare.wipay.model.CheckWifiExistance;
import ke.co.stashare.wipay.model.ExistingHoti;
import ke.co.stashare.wipay.model.HotSpDetails;
import ke.co.stashare.wipay.model.HotSpotDetails;
import ke.co.stashare.wipay.model.ImageUpload;
import ke.co.stashare.wipay.model.OldWifi;
import ke.co.stashare.wipay.model.PayMethList;
import ke.co.stashare.wipay.model.SimulateClass;
import ke.co.stashare.wipay.model.Upload;

/**
 * Created by Ken Wainaina on 01/04/2017.
 */

public class HotspotAddPage extends AppCompatActivity implements View.OnClickListener {

    String mpesa1;
    String orange1;
    String airtel1;
    String equity1;
    String building1;
    String county1;
    String location1;

    List<HotSpotDetails> hotspot;

    Toolbar mToolbar;

    String wifi_name;

    EditText mpesa;
    EditText orange;
    EditText airtel;
    EditText equity;
    EditText building;
    EditText county;
    EditText location;
    ThreeStrongAdapter elementadapter;
    Button buttonAdd;
    ImageView imageView;
    FloatingActionButton fab;
    List<ExistingHoti> existingHotspot;
    List<HotSpotDetails>simulated_List;
    List<CheckWifiExistance> checkWifi;

    //constant to track image chooser intent
    private static final int PICK_IMAGE_REQUEST = 234;

    //uri to store file
    private Uri filePath;

    //firebase objects
    private StorageReference storageReference;

    //our database reference object
    DatabaseReference databaseHotspots;

    protected void onCreate(Bundle savedInstanceState)  {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_biz);


        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            getWindow().setStatusBarColor(getResources().getColor(R.color.type_message_dark));
        }



        Intent getWifiName = getIntent();

        wifi_name = getWifiName.getStringExtra("Wifi");

        mToolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(mToolbar);

        final ActionBar actionBar = getSupportActionBar();
        assert actionBar != null;

        actionBar.setTitle(wifi_name);
        actionBar.setDisplayShowTitleEnabled(true);

        storageReference = FirebaseStorage.getInstance().getReference();

        //getting the reference of artists node
        databaseHotspots = FirebaseDatabase.getInstance().getReference("Registered_Hotspots");

        hotspot= new ArrayList<>();

        mpesa = (EditText)findViewById(R.id.mpesa);
        orange = (EditText)findViewById(R.id.orange);
        airtel = (EditText)findViewById(R.id.airtel);
        equity = (EditText)findViewById(R.id.equity_account);
        building = (EditText)findViewById(R.id.situated_at);
        county = (EditText)findViewById(R.id.county);
        location = (EditText)findViewById(R.id.location);

        fab =(FloatingActionButton)findViewById(R.id.image_choose);

        existingHotspot= new ArrayList<>();
        checkWifi = new ArrayList<>();
        simulated_List = new ArrayList<>();

        fab.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                showFileChooser();
            }
        });

        buttonAdd =(Button)findViewById(R.id.buttonAdd);

        imageView= (ImageView)findViewById(R.id.imageViewCompany);

        buttonAdd.setOnClickListener(this);


    }

    private void showFileChooser() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Select Picture"), PICK_IMAGE_REQUEST);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null && data.getData() != null) {
            filePath = data.getData();
            try {
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), filePath);
                imageView.setImageBitmap(bitmap);
                //uploadFile();

            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public String getFileExtension(Uri uri) {
        ContentResolver cR = getContentResolver();
        MimeTypeMap mime = MimeTypeMap.getSingleton();
        return mime.getExtensionFromMimeType(cR.getType(uri));
    }

    private void uploadFile() {
        //checking if file is available
        if (filePath != null) {
            //displaying progress dialog while image is uploading
            final ProgressDialog progressDialog = new ProgressDialog(this);
            progressDialog.setTitle("Updating...");
            progressDialog.show();

            final Query firebaseRef = FirebaseDatabase.getInstance().getReference("Registered_Hotspots")
                    .orderByChild("hotspotName").equalTo(wifi_name);

            //attaching value event listener
            firebaseRef.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {

                    if (dataSnapshot.exists()) {

                        //iterating through all the nodes
                        for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {

                            ExistingHoti existingHoti = postSnapshot.getValue(ExistingHoti.class);

                            //adding the existing hotspot to the list
                            existingHotspot.add(existingHoti);
                        }

                        //Log.d("TAG_LONGTIME", Constants.STORAGE_PATH_UPLOADS + existingHotspot.get(0).getCurtime() + "." + getFileExtension(filePath));

                        /*
                        // Create a reference to the file to delete
                        StorageReference desertRef = storageReference.child(Constants.STORAGE_PATH_UPLOADS + existingHotspot.get(0).getCurtime() + "." + getFileExtension(filePath));
                        // Delete the file
                        desertRef.delete().addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {
                                // File deleted successfully
                            }
                        }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception exception) {
                                // Uh-oh, an error occurred!
                                //Toast.makeText(getApplicationContext(), exception.getMessage(), Toast.LENGTH_LONG).show();

                            }
                        });
                         **/


                        final long currenttym =  System.currentTimeMillis();

                        //getting the storage reference
                            StorageReference sRef = storageReference.child(Constants.STORAGE_PATH_UPLOADS +
                                    currenttym + "." + getFileExtension(filePath));

                            //adding the file to reference
                            sRef.putFile(filePath)
                                    .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                                        @Override
                                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                                            //dismissing the progress dialog
                                            progressDialog.dismiss();

                                            //displaying success toast
                                            Toast.makeText(getApplicationContext(), "Image updated ", Toast.LENGTH_LONG).show();

                                            //creating the upload object to store uploaded image details
                                            ImageUpload upload = new ImageUpload(wifi_name, taskSnapshot.getDownloadUrl().toString());


                                            databaseHotspots.child(existingHotspot.get(0).getHotspotId()).setValue(upload);
                                        }
                                    })
                                    .addOnFailureListener(new OnFailureListener() {
                                        @Override
                                        public void onFailure(@NonNull Exception exception) {
                                            progressDialog.dismiss();
                                            Toast.makeText(getApplicationContext(), exception.getMessage(), Toast.LENGTH_LONG).show();
                                        }
                                    })
                                    .addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                                        @Override
                                        public void onProgress(UploadTask.TaskSnapshot taskSnapshot) {
                                            //displaying the upload progress
                                            double progress = (100.0 * taskSnapshot.getBytesTransferred()) / taskSnapshot.getTotalByteCount();
                                            progressDialog.setMessage("Uploaded " + ((int) progress) + "%...");
                                        }
                                    });



                    }else {

                        final long currenttym =  System.currentTimeMillis();

                        //getting the storage reference
                        StorageReference sRef = storageReference.child(Constants.STORAGE_PATH_UPLOADS + currenttym + "." + getFileExtension(filePath));

                        //adding the file to reference
                        sRef.putFile(filePath)
                                .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                                    @Override
                                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                                        //dismissing the progress dialog
                                        progressDialog.dismiss();

                                        //displaying success toast
                                        Toast.makeText(getApplicationContext(), "Image updated ", Toast.LENGTH_LONG).show();

                                        //adding an upload to firebase database
                                        String uploadId = databaseHotspots.push().getKey();

                                        //creating the upload object to store uploaded image details
                                        ExistingHoti existingHoti = new ExistingHoti(uploadId, wifi_name, taskSnapshot.getDownloadUrl().toString());


                                        databaseHotspots.child(uploadId).setValue(existingHoti);
                                    }
                                })
                                .addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception exception) {
                                        progressDialog.dismiss();
                                        Toast.makeText(getApplicationContext(), exception.getMessage(), Toast.LENGTH_LONG).show();
                                    }
                                })
                                .addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                                    @Override
                                    public void onProgress(UploadTask.TaskSnapshot taskSnapshot) {
                                        //displaying the upload progress
                                        double progress = (100.0 * taskSnapshot.getBytesTransferred()) / taskSnapshot.getTotalByteCount();
                                        progressDialog.setMessage("Uploaded " + ((int) progress) + "%...");
                                    }
                                });


                    }


                }


                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });

            existingHotspot.clear();

        } else {

            Toast.makeText(getApplicationContext(), "No Image Selected", Toast.LENGTH_LONG).show();

            //display an error if no file is selected
        }
    }


    @Override
    public void onClick(View v) {

        if (v.getId() == buttonAdd.getId()) {

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

            }

            else{
                Snackbar snackbar = Snackbar.make(findViewById(android.R.id.content), "Please fill in all the details", Snackbar.LENGTH_LONG)
                        .setAction("Action", null);
                View sbView = snackbar.getView();
                sbView.setBackgroundColor(ContextCompat.getColor(HotspotAddPage.this, R.color.type_message_dark));
                snackbar.show();
            }

        }

    }

    private void addDetails(){


        final Query firebaseRef = FirebaseDatabase.getInstance().getReference("Registered_Hotspots")
                .orderByChild("hotspotName").equalTo(wifi_name);

        //attaching value event listener
        firebaseRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                if (dataSnapshot.exists()) {

                    //iterating through all the nodes
                    for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {

                        CheckWifiExistance checkWifiExistance = postSnapshot.getValue(CheckWifiExistance.class);

                        //adding the existing hotspot to the list
                        checkWifi.add(checkWifiExistance);
                    }


                    String loc = building1 + " "+"- "+ location1 + ", "+ county1+ " County";
                    String url="add image url";

                    //creating an HotspotDetail Object

                    HotSpDetails hotSpDetails= new HotSpDetails(wifi_name,mpesa1,equity1,airtel1,orange1,loc,url);


                    databaseHotspots.child(checkWifi.get(0).getHotspotId()).setValue(hotSpDetails);
                }
                else{

                    //getting a unique id using push().getKey() method
                    //it will create a unique id and we will use it as the Primary Key for our Artist
                    String id = databaseHotspots.push().getKey();
                    String url="https://firebasestorage.googleapis.com/v0/b/wipaydb.appspot.com/o/uploads%2Fjava_house.jpg?alt=media&token=5a787e5c-4bc2-4184-b4a9-f8d0f99c744d";
                    String loc = building1 + " "+"- "+ location1 + ", "+ county1+ " County";

                    //creating an HotspotDetail Object

                    HotSpotDetails hotSpotDetails= new HotSpotDetails(id,wifi_name,mpesa1,equity1,airtel1,orange1,loc,url);

                    SimulateClass get_simul = get_simulatedList(getApplicationContext());

                    simulated_List = get_simul.getResults();


                    simulated_List.add(hotSpotDetails);

                     SimulateClass simulateClass = new SimulateClass(simulated_List);

                    save_simulatedList(getApplicationContext(), simulateClass);
                    //dialog.dismiss();
                    elementadapter.notifyDataSetChanged();

                    //Saving the Artist
                    databaseHotspots.child(id).setValue(hotSpotDetails);

                }
            }


                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });


        Snackbar snackbar = Snackbar.make(findViewById(android.R.id.content),  "Hotspot added", Snackbar.LENGTH_LONG)
                .setAction("Action", null);
        View sbView = snackbar.getView();
        sbView.setBackgroundColor(ContextCompat.getColor(HotspotAddPage.this, R.color.type_message_dark));
        snackbar.show();

        Intent goBack = new Intent(HotspotAddPage.this,
                AddHotspot.class);

        startActivity(goBack);
        finish();
    }

    public static void save_simulatedList(Context context, SimulateClass _simulateClass) {
        SharedPreferences appSharedPrefs = PreferenceManager
                .getDefaultSharedPreferences(context.getApplicationContext());
        SharedPreferences.Editor prefsEditor = appSharedPrefs.edit();
        Gson gson = new Gson();
        String json = gson.toJson(_simulateClass);
        prefsEditor.putString("simulation", json);
        prefsEditor.apply();

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
