package com.rgala98.hiwefitness.Nearby;

import android.app.Activity;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.rgala98.hiwefitness.R;

import java.util.ArrayList;

public class NearbyAdapter extends RecyclerView.Adapter<NearbyAdapter.ViewHolder> {

    private Activity mActivity;
    private ArrayList<ContentsNearby> nearbyList;

    public NearbyAdapter(Activity mActivity, ArrayList<ContentsNearby> nearbyList) {
        this.mActivity = mActivity;
        this.nearbyList = nearbyList;
    }

    @NonNull
    @Override
    public NearbyAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater=LayoutInflater.from(mActivity.getApplicationContext());
        View view=layoutInflater.inflate(R.layout.row_nearby_gym,null);
        ViewHolder holder=new ViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull NearbyAdapter.ViewHolder holder,final int i) {

        final ContentsNearby contents = nearbyList.get(i);

        holder.tv_time.setText(String.valueOf(contents.getStart_time()) + " - " + contents.getEnd_time());
        holder.tv_gym_name.setText(contents.getGym_name());
        holder.tv_gym_address.setText(contents.getShort_address());
        holder.tv_distance.setText(String.valueOf(contents.getDistance()) + " Km");

        holder.gym_image.setClipToOutline(true);

        Glide
                .with(mActivity.getApplicationContext())
                .load(nearbyList.get(i).getImage_url())
                .centerCrop()
                .into(holder.gym_image);

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(mActivity.getApplicationContext(),ViewGymActivity.class);
                intent.putParcelableArrayListExtra("nearbyArrayList",nearbyList);
                intent.putExtra("position",i);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                mActivity.getApplicationContext().startActivity(intent);
                mActivity.overridePendingTransition(0,0);
            }
        });

    }

    @Override
    public int getItemCount() {
        return nearbyList.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {

        TextView tv_gym_name, tv_gym_address, tv_distance, tv_time;
        ImageView gym_image;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            tv_gym_name = (TextView)itemView.findViewById(R.id.tv_gym_name);
            tv_gym_address = (TextView)itemView.findViewById(R.id.tv_gym_address);
            tv_distance = (TextView) itemView.findViewById(R.id.tv_distance);
            tv_time = (TextView)itemView.findViewById(R.id.tv_time);
            gym_image = (ImageView) itemView.findViewById(R.id.img_gym);


        }
    }
}
