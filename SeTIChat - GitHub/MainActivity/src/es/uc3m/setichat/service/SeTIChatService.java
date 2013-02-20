package es.uc3m.setichat.service;
import es.uc3m.setichat.utils.Parser;
import es.uc3m.setichat.utils.SeTIMessage;
import java.io.IOException;

//SAX
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;
import org.xml.sax.XMLReader;

//SAX and external XSD
import javax.xml.transform.Source;
import javax.xml.transform.stream.StreamSource;
import javax.xml.validation.SchemaFactory;

import javax.xml.parsers.ParserConfigurationException;
import org.xml.sax.ErrorHandler;
import org.xml.sax.SAXException;
import org.xml.sax.SAXParseException;
import org.xml.sax.InputSource;
import org.xml.sax.ErrorHandler;
import org.xml.sax.SAXException;
import org.xml.sax.SAXParseException;
import java.io.File;
import java.io.IOException;

import javax.xml.XMLConstants;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;
import javax.xml.transform.Source;
import javax.xml.transform.sax.SAXSource;
import javax.xml.transform.stream.StreamSource;
import javax.xml.validation.Schema;
import javax.xml.validation.Validator;

import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.SAXParseException;
import org.xml.sax.XMLReader;

import edu.gvsu.cis.masl.channelAPI.ChannelAPI;
import edu.gvsu.cis.masl.channelAPI.ChannelService;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.IBinder;
import android.util.Log;

/**
 * This service is used to connecto to the SeTIChat server. 
 * It should remain running even if the app is not in the foreground
 *  
 * 
 * @author Guillermo Suarez de Tangil <guillermo.suarez.tangil@uc3m.es>
 * @author Jorge Blasco Al’s <jbalis@inf.uc3m.es>
 */

public class SeTIChatService extends Service implements ChannelService {
	
	// Used to communicate with the server
	ChannelAPI channel;
	
	// Used to bind activities
	private final SeTIChatServiceBinder binder=new SeTIChatServiceBinder();
	
	

	
	public SeTIChatService() {
		Log.i("SeTIChat Service", "Service constructor");
	}

	
	
	  @Override
	  public void onCreate() {
	    super.onCreate();
	    Log.i("SeTIChat Service", "Service created");
		
	    
	    // SeTIChat connection is seted up in this step. 
	    // Mobile phone should be changed with the appropiate value
	    channel = new ChannelAPI();
		this.connect("685557254");  
	    binder.onCreate(this);
	    
		 
	  }

	  @Override
	  public IBinder onBind(Intent intent) {
		  Log.i("SeTIChat Service", "Service binded");
		  return(binder);
	  }

	  @Override
	  public void onDestroy() {
	    super.onDestroy();
	    Log.i("SeTIChat Service", "Service destrotyed");
	    // When the service is destroyed, the connection is closed 
	    try {
			channel.close();
		} catch (Exception e){
			System.out.println("Problem Closing Channel");
		}
	    binder.onDestroy();    
	  }
	  

		//Methods exposed to service binders
		// Login user, send message, update public key, etc.
	  
	  	// All of them are implemented with AsyncTask examples to avoid UI Thread blocks.
		 public void connect(String key){
			 final SeTIChatService current = this;
			 class ChannelConnect extends AsyncTask<String, String, String> {
			    
				 protected String doInBackground(String... keys) {
					 Log.i("Service connect", "Connect test");
					 String key = keys[0];
					 try {
							channel = new ChannelAPI("http://setichatchannelapitest2.appspot.com", key, current); //Production Example
							channel.open();
							
						} catch (Exception e){
							System.out.println("Something went wrong...");
							Log.i("Service connect", "Error connecting..."+e.getLocalizedMessage());
						}
					 return "ok";
			     }

			     protected void onProgressUpdate(String... progress) {
			         //setProgressPercent(progress[0]);
			     }

			     protected void onPostExecute(String result) {
			         //
			     }
			 }
			 new ChannelConnect().execute(key,key,key);
		 }

		 
		 public void sendMessage(String message){
			 
			 
			 class SendMessage extends AsyncTask<String, String, String> {
				 protected String doInBackground(String... messages) {
					 Log.i("SendMessage", "send message: " + messages[0]);
					 String message = messages[0];
					 try {
							channel.send(message, "/chat");
						} catch (IOException e) {
							System.out.println("Problem Sending the Message");
						}
					 return "ok";
			     }

			     protected void onProgressUpdate(String... progress) {
			         //setProgressPercent(progress[0]);
			     }

			     protected void onPostExecute(String result) {
			    	// TODO Auto-generated method stub
			    	
			     }
				 
				 
			 }
			 new SendMessage().execute(message,message,message);
		 }

		 
		 // Callback method for the Channel API. This methods are called by ChannelService when some kind 
		 // of event happens
		 
		 
		 /**
		  *  Called when the client is able to correctly establish a connection to the server. In this case,
		  *  the main activity is notified with a Broadcast Intent.
		  */
		@Override
		public void onOpen() {
			Log.i("onOpen", "Channel Opened");
			String intentKey = "es.uc3m.SeTIChat.CHAT_OPEN";
			Intent openIntent = new Intent(intentKey);
			// Why should we set a Package?
			openIntent.setPackage("es.uc3m.setichat");
			Context context = getApplicationContext();
			context.sendBroadcast(openIntent);  
		}

