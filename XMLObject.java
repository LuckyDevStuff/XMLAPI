package de.lucky.simple.xml;

import java.util.ArrayList;

public class XMLObject extends XMLValue {
	ArrayList<XMLField> fields = new ArrayList<>();
	
	public XMLValue get(String name) {
		for(XMLField field : fields)
			if(field.name.equals(name)) return field.value;
		return null;
	}
	
	public XMLField getField(String name) {
		for(XMLField field : fields)
			if(field.name.equals(name)) return field;
		return null;
	}
	
	public void addField(XMLField field) {
		fields.add(field);
	}
}