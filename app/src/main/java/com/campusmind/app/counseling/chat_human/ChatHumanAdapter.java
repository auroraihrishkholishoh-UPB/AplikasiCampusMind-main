package com.campusmind.app.counseling.chat_human;

import android.content.Context;
import android.media.MediaPlayer;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.campusmind.app.R;

import java.io.IOException;
import java.util.List;

public class ChatHumanAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private static final int TYPE_TEXT = 0;
    private static final int TYPE_VOICE = 1;

    private List<MessageModel> messages;
    private Context context;

    public ChatHumanAdapter(Context context, List<MessageModel> messages) {
        this.context = context;
        this.messages = messages;
    }

    @Override
    public int getItemViewType(int position) {
        MessageModel msg = messages.get(position);
        return msg.isVoiceNote() ? TYPE_VOICE : TYPE_TEXT;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if (viewType == TYPE_VOICE) {
            View view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.item_message_voice, parent, false);
            return new VoiceViewHolder(view);
        } else {
            View view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.item_message_text, parent, false);
            return new TextViewHolder(view);
        }
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        MessageModel msg = messages.get(position);

        if (holder instanceof TextViewHolder) {
            ((TextViewHolder) holder).tvMessage.setText(msg.getText());
        } else if (holder instanceof VoiceViewHolder) {
            ((VoiceViewHolder) holder).bind(msg.getAudioPath());
        }
    }

    @Override
    public int getItemCount() {
        return messages.size();
    }

    static class TextViewHolder extends RecyclerView.ViewHolder {
        TextView tvMessage;
        TextViewHolder(@NonNull View itemView) {
            super(itemView);
            tvMessage = itemView.findViewById(R.id.tv_message);
        }
    }

    class VoiceViewHolder extends RecyclerView.ViewHolder {
        ImageButton btnPlay;
        TextView tvLabel;
        MediaPlayer mediaPlayer;
        boolean isPlaying = false;

        VoiceViewHolder(@NonNull View itemView) {
            super(itemView);
            btnPlay = itemView.findViewById(R.id.btn_play_voice);
            tvLabel = itemView.findViewById(R.id.tv_voice_label);
        }

        void bind(String audioPath) {
            tvLabel.setText("Voice note");

            btnPlay.setOnClickListener(v -> {
                if (!isPlaying) {
                    startPlay(audioPath);
                } else {
                    stopPlay();
                }
            });
        }

        private void startPlay(String audioPath) {
            mediaPlayer = new MediaPlayer();
            try {
                mediaPlayer.setDataSource(audioPath);
                mediaPlayer.prepare();
                mediaPlayer.start();
                isPlaying = true;
                btnPlay.setImageResource(android.R.drawable.ic_media_pause);

                mediaPlayer.setOnCompletionListener(mp -> stopPlay());
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        private void stopPlay() {
            if (mediaPlayer != null) {
                mediaPlayer.stop();
                mediaPlayer.release();
                mediaPlayer = null;
            }
            isPlaying = false;
            btnPlay.setImageResource(android.R.drawable.ic_media_play);
        }
    }
}
