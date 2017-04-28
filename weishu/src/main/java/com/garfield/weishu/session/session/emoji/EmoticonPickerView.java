package com.garfield.weishu.session.session.emoji;

import android.content.Context;
import android.graphics.Bitmap;
import android.os.Handler;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.HorizontalScrollView;
import android.widget.ImageView;
import android.widget.LinearLayout;


import com.garfield.baselib.utils.media.BitmapDecoder;
import com.garfield.baselib.utils.system.ScreenUtils;
import com.garfield.weishu.R;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;


public class EmoticonPickerView extends LinearLayout implements IEmoticonCategoryChanged {

    private Context context;

    private IEmoticonSelectedListener listener;

    private boolean loaded = false;

    private boolean withSticker = true;

    private EmoticonView gifView;

    private ViewPager currentEmojiPage;

    private LinearLayout pageNumberLayout;//页面布局

    private HorizontalScrollView scrollView;

    private LinearLayout tabView;

    private int categoryIndex;

    private Handler uiHandler;

    public EmoticonPickerView(Context context) {
        super(context);
        init(context);
    }

    public EmoticonPickerView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public EmoticonPickerView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init(context);
    }

    private void init(Context context) {
        this.context = context;
        this.uiHandler = new Handler(context.getMainLooper());
        LayoutInflater.from(context).inflate(R.layout.view_emoji_layout, this);
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        setupEmojView();
    }

    protected void setupEmojView() {
        currentEmojiPage = (ViewPager) findViewById(R.id.emoji_viewpager);
        pageNumberLayout = (LinearLayout) findViewById(R.id.emoji_points);
        tabView = (LinearLayout) findViewById(R.id.emoji_tab_view);
        scrollView = (HorizontalScrollView) findViewById(R.id.emoji_tab_view_container);
    }

    public void show(IEmoticonSelectedListener listener) {
        if (loaded) {
            return;
        }

        this.listener = listener;
        loadStickers();
        show();

        loaded = true;
    }

    // 添加各个tab按钮
    OnClickListener tabCheckListener = new OnClickListener() {
        @Override
        public void onClick(View v) {
            onEmoticonBtnChecked(v.getId());
        }
    };

    private void loadStickers() {
        if (!withSticker) {
            scrollView.setVisibility(View.GONE);
            return;
        }

        final StickerManager manager = StickerManager.getInstance();

        tabView.removeAllViews();

        int index = 0;

        // emoji表情
        CheckedImageButton btn = addEmoticonTabBtn(index++, tabCheckListener);
        btn.setNormalImageId(R.drawable.emoji_icon_inactive);
        btn.setCheckedImageId(R.drawable.emoji_icon);

        // 贴图
        List<StickerCategory> categories = manager.getCategories();
        for (StickerCategory category : categories) {
            btn = addEmoticonTabBtn(index++, tabCheckListener);
            setCheckedButtonImage(btn, category);
        }
    }

    private CheckedImageButton addEmoticonTabBtn(int index, OnClickListener listener) {
        CheckedImageButton emotBtn = new CheckedImageButton(context);
        emotBtn.setNormalBkResId(R.drawable.sticker_button_background_normal_layer_list);
        emotBtn.setCheckedBkResId(R.drawable.sticker_button_background_pressed_layer_list);
        emotBtn.setId(index);
        emotBtn.setOnClickListener(listener);
        emotBtn.setScaleType(ImageView.ScaleType.FIT_CENTER);
        emotBtn.setPaddingValue(ScreenUtils.dp2px(7));
        ViewGroup.LayoutParams emojBtnLayoutParams = new ViewGroup.LayoutParams(ScreenUtils.dp2px(50), ViewGroup.LayoutParams.MATCH_PARENT);
        emotBtn.setLayoutParams(emojBtnLayoutParams);
        tabView.addView(emotBtn, emojBtnLayoutParams);
        return emotBtn;
    }

    private void setCheckedButtonImage(CheckedImageButton btn, StickerCategory category) {
        try {
            InputStream is = category.getCoverNormalInputStream(context);
            if (is != null) {
                Bitmap bmp = BitmapDecoder.decode(is);
                btn.setNormalImage(bmp);
                is.close();
            }
            is = category.getCoverPressedInputStream(context);
            if (is != null) {
                Bitmap bmp = BitmapDecoder.decode(is);
                btn.setCheckedImage(bmp);
                is.close();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void onEmoticonBtnChecked(int index) {
        updateTabButton(index);
        showEmotPager(index);
    }

    private void updateTabButton(int index) {
        for (int i = 0; i < tabView.getChildCount(); ++i) {
            View child = tabView.getChildAt(i);
            if (child instanceof FrameLayout) {
                child = ((FrameLayout) child).getChildAt(0);
            }

            if (child != null && child instanceof CheckedImageButton) {
                CheckedImageButton tabButton = (CheckedImageButton) child;
                if (tabButton.isChecked() && i != index) {
                    tabButton.setChecked(false);
                } else if (!tabButton.isChecked() && i == index) {
                    tabButton.setChecked(true);
                }
            }
        }
    }

    private void showEmotPager(int index) {
        if (gifView == null) {
            gifView = new EmoticonView(context, listener, currentEmojiPage, pageNumberLayout);
            gifView.setCategoryChangCheckedCallback(this);
        }

        gifView.showStickers(index);
    }

    private void showEmojiView() {
        if (gifView == null) {
            gifView = new EmoticonView(context, listener, currentEmojiPage, pageNumberLayout);
        }
        gifView.showEmojis();
    }

    private void show() {
        if (!withSticker) {
            showEmojiView();
        } else {
            onEmoticonBtnChecked(0);
            setSelectedVisible(0);
        }
    }

    private void setSelectedVisible(final int index) {
        final Runnable runnable = new Runnable() {
            @Override
            public void run() {
                if (scrollView.getChildAt(0).getWidth() == 0) {
                    uiHandler.postDelayed(this, 100);
                }
                int x = -1;
                View child = tabView.getChildAt(index);
                if (child != null) {
                    if (child.getRight() > scrollView.getWidth()) {
                        x = child.getRight() - scrollView.getWidth();
                    }
                }
                if (x != -1) {
                    scrollView.smoothScrollTo(x, 0);
                }
            }
        };
        uiHandler.postDelayed(runnable, 100);
    }


    @Override
    public void onCategoryChanged(int index) {
        if (categoryIndex == index) {
            return;
        }

        categoryIndex = index;
        updateTabButton(index);
    }

    public void setWithSticker(boolean withSticker) {
        this.withSticker = withSticker;
    }
}
