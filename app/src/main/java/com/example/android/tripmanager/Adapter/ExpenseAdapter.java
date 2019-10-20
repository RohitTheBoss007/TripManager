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
import com.example.android.tripmanager.Expense;
import com.example.android.tripmanager.R;
import com.google.firebase.database.DatabaseReference;

import java.util.List;

public class ExpenseAdapter extends RecyclerView.Adapter<ExpenseAdapter.ExpenseAdapterViewHolder> {
    Context context;
    List<Expense>expenses;
    DatabaseReference ref;
    RecyclerView rvxp;

    public ExpenseAdapter(Context context, List<Expense> expenses, DatabaseReference ref) {
        this.context = context;
        this.expenses = expenses;
        this.ref = ref;
    }

    @NonNull
    @Override
    public ExpenseAdapter.ExpenseAdapterViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view=LayoutInflater.from(context).inflate(R.layout.item_expense,parent,false);
        return new ExpenseAdapterViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ExpenseAdapter.ExpenseAdapterViewHolder holder, int position) {
        final Expense expense=expenses.get(position);
        if (expense!=null){
            holder.name.setText(expense.getDescription());
            holder.cost.setText("Cost: "+expense.getCost());
            holder.uploadedBy.setText("Uploaded By: "+expense.getUploadedBy());
            holder.time.setText(expense.getTime());
            Glide.with(context).load(expense.getImageUrl()).dontAnimate().into(holder.pic);
        }

    }

    @Override
    public int getItemCount() {
        return expenses.size();
    }

    public class ExpenseAdapterViewHolder extends RecyclerView.ViewHolder {
        TextView name,cost,uploadedBy,time;
        ImageView pic;
        public ExpenseAdapterViewHolder(View itemView) {
            super(itemView);
            name=itemView.findViewById(R.id.tv_name);
            cost=itemView.findViewById(R.id.tv_cost);
            uploadedBy=itemView.findViewById(R.id.tv_UploadedBy);
            time=itemView.findViewById(R.id.tv_Time);
            pic=itemView.findViewById(R.id.iv_upload);
        }
    }
}
