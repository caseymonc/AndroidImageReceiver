package com.m5.imagebroadcastreceiver;

import java.util.HashSet;
import java.util.Set;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.location.LocationManager;

public class GPSReceiver extends BroadcastReceiver {
	
	private static Set<GPSReceiverListener> listeners;
	private static Location lastLocation;
	
	@Override
	public void onReceive(Context context, Intent intent) {
		if (intent.hasExtra(LocationManager.KEY_LOCATION_CHANGED)) {
			lastLocation = (Location)intent.getExtras().get(LocationManager.KEY_LOCATION_CHANGED);
			
			ensureListenersSet();
			for(GPSReceiverListener listener : listeners) {
				listener.receivedLocation(lastLocation);
			}
		}

	}
	
	public static void addListener(GPSReceiverListener listener) {
		ensureListenersSet();
		listeners.add(listener);
	}
	
	public static void removeListener(GPSReceiverListener listener) {
		ensureListenersSet();
		listeners.remove(listener);
	}
	
	private static void ensureListenersSet() {
		if(listeners == null) listeners = new HashSet<GPSReceiverListener>();
	}
	
	public interface GPSReceiverListener {
		void receivedLocation(Location location);
	}

}
