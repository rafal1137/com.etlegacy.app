package com.etlegacy.app;

import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.InputDevice;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.RelativeLayout;

import androidx.preference.PreferenceManager;

import com.etlegacy.app.q3e.Q3EControlView;
import com.etlegacy.app.q3e.Q3ELang;
import com.etlegacy.app.q3e.Q3EUtils;
import com.etlegacy.app.q3e.Q3EPreference;
import com.etlegacy.app.q3e.karin.KDebugTextView;
import com.etlegacy.app.web.ETLDownload;

import org.libsdl.app.*;

public class ETLActivity extends SDLActivity {
	private Q3EControlView mControlGLSurfaceView;

	private String data;
	private String commands;

	/**
	 * Hide System UI
	 */
	private void hideSystemUI() {
		View decorView = getWindow().getDecorView();
		decorView.setSystemUiVisibility(
			View.SYSTEM_UI_FLAG_IMMERSIVE
			// Set the content to appear under the system bars so that the
			// content doesn't resize when the system bars hide and show.
			| View.SYSTEM_UI_FLAG_LAYOUT_STABLE
			| View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
			| View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
			// Hide the nav bar and status bar
			| View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
			| View.SYSTEM_UI_FLAG_FULLSCREEN);
	}

	@Override
	public void finish() {
		// finishAffinity();
		finishAndRemoveTask();
	}

	@Override
	protected void onCreate(final Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);


		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
			getWindow().getAttributes().layoutInDisplayCutoutMode = WindowManager.LayoutParams.LAYOUT_IN_DISPLAY_CUTOUT_MODE_SHORT_EDGES;
		}

		Bundle bundle = getIntent().getExtras();

		data = bundle.getString("data");
		commands = bundle.getString("command");
		Log.d("ETLActivity", "data: " + "+set fs_basepath " + data);
		Log.d("ETLActivity", "commands: " + commands);

		Q3EUtils.DumpPID(this);

		// setup language environment
		Q3ELang.Locale(this);

		// init GUI component
		InitGUI();
	}

	private void InitGUI()
	{
		SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this);

		if (mControlGLSurfaceView == null) {
			mControlGLSurfaceView = new Q3EControlView(this);
			Log.v("ETLActivity", "mControlGLSurfaceView created");
		}

		mControlGLSurfaceView.EnableGyroscopeControl(Q3EUtils.q3ei.view_motion_control_gyro);
		float gyroXSens = preferences.getFloat(Q3EPreference.pref_harm_view_motion_gyro_x_axis_sens, Q3EControlView.GYROSCOPE_X_AXIS_SENS);
		float gyroYSens = preferences.getFloat(Q3EPreference.pref_harm_view_motion_gyro_y_axis_sens, Q3EControlView.GYROSCOPE_Y_AXIS_SENS);
		if (Q3EUtils.q3ei.view_motion_control_gyro && (gyroXSens != 0.0f || gyroYSens != 0.0f))
			mControlGLSurfaceView.SetGyroscopeSens(gyroXSens, gyroYSens);

		RelativeLayout.LayoutParams params;

		params = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);

		//mControlGLSurfaceView.setZOrderOnTop();
		mControlGLSurfaceView.setZOrderMediaOverlay(true);
		mLayout.addView(mControlGLSurfaceView, params);

		if (Q3EUtils.q3ei.function_key_toolbar)
		{
			params = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, getResources().getDimensionPixelSize(R.dimen.toolbarHeight));
			View key_toolbar = mControlGLSurfaceView.CreateToolbar();
			mLayout.addView(key_toolbar, params);
		}

		setContentView(mLayout);

		mControlGLSurfaceView.requestFocus();
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event)
	{
		return mControlGLSurfaceView.OnKeyDown(keyCode, event);
	}

	@Override
	public boolean onKeyUp(int keyCode, KeyEvent event)
	{
		return mControlGLSurfaceView.OnKeyUp(keyCode, event);
	}

	@Override
	protected void onDestroy() {
		// shutdown the download thread executor
		ETLDownload.instance().shutdownExecutor();

		super.onDestroy();

		// FIXME: figure out what is actually keeping this thing alive.
		System.exit(0);
	}

	@Override
	protected void onPostCreate(Bundle savedInstanceState) {
		super.onPostCreate(savedInstanceState);
	}

	@Override
	public void onWindowFocusChanged(boolean hasFocus) {
		if (hasFocus) {
			hideSystemUI();
		}
		super.onWindowFocusChanged(hasFocus);
	}

	@Override
	public boolean onGenericMotionEvent(MotionEvent event) {
		// Check that the event came from a game controller
		if (((event.getSource() & InputDevice.SOURCE_JOYSTICK) == InputDevice.SOURCE_JOYSTICK ||
			 (event.getSource() & InputDevice.SOURCE_GAMEPAD) == InputDevice.SOURCE_GAMEPAD) && event.getAction() == MotionEvent.ACTION_MOVE) {
			SDLControllerManager.handleJoystickMotionEvent(event);
			return true;
		}
		return super.onGenericMotionEvent(event);
	}

	@Override
	protected String[] getArguments() {
		return new String[]{
				"+set fs_basepath " + data,
				commands
		};
	}

	@Override
	protected String[] getLibraries() {
		return new String[]{
			"etl"
		};
	}

}
