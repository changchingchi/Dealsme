package com.sim.dealsme;
import android.app.AlertDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.util.Log;
import android.widget.Toast;

public class NetworkChangeReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(final Context context, final Intent intent) {
        final ConnectivityManager connMgr = (ConnectivityManager) context
                .getSystemService(Context.CONNECTIVITY_SERVICE);

        final android.net.NetworkInfo wifi = connMgr
                .getNetworkInfo(ConnectivityManager.TYPE_WIFI);

        final android.net.NetworkInfo mobile = connMgr
                .getNetworkInfo(ConnectivityManager.TYPE_MOBILE);

        if (!wifi.isAvailable() && !mobile.isAvailable()) {
        	
        	AlertDialog.Builder builder = new AlertDialog.Builder(context);
			AlertDialog dialog;
			dialog = builder.create();
			dialog.setMessage("FOODasian can not find Internet available, please check internet status.");
			dialog.setCancelable(false);
			dialog.setButton(DialogInterface.BUTTON_POSITIVE, "Close FOODasian", new DialogInterface.OnClickListener() {

				@Override
				public void onClick(DialogInterface dialog, int which) {
					// TODO Auto-generated method stub
					System.exit(0);
				}
			});

			dialog.show();

            Log.d("Netowk Available ", "Flag No 1");
        }
    }
}