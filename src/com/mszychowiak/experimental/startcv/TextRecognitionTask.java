package com.mszychowiak.experimental.startcv;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.util.Log;

public abstract class TextRecognitionTask
		extends
		AsyncTask<android.graphics.Bitmap, Void, List<Map<String, String>>> {
	private static final String TAG = TextRecognitionTask.class.getSimpleName();
	
	protected final List<Map<String, String>> results = new ArrayList<Map<String,String>>();
	
	private final OnRecognitionTaskListener listener;
	
	public TextRecognitionTask(OnRecognitionTaskListener listener) {
		this.listener = listener;
	}
	@Override
	protected List<Map<String,String>> doInBackground(Bitmap... params) {
		Log.i(TAG, "Background task staret with " + params.length + " bitmaps");
		//TODO: pre-fabricate
		for (Bitmap bmp : params) {
			performExtraction(bmp);			
		}
		return results;
	}

	private void performExtraction(Bitmap bmp) {
		boolean extractionSucceed = false;
		// TODO Auto-generated method stub
		String extractedResult = extraction(bmp);
		extractionSucceed = (extractedResult != null && extractedResult.trim().length() > 0);
		if (extractionSucceed) {
			results.add(new HashMap<String, String>());
			Log.d(TAG + "-Result", extractedResult);
		}
		Log.i(TAG, "Bitmap extraction finished");
	}
	
	protected abstract String extraction(Bitmap bmp);
	
	@Override
	protected void onPostExecute(List<Map<String, String>> result) {		
		super.onPostExecute(result);
		if (listener != null) {
			listener.onExtractionFinished(result);
		}
		Log.i(TAG, "Background task finished");
	}
	

}
