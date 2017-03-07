package com.garfield.weishu.developer.music.ui;

import android.content.Intent;
import android.media.RemoteController;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.support.annotation.RequiresApi;
import android.view.KeyEvent;
import android.view.View;
import android.widget.TextView;

import com.garfield.weishu.R;
import com.garfield.weishu.developer.music.model.MusicInfo;
import com.garfield.weishu.developer.music.service.NotificationStateListener;
import com.garfield.weishu.ui.fragment.AppBaseFragment;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * Created by gaowei3 on 2017/2/15.
 */

@RequiresApi(api = Build.VERSION_CODES.KITKAT)
public class MusicControlFragment extends AppBaseFragment {

    @BindView(R.id.fragment_music_text)
    TextView mTextView;

    private RemoteController mRemoteController;

    @Override
    protected int onGetFragmentLayout() {
        return R.layout.fragment_music;
    }

    @Override
    protected String onGetToolbarTitleResource() {
        return getString(R.string.music);
    }

    @Override
    protected void onInitViewAndData(View rootView, Bundle savedInstanceState) {
        super.onInitViewAndData(rootView, savedInstanceState);

        NotificationStateListener.addListener(mMusicListener);
        mRemoteController = NotificationStateListener.getRemoteController();
        mTextView.setText(NotificationStateListener.getMusicInfo().getName());
    }

    @OnClick({R.id.fragment_music_1, R.id.fragment_music_2, R.id.fragment_music_3})
    public void onClick(View view) {
        if (mRemoteController == null) {
            return;
        }
        switch (view.getId()) {
            case R.id.fragment_music_1:

//                intent = new Intent(Intent.ACTION_MEDIA_BUTTON);
//                keyEvent = new KeyEvent(KeyEvent.ACTION_DOWN, KeyEvent.KEYCODE_MEDIA_PREVIOUS);
//                intent.putExtra(Intent.EXTRA_KEY_EVENT, keyEvent);
//                mActivity.sendBroadcast(intent);
//
//                keyEvent = new KeyEvent(KeyEvent.ACTION_UP, KeyEvent.KEYCODE_MEDIA_PREVIOUS);
//                intent.putExtra(Intent.EXTRA_KEY_EVENT, keyEvent);
//                mActivity.sendBroadcast(intent);

                mRemoteController.sendMediaKeyEvent(new KeyEvent(KeyEvent.ACTION_DOWN, KeyEvent.KEYCODE_MEDIA_PREVIOUS));
                mRemoteController.sendMediaKeyEvent(new KeyEvent(KeyEvent.ACTION_UP, KeyEvent.KEYCODE_MEDIA_PREVIOUS));
                break;
            case R.id.fragment_music_2:
                mRemoteController.sendMediaKeyEvent(new KeyEvent(KeyEvent.ACTION_DOWN, KeyEvent.KEYCODE_MEDIA_PLAY_PAUSE));
                mRemoteController.sendMediaKeyEvent(new KeyEvent(KeyEvent.ACTION_UP, KeyEvent.KEYCODE_MEDIA_PLAY_PAUSE));
                break;
            case R.id.fragment_music_3:
                mRemoteController.sendMediaKeyEvent(new KeyEvent(KeyEvent.ACTION_DOWN, KeyEvent.KEYCODE_MEDIA_NEXT));
                mRemoteController.sendMediaKeyEvent(new KeyEvent(KeyEvent.ACTION_UP, KeyEvent.KEYCODE_MEDIA_NEXT));
                break;
        }
    }

    @OnClick(R.id.fragment_music_4)
    void requestPermission() {
        Intent intent = new Intent(Settings.ACTION_NOTIFICATION_LISTENER_SETTINGS);
        mActivity.startActivity(intent);
    }

    private NotificationStateListener.MusicListener mMusicListener = new NotificationStateListener.MusicListener() {
        @Override
        public void musicInfoChanged(MusicInfo info) {
            mTextView.setText(info.getName());
        }
    };

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        NotificationStateListener.removeListener(mMusicListener);
    }
}
