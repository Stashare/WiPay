package ke.co.stashare.wipay.Adapters;

/**
 * Created by Ken Wainaina on 31/03/2017.
 */

import android.content.Context;
import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.AppCompatButton;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.scottyab.showhidepasswordedittext.ShowHidePasswordEditText;

import java.util.List;

import ke.co.stashare.wipay.helper.SharedPrefManager;
import ke.co.stashare.wipay.model.Elements;
import ke.co.stashare.wipay.R;
import ke.co.stashare.wipay.model.HotSpotDetails;
import ke.co.stashare.wipay.ui.MainActivity;
import ke.co.stashare.wipay.ui.TheMainClass;

public class ThreeStrongAdapter extends RecyclerView.Adapter {

    public interface Callbacks {
        public void onClickLoadMore();
    }

    private Callbacks mCallbacks;
    private static final int TYPE_HEADER = 0;
    private static final int TYPE_FIRST_ITEM = 3;
    private static final int TYPE_ITEM = 1;
    private static final int TYPE_FOOTER = 2;

    private boolean mWithHeader = false;
    private boolean mWithFirstElement = false;
    private boolean mWithFooter = false;
    private List<HotSpotDetails> mFeedList;
    private Context context;
    private  String title2;
    private String desc2;
    private  String output;
    private String url;


