package net.gree.unitywebview;

import android.app.Activity;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.Window;
import com.unity3d.player.*;

//Matific NOTE:
//This is the Activity as exported from Unity as an Android project
//+ Expose PauseUnityPlayer/ResumeUnityPlayer + some logic


public class CUnityPlayerActivity extends Activity implements IUnityPlayerLifecycleEvents
{
    public static CUnityPlayerActivity GlobalUnityActivity = null;
    protected CUnityPlayer mUnityPlayer;
    private boolean mShouldPlayerPause = false;

    public void PauseUnityPlayer() {
        mShouldPlayerPause = true;
        mUnityPlayer.pause();
    }

    public void ResumeUnityPlayer() {
        mShouldPlayerPause = false;
        mUnityPlayer.resume();
    }

    @Override public void onUnityPlayerUnloaded() { moveTaskToBack(true); }
    @Override public void onUnityPlayerQuitted() {}

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        super.onCreate(savedInstanceState);
        getWindow().setFormat(2);
        mUnityPlayer = new CUnityPlayer(this, this);
        CUnityPlayerActivity.GlobalUnityActivity = this;
        setContentView(mUnityPlayer.getFrameLayout());
        mUnityPlayer.getFrameLayout().requestFocus();
    }

    @Override protected void onNewIntent(Intent intent) {
        setIntent(intent);
        mUnityPlayer.newIntent(intent);
    }

    @Override protected void onDestroy() {
        mUnityPlayer.destroy();
        super.onDestroy();
    }

    @Override protected void onPause() {
        super.onPause();
        mUnityPlayer.pause();
    }

    @Override protected void onResume() {
        super.onResume();
        if (!mShouldPlayerPause) {
            mUnityPlayer.resume();
        }
    }

    @Override public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        mUnityPlayer.configurationChanged(newConfig);
    }

    @Override public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);
        mUnityPlayer.windowFocusChanged(hasFocus);
    }

    @Override public boolean dispatchKeyEvent(KeyEvent event) {
        if (event.getAction() == KeyEvent.ACTION_MULTIPLE)
            return mUnityPlayer.injectEvent(event);
        return super.dispatchKeyEvent(event);
    }

    @Override public boolean onKeyUp(int keyCode, KeyEvent event)    { return mUnityPlayer.injectEvent(event); }
    @Override public boolean onKeyDown(int keyCode, KeyEvent event)  { return mUnityPlayer.injectEvent(event); }
    @Override public boolean onTouchEvent(MotionEvent event)         { return mUnityPlayer.injectEvent(event); }
    public boolean onGenericMotionEvent(MotionEvent event)           { return mUnityPlayer.injectEvent(event); }
}