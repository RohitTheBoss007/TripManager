package com.example.android.tripmanager.Adapter;

import android.content.Context;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.android.tripmanager.ChatMessage;
import com.example.android.tripmanager.Common;
import com.example.android.tripmanager.R;
import com.google.firebase.database.DatabaseReference;

import java.util.List;

public class MessageAdapter extends RecyclerView.Adapter<MessageAdapter.MessageAdapterViewHolder> {
    Context context;
    List<ChatMessage> messages;
    DatabaseReference messageDb;
    TextView tvTitle;
    ImageButton ibDelete;


    public MessageAdapter(Context context, List<ChatMessage> messages, DatabaseReference messageDb) {
        this.context = context;
        this.messages = messages;
        this.messageDb = messageDb;
    }

    @NonNull
    @Override
    public MessageAdapterViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view=LayoutInflater.from(context).inflate(R.layout.item_message,parent,false);
        return new MessageAdapterViewHolder(view);

    }

    @Override
    public void onBindViewHolder(@NonNull MessageAdapterViewHolder holder, int position) {
        ChatMessage message=messages.get(position);
        if(message.getName().equals(Common.currentUser.getUsername()))
        {
            holder.tvTitle.setText("You: "+message.getMessage());
            holder.tvTitle.setGravity(Gravity.START);
            holder.l.setBackgroundColor(Color.parseColor("#66DB49"));
        }
        else
        {
            holder.tvTitle.setText(message.getName()+": "+message.getMessage());
            holder.tvTitle.setGravity(Gravity.START);
            holder.ibDelete.setVisibility(View.GONE);
        }

    }

    @Override
    public int getItemCount() {
        return messages.size();
    }

    public class MessageAdapterViewHolder extends RecyclerView.ViewHolder {
        LinearLayout l;
        TextView tvTitle;
        ImageButton ibDelete;
        public MessageAdapterViewHolder(View itemView) {
            super(itemView);
            l=itemView.findViewById(R.id.message);
            tvTitle=itemView.findViewById(R.id.tvTitle);
            ibDelete=itemView.findViewById(R.id.ibDelete);
            ibDelete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    messageDb.child(messages.get(getAdapterPosition()).getKey()).removeValue();
                }
            });

        }
    }
}
