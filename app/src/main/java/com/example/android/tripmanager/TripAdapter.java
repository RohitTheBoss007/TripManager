package com.example.android.tripmanager;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class TripAdapter extends RecyclerView.Adapter<TripAdapter.TripAdapterViewHolder>implements Filterable
{
    Context context;
    List<Trip> trips;
    List<Trip>copyTrips;
    DatabaseReference tripDb;
    TextView tvTrip;

    public TripAdapter(Context context, List<Trip> trips, DatabaseReference tripDb) {
        this.context = context;
        this.trips = trips;
        this.tripDb = tripDb;
        copyTrips=new ArrayList<>(trips);
    }
    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public int getItemViewType(int position) {
        return position;
    }

    @NonNull
    @Override
    public TripAdapterViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view=LayoutInflater.from(context).inflate(R.layout.item_trip,parent,false);
        return new TripAdapterViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final TripAdapterViewHolder holder, int position) {
        final Trip trip=trips.get(position);

                    holder.tvTrip.setText(trip.getTripName());
                    holder.tvTrip.setGravity(Gravity.START);


        holder.lltrip.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(context, "You are now managing "+trip.getTripName()+" Trip ", Toast.LENGTH_SHORT).show();
                Intent intent=new Intent(context, TripActivity.class);
                intent.putExtra("Trip",trip.getTripName());
                intent.putExtra("Creator",trip.getCreator());
                context.startActivity(intent);
            }
        });

    }

    @Override
    public int getItemCount() {
        return trips.size();
    }

    @Override
    public Filter getFilter() {
        return tripFilter;
    }
    private Filter tripFilter=new Filter() {
        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
            List<Trip> filteredList=new ArrayList<>();
            if(constraint==null||constraint.length()==0)
            {
                filteredList.addAll(copyTrips);
            }
            else {
                String filterPattern = constraint.toString().toLowerCase().trim();
                for(Trip t:copyTrips)
                {
                    if(t.getTripName().toLowerCase().contains(filterPattern))
                        filteredList.add(t);
                }
            }
            FilterResults results=new FilterResults();
            results.values=filteredList;
            return results;
        }

        @Override
        protected void publishResults(CharSequence constraint, FilterResults results) {
            trips.clear();
            trips.addAll((List)results.values);
            notifyDataSetChanged();

        }
    };

    public class TripAdapterViewHolder extends RecyclerView.ViewHolder {
        TextView tvTrip;
        LinearLayout lltrip;
        public TripAdapterViewHolder(View itemView) {
            super(itemView);
            lltrip=itemView.findViewById(R.id.lltrip);
            tvTrip=itemView.findViewById(R.id.tvTrip);
        }
    }
}
