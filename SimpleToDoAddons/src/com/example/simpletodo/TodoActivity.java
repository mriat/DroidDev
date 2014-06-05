package com.example.simpletodo;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

import org.apache.commons.io.FileUtils;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.GestureDetector;
import android.view.GestureDetector.SimpleOnGestureListener;
import android.view.Menu;
import android.view.MotionEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

public class TodoActivity extends Activity {
	private ArrayList<String> todoItems;
	private ArrayAdapter<String> todoAdapter;
	private ListView lvItems;
	private EditText etNewItem;
	private final int REQUEST_CODE = 20;
	private int onClickPos;	
	private GestureDetector gestureDetector;
	private boolean click;
	float onSwipePos;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		setContentView(R.layout.activity_todo);
		
		etNewItem = (EditText) findViewById(R.id.etNewItem);
		lvItems = (ListView) findViewById(R.id.lvItems);
		
		readItems();
		
		todoAdapter = new ArrayAdapter(this, android.R.layout.simple_list_item_1, todoItems);
		lvItems.setAdapter(todoAdapter);
		
		// todoAdapter.add("Item 5");
		
		setupListViewListener();
		gestureDetector = new GestureDetector(this, new SwipeGestureDetector());

	}

	private void setupListViewListener() {
		
		//https://play.google.com/store/apps/details?id=com.fortysevendeg.android.swipelistview
		/*lvItems.setOnTouchListener(new View.OnTouchListener() {
	        @Override
	        public boolean onTouch(View arg0, MotionEvent event) {
	            click=true;
	            return click;
	        }
	    });*/
		
		lvItems.setOnItemLongClickListener(new OnItemLongClickListener() {
			@Override
			public boolean onItemLongClick(AdapterView<?> adapter, View item, int pos, long id) {
					todoItems.remove(pos);
					
					todoAdapter.notifyDataSetChanged();
					writeItems();
					return true;
			}
		});
		
		lvItems.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> adapter, View item, int pos, long id) {
				
				    // first parameter is the context, second is the class of the activity to launch
					onClickPos = pos;
					Intent i = new Intent(TodoActivity.this, EditItemActivity.class);
					i.putExtra("toedit", todoItems.get(pos));
					
					
					startActivityForResult(i, REQUEST_CODE); // brings up the second activity
					}
					
					
					/*todoItems.remove(pos);
					todoAdapter.notifyDataSetChanged();
					writeItems();
					return;*/
			//} 
		});
	}
	
