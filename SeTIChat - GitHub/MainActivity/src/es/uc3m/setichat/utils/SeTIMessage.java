package es.uc3m.setichat.utils;

import java.util.Arrays;

public class SeTIMessage {
	
	public SeTIMessage() {
		// TODO Auto-generated constructor stub
	}
	
	public SeTIMessage(String idSource, String idDestination, byte[] idMessage,
			boolean encrypted, boolean signed, int type, String nick,
			String mobile, String[] mobileList, String[][] contactList,
			String chatMessage, byte responseCode, String responseMessage,
			String revokedMobile, String key, String keyType, byte[] signature) {
		super();
		this.idSource = idSource;
		this.idDestination = idDestination;
		this.idMessage = idMessage;
		this.encrypted = encrypted;
		this.signed = signed;
		this.type = type;
		this.nick = nick;
		this.mobile = mobile;
		this.mobileList = mobileList;
		this.contactList = contactList;
		this.chatMessage = chatMessage;
		this.responseCode = responseCode;
		this.responseMessage = responseMessage;
		this.revokedMobile = revokedMobile;
		this.key = key;
		this.keyType = keyType;
		this.signature = signature;
	}

		// Header - Mandatory
		private String idSource;
		private String idDestination;
		private byte[] idMessage;
		private boolean encrypted;
		private boolean signed;
		private int type;
		
		// Content - Mandatory
		
		// 1 : Sign up
		private String nick;
		private String mobile;
		
		// 2 : Contact Request
		private String[] mobileList;
		
		// 3: Contact Response
		private String [][] contactList;
		
		// 4: Chat message
		private String chatMessage;
		
		// 5: Connection (Empty)
		
		// 6: Response
		private byte responseCode;
		private String responseMessage;
		
		// 7: Revocation
		private String revokedMobile;
		
		// 8: Download
		private String key;
		
		// 9: Upload
		private String keyType;
		
		// 10: Key Request
		
		// Signature
		private byte[] signature;


	public String getIdSource() {
		return idSource;
	}

	public void setIdSource(String idSource) {
		this.idSource = idSource;
	}

	public String getIdDestination() {
		return idDestination;
	}

	public void setIdDestination(String idDestination) {
		this.idDestination = idDestination;
	}

	public byte[] getIdMessage() {
		return idMessage;
	}

	public void setIdMessage(byte[] idMessage) {
		this.idMessage = idMessage;
	}

	public boolean isEncrypted() {
		return encrypted;
	}

	public void setEncrypted(boolean encrypted) {
		this.encrypted = encrypted;
	}

	public boolean isSigned() {
		return signed;
	}

	public void setSigned(boolean signed) {
		this.signed = signed;
	}

	public int getType() {
		return type;
	}

	public void setType(int type) {
		this.type = type;
	}

	public String getNick() {
		return nick;
	}

	public void setNick(String nick) {
		this.nick = nick;
	}

	public String getMobile() {
		return mobile;
	}

	public void setMobile(String mobile) {
		this.mobile = mobile;
	}

	public String[] getMobileList() {
		return mobileList;
	}

	public void setMobileList(String[] mobileList) {
		this.mobileList = mobileList;
	}

	public String[][] getContactList() {
		return contactList;
	}

	public void setContactList(String[][] contactList) {
		this.contactList = contactList;
	}

	public String getChatMessage() {
		return chatMessage;
	}

	public void setChatMessage(String chatMessage) {
		this.chatMessage = chatMessage;
	}

	public byte getResponseCode() {
		return responseCode;
	}

	public void setResponseCode(byte responseCode) {
		this.responseCode = responseCode;
	}

	public String getResponseMessage() {
		return responseMessage;
	}

	public void setResponseMessage(String responseMessage) {
		this.responseMessage = responseMessage;
	}

	public String getRevokedMobile() {
		return revokedMobile;
	}

	public void setRevokedMobile(String revokedMobile) {
		this.revokedMobile = revokedMobile;
	}

	public String getKey() {
		return key;
	}

	public void setKey(String key) {
		this.key = key;
	}

	public String getKeyType() {
		return keyType;
	}

	public void setKeyType(String keyType) {
		this.keyType = keyType;
	}

	public byte[] getSignature() {
		return signature;
	}

	public void setSignature(byte[] signature) {
		this.signature = signature;
	}

	@Override
	public String toString() {
		
		String xml = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>";
		xml+="\n<message>\n";
		
		// Header
		String header = "<header>\n";
		header+="<idSource>"+this.idSource+"</idSource>\n";
		header+="<idDestination>"+this.idDestination+"</idDestination>\n";
		header+="<idMessage>"+this.idMessage+"</idMessage>\n";
		header+="<type>"+this.type+"</type>\n";
		header+="<encrypted>"+this.encrypted+"</encrypted>\n";
		header+="<signed>"+this.signed+"</signed>\n";
		header+="</header>\n";
		
		// Content
		String content = "<content>\n";
		
		switch(this.type){
		
			case 1: // SignUp
				content+= "<signup>\n";
				content+= "<nick>"+this.nick+"</nick>\n";
				content+= "<mobile>"+this.mobile+"</mobile>\n";
				content+= "</signup>\n";
				break;
				
			case 2: // Contact Request
				content+= "<mobileList>\n";
				
				for(String phone:this.mobileList)
				{
					content+= "<mobile>"+phone+"</mobile>\n";
				}
				
				content+= "</mobileList>\n";
				break;
				
			case 3: // Contact Response
				content+= "<contactList>\n";
				
				for(String[] contact:this.contactList)
				{
					content+= "<contact>\n" +
							"<mobile>"+contact[0]+"</mobile>\n" +
							"<nick>"+contact[1]+"</nick>\n +" +
							"</contact>\n";
				}			
				content+= "</contactList>\n";
				break;
				
			case 4: // Chat Message
				//String mes = (String) ((this.encrypted) ? Base64.encodeToString(this.chatMessage.getBytes(), false): this.chatMessage);
				content+= "<chatMessage>\n"+this.chatMessage.getBytes()+"</chatMessage>\n";
				break;
				
			case 5: // Connection
				content+= "<connection>\n</connection>\n";
				break;
				
			case 6: // Response
				content+= "<response>\n";
				content+= "<responseCode>"+this.responseCode+"</responseCode>\n" +
						"<responseMessage>"+this.responseMessage+"</responseMessage>\n";
				content+= "</response>\n";
				break;
				
			case 7: // Revocation
				content+= "<revokedMobile>"+this.revokedMobile+"</revokedMobile>\n";
				break;
				
			case 8: // Download
				content+= "<download>\n";
				content+= "<key>"+this.key+"</key>" +
						"<type>public</type>\n" +
						"<mobile>"+this.mobile+"</mobile>\n" +
						"</download>\n";
				break;
				
			case 9: // Upload
				content+= "<upload>\n";
				content+= "<key>"+this.key+"</key>";
				content+="<type>"+this.keyType+"</type>\n";
				content+= "</upload>\n";
				break;
				
			case 10: // Key Request
				content+= "<keyrequest>\n";				
				content+="<type>"+this.keyType+"</type>\n";
				content+="<mobile>"+this.mobile+"</mobile>\n";
				content+= "</keyrequest>\n";
				break;
		}
		
		content += "</content>\n";
		String signature=null;
		
		if(this.signed==true)
		//  Signature
		{
			signature = "<signature>\n";
			//signature+= (String) ((this.signed) ? Base64.encodeToString(this.signature, false) : this.signature);
			signature+= "\n</signature>";
		}
		
		
		xml+= header + content + signature;
		xml+="\n</message>";
		
		return xml;
	}
	

}
