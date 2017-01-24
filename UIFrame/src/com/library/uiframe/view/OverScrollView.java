package com.library.uiframe.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ScrollView;

/**
 * 下拉到顶部 无阴影 2015-4-21 @author lzx
 * 
 */
public class OverScrollView extends ScrollView {

	public OverScrollView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
	}

	public OverScrollView(Context context) {
		super(context);
	}

	public OverScrollView(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	@Override
	public int getOverScrollMode() {
		// TODO Auto-generated method stub
		return View.OVER_SCROLL_NEVER;
	}

}
