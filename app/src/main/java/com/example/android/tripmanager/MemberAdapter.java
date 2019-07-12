package com.example.android.tripmanager;

import android.content.Context;
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

import com.google.firebase.database.DatabaseReference;

import java.util.ArrayList;
import java.util.List;

public class MemberAdapter extends RecyclerView.Adapter<MemberAdapter.MemberAdapterViewHolder> implements Filterable {
    Context context;
    List<String> members;
    List<String>copyMembers;
    DatabaseReference ref;
    RecyclerView rvMembers;


    public MemberAdapter(Context context, List<String> members, DatabaseReference ref) {
        this.context = context;
        this.members = members;
        this.ref = ref;
        copyMembers=new ArrayList<>(members);
    }

    @NonNull
    @Override
    public MemberAdapterViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view=LayoutInflater.from(context).inflate(R.layout.item_member,parent,false);
        return new MemberAdapterViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MemberAdapterViewHolder holder, int position) {
        final String mem=members.get(position);
        holder.tvMember.setText(mem);
        holder.tvMember.setGravity(Gravity.START);
        holder.llMember.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(context, "You clicked "+mem, Toast.LENGTH_SHORT).show();
            }
        });

    }

    @Override
    public int getItemCount() {
        return members.size();
    }

    @Override
    public Filter getFilter() {
        return memberFilter;
    }
    private Filter memberFilter=new Filter() {
        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
            List<String>filteredList=new ArrayList<>();
            if(constraint==null||constraint.length()==0)
            {
                filteredList.addAll(copyMembers);
            }
            else {
                String filterPattern = constraint.toString().toLowerCase().trim();
                for(String m:copyMembers)
                {
                    if(m.toLowerCase().contains(filterPattern))
                        filteredList.add(m);
                }
            }
            FilterResults results=new FilterResults();
            results.values=filteredList;
            return results;
        }

        @Override
        protected void publishResults(CharSequence constraint, FilterResults results) {
            members.clear();
            members.addAll((List)results.values);
            notifyDataSetChanged();
        }
    };

    public class MemberAdapterViewHolder extends RecyclerView.ViewHolder {
        TextView tvMember;
        LinearLayout llMember;
        public MemberAdapterViewHolder(View itemView) {
            super(itemView);
            tvMember=itemView.findViewById(R.id.tvMember);
            llMember=itemView.findViewById(R.id.llmember);
        }
    }
}
