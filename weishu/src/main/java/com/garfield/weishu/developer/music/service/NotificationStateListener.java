package com.garfield.weishu.developer.music.service;

import android.content.Context;
import android.media.AudioManager;
import android.media.RemoteController;
import android.os.Build;
import android.service.notification.NotificationListenerService;
import android.service.notification.StatusBarNotification;
import android.support.annotation.RequiresApi;

import com.garfield.baselib.utils.system.L;
import com.garfield.weishu.developer.music.model.MusicInfo;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

import static android.media.MediaMetadataRetriever.METADATA_KEY_TITLE;

/**
 * Created by gaowei3 on 2017/2/15.
 */

//http://hoyoshaw.github.io/2015/12/11/Android-4-3-NotificationListenerService-%E7%9A%84%E4%BD%BF%E7%94%A8/
@RequiresApi(api = Build.VERSION_CODES.KITKAT)
public class NotificationStateListener extends NotificationListenerService implements RemoteController.OnClientUpdateListener {

    private static final String TAG = NotificationStateListener.class.getSimpleName();
    private static RemoteController mRemoteController;
    private static MusicInfo mMusicInfo = new MusicInfo();
    private static List<MusicListener> mMusicListeners = new CopyOnWriteArrayList<>();

    @Override
    public void onCreate() {
        super.onCreate();
        L.d(TAG, "onCreate");
        AudioManager audioManager = (AudioManager) getSystemService(Context.AUDIO_SERVICE);
        mRemoteController = new RemoteController(this, this);
        boolean register = audioManager.registerRemoteController(mRemoteController);
        L.d(TAG, "register: "+register);
    }

    @Override
    public void onClientChange(boolean clearing) {
        L.d(TAG, "onClientChange");
    }

    @Override
    public void onClientPlaybackStateUpdate(int state) {
        L.d(TAG, "onClientPlaybackStateUpdate");

    }

    @Override
    public void onClientPlaybackStateUpdate(int state, long stateChangeTimeMs, long currentPosMs, float speed) {
        L.d("onClientPlaybackStateUpdate");

    }

    @Override
    public void onClientTransportControlUpdate(int transportControlFlags) {
        L.d("onClientTransportControlUpdate");

    }

    @Override
    public void onClientMetadataUpdate(RemoteController.MetadataEditor metadataEditor) {
        String name = (String) metadataEditor.getObject(METADATA_KEY_TITLE, "");
        //String name = (String) metadataEditor.getObject(METADATA_KEY_ALBUM, "");

        L.d("onClientMetadataUpdate: "+name);
        mMusicInfo.setName(name);

        for (MusicListener musicListener : mMusicListeners) {
            musicListener.musicInfoChanged(mMusicInfo);
        }
    }

    @Override
    public void onNotificationPosted(StatusBarNotification sbn) {
        super.onNotificationPosted(sbn);
        L.d("onNotificationPosted");
    }

    @Override
    public void onNotificationRemoved(StatusBarNotification sbn) {
        super.onNotificationRemoved(sbn);
        L.d("onNotificationRemoved");
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mRemoteController = null;
    }

    public static void addListener(MusicListener musicListener) {
        mMusicListeners.add(musicListener);
    }

    public static void removeListener(MusicListener musicListener) {
        mMusicListeners.remove(musicListener);
    }

    public interface MusicListener {
        void musicInfoChanged(MusicInfo info);
    }

    public static MusicInfo getMusicInfo() {
        return mMusicInfo;
    }

    public static RemoteController getRemoteController() {
        return mRemoteController;
    }
}
