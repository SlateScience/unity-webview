package net.gree.unitywebview;
import com.unity3d.player.*;
import android.os.Bundle;
// MATIFIC NOTE:
// Extends UnityPlayerActivity (Unity 6 compatible) instead of the old approach of manually
// implementing Activity + IUnityPlayerLifecycleEvents. Unity 6 changed UnityPlayer's constructor
// API which caused NoSuchMethodError crashes on the old implementation.
//
// MATIFIC additions vs upstream:
//   - GlobalUnityActivity: static ref so CWebViewPlugin can call pause/resume
//   - mShouldPlayerPause / PauseUnityPlayer / ResumeUnityPlayer: let JS pause Unity while
//     the webview episode is running
//   - onResume override: re-pauses after super.onResume() if Unity was explicitly paused,
//     because UnityPlayerActivity.onResume() unconditionally calls mUnityPlayer.resume()
public class CUnityPlayerActivity extends UnityPlayerActivity
{
    public static CUnityPlayerActivity GlobalUnityActivity = null;
    private boolean mShouldPlayerPause = false;
    
    public void PauseUnityPlayer() {
        mShouldPlayerPause = true;
        mUnityPlayer.pause();
    }
    
    public void ResumeUnityPlayer() {
        mShouldPlayerPause = false;
        mUnityPlayer.resume();
    }
    
    @Override
    public void onCreate(Bundle bundle) {
        requestWindowFeature(1);
        super.onCreate(bundle);
        getWindow().setFormat(2);
        mUnityPlayer = new CUnityPlayer(this);
        setContentView(mUnityPlayer);
        mUnityPlayer.requestFocus();
        CUnityPlayerActivity.GlobalUnityActivity = this;
    }
    
    @Override
    protected void onResume() {
        super.onResume(); // UnityPlayerActivity.onResume() calls mUnityPlayer.resume()
        if (mShouldPlayerPause) {
            mUnityPlayer.pause(); // re-pause if we were explicitly paused from JS
        }
    }
}