package com.example.androidtest;

import android.app.Activity;
import android.os.Bundle;
import android.widget.ArrayAdapter;

public class MemListViewActivity extends Activity {
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.memlistview_test);
		
		MemListView listView = (MemListView) findViewById(R.id.listView1);
		listView.setAdapter(new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, new String[] {"1", "2", "3"}));
	}
}
