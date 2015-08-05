package com.mszychowiak.experimental.startcv;

import java.util.List;
import java.util.Map;

public interface OnRecognitionTaskListener {
	void onExtractionFinished(List<Map<String, String>> result);
}
