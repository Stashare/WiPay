package ke.co.stashare.wipay.ui;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.support.v7.widget.Toolbar;
import android.view.WindowManager;
import android.widget.Toast;

import java.util.ArrayList;

import eu.davidea.flexibleadapter.common.SmoothScrollGridLayoutManager;
import ke.co.stashare.wipay.Adapters.HomeAdapter;
import ke.co.stashare.wipay.R;
import ke.co.stashare.wipay.helper.SharedPrefManager;
import ke.co.stashare.wipay.model.Home;
import ke.co.stashare.wipay.model.PayMethList;

/**
 * Created by Ken Wainaina on 06/04/2017.
 */

public class HomePage extends AppCompatActivity {

    Toolbar mToolbar;
    RecyclerView recyclerView;
    HomeAdapter adapter;
    String title;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_homepage);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            getWindow().setStatusBarColor(getResources().getColor(R.color.type_message_dark));
        }


        mToolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(mToolbar);

        final ActionBar actionBar = getSupportActionBar();
        assert actionBar != null;
        actionBar.setDisplayShowTitleEnabled(true);

        recyclerView = (RecyclerView) findViewById(R.id.recycler_view);

        //StaggeredGridLayoutManager gridLayoutManager = new StaggeredGridLayoutManager(2,1);


        LinearLayoutManager llm = new LinearLayoutManager(this);

        recyclerView.setLayoutManager(llm);

        //SmoothScrollGridLayoutManager gridLayoutManager = new SmoothScrollGridLayoutManager(getApplicationContext(),3);

        recyclerView.setItemViewCacheSize(0); //Setting ViewCache to 0 (default=2)
        // will animate items better while scrolling down+up with LinearLayout
        //recyclerView.setLayoutManager(gridLayoutManager);
        recyclerView.setHasFixedSize(true); //Size of RV will not change


        adapter = new HomeAdapter(getItems(), new HomeAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(Home item) {

                // This check which radio button was clicked
                switch (item.getTitle()) {
                    case "My Account":

                        Toast.makeText(getApplicationContext(), "My Account", Toast.LENGTH_LONG).show();

                        break;


                }

                // This check which radio button was clicked
                switch (item.getTitle()) {
                    case "Make Payment":

                        Intent openHomepage = new Intent(HomePage.this, Main.class);

                        startActivity(openHomepage);

                        break;


                }

                // This check which radio button was clicked
                switch (item.getTitle()) {
                    case "Withdrawal Cash":

                        Toast.makeText(getApplicationContext(), "Withdrawal", Toast.LENGTH_LONG).show();

                        break;


                }

                // This check which radio button was clicked
                switch (item.getTitle()) {
                    case "Buy Airtime":

                        Toast.makeText(getApplicationContext(), "Buy Airtime", Toast.LENGTH_LONG).show();

                        break;


                }
            }
        },new HomeAdapter.OnItemLongClickListener(){
            @Override
            public void onItemLongClicked(Home item) {
                //wifi_name= item.SSID;

            }


        });

       recyclerView.setAdapter(adapter);

    }


    private ArrayList<Home> getItems() {
        //COLECTION OF CRIME MOVIES
        ArrayList<Home> items = new ArrayList<>();


        Home home = new Home(R.drawable.ic_payhand, R.drawable.backrepeat, "Make Payment","You can now pay to over" +
                " 100 businesses & companies countrywide via MPESA, EQUITEL...");
        items.add(home);


        home = new Home(R.drawable.ic_cash_out, R.drawable.withdrawal,"Withdrawal Cash","In partnership with the major banks in the country, cash withdrawal has never been this easy; thanks to Wipay");
        items.add(home);

        home = new Home(R.drawable.ic_simcard, R.drawable.backrepeat,"Buy Airtime","Buy airtime for any number, via " +
                "the MPESA or Equitel  ");
        items.add(home);

        home = new Home(R.drawable.ic_account_white,R.drawable.withdrawal, "View My Account","Update profile & preferences; " +
                "check out the mini-statement of all your transactions");

        //ADD ITR TO COLLECTION
        items.add(home);

        return items;
    }
}

