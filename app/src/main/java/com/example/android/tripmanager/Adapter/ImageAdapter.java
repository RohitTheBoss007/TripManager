package com.example.android.tripmanager.Adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.android.tripmanager.R;
import com.example.android.tripmanager.Upload;
import com.google.firebase.database.DatabaseReference;

import java.util.List;

public class ImageAdapter extends RecyclerView.Adapter<ImageAdapter.ImageAdapterViewHolder> {
    Context context;
    List<Upload>pics;
    DatabaseReference ref;
    RecyclerView rvPics;

    public ImageAdapter(Context context, List<Upload> pics, DatabaseReference ref) {
        this.context = context;
        this.pics = pics;
        this.ref = ref;
    }

    @NonNull
    @Override
    public ImageAdapterViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view=LayoutInflater.from(context).inflate(R.layout.item_image,parent,false);
        return new ImageAdapterViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ImageAdapterViewHolder holder, int position) {
        final Upload upload=pics.get(position);
        if(upload!=null)
        {
            holder.name.setText(upload.getDescription());
            holder.uploadedBy.setText("Uploaded By: "+upload.getUploadedBy());
            holder.time.setText(upload.getTime());
            Glide.with(context).load(upload.getImageUrl()).dontAnimate().into(holder.pic);
            
        }

    }

    @Override
    public int getItemCount() {
        return pics.size();
    }

    public class ImageAdapterViewHolder extends RecyclerView.ViewHolder {
        TextView name,uploadedBy,time;
        ImageView pic;
        public ImageAdapterViewHolder(View itemView) {
            super(itemView);
            name=itemView.findViewById(R.id.text_view_name);
            uploadedBy=itemView.findViewById(R.id.tvUploadedBy);
            time=itemView.findViewById(R.id.tvTime);
            pic=itemView.findViewById(R.id.image_view_upload);
        }
    }
}
