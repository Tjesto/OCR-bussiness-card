package com.mszychowiak.experimental.startcv;

import com.googlecode.tesseract.android.TessBaseAPI;

import android.graphics.Bitmap;

public class TesseractRecognitionTask extends TextRecognitionTask {

	public TesseractRecognitionTask(OnRecognitionTaskListener listener) {
		super(listener);
	}

	@Override
	protected String extraction(Bitmap bmp) {
		TessBaseAPI tess = new TessBaseAPI();	
		tess.setImage(bmp);
		return tess.getUTF8Text();
	}

}
