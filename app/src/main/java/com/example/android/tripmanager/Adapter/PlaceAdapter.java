package com.example.android.tripmanager.Adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.android.tripmanager.R;
import com.example.android.tripmanager.RecommendedPlace;
import com.google.firebase.database.DatabaseReference;

import java.util.List;

public class PlaceAdapter extends RecyclerView.Adapter<PlaceAdapter.PlaceAdapterViewHolder> {
    Context context;
    List<RecommendedPlace> places;
    DatabaseReference ref;
    RecyclerView rvPlaces;

    public PlaceAdapter(Context context, List<RecommendedPlace> places, DatabaseReference ref) {
        this.context = context;
        this.places = places;
        this.ref = ref;
    }

    @NonNull
    @Override
    public PlaceAdapterViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view=LayoutInflater.from(context).inflate(R.layout.item_places,parent,false);
        return new PlaceAdapterViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull PlaceAdapterViewHolder holder, int position) {
        final RecommendedPlace place=places.get(position);
        if(place!=null) {
            holder.tvCategory.setText("Category: "+place.getCategory());
            holder.tvDes.setText("Description: "+place.getDescription());
            holder.tvDate.setText("Date of Visit: "+place.getDov());
            holder.tvAddedBy.setText("Added by: "+place.getAddedBy());
        }

    }

    @Override
    public int getItemCount() {
        return places.size();
    }

    public class PlaceAdapterViewHolder extends RecyclerView.ViewHolder {
        TextView tvCategory,tvDes,tvDate,tvAddedBy;
        public PlaceAdapterViewHolder(View itemView) {
            super(itemView);
            tvCategory=itemView.findViewById(R.id.tvCategory);
            tvDes=itemView.findViewById(R.id.tvDes);
            tvDate=itemView.findViewById(R.id.tvDate);
            tvAddedBy=itemView.findViewById(R.id.tvAddedBy);
        }
    }
}
