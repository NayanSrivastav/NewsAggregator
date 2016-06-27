package datalicious.com.news.utils;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.telephony.TelephonyManager;

/**
 * Created by nayan on 24/2/16.
 */
public class ConnectionUtils {
    public enum CONNECTION_TYPES {WIFI, THREE_G, TWO_G}

    /**
     * @param context
     * @return true if device is connected to internet, false otherwise
     */
    public static boolean isConnected(Context context) {
        NetworkInfo activeNetworkInfo = getNetworkInfo(context);
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }

    /**
     *
     * @param context
     * @return
     */
    public static NetworkInfo getNetworkInfo(Context context) {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo;
    }

    /**
     *
     * @param context
     * @return on which network type device is connected,{@code CONNECTION_TYPES.THREE_G,..}, null if device is not connected
     */
    public static CONNECTION_TYPES getConnectionType(Context context) {
        if (isConnected(context)) {
            // default type is assumed to be 2g if connected
            CONNECTION_TYPES connection_types = CONNECTION_TYPES.TWO_G;
            TelephonyManager mTelephonyManager = (TelephonyManager)
                    context.getSystemService(Context.TELEPHONY_SERVICE);
            int networkType = mTelephonyManager.getNetworkType();

            if (networkType == TelephonyManager.NETWORK_TYPE_HSPAP) {
                connection_types = CONNECTION_TYPES.THREE_G;
            } else {
                ConnectivityManager manager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
                if (manager.getNetworkInfo(ConnectivityManager.TYPE_WIFI)
                        .isConnectedOrConnecting()) {
                    connection_types = CONNECTION_TYPES.WIFI;
                }
            }
            return connection_types;
        } else {
            return null;
        }
    }
}
