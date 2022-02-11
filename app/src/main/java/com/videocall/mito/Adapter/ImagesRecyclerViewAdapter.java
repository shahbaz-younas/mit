package com.videocall.mito.Adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.cardview.widget.CardView;
import androidx.fragment.app.FragmentActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnSuccessListener;
import com.videocall.mito.Models.DataFire;
import com.videocall.mito.Models.Images;
import com.videocall.mito.Models.Users;
import com.videocall.mito.R;
import com.videocall.mito.Utils.BSDSelectPhoto;
import com.squareup.picasso.Callback;
import com.squareup.picasso.NetworkPolicy;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class ImagesRecyclerViewAdapter extends RecyclerView.Adapter<ImagesRecyclerViewAdapter.ViewHolder> {

    private static final String TAG = "RecyclerViewAdapter";

    private ArrayList<Images> mImages ;
    private Context mContext;
    private Users mUsers;
    private DataFire dataFire;
    public static String posImage;



    public ImagesRecyclerViewAdapter(Context context, ArrayList<Images> images,Users users) {
        mImages = images;
        mContext = context;
        mUsers = users;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_edit_profile_photo, parent, false);
        ViewHolder holder = new ViewHolder(view);
        dataFire=new DataFire();
        return holder;
    }

    @SuppressLint("ClickableViewAccessibility")
    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {
        Log.d(TAG, "onBindViewHolder: called.");

        if(mImages.size()>0) {
            holder.tv_edit_profile_count.setText(String.valueOf(position+1));
            Picasso.get().load(mImages.get(position).getThumb_picture()).networkPolicy(NetworkPolicy.OFFLINE)
                    .into(holder.imgvPhoto, new Callback() {
                        @Override
                        public void onSuccess() {
                        }

                        @Override
                        public void onError(Exception e) {
                            Picasso.get().load(mImages.get(position).getThumb_picture())
                                    .into(holder.imgvPhoto);
                        }
                    });

            if(!mImages.get(position).getThumb_picture().equals("none")){
                holder.iv_edit_profile_add.setVisibility(View.GONE);
                holder.iv_edit_profile_delete.setVisibility(View.VISIBLE);
            }else{
                holder.iv_edit_profile_add.setVisibility(View.VISIBLE);
                holder.iv_edit_profile_delete.setVisibility(View.GONE);
            }

        }

        holder.iv_edit_profile_add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                posImage=String.valueOf(position);
                BSDSelectPhoto bsdSelectPhoto = new BSDSelectPhoto();
                bsdSelectPhoto.show(((FragmentActivity)mContext).getSupportFragmentManager(), "exampleBottomSheet");
            }
        });



        holder.iv_edit_profile_delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                dataFire.getDbRefUsers()
                        .child(dataFire.getUserID())
                        .child("images")
                        .child(String.valueOf(position))
                        .child("thumb_picture").setValue("none").addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                    }
                });

               notifyItemRemoved(position);
               if(position==0){
                   dataFire.getDbRefUsers().child(dataFire.getUserID()).child("photoProfile")
                           .setValue("none");
               }
            }
        });

    }

    @Override
    public int getItemCount() {
        return mImages.size();
    }




    public class ViewHolder extends RecyclerView.ViewHolder{

        ImageView imgvPhoto,iv_edit_profile_add,iv_edit_profile_delete;
        TextView tv_edit_profile_count;
        CardView cvItemImages;

        public ViewHolder(View itemView) {
            super(itemView);
            imgvPhoto = itemView.findViewById(R.id.iv_edit_profile_photo);
            iv_edit_profile_add = itemView.findViewById(R.id.iv_edit_profile_add);
            iv_edit_profile_delete = itemView.findViewById(R.id.iv_edit_profile_delete);
            tv_edit_profile_count = itemView.findViewById(R.id.tv_edit_profile_count);
            cvItemImages = itemView.findViewById(R.id.cvItemImages);
        }


    }

}