    public ThreeStrongAdapter(Context context, List<HotSpotDetails> feedList) {
        this.context = context;
        this.mFeedList = feedList;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View itemView = null;

        if (viewType == TYPE_FOOTER) {

            itemView = View.inflate(parent.getContext(), R.layout.row_load, null);
            return new LoadMoreViewHolder(itemView);

        } else if (viewType == TYPE_FIRST_ITEM) {

            itemView = View.inflate(parent.getContext(), R.layout.first_item, null);
            return new FirstViewHolder(itemView);
        } else {
            itemView = View.inflate(parent.getContext(), R.layout.pay_activity, null);
            return new ElementsViewHolder(itemView);
        }
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {

        if (holder instanceof LoadMoreViewHolder) {

            LoadMoreViewHolder loadMoreViewHolder = (LoadMoreViewHolder) holder;

            loadMoreViewHolder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (mCallbacks != null)
                        mCallbacks.onClickLoadMore();
                }
            });

        } else if (holder instanceof ElementsViewHolder) {
            ElementsViewHolder elementsViewHolder = (ElementsViewHolder) holder;

            HotSpotDetails elements = mFeedList.get(position);
            Glide.with(context).load(elements.getUrl()).into( elementsViewHolder.logo);
            elementsViewHolder.title.setText(elements.getHotspotName());
            elementsViewHolder.desc.setText(elements.getLocation());
        } else {
            FirstViewHolder elementsViewHolder = (FirstViewHolder) holder;

            HotSpotDetails elements = mFeedList.get(position);
            Glide.with(context).load(elements.getUrl()).into( elementsViewHolder.logo);
            elementsViewHolder.title.setText(elements.getHotspotName());
            elementsViewHolder.desc.setText(elements.getLocation());
        }

    }

    @Override
    public int getItemCount() {
        int itemCount = mFeedList.size();
        if (mWithHeader)
            itemCount++;
        if (mWithFooter)
            itemCount++;
        return itemCount;
    }

    @Override
    public int getItemViewType(int position) {
        if (mWithHeader && isPositionHeader(position))
            return TYPE_HEADER;
        if (mWithFooter && isPositionFooter(position))
            return TYPE_FOOTER;
        if (mWithFirstElement && isPositionFirst(position))

            return TYPE_FIRST_ITEM;

        return TYPE_ITEM;
    }

    public boolean isPositionHeader(int position) {
        return position == 0 && mWithHeader;
    }

    public boolean isPositionFirst(int position) {
        return position == 0 && mWithFirstElement;
    }


    public boolean isPositionFooter(int position) {
        return position == getItemCount() - 1 && mWithFooter;
    }

    public void setWithHeader(boolean value) {
        mWithHeader = value;
    }

    public void setWithFooter(boolean value) {
        mWithFooter = value;
    }

    public void setWithFirstElement(boolean value) {
        mWithFirstElement = value;
    }

    public void setCallback(Callbacks callbacks) {
        mCallbacks = callbacks;
    }

    public class ElementsViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener, View.OnLongClickListener {

        private ImageView logo;
        private TextView title;
        private TextView desc;
        private AppCompatButton pay;

        public ElementsViewHolder(View itemView) {
            super(itemView);
            logo = (ImageView) itemView.findViewById(R.id.biz_logo);
            title = (TextView) itemView.findViewById(R.id.list_title);
            desc = (TextView) itemView.findViewById(R.id.list_desc);
            pay = (AppCompatButton) itemView.findViewById(R.id.unpay);

            itemView.setOnClickListener(this);
            pay.setOnClickListener(this);

        }

        @Override
        public void onClick(View v) {

            if (v.getId() == pay.getId()) {
                String title = mFeedList.get(getAdapterPosition()).getHotspotName();

                String sure = "Paying to " + title +", "+"which isn't within your current location, " + "do you wish to continue?";

                AlertDialog.Builder builder = new AlertDialog.Builder(context);
                builder.setMessage(sure);
                builder.setPositiveButton("YES", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        //Toast.makeText(context,"Proceed to pay dialog",Toast.LENGTH_LONG).show();

                        title2= mFeedList.get(getAdapterPosition()).getHotspotName();
                        desc2 =  mFeedList.get(getAdapterPosition()).getLocation();

                        output = "Paying " + title2 + " (" + desc2 +")";

                        promptPayBill();
                    }
                });

                builder.setNegativeButton("NO", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        dialog.cancel();
                    }
                });

                AlertDialog alert = builder.create();
                alert.show();
            }
        }



        @Override
        public boolean onLongClick(View v) {
            return false;
        }
    }

    public class FirstViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener, View.OnLongClickListener {

        private ImageView logo;
        private TextView title;
        private TextView desc;
        private AppCompatButton pay;

        public FirstViewHolder(View itemView) {
            super(itemView);
            logo = (ImageView) itemView.findViewById(R.id.biz_logo);
            title = (TextView) itemView.findViewById(R.id.list_title);
            desc = (TextView) itemView.findViewById(R.id.list_desc);
            pay = (AppCompatButton)itemView.findViewById(R.id.pay);

            itemView.setOnClickListener(this);
            pay.setOnClickListener(this);

        }

        @Override
        public void onClick(View v) {

            if (v.getId() == pay.getId()){
                title2= mFeedList.get(getAdapterPosition()).getHotspotName();
                desc2 =  mFeedList.get(getAdapterPosition()).getLocation();
                url= mFeedList.get(getAdapterPosition()).getUrl();

                output = "Paying " + title2 + " (" + desc2 +")";

                promptPayBill();

                //Toast.makeText(v.getContext(), "ITEM PRESSED = " + title, Toast.LENGTH_LONG).show();

            }

        }

        @Override
        public boolean onLongClick(View v) {
            return false;
        }
    }

    public class LoadMoreViewHolder  extends RecyclerView.ViewHolder {

        public LoadMoreViewHolder(View itemView) {
            super(itemView);
        }
    }
    private String amount;
    private String password;

    private void promptPayBill(){



        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(context);

        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        final View dialogView = inflater.inflate(R.layout.amount_pin, null);
        dialogBuilder.setView(dialogView);


        final EditText editTextAmount = (EditText) dialogView.findViewById(R.id.amount);
        final TextView desc= (TextView) dialogView.findViewById(R.id.list_description);
        final ShowHidePasswordEditText showHidePasswordEditText = (ShowHidePasswordEditText)dialogView.findViewById(R.id.pin);
        final Button buttonSend = (Button) dialogView.findViewById(R.id.send);
        final Button buttonCancel=(Button) dialogView.findViewById(R.id.cancel);
        final ImageView avator= (ImageView)dialogView.findViewById(R.id.list_avatar);
        final AlertDialog b = dialogBuilder.create();

        Glide.with(context).load(url).into(avator);

        desc.setText(output);

        final String choice = convertDBColumnsToString(SharedPrefManager.getInstance(context).getPaymeth());

        String output2 = "Enter your " + choice +" pin";

        showHidePasswordEditText.setHint(output2);

        b.setCanceledOnTouchOutside(false);
        b.show();


        buttonSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                amount = editTextAmount.getText().toString().trim();
                //String genre = spinnerGenre.getSelectedItem().toString();
                password = showHidePasswordEditText.getText().toString().trim();

                if (!TextUtils.isEmpty(amount) && !TextUtils.isEmpty(password) ) {

                    Toast.makeText(context, amount + " "+ password, Toast.LENGTH_LONG).show();

                    b.dismiss();
                }
                else{
                    Toast.makeText(context, "Please fill in all the fields", Toast.LENGTH_LONG).show();

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

    private static String convertDBColumnsToString(String type) {
        if (type == null) { return null; }
        if (type.equalsIgnoreCase("mpesa_paybill")) {
            return "M-PESA";
        } else if (type.equalsIgnoreCase("equity_acc")) {
            return "EQUITEL";
        } else if (type.equalsIgnoreCase("airtel_paybill")) {
            return "AIRTEL";
        }
        else {
            return "ORANGE";
        }

    }

    private void connectTODB(){

    }



}



