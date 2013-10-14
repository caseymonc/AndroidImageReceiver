package com.m5.imagebroadcastreceiver;

import com.m5.imagebroadcastreceiver.ExternalPhotoService.ExternalPhotoListener;

import android.location.Criteria;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.app.Activity;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.IntentFilter.MalformedMimeTypeException;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Toast;

public class MainActivity extends Activity implements ExternalPhotoListener {

	private View imageButton;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		imageButton = findViewById(R.id.imageButton);
				
		ExternalPhotoService.getInstance().addListener(this);
		
		//Turn the receiver on/off
		imageButton.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				
				if(!ExternalPhotoService.getInstance().isListeningForImages()){
					ExternalPhotoService.getInstance().startListeningForImages(getActivity());
					Toast.makeText(getActivity(), "On", Toast.LENGTH_SHORT).show();
				}else{
					ExternalPhotoService.getInstance().stopListeningForImages(getActivity());
					Toast.makeText(getActivity(), "Off", Toast.LENGTH_SHORT).show();
				}
			}
		});
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}
	
	public Activity getActivity() {
		return this;
	}

	@Override
	public void receivedImage(String uri, Location location) {
		Toast.makeText(getActivity(), "Image: " + uri + " Location: " + location, Toast.LENGTH_SHORT).show();
	}

}
