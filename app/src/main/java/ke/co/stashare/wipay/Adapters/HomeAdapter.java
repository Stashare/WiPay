package ke.co.stashare.wipay.Adapters;

import android.net.wifi.ScanResult;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.List;

import ke.co.stashare.wipay.R;
import ke.co.stashare.wipay.model.Home;
import ke.co.stashare.wipay.ui.HomePage;

/**
 * Created by Ken Wainaina on 06/04/2017.
 */

public class HomeAdapter extends RecyclerView.Adapter<HomeAdapter.RecyclerViewHolder> {

    private List<Home> results;
    private final OnItemClickListener listener;
    private final OnItemLongClickListener longclick;



    public interface OnItemClickListener {

        void onItemClick(Home item);

    }

    public interface OnItemLongClickListener {
        void onItemLongClicked(Home item);
    }


    public HomeAdapter(List<Home> results, OnItemClickListener listener, OnItemLongClickListener longclick) {

        this.results = results;
        this.listener=listener;
        this.longclick=longclick;

    }


    @Override
    public HomeAdapter.RecyclerViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.home_carditems, parent, false);
        return new HomeAdapter.RecyclerViewHolder(v);
    }


    @Override
    public void onBindViewHolder(HomeAdapter.RecyclerViewHolder holder, int position) {

        //holder.bind(position);
        holder.bind(results.get(position), listener,longclick);

    }

    @Override
    public int getItemCount()    {
        return results.size();
    }


    class RecyclerViewHolder extends RecyclerView.ViewHolder {
        private TextView mAccessPoint;
        private ImageView icon;
        private TextView desc;
        private LinearLayout linearLayout;

        public RecyclerViewHolder(View itemView) {
            super(itemView);
            mAccessPoint = (TextView) itemView.findViewById(R.id.title);
            icon =(ImageView)itemView.findViewById(R.id.icon2);
            desc = (TextView)itemView.findViewById(R.id.subtitle);

            linearLayout =(LinearLayout)itemView.findViewById(R.id.name_title);

        }

        public void bind(final Home item,
                         final OnItemClickListener listener, final OnItemLongClickListener longclick) {
            mAccessPoint.setText(item.getTitle());
            icon.setImageResource(item.getImage());
            linearLayout.setBackgroundResource(item.getImage_bcg());
            desc.setText(item.getDesc());

            itemView.setOnClickListener(new View.OnClickListener() {

                @Override public void onClick(View v) {

                    listener.onItemClick(item);

                }

            });
            itemView.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View view) {

                    longclick.onItemLongClicked(item);

                    return true;
                }
            });

        }

    }


}



