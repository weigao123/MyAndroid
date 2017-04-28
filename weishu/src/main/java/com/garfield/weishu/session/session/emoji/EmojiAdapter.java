package com.garfield.weishu.session.session.emoji;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import com.garfield.weishu.R;

/**
 * emoji每一页的grid adapter
 */
public class EmojiAdapter extends BaseAdapter {

	private Context context;
	
	private int startIndex;
	
	public EmojiAdapter(Context mContext, int startIndex) {
		this.context = mContext;
		this.startIndex = startIndex;
	}

	public int getCount() {
		int count = EmojiManager.getDisplayCount() - startIndex + 1;
		count = Math.min(count, EmoticonView.EMOJI_PER_PAGE + 1);
		return count;
	}

	@Override
	public Object getItem(int position) {
		return null;
	}

	@Override
	public long getItemId(int position) {
		return startIndex + position;
	}
	 

	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder viewHolder;
		if (convertView == null) {
			convertView = LayoutInflater.from(context).inflate(R.layout.item_emoji, null);
			viewHolder = new ViewHolder();
			viewHolder.mImageView = (ImageView) convertView.findViewById(R.id.emoji_item_imgEmoji);
			convertView.setTag(viewHolder);
		} else {
			viewHolder = (ViewHolder) convertView.getTag();
		}
		int count = EmojiManager.getDisplayCount();
		int index = startIndex + position;
		if (position == EmoticonView.EMOJI_PER_PAGE || index == count) {
			viewHolder.mImageView.setBackgroundResource(R.drawable.emoji_del);
		} else if (index < count) {
			viewHolder.mImageView.setBackgroundDrawable(EmojiManager.getDisplayDrawable(context, index));
		}
		return convertView;
	}

	private static class ViewHolder {
		ImageView mImageView;
	}

}