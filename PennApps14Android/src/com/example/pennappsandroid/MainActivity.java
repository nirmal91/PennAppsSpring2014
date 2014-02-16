package com.example.pennappsandroid;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.DropBoxManager.Entry;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

import com.dropbox.chooser.android.DbxChooser;

import java.io.*;
import java.util.Locale;
import com.dropbox.core.*;


public class MainActivity extends Activity {
	
	// Get your app key and secret from the Dropbox developers website.
    final String APP_KEY = "gffy44ifl4tl003";
    final String APP_SECRET = "INSERT_APP_SECRET";
    
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		

        DbxAppInfo appInfo = new DbxAppInfo(APP_KEY, APP_SECRET);

        DbxRequestConfig config = new DbxRequestConfig(
            "JavaTutorial/1.0", Locale.getDefault().toString());
        DbxWebAuthNoRedirect webAuth = new DbxWebAuthNoRedirect(config, appInfo);
	}
	/*
	static final int DBX_CHOOSER_REQUEST = 0;  // You can change this if needed
	static final String APP_KEY = "gffy44ifl4tl003";

	private Button mChooserButton;
	private DbxChooser mChooser;
	
	

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
	    mChooser = new DbxChooser(APP_KEY);
	    
	    mChooserButton = (Button) findViewById(R.id.chooser_button);
	    mChooserButton.setOnClickListener(new OnClickListener() {
	        @Override
	        public void onClick(View v) {
	            mChooser.forResultType(DbxChooser.ResultType.PREVIEW_LINK);
                mChooser.launch(MainActivity.this, DBX_CHOOSER_REQUEST);
	        }
	    });
	}
	
	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
        System.out.println("kjbnsakjn");

	    if (requestCode == DBX_CHOOSER_REQUEST) {
	        if (resultCode == Activity.RESULT_OK) {
	            DbxChooser.Result result = new DbxChooser.Result(data);
	            Log.d("main", "Link to selected file: " + result.getLink());

	            // Handle the result
	        } else {
	            // Failed or was cancelled by the user.
	        }
	    } else {
	        super.onActivityResult(requestCode, resultCode, data);
	    }
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}
	*/

}
