package es.uc3m.setichat.activity;


import es.uc3m.setichat.R;
import es.uc3m.setichat.service.SeTIChatService;
import es.uc3m.setichat.service.SeTIChatServiceBinder;
import android.app.ActionBar;
import android.app.AlertDialog;
import android.app.FragmentTransaction;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.ServiceConnection;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.IBinder;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.Menu;
import android.widget.Toast;

/**
 * This is the main activity and its used to initialize all the SeTIChat features. 
 * It configures the three tabs used in this preliminary version of SeTIChat.
 * It also start the service that connects to the SeTIChat server.
 * 
 * @author Guillermo Suarez de Tangil <guillermo.suarez.tangil@uc3m.es>
 * @author Jorge Blasco Alis <jbalis@inf.uc3m.es>
 */

public class MainActivity extends FragmentActivity implements ActionBar.TabListener {

	
	// Service used to access the SeTIChat server
	private SeTIChatService mService;
	private static final String STATE_SELECTED_NAVIGATION_ITEM = "selected_navigation_item";
	
	// Receivers that wait for notifications from the SeTIChat server
	private BroadcastReceiver openReceiver;
	private BroadcastReceiver chatMessageReceiver;
	
	private enum TabType {CONTACTS, CHATS, SETTINGS};
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		
		SharedPreferences settings = getSharedPreferences("UserLoggedIn", 0);
		String username = settings.getString("telephone", null);
		
		if (username == null){
			Log.d("PREFS","This is not working");
			Intent intent = new Intent(this, LoginActivity.class);
			startActivity(intent);
		}
		
		
			
			
			
