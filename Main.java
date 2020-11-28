import java.lang.reflect.Field;

import com.google.gson.Gson;

import de.lucky.simple.xml.XMLObject;
import de.lucky.simple.xml.XMLReadException;
import de.lucky.simple.xml.XMLReader;

public class Main {
	
	public static void main(String[] args) throws Exception {
		XMLObject obj = XMLReader.fromXML("<Lobby x='5' y='15' z='58'></Lobby>");
		
		System.out.println(obj.getField("Lobby").getPropertyV("x"));
		sun.reflect.Reflection.getCallerClass().getName();
	}
}