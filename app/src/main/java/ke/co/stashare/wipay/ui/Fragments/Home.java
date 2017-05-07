package ke.co.stashare.wipay.ui.Fragments;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.widget.Toast;

import java.util.ArrayList;

import ke.co.stashare.wipay.Adapters.HomeAdapter;
import ke.co.stashare.wipay.R;
import ke.co.stashare.wipay.ui.Main;

/**
 * Created by Ken Wainaina on 08/04/2017.
 */

public class Home extends Fragment {

    RecyclerView recyclerView;
    HomeAdapter adapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.activity_load_more, container, false);


        recyclerView = (RecyclerView)view.findViewById(R.id.recycler_view);

        //StaggeredGridLayoutManager gridLayoutManager = new StaggeredGridLayoutManager(2,1);


        LinearLayoutManager llm = new LinearLayoutManager(getContext());

        recyclerView.setLayoutManager(llm);

        //SmoothScrollGridLayoutManager gridLayoutManager = new SmoothScrollGridLayoutManager(getApplicationContext(),3);

        recyclerView.setItemViewCacheSize(0); //Setting ViewCache to 0 (default=2)
        // will animate items better while scrolling down+up with LinearLayout
        //recyclerView.setLayoutManager(gridLayoutManager);
        recyclerView.setHasFixedSize(true); //Size of RV will not change


        adapter = new HomeAdapter(getItems(), new HomeAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(ke.co.stashare.wipay.model.Home item) {

                // This check which radio button was clicked
                switch (item.getTitle()) {
                    case "My Account":

                        Toast.makeText(getContext(), "My Account", Toast.LENGTH_LONG).show();

                        break;


                }

                // This check which radio button was clicked
                switch (item.getTitle()) {
                    case "Make Payment":

                        Intent openHomepage = new Intent(getContext(), Main.class);

                        startActivity(openHomepage);

                        break;


                }

                // This check which radio button was clicked
                switch (item.getTitle()) {
                    case "Withdrawal Cash":

                        Toast.makeText(getContext(), "Withdrawal", Toast.LENGTH_LONG).show();

                        break;


                }

                // This check which radio button was clicked
                switch (item.getTitle()) {
                    case "Buy Airtime":

                        Toast.makeText(getContext(), "Buy Airtime", Toast.LENGTH_LONG).show();

                        break;


                }
            }
        },new HomeAdapter.OnItemLongClickListener(){
            @Override
            public void onItemLongClicked(ke.co.stashare.wipay.model.Home item) {
                //wifi_name= item.SSID;

            }


        });

        recyclerView.setAdapter(adapter);


        return view;


    }


    private ArrayList<ke.co.stashare.wipay.model.Home> getItems() {
        //COLECTION OF CRIME MOVIES
        ArrayList<ke.co.stashare.wipay.model.Home> items = new ArrayList<>();


        ke.co.stashare.wipay.model.Home home = new ke.co.stashare.wipay.model.Home(R.drawable.ic_payhand, R.drawable.backrepeat, "Make Payment","You can now pay to over" +
                " 100 businesses & companies countrywide via MPESA, EQUITEL...");
        items.add(home);


        home = new ke.co.stashare.wipay.model.Home(R.drawable.ic_cash_out, R.drawable.withdrawal,"Withdrawal Cash","In partnership with the major banks in the country, cash withdrawal has never been this easy; thanks to Wipay");
        items.add(home);

        home = new ke.co.stashare.wipay.model.Home(R.drawable.ic_simcard, R.drawable.backrepeat,"Buy Airtime","Buy airtime for any number, via " +
                "the MPESA or Equitel  ");
        items.add(home);

        home = new ke.co.stashare.wipay.model.Home(R.drawable.ic_account_white,R.drawable.withdrawal, "View My Account","Update profile & preferences; " +
                "check out the mini-statement of all your transactions");

        //ADD ITR TO COLLECTION
        items.add(home);

        return items;
    }


    /*
    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu_b, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            case R.id.menu_item_refresh:
                //do something
                webView.loadUrl(myBlogAddr);
                return true;

            case R.id.contacts:
                //do something
                return true;

            case R.id.about:
                //do something
                return true;
        }

        return super.onOptionsItemSelected(item);
    }

**/

}
