package com.campusmind.app.counseling.chat_ai;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.campusmind.app.R;

import java.util.List;

public class ChatAIAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private static final int TYPE_USER = 0;
    private static final int TYPE_AI = 1;

    private List<AIMessageModel> messages;

    public ChatAIAdapter(List<AIMessageModel> messages) {
        this.messages = messages;
    }

    @Override
    public int getItemViewType(int position) {
        AIMessageModel msg = messages.get(position);
        if ("user".equalsIgnoreCase(msg.getRole())) {
            return TYPE_USER;
        } else {
            return TYPE_AI;
        }
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if (viewType == TYPE_USER) {
            View view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.item_message_ai_user, parent, false);
            return new UserViewHolder(view);
        } else {
            View view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.item_message_ai_bot, parent, false);
            return new AIViewHolder(view);
        }
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        AIMessageModel msg = messages.get(position);
        if (holder instanceof UserViewHolder) {
            ((UserViewHolder) holder).tvMessage.setText(msg.getMessage());
        } else if (holder instanceof AIViewHolder) {
            ((AIViewHolder) holder).tvMessage.setText(msg.getMessage());
        }
    }

    @Override
    public int getItemCount() {
        return messages.size();
    }

    static class UserViewHolder extends RecyclerView.ViewHolder {
        TextView tvMessage;
        UserViewHolder(@NonNull View itemView) {
            super(itemView);
            tvMessage = itemView.findViewById(R.id.tv_message_ai_user);
        }
    }

    static class AIViewHolder extends RecyclerView.ViewHolder {
        TextView tvMessage;
        AIViewHolder(@NonNull View itemView) {
            super(itemView);
            tvMessage = itemView.findViewById(R.id.tv_message_ai_bot);
        }
    }
}
