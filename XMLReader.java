package de.lucky.simple.xml;
import java.util.ArrayList;

import com.google.gson.Gson;

public class XMLReader {
	
	public static XMLObject fromXML(String xml) {
		try {
		XMLObject obj = new XMLObject();
		
		XMLField field = new XMLField();
		XMLObject addTo = obj;
		
		ArrayList<XMLField> fields = new ArrayList<>();
		
		//String
		boolean stringRead = false;
		boolean stringSkip = false;
		boolean stringAllowed = false;
		char stringChar = ' ';
		String string = null;
		
		//<>
		boolean bracketOpen = false;
		boolean bracketC = false;
		Integer bracketNameStart = null; 
		
		//<> properties
		Integer propertyStart = null;
		String propertyName = null;
		
		//<> value
		int valueStart = 0;
		boolean valueRead = false;
		
		for(int index = 0; index < xml.length(); index++) {
			char chr = xml.charAt(index);
			
			if(stringRead && stringAllowed) {
				//Read String
				if(string == null) string = "";
				switch(chr) {
					case '\\':
					if(!stringSkip) {
						stringSkip = true;
					} else {
					string += chr;
					stringSkip = false;
					}
					break;
					
					case '\'': case '"':
					if(chr == stringChar && !stringSkip) {
						stringRead = false;
					} else  {
					string += chr;
					stringSkip = false;
					}
					break;
					
					default:
					string += chr;
					stringSkip = false;
				}
				
			} else {
				//No String
				switch(chr) {
					
					case '\'': case '"':
					stringChar = chr;
					stringRead = true;
					string = "";
					break;
					
					case '<':
					if(xml.charAt(index+1) == '/') {
						if(!xml.substring(valueStart, index).replaceAll(" ", "").equals("") && valueRead) {
								valueRead = false;
								fields.get(fields.size() - 1).value = new XMLString(xml.substring(valueStart, index));
							} else {
								valueRead = false;
							}
					}
					bracketOpen = true;
					bracketC = false;
					bracketNameStart = index + 1;
					break;
					
					case '/':
					if(xml.charAt(index - 1) == '<') {
						bracketC = true;
					}
					break;
					
					case '>':
					if(!bracketC) {
						if(bracketNameStart != null) {
						field.name = xml.substring(bracketNameStart, index);
						bracketNameStart = null;
						propertyStart = index + 1;
						stringAllowed = true;
					} else if(propertyStart != null) {
						if(!propertyName.replaceAll(" ", "").equals("")) {
							if(string != null) {
								field.addProperty(new XMLFieldProperty(propertyName, string));
							} else {
								throw new XMLReadException("Property value can not be null!\n" + xml.substring(0, index) + " [<-- here]");
							}
						} else {
							throw new XMLReadException("Property name can not be empty!\n[here -->] " + xml.substring(propertyStart, xml.length()));
						}
					}
					propertyName = null;
					valueStart = index + 1;
					valueRead = true;
					field.value = new XMLObject();
					fields.add(field);
					field = new XMLField();
					} else {
						if(fields.size() > 1) {
						fields.get(fields.size() - 2).value.obj().addField(fields.get(fields.size() - 1));
						fields.remove(fields.size() - 1);
						} else {
							obj.addField(fields.get(0));
							fields.remove(0);
						}
					}
					break;
					
					case '=':
					if(propertyStart != null) {
						propertyName = xml.substring(propertyStart, index);
						if(propertyName.contains("=")) {
							throw new XMLReadException("Property name can not contain '='\n[here -->] " + xml.substring(propertyStart, xml.length()));
						}
					}
					break;
					
					case ' ':
					if(bracketNameStart != null) {
						field.name = xml.substring(bracketNameStart, index);
						bracketNameStart = null;
						propertyStart = index + 1;
						stringAllowed = true;
					} else if(propertyStart != null) {
						if(!propertyName.replaceAll(" ", "").equals("")) {
							if(string != null) {
								field.addProperty(new XMLFieldProperty(propertyName, string));
								propertyName = null;
							} else {
								throw new XMLReadException("Property value can not be null!\n" + xml.substring(0, index) + " [<-- here]");
							}
						} else {
							throw new XMLReadException("Property name can not be empty!\n[here -->] " + xml.substring(propertyStart, xml.length()));
						}
						propertyStart = index + 1;				
					}
					break;
				}
			}
			
			if(!stringRead && propertyName != null && chr != ' ' && chr != '\'' && chr != '"' && chr != '=') {
				throw new XMLReadException("The Property Value need to be in \" or '\n" + xml.substring(0, index + 1)  + " [<-- here]");
			}
		}
		
		return obj;
	} catch(Exception e) {
		e.printStackTrace();
		return null;
	}
	}
	
}