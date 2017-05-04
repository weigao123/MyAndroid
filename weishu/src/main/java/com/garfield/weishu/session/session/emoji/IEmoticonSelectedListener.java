package com.garfield.weishu.session.session.emoji;

public interface IEmoticonSelectedListener {
	void onEmotionSelected(String key);

	void onStickerSelected(String categoryName, String stickerName);
}