		/**
		  *  Called when the client receives a chatMessage. In this case,
		  *  the main activity is notified with a Broadcast Intent.
		  */
		@Override
		public void onMessage(String message) {
			Log.i("onMessage", "Message received :"+message);
			// Extract message type (server or user) to decide handler
			InputSource is=null;
			SeTIMessage sms=new SeTIMessage();
			
			try
			{
				is = new InputSource(getAssets().open("example.xml"));
	            is.setEncoding("utf-8"); 
	            
	            /*if(SeTIChatService.validateWithExtXSDUsingSAX("assets/example.xml", "assets/SeTIChatXMLSchema.xsd"))
	            {
	            	System.out.println("true");
	            	Parser parser=new Parser("",is.getByteStream(),"local");
		            sms=parser.parse();
	            }
	            else{
	            	System.out.println("false");
	            }*/
	            
	            Parser parser=new Parser("",is.getByteStream(),"local");
	            sms=parser.parse();
			}
			catch(Exception e){
				System.out.println("excepti: "+e.getMessage());
			}
			Log.i("onMessage", "idsource:"+sms.getIdSource());
			Log.i("onMessage","type:"+sms.getType());
			Log.i("onMessage","key "+sms.getKey());
			Log.i("onMessage","type "+sms.getKeyType());
			Log.i("onMessage","idDestination "+sms.getIdDestination());
			Log.i("onMessage","idMessage "+sms.getIdMessage());
			Log.i("onMessage","encrypted "+sms.isEncrypted());
			Log.i("onMessage","signed "+sms.isSigned());
            //"assets/example.xml"
			
			String intentKey = "es.uc3m.SeTIChat.CHAT_MESSAGE";
			
			Intent openIntent = new Intent(intentKey);
			openIntent.setPackage("es.uc3m.setichat");
			openIntent.putExtra("message", message);
			
			Context context = getApplicationContext();
			context.sendBroadcast(openIntent); 
			
		}

		  public static boolean validateWithExtXSDUsingSAX(String xml, String xsd) 
				  throws ParserConfigurationException, IOException 
				  {
				    try {
				      SAXParserFactory factory = SAXParserFactory.newInstance();
				      factory.setValidating(false); 
				      factory.setNamespaceAware(true);
				      SchemaFactory schemaFactory=null;
				      
				      try{
				    	  schemaFactory= SchemaFactory.newInstance(XMLConstants.W3C_XML_SCHEMA_NS_URI);
				      }
				      catch(Exception e)
				      {
				    	  System.out.println("schema factory error"+e.getMessage());
				      }
				      SAXParser parser = null;
				      try {
				         factory.setSchema(schemaFactory.newSchema(new Source[] {new StreamSource( xsd )}));
				         parser = factory.newSAXParser();
				      }
				      catch (SAXException se) {
				        System.out.println("SCHEMA : " + se.getMessage());  // problem in the XSD itself
				        return false;
				      }
				      XMLReader reader = parser.getXMLReader();
				      reader.setErrorHandler(
				          new ErrorHandler() {
				            public void warning(SAXParseException e) throws SAXException {
				              System.out.println("WARNING: " + e.getMessage()); // do nothing
				            }

				            public void error(SAXParseException e) throws SAXException {
				              System.out.println("ERROR : " + e.getMessage());
				              throw e;
				            }

				            public void fatalError(SAXParseException e) throws SAXException {
				              System.out.println("FATAL : " + e.getMessage());
				              throw e;
				            }
				          }
				          );
				      reader.parse(new InputSource(xml));
				      return true;
				    }    
				    catch (ParserConfigurationException pce) {
				      throw pce;
				    } 
				    catch (IOException io) {
				      throw io;
				    }
				    catch (SAXException se){
				      return false;
				  }
				}
		@Override
		public void onClose() {
			// Called when the connection is closed
			
		}


		@Override
		public void onError(Integer errorCode, String description) {
			// Called when there is an error in the connection
			
		}
	  
}