		// Set up the action bar to show tabs.
		final ActionBar actionBar = getActionBar();
		actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);
		// For each of the sections in the app, add a tab to the action bar.
		actionBar.addTab(actionBar.newTab().setText(R.string.title_section1)
				.setTabListener(this));
		actionBar.addTab(actionBar.newTab().setText(R.string.title_section2)
				.setTabListener(this));
		actionBar.addTab(actionBar.newTab().setText(R.string.title_section3)
				.setTabListener(this));
		Log.i("Activity", "onCreate");
		
		try{
	        
	        // Make sure the service is started.  It will continue running
	        // until someone calls stopService().  The Intent we use to find
	        // the service explicitly specifies our service component, because
	        // we want it running in our own process and don't want other
	        // applications to replace it.
	        startService(new Intent(MainActivity.this,
	                SeTIChatService.class));
	        
        }catch(Exception e){

    		Log.d("MainActivity", "Unknown Error", e);

	        stopService(new Intent(MainActivity.this,
	                SeTIChatService.class));
        }
		
		
		// Create and register broadcast receivers
		IntentFilter openFilter = new IntentFilter();
		openFilter.addAction("es.uc3m.SeTIChat.CHAT_OPEN");

		 openReceiver = new BroadcastReceiver() {
		    @Override
		    public void onReceive(Context context, Intent intent) {
		    	Context context1 = getApplicationContext();
				CharSequence text = "SeTIChatConnected";
				int duration = Toast.LENGTH_SHORT;

				Toast toast = Toast.makeText(context1, text, duration);
				toast.show();
		    }
		  };

		  registerReceiver(openReceiver, openFilter);
		  
		  chatMessageReceiver = new BroadcastReceiver() {
			    @Override
			    public void onReceive(Context context, Intent intent) {
			      //do something based on the intent's action
			    	Context context1 = getApplicationContext();
					CharSequence text = "SeTIChat Message Received";
					int duration = Toast.LENGTH_SHORT;

					Toast toast = Toast.makeText(context1, text, duration);
					toast.show();
			    }
			  };
			  
		IntentFilter chatMessageFilter = new IntentFilter();
		chatMessageFilter.addAction("es.uc3m.SeTIChat.CHAT_MESSAGE");
		registerReceiver(chatMessageReceiver, chatMessageFilter);
		
		
	}
	
	
	@Override
	  public void onDestroy() {
	    super.onDestroy();
        // We stop the service if activity is destroyed
	    stopService(new Intent(MainActivity.this,
                SeTIChatService.class));
	    // We also unregister the receivers to avoid leaks.
        unregisterReceiver(chatMessageReceiver);
        unregisterReceiver(openReceiver);
	 }
	
	@Override
	protected void onStop() {
		super.onStop();
	}

	@Override
	protected void onResume() {
		Log.v("MainActivity", "onResume: Resuming activity...");
		super.onResume();
	}



	
	@Override
	public void onRestoreInstanceState(Bundle savedInstanceState) {
		// Restore the previously serialized current tab position.
		if (savedInstanceState.containsKey(STATE_SELECTED_NAVIGATION_ITEM)) {
			getActionBar().setSelectedNavigationItem(
					savedInstanceState.getInt(STATE_SELECTED_NAVIGATION_ITEM));
		}
	}

	@Override
	public void onSaveInstanceState(Bundle outState) {
		// Serialize the current tab position.
		outState.putInt(STATE_SELECTED_NAVIGATION_ITEM, getActionBar()
				.getSelectedNavigationIndex());
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.activity_main, menu);
		return true;
	}

	@Override
	public void onTabSelected(ActionBar.Tab tab,
			FragmentTransaction fragmentTransaction) {
		// When the given tab is selected, show the tab contents in the
		// container view.
		
		Log.d("TABSELECTOR", tab.getText().toString());
		
	
		String tabTextSelected = tab.getText().toString();
		
		if (tabTextSelected.equalsIgnoreCase("Contacts")){
			ContactsFragment contactFragment = new ContactsFragment();
			getFragmentManager().beginTransaction().replace(R.id.container, contactFragment).commit();
		}else if(tabTextSelected.equalsIgnoreCase("Chats")){
			ChatsFragment chatFragment = new ChatsFragment();
			getFragmentManager().beginTransaction().replace(R.id.container, chatFragment).commit();
		}else if(tabTextSelected.equalsIgnoreCase("Settings")){
			SettingsFragment settingFragment = new SettingsFragment();
			getFragmentManager().beginTransaction().replace(R.id.container, settingFragment).commit();
		}
		
		
		/*
		switch(TabType.valueOf(tab.getText().toString())){
	
		case CONTACTS:
			Log.d("TABSELECTOR", "Some more info");
			break;
		case CHATS:
			ChatsFragment chatFragment = new ChatsFragment();
			getFragmentManager().beginTransaction().replace(R.id.container, chatFragment).commit();
			break;
		case SETTINGS:
			
			break;
		default:
			Log.d("TABSELECTOR", "Why is this wrong?");
			ContactsFragment contactFragment = new ContactsFragment();
			getFragmentManager().beginTransaction().replace(R.id.container, contactFragment).commit();
			break;
		}
		
		*/
		
		//Log.d("TABSELECTOR", "Tab selected");
		
	}

	@Override
	public void onTabUnselected(ActionBar.Tab tab,
			FragmentTransaction fragmentTransaction) {
	}

	@Override
	public void onTabReselected(ActionBar.Tab tab,
			FragmentTransaction fragmentTransaction) {
	}
	
	public void update(){
		
	}
	
	  public void showException(Throwable t) {
		    AlertDialog.Builder builder=new AlertDialog.Builder(this);

		    builder
		      .setTitle("Exception!")
		      .setMessage(t.toString())
		      .setPositiveButton("OK", null)
		      .show();
	  }
	  
	  /** Defines callbacks for service binding, passed to bindService() */
	    private ServiceConnection mConnection = new ServiceConnection() {

	        @Override
	        public void onServiceConnected(ComponentName className,
	                IBinder service) {
	            // We've bound to LocalService, cast the IBinder and get LocalService instance
	        	Log.i("Service Connection", "Estamos en onServiceConnected");
	            SeTIChatServiceBinder binder = (SeTIChatServiceBinder) service;
	            mService = binder.getService();
	            
	        }

	        @Override
	        public void onServiceDisconnected(ComponentName arg0) {
	           
	        }
	    };



	public SeTIChatService getService() {
		
		return mService;
	}
	
	//SeTIChatServiceDelegate Methods
	
	public void showNotification(String message){
		Context context = getApplicationContext();
		CharSequence text = message;
		int duration = Toast.LENGTH_SHORT;

		Toast toast = Toast.makeText(context, text, duration);
		toast.show();
	}
	

}
