package rdev.findit;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

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
        view = mInflater.inflate(R.layout.cardview_item, parent, false);


        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, final int position) {

        holder.post_title.setText((CharSequence) mData.get(position).getName().toUpperCase());
        Picasso.with(mContext)
                .load(mData.get(position).getId())
                .fit()
                .centerCrop()
                .into(holder.post_Image);

        //CLICK_LISTENER

        holder.mCardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(mContext, PostActivity.class);
                intent.putExtra("PostTitle", mData.get(position).getName());
                intent.putExtra("PostDesc", mData.get(position).getDesc());
                intent.putExtra("PostContact", mData.get(position).getContact());
                intent.putExtra("PostImage", mData.get(position).getId());
                mContext.startActivity(intent);


            }
        });

        //CLICK_LISTENER

    }

    @Override
    public int getItemCount() {
        return mData.size();
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder {

        TextView post_title;
        ImageView post_Image;
        CardView mCardView;


        public MyViewHolder(View itemView) {
            super(itemView);


            post_title = (TextView) itemView.findViewById(R.id.waht_is_it_text);
            post_Image = (ImageView) itemView.findViewById(R.id.image_card);
            mCardView = (CardView) itemView.findViewById(R.id.cardview_id);

        }

    }

}
