package ke.co.stashare.wipay.Adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import ke.co.stashare.wipay.R;
import android.net.wifi.ScanResult;
/**
 * Created by Ken Wainaina on 15/03/2017.
 */

public class RecyclerAdapter extends RecyclerView.Adapter<RecyclerAdapter.RecyclerViewHolder> {

    private List<ScanResult> results;
    private final OnItemClickListener listener;
    private final OnItemLongClickListener longclick;



    public interface OnItemClickListener {

        void onItemClick(ScanResult item);

    }

    public interface OnItemLongClickListener {
        void onItemLongClicked(ScanResult item);
    }


    public RecyclerAdapter(List<ScanResult> results,OnItemClickListener listener,OnItemLongClickListener longclick) {

        this.results = results;
        this.listener=listener;
        this.longclick=longclick;

    }


    @Override
    public RecyclerViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.wifi_list, parent, false);
        return new RecyclerViewHolder(v);
    }


    @Override
    public void onBindViewHolder(RecyclerViewHolder holder, int position) {

        //holder.bind(position);
        holder.bind(results.get(position), listener,longclick);

    }

    @Override
    public int getItemCount()    {
        return results.size();
    }


    class RecyclerViewHolder extends RecyclerView.ViewHolder {
        private TextView mAccessPoint;


        public RecyclerViewHolder(View itemView) {
            super(itemView);
            mAccessPoint = (TextView) itemView.findViewById(R.id.access_point);

        }

        public void bind(final ScanResult item,final OnItemClickListener listener,final OnItemLongClickListener longclick) {
            mAccessPoint.setText(item.SSID);

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


