package com.mszychowiak.experimental.startcv;

import java.util.List;
import java.util.Map;

import org.opencv.android.BaseLoaderCallback;
import org.opencv.android.CameraBridgeViewBase;
import org.opencv.android.CameraBridgeViewBase.CvCameraViewFrame;
import org.opencv.android.CameraBridgeViewBase.CvCameraViewListener;
import org.opencv.android.CameraBridgeViewBase.CvCameraViewListener2;
import org.opencv.android.LoaderCallbackInterface;
import org.opencv.android.OpenCVLoader;
import org.opencv.android.Utils;
import org.opencv.core.Mat;
import org.opencv.core.Size;
import org.opencv.imgproc.Imgproc;

import com.mszychowiak.experimental.opencvstart.R;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;
import android.view.SurfaceView;
import android.view.View;
import android.view.WindowManager;
import android.widget.SeekBar;
import android.widget.SeekBar.OnSeekBarChangeListener;
import android.widget.TextView;

public class MainActivity extends Activity implements CvCameraViewListener2, OnRecognitionTaskListener{
	
	private CameraBridgeViewBase cameraView;
	private SeekBar t1;
	private SeekBar t2;
	private TextView t1t;
	private TextView t2t;
	
	private BaseLoaderCallback callback = new BaseLoaderCallback(this) {
		public void onManagerConnected(int status) {
			switch (status) {
			case LoaderCallbackInterface.SUCCESS:
				Log.i("Log_for_mszychowiak", "OpenCV Started");
				cameraView.enableView();
				break;

			default:
				super.onManagerConnected(status);
				break;
			}
		};
	};
	private volatile TextRecognitionTask recognitionTask;
	
	protected void onCreate(android.os.Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
		setContentView(R.layout.main_layout);
		cameraView = (CameraBridgeViewBase) findViewById(R.id.HelloOpenCvView);
		cameraView.setVisibility(SurfaceView.VISIBLE);
		cameraView.setCvCameraViewListener(this);
		t1 = (SeekBar) findViewById(R.id.t1);
		t2 = (SeekBar) findViewById(R.id.t2);
		t1t = (TextView) findViewById(R.id.t1Text);
		t2t = (TextView) findViewById(R.id.t2Text);
		t1.setVisibility(View.GONE);
		t1t.setVisibility(View.GONE);
		t2.setVisibility(View.GONE);
		t2t.setVisibility(View.GONE);
		t1.setOnSeekBarChangeListener(new OnSeekBarChangeListener() {
			
			@Override
			public void onStopTrackingTouch(SeekBar seekBar) {
				//empty
				
			}
			
			@Override
			public void onStartTrackingTouch(SeekBar seekBar) {
				// empty
				
			}
			
			@Override
			public void onProgressChanged(SeekBar seekBar, int progress,
					boolean fromUser) {
				t1t.setText("t1 " + progress);
				
			}
		});
		t2.setOnSeekBarChangeListener(new OnSeekBarChangeListener() {
			
			@Override
			public void onStopTrackingTouch(SeekBar seekBar) {
				//empty
				
			}
			
			@Override
			public void onStartTrackingTouch(SeekBar seekBar) {
				// empty
				
			}
			
			@Override
			public void onProgressChanged(SeekBar seekBar, int progress,
					boolean fromUser) {
				t2t.setText("t2 " + progress);
				
			}
		});
		
	};
	
	@Override
	protected void onResume() {	
		super.onResume();
		OpenCVLoader.initAsync(OpenCVLoader.OPENCV_VERSION_2_4_6, this, callback);
	}
	
	@Override
	protected void onPause() {		
		super.onPause();
		if (cameraView != null) {
			cameraView.disableView();
		}
	}

	@Override
	public void onCameraViewStarted(int width, int height) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onCameraViewStopped() {
		// TODO Auto-generated method stub

	}

	@Override
	public Mat onCameraFrame(CvCameraViewFrame inputFrame) {
		Mat s = inputFrame.gray();
		Bitmap bmp = null;
		Mat out = new Mat();
		Mat mid = new Mat();
		//t1= 83; t2 = 64		
		Imgproc.Canny(s, out, /*t1.getProgress()*/83, /*t2.getProgress()*/64);
		bmp = Bitmap.createBitmap(out.cols(), out.rows(), Bitmap.Config.ARGB_8888);
		Utils.matToBitmap(out, bmp);
		if (recognitionTask == null) {
			recognitionTask = new TesseractRecognitionTask(this);
			recognitionTask.execute(bmp);
		}
		return out;
	}

	@Override
	public void onExtractionFinished(List<Map<String, String>> result) {
		recognitionTask = null;
		//TODO; handle result
	}

}
