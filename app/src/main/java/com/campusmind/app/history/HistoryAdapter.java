package com.campusmind.app.history;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RatingBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.campusmind.app.R;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class HistoryAdapter extends RecyclerView.Adapter<HistoryAdapter.HistoryViewHolder> {

    private final List<HistorySession> sessions;

    public HistoryAdapter(List<HistorySession> sessions) {
        this.sessions = sessions;
    }

    static class HistoryViewHolder extends RecyclerView.ViewHolder {
        TextView tvType, tvDate, tvLastMessage, tvStatus;
        RatingBar rbRating;

        public HistoryViewHolder(@NonNull View itemView) {
            super(itemView);
            tvType = itemView.findViewById(R.id.tv_session_type);
            tvDate = itemView.findViewById(R.id.tv_session_time);
            tvLastMessage = itemView.findViewById(R.id.tv_session_last_message);
            tvStatus = itemView.findViewById(R.id.tv_session_status);
            rbRating = itemView.findViewById(R.id.rb_session_rating);
        }
    }

    @NonNull
    @Override
    public HistoryViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_history, parent, false);
        return new HistoryViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull HistoryViewHolder holder, int position) {
        HistorySession session = sessions.get(position);

        holder.tvType.setText(session.session_type != null ? session.session_type.toUpperCase() : "-");
        holder.tvStatus.setText(session.status != null ? session.status : "-");
        holder.tvDate.setText(session.started_at != null ? formatDate(session.started_at) : "-");

        if (session.messages != null && !session.messages.isEmpty()) {
            holder.tvLastMessage.setText(
                    session.messages.get(session.messages.size() - 1).text_content
            );
        } else {
            holder.tvLastMessage.setText("-");
        }

        if (session.assessment != null) {
            holder.rbRating.setRating(session.assessment.rating);
        } else {
            holder.rbRating.setRating(0);
        }
    }

    @Override
    public int getItemCount() {
        return sessions != null ? sessions.size() : 0;
    }

    private String formatDate(String dateStr) {
        try {
            SimpleDateFormat input = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault());
            Date date = input.parse(dateStr);
            SimpleDateFormat output = new SimpleDateFormat("dd MMM yyyy, HH:mm", Locale.getDefault());
            return output.format(date);
        } catch (Exception e) {
            return dateStr;
        }
    }
}
