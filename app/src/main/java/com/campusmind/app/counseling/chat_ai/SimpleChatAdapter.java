package com.campusmind.app.counseling.chat_ai;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.campusmind.app.R;

import java.util.List;

public class SimpleChatAdapter extends RecyclerView.Adapter<SimpleChatAdapter.ChatViewHolder> {

    private final List<String> items;

    public SimpleChatAdapter(List<String> items) {
        this.items = items;
    }

    @NonNull
    @Override
    public ChatViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_simple_message, parent, false);
        return new ChatViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ChatViewHolder holder, int position) {
        holder.txtMessage.setText(items.get(position));
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    static class ChatViewHolder extends RecyclerView.ViewHolder {
        TextView txtMessage;

        ChatViewHolder(@NonNull View itemView) {
            super(itemView);
            txtMessage = itemView.findViewById(R.id.txt_message);
        }
    }
}
