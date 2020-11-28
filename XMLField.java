package de.lucky.simple.xml;

import java.util.ArrayList;

public class XMLField {
	public String name;
	public XMLValue value;
	public ArrayList<XMLFieldProperty> properties = new ArrayList<>();
	
	public XMLField(String name, XMLValue value) {
		this.name = name;
		this.value = value;
	}
	
	public XMLField() {}
	
	public void addProperty(XMLFieldProperty property) {
		properties.add(property);
	}
	
	public String getPropertyV(String name) {
		for(XMLFieldProperty property : properties) if(property.name.equals(name)) return property.value;
		return null;
	}
	
	public XMLFieldProperty getProperty(String name) {
		for(XMLFieldProperty property : properties) if(property.name.equals(name)) return property;
		return null;
	}
}