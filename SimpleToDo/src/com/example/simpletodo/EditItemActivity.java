package com.example.simpletodo;

import android.app.Activity;
import android.app.ActionBar;
import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.os.Build;

public class EditItemActivity extends Activity {
	
	private EditText etEditItem;
	
	
	//private String asdf;
	//private String edit_hint;
	
	//String edit_text = getIntent().getStringExtra("edits");

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_edit_item);
		
		etEditItem = (EditText) findViewById(R.id.etEditItem);
		
		//asdf = getIntent().getStringExtra("edits");
		//Log.i(asdf, asdf);
		
		etEditItem.setText(getIntent().getStringExtra("toedit"));
		etEditItem.setSelection(etEditItem.length());
		

			
		

		
		
	}

	public void onEditedItem(View v) {
		etEditItem = (EditText) findViewById(R.id.etEditItem);
		
		Intent data = new Intent();
		// Pass relevant data back as a result
		data.putExtra("toedit", etEditItem.getText().toString());
		// Activity finished ok, return the data
		setResult(RESULT_OK, data); // set result code and bundle data for response
		finish(); // closes the activity, pass data to parent
		
		/*String itemText = etNewItem.getText().toString();
		todoAdapter.add(itemText);
		etNewItem.setText("");
		writeItems();*/
	}
	

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {

		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.edit_item, menu);
		return true;
	}

}
