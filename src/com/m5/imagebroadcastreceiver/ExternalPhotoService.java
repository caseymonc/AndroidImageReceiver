package com.m5.imagebroadcastreceiver;

import java.util.HashSet;
import java.util.Set;

import com.m5.imagebroadcastreceiver.GPSReceiver.GPSReceiverListener;
import com.m5.imagebroadcastreceiver.ImageReceiver.ImageReceiverListener;

import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.IntentFilter.MalformedMimeTypeException;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationManager;

public class ExternalPhotoService implements ImageReceiverListener, GPSReceiverListener{

	private ImageReceiver imageReceiver;
	private PendingIntent gpsIntent;
	private Set<ExternalPhotoListener> listeners;
	private Location lastLocation;
	private static ExternalPhotoService instance;
	
	
	private ExternalPhotoService() { 
		listeners = new HashSet<ExternalPhotoListener>();
	}
	
	public static ExternalPhotoService getInstance() {
		if(instance == null) {
			instance = new ExternalPhotoService();
		}
		return instance;
	}
	
	public void toggleListeningForImages(Context context) {
		if(isListeningForImages()) {
			this.stopListeningForImages(context);
		}else{
			this.startListeningForImages(context);
		}
	}
	
	public void startListeningForImages(Context context) {
		if(isListeningForImages()) return;
		try {
			
			IntentFilter filter = new IntentFilter();
			filter.addAction("com.android.camera.NEW_PICTURE");//Pre android 4.0
			filter.addAction("android.hardware.action.NEW_PICTURE");//4.0+
			filter.addDataType("image/*");
			ImageReceiver.getInstance().addListener(this);
			context.registerReceiver(ImageReceiver.getInstance(), filter);
			imageReceiver = ImageReceiver.getInstance();
			startListeningForLocation(context);
		} catch (MalformedMimeTypeException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public void stopListeningForImages(Context context) {
		if(!isListeningForImages()) return;
		context.unregisterReceiver(imageReceiver);
		imageReceiver = null;
		stopListeningForLocation(context);
		ImageReceiver.getInstance().removeListener(this);
	}

	public boolean isListeningForImages() {
		return imageReceiver != null;
	}
	
	private void startListeningForLocation(Context context) {
		LocationManager locationManager = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);
		Intent activeIntent = new Intent(context, GPSReceiver.class);
		gpsIntent = PendingIntent.getBroadcast(context, 0, activeIntent, PendingIntent.FLAG_UPDATE_CURRENT);
		
		Criteria criteria = new Criteria();
		criteria.setAccuracy(Criteria.ACCURACY_FINE);
		
		locationManager.requestLocationUpdates(0, 0, criteria, gpsIntent);
		
		GPSReceiver.addListener(this);
	}
	
	private void stopListeningForLocation(Context context) {
		LocationManager locationManager = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);
		locationManager.removeUpdates(gpsIntent);
		gpsIntent = null;
		GPSReceiver.removeListener(this);
	}
	
	public boolean isListeningForLocation() {
		return gpsIntent != null;
	}
	
	@Override
	public void receivedImage(String uri) {
		for(ExternalPhotoListener listener : listeners) {
			listener.receivedImage(uri, lastLocation);
		}
	}
	
	@Override
	public void receivedLocation(Location location) {
		lastLocation = location;
	}
	
	public void addListener(ExternalPhotoListener listener) {
		listeners.add(listener);
	}
	
	public void removeListener(ExternalPhotoListener listener) {
		listeners.remove(listener);
	}
	
	public interface ExternalPhotoListener {
		void receivedImage(String uri, Location location);
	}

	
	
}
