package io.mindease.cordova;

import android.content.pm.PackageManager;
import android.Manifest;
import android.util.Log;

import java.lang.reflect.Field;
import java.lang.IllegalAccessException;
import java.lang.NoSuchFieldException;
import java.util.Map;
import java.util.Objects;
import java.util.Random;
import java.util.concurrent.ConcurrentHashMap;

import org.apache.cordova.CordovaPlugin;
import org.apache.cordova.CallbackContext;

import org.json.JSONArray;
import org.json.JSONException;

public class Permission extends CordovaPlugin {
    private static final String TAG = "mindease.Permission";

    private final Map<Integer, CallbackContext> resultCallbacks =
        new ConcurrentHashMap<Integer, CallbackContext>();

    @Override
    public boolean execute(
        String action, JSONArray args, CallbackContext callbackContext
    ) throws JSONException {
        Log.i(TAG, "Starting");

        if (!action.equals("ensure")) {
          callbackContext.error("Permission does not support action " + action + ".");
          return false;
        }

        this.ensure(args.getString(0), callbackContext);

        return true;
    }

    private void ensure(String name, CallbackContext callbackContext) {
        Field permissionField = null;
        try {
            permissionField = Manifest.permission.class.getDeclaredField(name);
        } catch (NoSuchFieldException _e) {
            callbackContext.error("Could not find permission " + name + ".");
            return;
        }

        String permission = null;
        try {
            permission = (String) permissionField.get(null);
        } catch (IllegalAccessException _e) {
            callbackContext.error(
                    "Could not get constant value of permission " + name + "."
            );
            return;
        }

        Log.i(TAG, "Checking permission " + permission + ".");
        if (cordova.hasPermission(permission)) {
            Log.i(TAG, "Already had permission.");
            callbackContext.success();
            return;
        } else {
            Log.i(TAG, "Didn't have permission.");
            // Not a UUID, but still only 1 in 2 billion chance of collision.
            int requestCode = new Random().nextInt(Integer.MAX_VALUE);
            Log.i(TAG, "Request code: " + requestCode);
            resultCallbacks.put(requestCode, callbackContext);

            Log.i(TAG, "Requesting permission " + permission + ".");
            cordova.requestPermission(this, requestCode, permission);
        }
    }

    /*
     * This is deprecated in favour of onRequestPermissionsResult, but as of
     * this writing, Cordova never calls onRequestPermissionsResult. So we have
     * to use the deprecated method until they fix this.
     */
    @SuppressWarnings("deprecation")
    @Override
    public void onRequestPermissionResult(
        int requestCode, String[] permissions, int[] grantResults
    ) throws JSONException {
        Log.i(TAG, "Permission request callback called. requestCode: " + requestCode);

        assert grantResults.length == 1;

        CallbackContext callbackContext = resultCallbacks.remove(requestCode);

        if (Objects.equals(grantResults[0], PackageManager.PERMISSION_GRANTED)) {
            Log.i(TAG, "Granted permission " + permissions[0] + ".");
            callbackContext.success();
            return;
        } else {
            Log.i(TAG, "Not granted permission " + permissions[0] + ".");
            callbackContext.error("Could not obtain permission " + permissions[0] + ".");
            return;
        }
    }
}
