package com.mszychowiak.meishiscanner.ui.controller;

import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.hardware.Camera;
import android.hardware.Camera.Parameters;
import android.hardware.camera2.CameraAccessException;
import android.hardware.camera2.CameraManager;
import android.os.Build;

public abstract class BaseCamera {

	public static BaseCamera create(Activity activity) {
		BaseCamera camera = null;
		if (Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP) {
			//camera = new OldApiCamera(activity);
		} else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
			camera = new MershmallowCamera(activity);
		} else {
			//camera = new OldApiCamera(activity);
		}
		return camera;
	}
	
	public abstract void turnOnLight();
	public abstract void turnOffLight();
}

class OldApiCamera extends BaseCamera {

	private Camera camera;
	private final boolean hasLight;
	
	protected OldApiCamera(Activity activity) {	
		hasLight = activity.getPackageManager().hasSystemFeature(PackageManager.FEATURE_CAMERA_FLASH);		
	}
	
	@Override
	public void turnOnLight() {
		if (hasLight)
		camera = Camera.open();
		Parameters params = camera.getParameters();
		params.setFlashMode(Parameters.FLASH_MODE_TORCH);
		camera.setParameters(params);
		camera.startPreview();
	}

	@Override
	public void turnOffLight() {
		if (camera != null) {
			camera.stopPreview();
			camera.release();
		}
		
	}
	
}

class LollipopCamera extends BaseCamera {

	@Override
	public void turnOnLight() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void turnOffLight() {
		// TODO Auto-generated method stub
		
	}
	
}

class MershmallowCamera extends BaseCamera {
	
	private final CameraManager cameraManager;
	
	protected MershmallowCamera(Activity activity) {
		cameraManager = (CameraManager) activity.getSystemService(Context.CAMERA_SERVICE);
	}
	
	@Override
	public void turnOnLight() {
		try {
			cameraManager.setTorchMode("1", true);
		} catch (CameraAccessException e) {
			e.printStackTrace();
		}		
	}

	@Override
	public void turnOffLight() {
		try {
			cameraManager.setTorchMode("1", false);
		} catch (CameraAccessException e) {
			e.printStackTrace();
		}
		
	}
	
}
