package es.uc3m.setichat.utils;


import java.util.ArrayList;

import org.apache.http.util.ByteArrayBuffer;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.ext.LexicalHandler;
import org.xml.sax.helpers.DefaultHandler;

import android.util.Log;

public class ParserHandler extends DefaultHandler implements LexicalHandler{
	
	public ParserHandler(String data, boolean in_message, SeTIMessage message) {
		super();
		this.data = data;
		this.in_message = in_message;
		this.message=message;
	}

	public ParserHandler() {
		super();
		// TODO Auto-generated constructor stub
	}

	private String data;
	private SeTIMessage message;
	
	private boolean in_message = false;
	private boolean in_header=false;
	private boolean in_content=false;
	private boolean in_signature=false;

	
	public SeTIMessage getMessage() {
		return this.message;
	}
	
	@Override
    public void startDocument() throws SAXException {
    	
    	data = "";
    	message=new SeTIMessage();
    	
    }
	
	@Override
    public void endDocument() throws SAXException {
		
    }
	
	
	@Override
    public void startElement(String namespaceURI, String localName, String qName, Attributes atts) throws SAXException {
    	
    	super.startElement(namespaceURI, localName, qName, atts);
    	
    	if (localName.equals("message")) {
    		
        	in_message = true;
        	
        } else if (in_message) {
        	
        	if (localName.equals("header")) {
        		in_header=true;
        	}	
        	else if(localName.equals("content"))
        	{
        		//in_content=true;
        	}
        	else if(localName.equals("singnature"))
        	{
        		in_signature=true;
        	}
        }
        
    }
	
	//Este se llama con los tags que se cierran
	@Override
    public void endElement(String namespaceURI, String localName, String qName) throws SAXException {
    	
    	super.endElement(namespaceURI, localName, qName);
    	
    	if (localName.equals("message")) {
        	in_message= false;
        }
        else if (in_message) {
        	        	
        	if(in_header)
        	{
        		if(localName.equals("idSource")) {
        			message.setIdSource(data);
        		}
        		else if(localName.equals("idDestination")) {
        			message.setIdDestination(data);
        		}
        		else if(localName.equals("idMessage")) {
        			message.setIdMessage((data).getBytes());
        		}
        		else if(localName.equals("type")) {
        			message.setType(9);
        			//message.setType(Integer.valueOf(data).intValue());
        		}
        		else if(localName.equals("encrypted"))
        		{
        			if(data.equals("true"))
        			{
        				message.setEncrypted(true);
        			}
        			else
        			{
        				message.setEncrypted(false);
        			}
        		}
        		else if(localName.equals("signed"))
        		{
        			if(data.equals("true"))
        			{
        				message.setSigned(true);
        			}
        			else
        			{
        				message.setSigned(false);
        			}
        		}
        	}
        	else if (localName.equals("header")) {
        		in_header=false;
        	}
        	else if(in_content)
        	{
        		switch(message.getType()){
        		
    			case 1: // SignUp
    				if(localName.equals("signup"))
    				{
    					if(localName.equals("nick"))
    					{
    						message.setNick(data);
    					}
    					else if(localName.equals("mobile"))
    					{
    						message.setNick(data);
    					}
    				}
    				break;
    				
    			case 2: // Contact Request
    				if(localName.equals("mobileList"))
    				{
    					String[] list=null;
    					int i=0;
    					while(localName.equals("mobile"))
    					{
    						//list[i]=data;
    						i++;
    					}
    					message.setMobileList(list);
    				}
    				break;
    				
    			case 3: // Contact Response
    				if(localName.equals("contactList"))
    				{
    					String[][] list=null;
    					int i=0,j=0;
    					while(localName.equals("contact"))
    					{
    						j=0;
    						if(localName.equals("mobile"))
    						{
    							//list[i][j]=data;
    							message.setMobile(data);
    							j++;
    						}
    						else if(localName.equals("nick"))
    						{
        						//list[i][j]=data;
        						message.setNick(data);
    						}
    						i++;
    					}
    					message.setContactList(list);

    				}
    				break;
    				
    			case 4: // Chat Message
    				if(localName.equals("chatMessage"))
    				{
    					String aux= (String) ((message.isEncrypted()) ? Base64.encodeToString(data.getBytes(), false): data);
    					message.setChatMessage(aux);
    				}
    				break;
    				
    			case 5: // Connection
    				//user connected
    				break;
    				
    			case 6: // Response
    				if(localName.equals("response"))
    				{
    					if(localName.equals("responseCode"))
    					{
    						message.setResponseCode(Byte.parseByte(data));
    					}
    					else if(localName.equals("responseMessage"))
    					{
    						message.setResponseMessage(data);
    					}
    				}    				
    				break;
    				
    			case 7: // Revocation
    				if(localName.equals("revokedMobile"))
    				{
    					message.setRevokedMobile(data);
    				}
    				break;
    				
    			case 8: // Download
    				if(localName.equals("download"))
    				{
    					if(localName.equals("key"))
    					{
    						message.setKey(data);
    					}
    					else if(localName.equals("type"))
    					{
    						message.setKeyType(data);
    					}
    					else if(localName.equals("mobile"))
    					{
    						message.setMobile(data);
    					}
    				}
    				break;
    				
    			case 9: // Upload
    				
    				Log.i("ParserHandler", "case 9");
    				if(localName.equals("upload"))
    				{
    					System.out.println("enter upload");
    					if(localName.equals("key"))
    					{
    						System.out.println("key: "+data);
    						message.setKey(data);
    					}
    					else if(localName.equals("type"))
    					{
    						message.setKeyType(data);
    					}
    				}
    				break;
    				
    			case 10: // Key Request
    				if(localName.equals("keyrequest"))
    				{
    					if(localName.equals("type"))
    					{
    						message.setKeyType(data);
    					}
    					else if(localName.equals("mobile"))
    					{
    						message.setMobile(data);
    					}
    				}
    				break;
    		}
        	}
        	else if(localName.equals("content"))
        	{
        		in_content=false;
        	}
        	else if(in_signature)
        	{
        		if(message.isSigned()==true)
        		{
        			message.setSignature(data.getBytes());
        			String aux=((String) ((message.isSigned()) ? Base64.encodeToString(message.getSignature(), false) : message.getSignature()));
        			message.setSignature(aux.getBytes());
        		}
        	}
        	else if(localName.equals("signature"))
        	{
        		in_signature=false;
        	}
        }
    	//Esta string hace de buffer de lo que vas leyendo, as’ que te toca borrarla tras leer un tag de cierre para no ir concatenando distintos elementos xD
    	data = "";
	}
	
	//Este metodo es llamado para leer lo que hay entre los tags de apertura y cierre. Se usa el for porque puede que no lea todo el cuerpo de golpe, por lo que te toca leer varias substrings
	@Override
    public void characters(char ch[], int start, int length) {
    	for (int i=start; i<start+length; i++) {
			data = data + ch[i];
		}
    	//Log.i("Parser-characters", data+"length:"+length);
    }

	//Estos mŽtodos no creo que tengas usarlos. Se usan para otro tipo de cosas (bloques CDATA, comentarios...).
	@Override
	public void comment(char[] ch, int start, int length) throws SAXException {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void endCDATA() throws SAXException {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void endDTD() throws SAXException {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void endEntity(String name) throws SAXException {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void startCDATA() throws SAXException {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void startDTD(String name, String publicId, String systemId)
			throws SAXException {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void startEntity(String name) throws SAXException {
		// TODO Auto-generated method stub
		
	}
	
}
