package rdev.findit;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

/**
 * Created by Oliver on 2018. 02. 27..
 */

public class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerViewAdapter.MyViewHolder> {

    private Context mContext;
    private List<DataModel> mData;


    public RecyclerViewAdapter(Context context, List<DataModel> data) {
        mContext = context;
        mData = data;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view;
        LayoutInflater mInflater = LayoutInflater.from(mContext);
        view = mInflater.inflate(R.layout.cardview_item,parent,false);


        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {

        holder.post_title.setText((CharSequence) mData.get(position).getName());
        holder.post_Image.setImageResource(mData.get(position).getKep());

    }

    @Override
    public int getItemCount() {
        return mData.size();
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder{

        TextView post_title;
        ImageView post_Image;


        public MyViewHolder(View itemView) {
            super(itemView);


            post_title = (TextView) itemView.findViewById(R.id.waht_is_it_text);
            post_Image = (ImageView) itemView.findViewById(R.id.image_card);

        }

    }

}
