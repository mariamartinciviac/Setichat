package es.uc3m.setichat.utils;


import java.util.ArrayList;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.ext.LexicalHandler;
import org.xml.sax.helpers.DefaultHandler;

public class ParserHandler extends DefaultHandler implements LexicalHandler{
	
	private String data;
	private ArrayList<Object> objects;
	private Object object;
	
	private boolean in_item = false;
	
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
    	
    	if (localName.equals("item")) {
    		
        	in_item = true;
        	
        } else if (in_item) {
        	
        	//Esto sirve para leer los atributos de las cosas que te meten en los tags de inicio.
        	if (localName.equals("credit")) {
        		if(atts.getValue("role").equals("Autor/a Principal")) {
        			//hacer lo que sea con el atributo leído.
        		}
        	}	
        }
        
    }
	
	//Este se llama con los tags que se cierran
	@Override
    public void endElement(String namespaceURI, String localName, String qName) throws SAXException {
    	
    	super.endElement(namespaceURI, localName, qName);
    	
    	if (localName.equals("item")) {
    		objects.add(object);
    		object = new Object();
        	in_item = false;
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
