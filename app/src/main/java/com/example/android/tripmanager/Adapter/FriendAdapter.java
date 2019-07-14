package com.example.android.tripmanager.Adapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.android.tripmanager.Common;
import com.example.android.tripmanager.R;
import com.example.android.tripmanager.TripActivity;
import com.example.android.tripmanager.User;
import com.google.firebase.database.DatabaseReference;

import java.util.ArrayList;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class FriendAdapter extends RecyclerView.Adapter<FriendAdapter.FriendAdapterViewHolder> implements Filterable {
    Context context;
    List<User> friends;
    List<User>copyFriends;
    DatabaseReference users;

    public FriendAdapter(Context context, List<User> friends, DatabaseReference users) {
        this.context = context;
        this.friends = friends;
        this.users = users;
        copyFriends=new ArrayList<>(friends);
    }

    @NonNull
    @Override
    public FriendAdapterViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view=LayoutInflater.from(context).inflate(R.layout.item_friend,parent,false);
        return new FriendAdapterViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull FriendAdapterViewHolder holder, int position) {
        final User name=friends.get(position);
        if(!name.getUsername().equals(Common.currentUser.getUsername()))
        {
            holder.tvFriend.setText(name.getUsername());
            holder.tvFriend.setGravity(Gravity.START);


        }
        else {
            holder.tvFriend.setText("You");
            holder.tvFriend.setGravity(Gravity.START);

        }
        Glide.with(context).load(name.getPicUrl()).into(holder.dp);
        holder.llFriend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(context, "You added "+name.getUsername(), Toast.LENGTH_SHORT).show();
                Intent i=new Intent(context,TripActivity.class);
                i.putExtra("Name",name.getUsername());
                ((Activity)context).setResult(Activity.RESULT_OK,i);
                ((Activity)context).finish();
            }
        });
        holder.call.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent callIntent=new Intent();
                callIntent.setData(Uri.parse("tel:"+name.getMobile()));
                context.startActivity(callIntent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return friends.size();
    }

    @Override
    public Filter getFilter() {
        return friendFilter;
    }
    private Filter friendFilter=new Filter() {
        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
            List<User>filteredList=new ArrayList<>();
            if(constraint==null||constraint.length()==0)
            {
                filteredList.addAll(copyFriends);
            }
            else {
                String filterPattern = constraint.toString().toLowerCase().trim();
                for(User f:copyFriends)
                {
                    if(f.getUsername().toLowerCase().contains(filterPattern))
                        filteredList.add(f);
                }
            }
            FilterResults results=new FilterResults();
            results.values=filteredList;
            return results;
        }

        @Override
        protected void publishResults(CharSequence constraint, FilterResults results) {
            friends.clear();
            friends.addAll((List)results.values);
            notifyDataSetChanged();

        }
    };

    public class FriendAdapterViewHolder extends RecyclerView.ViewHolder {
        TextView tvFriend;
        LinearLayout llFriend;
        CardView cv;
        CircleImageView dp;
        ImageButton call;
        public FriendAdapterViewHolder(View itemView) {
            super(itemView);
            dp=itemView.findViewById(R.id.dp);
            call=itemView.findViewById(R.id.call);
            cv=itemView.findViewById(R.id.cv);
            tvFriend=itemView.findViewById(R.id.tvFriend);
            llFriend=itemView.findViewById(R.id.llfriend);
        }
    }
}