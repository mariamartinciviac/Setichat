package es.uc3m.setichat.utils;


import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;

import android.os.Handler;
import android.util.Xml;

public class Parser {
	
	final URL feedUrl;

    public Parser(String feedUrl) {
    	
    	try {
    		
            this.feedUrl = new URL(feedUrl);
            
        } catch (MalformedURLException e) {
        	
            throw new RuntimeException(e);
            
        }
        
    }

    public ArrayList<Object> parse() {
    	
        ParserHandler handler = new ParserHandler();
        
        try {
        	
            Xml.parse(this.getInputStream(), Xml.Encoding.UTF_8, handler);
            
        } catch (Exception e) {
        	
            throw new RuntimeException(e);
            
        }
        
        return handler.getList();
        
    }
    
    protected InputStream getInputStream() {
    	
        try {
        	
            return feedUrl.openConnection().getInputStream();
            
        } catch (IOException e) {
        	
            throw new RuntimeException(e);
            
        }
    }

}