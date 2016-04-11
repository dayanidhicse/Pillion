package com.example.dayanidhi.pillion;

/**
 * Created by arvind on 17/01/16.
 */

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.List;

public class MyRecyclerAdapter extends RecyclerView.Adapter<MyRecyclerAdapter.CustomViewHolder>{
    private List<FeedItem> feedItemList;
    private Context mContext;
    public Account obj;

    public MyRecyclerAdapter(Context context, List<FeedItem> feedItemList,Account obj) {
        this.feedItemList = feedItemList;
        this.obj=obj;
        this.mContext = context;
    }

    @Override
    public CustomViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.list_row, null);

        CustomViewHolder viewHolder = new CustomViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(CustomViewHolder customViewHolder, int i) {
        FeedItem feedItem = feedItemList.get(i);

        //feedItem.setThumbnail(obj.dp);
        //Download image using picasso library

        Picasso.with(mContext).load("https://graph.facebook.com/" + feedItem.getThumbnail()+ "/picture?type=large")
                .error(R.drawable.user157)
                .placeholder(R.drawable.user157)
                .into(customViewHolder.imageView);
//Handle click event on both title and image click
        customViewHolder.textView.setOnClickListener(clickListener);
        customViewHolder.imageView.setOnClickListener(clickListener);

        customViewHolder.textView.setTag(customViewHolder);
        customViewHolder.imageView.setTag(customViewHolder);
        //Setting text view title
        customViewHolder.textView.setText(Html.fromHtml(feedItem.getTitle()));
    }
    View.OnClickListener clickListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            CustomViewHolder holder = (CustomViewHolder) view.getTag();
            int position = holder.getPosition();

            FeedItem feedItem = feedItemList.get(position);
          //  Toast.makeText(mContext, feedItem.getTitle(), Toast.LENGTH_SHORT).show();
            Intent ii = new Intent(mContext,FindARide2.class);
                ii.putExtra("trip_id",feedItem.getTrip_id());
                ii.putExtra("Account",obj);
            mContext.startActivity(ii);


           // Intent i = new Intent(con,FindARide2.class);

        //    Toast.makeText(mContext, feedItem.getTrip_id(), Toast.LENGTH_SHORT).show();

        }
    };

    @Override
    public int getItemCount() {
        return (null != feedItemList ? feedItemList.size() : 0);
    }


    public class CustomViewHolder extends RecyclerView.ViewHolder {
        protected ImageView imageView;
        protected TextView textView;

        public CustomViewHolder(View view) {
            super(view);
            this.imageView = (ImageView) view.findViewById(R.id.thumbnail);
            this.textView = (TextView) view.findViewById(R.id.title);
        }
    }
}
