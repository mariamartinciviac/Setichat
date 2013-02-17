package es.uc3m.setichat.activity;

import es.uc3m.setichat.service.SeTIChatService;
import android.app.Activity;
import android.app.ListFragment;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;

public class ChatsFragment extends ListFragment {

	// Service, that may be used to access chat features
		private SeTIChatService mService;

	    @Override
	    public void onActivityCreated(Bundle savedInstanceState) {
	        super.onActivityCreated(savedInstanceState);
	        
	        
	    } 
	    
	    
	    @Override
	    public void onAttach(Activity activity) {
	        // TODO Auto-generated method stub
	        super.onAttach(activity);
	        mService = ((MainActivity)activity).getService();
	    }
	    
	    @Override
		public void onStop(){
	    	super.onStop();
	    	
	    	
	    }
	    
	    @Override
	    public void onCreate(Bundle savedInstanceState) {
	        super.onCreate(savedInstanceState);
	    	//Populate list with contacts.
	    	//Ey, a more fancy layout could be used! You dare?!
	        setListAdapter(new ArrayAdapter<String>(getActivity(),
	                android.R.layout.simple_list_item_activated_1, 
	                new String[]{"First chat", "Second chat", "Third chat", "Fourth chat", "Fifth chat)"}));
	    }


	    @Override
	    public void onListItemClick(ListView l, View v, int position, long id) {
	    	// We need to launch a new activity to display
	        // the dialog fragment with selected text.
	        Intent intent = new Intent();
	        intent.setClass(getActivity(), SeTIChatConversationActivity.class);
	        intent.putExtra("index", position);           
	        startActivity(intent);
	    }
	
	
}
