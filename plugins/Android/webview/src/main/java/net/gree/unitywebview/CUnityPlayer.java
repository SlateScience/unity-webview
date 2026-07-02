package net.gree.unitywebview;
import com.unity3d.player.*;
import android.content.Context;

public class CUnityPlayer extends UnityPlayerForActivityOrService
{
    public CUnityPlayer(Context context, IUnityPlayerLifecycleEvents lifecycleEvents) {
        super(context, lifecycleEvents);
    }
}