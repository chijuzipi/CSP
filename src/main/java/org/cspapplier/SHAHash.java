package main.java.org.cspapplier;

import java.security.MessageDigest;
import java.util.*;

public class SHAHash {
	private String hashCode; 
		public SHAHash(String s) throws Exception{
	        MessageDigest md = MessageDigest.getInstance("SHA-256");
	        md.update(s.getBytes());
	        byte byteData[] = md.digest();
	        
	        //convert the byte to hex format method 1
	        StringBuffer hexString = new StringBuffer();
	    	for (int i=0;i<byteData.length;i++) {
	    		String hex=Integer.toHexString(0xff & byteData[i]);
	   	     	if(hex.length()==1) hexString.append('0');
	   	     	hexString.append(hex);
	    	}
	 
	    	hashCode = hexString.toString();
	        System.out.println("Hex format for password : " + hashCode);
		}
		
		public String getHasCode(){
			return hashCode;
		}

	    public static void main(String[] args)throws Exception{
	    	SHAHash test1 = new SHAHash("test1test2test3test4testesfgasdfasgagasdgasdgadgasdgasdfasdfadfiasfaeownlsdknf;lasdnkglashdg;oias;");
	    	SHAHash test2 = new SHAHash("test2");
	    	SHAHash test3 = new SHAHash("test3");
	    }
}
