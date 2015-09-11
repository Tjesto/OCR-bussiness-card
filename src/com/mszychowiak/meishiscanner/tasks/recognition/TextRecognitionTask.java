package com.mszychowiak.meishiscanner.tasks.recognition;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.mszychowiak.meishiscanner.tasks.parsing.AbstractParsingTask;

import android.app.AlertDialog;
import android.content.Context;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.util.Log;

public abstract class TextRecognitionTask
		extends
		AsyncTask<android.graphics.Bitmap, Void, List<Map<String, List<String>>>> {
	private static final String TAG = TextRecognitionTask.class.getSimpleName();
	
	protected final List<Map<String, List<String>>> results = new ArrayList<Map<String,List<String>>>();
	
	private final OnRecognitionTaskListener listener;

	private final AbstractParsingTask parsingTask;
	
	public TextRecognitionTask(OnRecognitionTaskListener listener) {
		this.listener = listener;
		this.parsingTask = AbstractParsingTask.Creator.create();
	}
	@Override
	protected List<Map<String,List<String>>> doInBackground(Bitmap... params) {
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
		Log.d("mszych", "extractionPerfrmed");
		extractionSucceed = (extractedResult != null && extractedResult.trim().length() > 0);
		if (extractionSucceed) {
			Map<String, List<String>> resultsMap = new HashMap<String, List<String>>();
			List<String> a = new ArrayList<String>();
			a.add(extractedResult);
			resultsMap.put("all", a);			
			results.add(resultsMap);
			results.add(parsingTask.execute(extractedResult));
			Log.d("mszych", extractedResult);				
		}
		Log.i(TAG, "Bitmap extraction finished");
	}
	
	protected abstract String extraction(Bitmap bmp);
	
	@Override
	protected void onPostExecute(List<Map<String, List<String>>> result) {		
		super.onPostExecute(result);
		if (listener != null) {
			listener.onExtractionFinished(result);
		}
		Log.i(TAG, "Background task finished");
	}
	

}
