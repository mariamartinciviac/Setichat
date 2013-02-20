package es.uc3m.setichat.utils;


import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;

import android.os.Handler;
import android.util.Xml;

public class Parser {
	
	private URL feedUrl;
	private InputStream is;
	private String type;

    public Parser(String feedUrl, InputStream is,String type) {
    	
    	try {
    		if(type.equals("local"))
    		{
    			this.is=is;
    			this.type=type;
    			this.feedUrl=null;
    		}
    		else if(type.equals("url"))
    		{
                this.feedUrl = new URL(feedUrl);
                this.type=type;
                this.is=null;
    		}
            
        } catch (MalformedURLException e) {
        	
            throw new RuntimeException(e);
            
        }
        
    }

    public SeTIMessage parse() {
    	
        ParserHandler handler = new ParserHandler();
        
        try {
        	if(this.type.equals("url"))
        	{
        		Xml.parse(this.getInputStream(), Xml.Encoding.UTF_8, handler);
        	}
        	else if(this.type.equals("local"))
        	{
        		Xml.parse(this.is, Xml.Encoding.UTF_8, handler);
        	}
            
            
        } catch (Exception e) {
        	
            throw new RuntimeException(e);
            
        }
        
        return handler.getMessage(); 
    }
    
    protected InputStream getInputStream() {
    	
        try {
        	
            return feedUrl.openConnection().getInputStream();
            
        } catch (IOException e) {
        	
            throw new RuntimeException(e);
            
        }
    }

}