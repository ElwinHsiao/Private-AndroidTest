package com.example.androidtest;

import android.content.Context;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.widget.ListView;

public class MemListView extends ListView {
	public MemListView(Context context) {
		super(context);
	}
	
	public MemListView(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	public MemListView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
	}

	@Override
    protected void onFocusChanged(boolean gainFocus, int direction, Rect previouslyFocusedRect) {
		int lastSelectItem = getSelectedItemPosition();
		super.onFocusChanged(gainFocus, direction, previouslyFocusedRect);
		if (gainFocus) {
			setSelection(lastSelectItem);
		}
	}
}
