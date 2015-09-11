package com.mszychowiak.meishiscanner.tasks.recognition;

import java.util.List;
import java.util.Map;

public interface OnRecognitionTaskListener {
	void onExtractionFinished(List<Map<String, List<String>>> result);
}
