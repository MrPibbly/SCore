package com.ssomar.score.commands.runnable;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.Base64;

public class ActionInfoSerializer {

	/** Write the object to a Base64 string. */
    public static String toString( Serializable o ) throws IOException {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        ObjectOutputStream oos = new ObjectOutputStream( baos );
        oos.writeObject( o );
        oos.close();
        return Base64.getEncoder().encodeToString(baos.toByteArray()); 
    }
    
    /** Read the object from Base64 string. */
    public static Object fromString( String s ) throws IOException , ClassNotFoundException {
         byte [] data = Base64.getDecoder().decode( s );
         ObjectInputStream ois = new ObjectInputStream( 
                                         new ByteArrayInputStream(  data ) );
         Object o  = ois.readObject();
         ois.close();
         return o;
    }
   
//    public static void main(String[]args) throws IOException, ClassNotFoundException {
//    	
//    	
//    	StringPlaceholder sp = new StringPlaceholder();
//    	sp.setItem("dsihfsud$$$");
//    	
//    	ActionInfo aInfo = new ActionInfo("myname", 6, sp);
//    	
//    	String s = ActionInfoSerializer.toString(aInfo);
//    	
//    	System.out.println(s);
//    	
//    	ActionInfo aInfo2 = (ActionInfo) ActionInfoSerializer.fromString(s);
//    	
//    	System.out.println(aInfo2.getName());
//    	System.out.println(aInfo2.getSp().getItem());
//    }
}
