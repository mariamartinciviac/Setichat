package es.uc3m.setichat.utils;


import java.util.ArrayList;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.ext.LexicalHandler;
import org.xml.sax.helpers.DefaultHandler;

public class ParserHandler extends DefaultHandler implements LexicalHandler{
	
	public ParserHandler(String data, ArrayList<Object> objects, Object object,
			boolean in_message, SeTIMessage message) {
		super();
		this.data = data;
		this.objects = objects;
		this.object = object;
		this.in_message = in_message;
		this.message=message;
	}

	public ParserHandler() {
		super();
		this.message=new SeTIMessage();
		// TODO Auto-generated constructor stub
	}

	private String data;
	private ArrayList<Object> objects;
	private Object object;
	private SeTIMessage message;
	
	private boolean in_message = false;
	
	public ArrayList<Object> getList() {
		return this.objects;
	}
	
	@Override
    public void startDocument() throws SAXException {
    	
		objects = new ArrayList<Object>();
		object = new Object();
    	data = "";
    	
    }
	
	@Override
    public void endDocument() throws SAXException {
		
    }
	
	//Este se llama con los tags de apertura
	@Override
    public void startElement(String namespaceURI, String localName, String qName, Attributes atts) throws SAXException {
    	
    	super.startElement(namespaceURI, localName, qName, atts);
    	
    	if (localName.equals("message")) {
    		
        	in_message = true;
        	
        } else if (in_message) {
        	
        	//Esto sirve para leer los atributos de las cosas que te meten en los tags de inicio.
        	if (localName.equals("header")) {
       
        		if(localName.equals("idSource")) {
        			message.setIdSource(atts.getValue("idSource"));
        		}
        		else if(localName.equals("idDestination")) {
        			message.setIdDestination(atts.getValue("idDestination"));
        		}
        		else if(localName.equals("idMessage")) {
        			message.setIdMessage(atts.getValue("idMessage").getBytes());
        		}
        		else if(localName.equals("type")) {
        			message.setType((atts.getValue("type").getBytes())[0]);
        		}
        	}	
        	else if(localName.equals("content"))
        	{
        		switch(message.getType()){
        		
    			case 1: // SignUp
    				if(localName.equals("signup"))
    				{
    					if(localName.equals("nick"))
    					{
    						message.setNick(atts.getValue("nick"));
    					}
    					else if(localName.equals("mobile"))
    					{
    						message.setNick(atts.getValue("mobile"));
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
    						list[i]=atts.getValue("mobile");
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
    						list[i][j]=atts.getValue("mobile");
    						j++;
    						list[i][j]=atts.getValue("nick");
    						i++;
    					}
    					message.setContactList(list);

    				}
    				break;
    				
    			case 4: // Chat Message
    				if(localName.equals("chatMessage"))
    				{
    					message.setChatMessage(atts.getValue("chatMessage"));
    					String aux= (String) ((message.isEncrypted()) ? Base64.encodeToString((message.getChatMessage()).getBytes(), false): message.getChatMessage());
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
    						message.setResponseCode((atts.getValue("responseCode").getBytes())[0]);
    					}
    					else if(localName.equals("responseMessage"))
    					{
    						message.setResponseMessage(atts.getValue("responseMessage"));
    					}
    				}    				
    				break;
    				
    			case 7: // Revocation
    				if(localName.equals("revokedMobile"))
    				{
    					message.setRevokedMobile(atts.getValue("revokedMobile"));
    				}
    				break;
    				
    			case 8: // Download
    				if(localName.equals("download"))
    				{
    					if(localName.equals("key"))
    					{
    						message.setKey(atts.getValue("key"));
    					}
    					else if(localName.equals("type"))
    					{
    						boolean auxb=false;
    						if(atts.getValue("type").equals(auxb))
    						{
    							message.setKeyType(false);
    						}
    						else{
    							message.setKeyType(true);
    						}
    					}
    					else if(localName.equals("mobile"))
    					{
    						message.setMobile(atts.getValue("mobile"));
    					}
    				}
    				break;
    				
    			case 9: // Upload
    				if(localName.equals("upload"))
    				{
    					if(localName.equals("key"))
    					{
    						message.setKey(atts.getValue("key"));
    					}
    					else if(localName.equals("type"))
    					{
    						boolean auxb=false;
    						if(atts.getValue("type").equals(auxb))
    						{
    							message.setKeyType(false);
    						}
    						else{
    							message.setKeyType(true);
    						}
    					}
    				}
    				break;
    				
    			case 10: // Key Request
    				if(localName.equals("keyrequest"))
    				{
    					if(localName.equals("type"))
    					{
    						boolean auxb=false;
    						if(atts.getValue("type").equals(auxb))
    						{
    							message.setKeyType(false);
    						}
    						else{
    							message.setKeyType(true);
    						}
    					}
    					else if(localName.equals("mobile"))
    					{
    						message.setMobile(atts.getValue("mobile"));
    					}
    				}
    				break;
    		}
        	}
        	else if(localName.equals("singnature"))
        	{
        		if(message.isSigned()==true)
        		{
        			message.setSignature(atts.getValue("signature").getBytes());
        			String aux=((String) ((message.isSigned()) ? Base64.encodeToString(message.getSignature(), false) : message.getSignature()));
        			message.setSignature(aux.getBytes());
        		}
        	}
        	}
        
    }
	
	//Este se llama con los tags que se cierran
	@Override
    public void endElement(String namespaceURI, String localName, String qName) throws SAXException {
    	
    	super.endElement(namespaceURI, localName, qName);
    	
    	if (localName.equals("message")) {
    		objects.add(object);
    		object = new Object();
        	in_message= false;
        }
    	
    	//Esta string hace de buffer de lo que vas leyendo, así que te toca borrarla tras leer un tag de cierre para no ir concatenando distintos elementos xD
    	data = "";
    	
	}
	
	//Este método es llamado para leer lo que hay entre los tags de apertura y cierre. Se usa el for porque puede que no lea todo el cuerpo de golpe, por lo que te toca leer varias substrings
	@Override
    public void characters(char ch[], int start, int length) {
    	for (int i=start; i<start+length; i++) {
			data = data + ch[i];
		}
    }

	//Estos métodos no creo que tengas usarlos. Se usan para otro tipo de cosas (bloques CDATA, comentarios...).
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
