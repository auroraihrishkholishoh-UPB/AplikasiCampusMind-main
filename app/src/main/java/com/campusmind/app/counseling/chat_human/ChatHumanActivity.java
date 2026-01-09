package com.campusmind.app.counseling.chat_human;

import android.Manifest;
import android.content.pm.PackageManager;
import android.media.MediaRecorder;
import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;
import android.widget.EditText;
import android.widget.ImageButton;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.campusmind.app.R;
import com.campusmind.app.counseling.chat_human.ChatHumanRepository;
import com.campusmind.app.counseling.chat_ai.SimpleChatAdapter;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ChatHumanActivity extends AppCompatActivity {

    private static final int REQ_RECORD_AUDIO = 1001;

    private RecyclerView rvHumanChat;
    private EditText etMessageHuman;
    private ImageButton btnSendHuman, btnVoiceNote;

    private SimpleChatAdapter adapter;
    private final List<String> messages = new ArrayList<>();

    private ChatHumanRepository repository;
    private String sessionId = "session-1"; // sementara
    private String sender = "user";

    // Voice
    private MediaRecorder mediaRecorder;
    private boolean isRecording = false;
    private File audioFile;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat_human);

        rvHumanChat = findViewById(R.id.rv_human_chat);
        etMessageHuman = findViewById(R.id.et_message_human);
        btnSendHuman = findViewById(R.id.btn_send_human);
        btnVoiceNote = findViewById(R.id.btn_voice_note);

        repository = new ChatHumanRepository(this);

        adapter = new SimpleChatAdapter(messages);
        rvHumanChat.setLayoutManager(new LinearLayoutManager(this));
        rvHumanChat.setAdapter(adapter);

        btnSendHuman.setOnClickListener(v -> sendTextMessage());
        btnVoiceNote.setOnClickListener(v -> onVoiceNoteClicked());
    }

    // ========= TEXT =========

    private void sendTextMessage() {
        String text = etMessageHuman.getText().toString().trim();
        if (TextUtils.isEmpty(text)) return;

        messages.add("You: " + text);
        adapter.notifyItemInserted(messages.size() - 1);
        rvHumanChat.scrollToPosition(messages.size() - 1);
        etMessageHuman.setText("");

        repository.sendHumanMessage(sessionId, sender, text)
                .enqueue(new Callback<ResponseBody>() {
                    @Override
                    public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                        String reply = "Konselor: Terima kasih, yuk kita bahas pelan-pelan.";
                        addReply(reply);
                    }

                    @Override
                    public void onFailure(Call<ResponseBody> call, Throwable t) {
                        String reply = "Konselor: Maaf, koneksi gangguan, tapi kamu tetap boleh cerita.";
                        addReply(reply);
                    }
                });
    }

    private void addReply(String reply) {
        messages.add(reply);
        runOnUiThread(() -> {
            adapter.notifyItemInserted(messages.size() - 1);
            rvHumanChat.scrollToPosition(messages.size() - 1);
        });
    }

    // ========= VOICE NOTE =========

    private void onVoiceNoteClicked() {
        if (!isRecording) {
            // mulai rekam
            if (checkRecordPermission()) {
                startRecording();
            }
        } else {
            // stop dan kirim
            stopRecording();
            messages.add("You mengirim voice note 🎤");
            adapter.notifyItemInserted(messages.size() - 1);
            rvHumanChat.scrollToPosition(messages.size() - 1);

            if (audioFile != null && audioFile.exists()) {
                uploadVoice(audioFile);
            }
        }
    }

    private boolean checkRecordPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            int perm = ContextCompat.checkSelfPermission(this, Manifest.permission.RECORD_AUDIO);
            if (perm != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.RECORD_AUDIO},
                        REQ_RECORD_AUDIO);
                return false;
            }
        }
        return true;
    }

    private void startRecording() {
        try {
            File dir = getExternalFilesDir("voice_notes");
            if (dir != null && !dir.exists()) dir.mkdirs();

            audioFile = new File(dir, "vn_" + System.currentTimeMillis() + ".3gp");

            mediaRecorder = new MediaRecorder();
            mediaRecorder.setAudioSource(MediaRecorder.AudioSource.MIC);
            mediaRecorder.setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP);
            mediaRecorder.setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB);
            mediaRecorder.setOutputFile(audioFile.getAbsolutePath());
            mediaRecorder.prepare();
            mediaRecorder.start();

            isRecording = true;
            btnVoiceNote.setImageResource(R.drawable.ic_stop); // ganti icon stop kalau ada
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void stopRecording() {
        try {
            if (mediaRecorder != null) {
                mediaRecorder.stop();
                mediaRecorder.release();
                mediaRecorder = null;
            }
        } catch (RuntimeException e) {
            // kalau user stop terlalu cepat
            e.printStackTrace();
        }
        isRecording = false;
        btnVoiceNote.setImageResource(R.drawable.ic_mic); // balik ke mic
    }

    private void uploadVoice(File file) {
        RequestBody reqFile =
                RequestBody.create(file, MediaType.parse("audio/3gp"));
        MultipartBody.Part body =
                MultipartBody.Part.createFormData("audio", file.getName(), reqFile);

        RequestBody sessionPart =
                RequestBody.create(sessionId, MediaType.parse("text/plain"));
        RequestBody senderPart =
                RequestBody.create(sender, MediaType.parse("text/plain"));

        repository.sendHumanVoice(body, sessionPart, senderPart)
                .enqueue(new Callback<ResponseBody>() {
                    @Override
                    public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                        // optional: kasih feedback sukses
                    }

                    @Override
                    public void onFailure(Call<ResponseBody> call, Throwable t) {
                        // optional: kasih toast kalau gagal upload
                    }
                });
    }
}