/*
	private void populateArrayItems() {
		todoItems = new ArrayList<String>();
		todoItems.add("Item 1");
		todoItems.add("Item 2");
		todoItems.add("Item 3");
	}
*/
	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
	  // REQUEST_CODE is defined above
	  if (resultCode == RESULT_OK && requestCode == REQUEST_CODE) {
	     // Extract name value from result extras
	     String name = data.getExtras().getString("toedit");
	     // Toast the name to display temporarily on screen
	     //Toast.makeText(this, name, Toast.LENGTH_SHORT).show();
	     
	     todoItems.set(onClickPos, name);
	     todoAdapter.notifyDataSetChanged();
	     writeItems();
	  }
	} 

	
	public void onAddedItem(View v) {
		String itemText = etNewItem.getText().toString();
		todoAdapter.add(itemText);
		etNewItem.setText("");
		writeItems();
	}
	
	public void onAsc(View v) {
		Collections.sort(todoItems, StringAscComparator);
		todoAdapter.notifyDataSetChanged();
        //Toast.makeText(this, "Sorting in Ascending Order", Toast.LENGTH_LONG).show();
	}
	
	public void onDesc(View v) {
		Collections.sort(todoItems, StringDescComparator);
		todoAdapter.notifyDataSetChanged();
        //Toast.makeText(this, "Sorting in Descending Order", Toast.LENGTH_LONG).show();
	}
	
	public void onRandom(View v) {
		Collections.shuffle(todoItems);
		todoAdapter.notifyDataSetChanged();
        //Toast.makeText(this, "Sorting in Random Order", Toast.LENGTH_LONG).show();
	}
	
	
	private void readItems() {
		File filesDir = getFilesDir();
		File todoFile = new File(filesDir, "todo.txt");
		try {
			todoItems = new ArrayList<String>(FileUtils.readLines(todoFile));
		} catch (IOException e) {
			todoItems = new ArrayList<String>();
		}
	}
	
	private void writeItems() {
		File filesDir = getFilesDir();
		File todoFile = new File(filesDir, "todo.txt");
		try {
			FileUtils.writeLines(todoFile, todoItems);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	// Comparator for Ascending Order
    public static Comparator<String> StringAscComparator = new Comparator<String>() {

        public int compare(String app1, String app2) {

           // String stringName1 = app1;
           // String stringName2 = app2;
            
            //return stringName1.compareToIgnoreCase(stringName2);
        	return app1.compareToIgnoreCase(app2);
        }
    };

    //Comparator for Descending Order
    public static Comparator<String> StringDescComparator = new Comparator<String>() {

        public int compare(String app1, String app2) {

            String stringName1 = app1;
            String stringName2 = app2;
            
            return stringName2.compareToIgnoreCase(stringName1);
        }
    };
    
    @Override
    public boolean onTouchEvent(MotionEvent event) {
      if (gestureDetector.onTouchEvent(event)) {
        return true;
      }
      return super.onTouchEvent(event);
    }
    
    @Override
    public boolean dispatchTouchEvent(MotionEvent ev){
	    super.dispatchTouchEvent(ev);
	    Log.v("Inside", "=====Dispatch Event===");
	    if(click){
	        Log.v("Inside", "=====Dispatch Event=T==");
	        click=false;
	         return gestureDetector.onTouchEvent(ev);
	
	    }else{
	        return false;
	    }
    }

    private void onLeftSwipe() {
        Toast.makeText(this, "Nice LEFT swipe", Toast.LENGTH_LONG).show();
    	
        todoItems.remove(onSwipePos);
		todoAdapter.notifyDataSetChanged();
    }

    private void onRightSwipe() {
    	Toast.makeText(this, "Nice RIGHT swipe", Toast.LENGTH_LONG).show();
    }

    // Private class for gestures
    private class SwipeGestureDetector 
            extends SimpleOnGestureListener {
      // Swipe properties, you can change it to make the swipe 
      // longer or shorter and speed
      private static final int SWIPE_MIN_DISTANCE = 120;
      private static final int SWIPE_MAX_OFF_PATH = 200;
      private static final int SWIPE_THRESHOLD_VELOCITY = 200;

      @Override
      public boolean onFling(MotionEvent e1, MotionEvent e2,
                           float velocityX, float velocityY) {
        try {
          float diffAbs = Math.abs(e1.getY() - e2.getY());
          float diff = e1.getX() - e2.getX();
          onSwipePos = e1.getY();

          if (diffAbs > SWIPE_MAX_OFF_PATH)
            return false;
          
          // Left swipe
          if (diff > SWIPE_MIN_DISTANCE
          && Math.abs(velocityX) > SWIPE_THRESHOLD_VELOCITY) {
             TodoActivity.this.onLeftSwipe();

          // Right swipe
          } else if (-diff > SWIPE_MIN_DISTANCE
          && Math.abs(velocityX) > SWIPE_THRESHOLD_VELOCITY) {
        	  TodoActivity.this.onRightSwipe();
          }
        } catch (Exception e) {
          Log.e("TodoActivity", "Error on gestures");
        }
        return false;
      }
    }
  
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {

		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.todo, menu);
		return true;
	}
}



/*
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		if (id == R.id.action_settings) {
			return true;
		}
		return super.onOptionsItemSelected(item);
	}
}
*/
