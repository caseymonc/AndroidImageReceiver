package com.m5.imagebroadcastreceiver;

import java.util.HashSet;
import java.util.Set;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.provider.MediaStore;
import android.util.Log;
import android.widget.Toast;

public class ImageReceiver extends BroadcastReceiver {

	private static ImageReceiver instance;
	private Set<ImageReceiverListener> listeners;
	
	public static ImageReceiver getInstance() {
		if(instance == null) {
			instance = new ImageReceiver();
		}
		
		return instance;
	}
	
	private ImageReceiver() {
		super();
		listeners = new HashSet<ImageReceiverListener>();
	}
	
	@Override
	public void onReceive(Context context, Intent intent) {
		String imageUri = intent.getData().getPath();
		Log.d("TAG", "Received new photo:::"+imageUri );
		//Log.d("TAG","Full File Path: "+getRealPathFromURI(intent.getData(), context));
		
		for(ImageReceiverListener listener : listeners) {
			listener.receivedImage(imageUri);
		}
	}
	
	// This should happen in the background
	public static String getRealPathFromURI(Uri contentUri,Context context)
    {
        try
        {
            String[] proj = {MediaStore.Images.Media.DATA};

            Cursor cursor =  context.getContentResolver().query(contentUri, proj, null, null, null);
            int columnIndex = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
            cursor.moveToFirst();
            return cursor.getString(columnIndex);
        }
        catch (Exception e)
        {
            return contentUri.getPath();
        }
    }
	
	public void addListener(ImageReceiverListener listener) {
		listeners.add(listener);
	}
	
	public void removeListener(ImageReceiverListener listener) {
		listeners.remove(listener);
	}
	
	public interface ImageReceiverListener {
		void receivedImage(String uri);
	}

}
