package com.mszychowiak.experimental.startcv;

import java.io.File;

import com.googlecode.leptonica.android.Pix;
import com.googlecode.tesseract.android.TessBaseAPI;

import android.graphics.Bitmap;
import android.util.Log;

public class TesseractRecognitionTask extends TextRecognitionTask {

	public TesseractRecognitionTask(OnRecognitionTaskListener listener) {
		super(listener);
	}

	@Override
	protected String extraction(Bitmap bmp) {
		Log.d("mszych", "extraction started: " + bmp.getHeight() + " " + bmp.getWidth());
		TessBaseAPI tess = new TessBaseAPI();	
		//tess.init(new File(MainActivity.TESS_DIR_F, "pol.traineddata").getAbsolutePath(), "pol");
		tess.init("data/local/tmp", "pol");
		tess.setImage(bmp);		
		int[] wtf = tess.wordConfidences();
		for (int i : wtf) {
			Log.d("mszychConfidences", i + " ");
		}		
		for (Pix p : tess.getWords()) {
			Log.d("mszychConfidences", p.toString());
		}		
		return tess.getUTF8Text();
	}

}
