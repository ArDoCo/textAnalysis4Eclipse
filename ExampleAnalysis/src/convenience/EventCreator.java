package convenience;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import javax.xml.namespace.QName;
import javax.xml.stream.XMLEventFactory;
import javax.xml.stream.events.Attribute;
import javax.xml.stream.events.Characters;
import javax.xml.stream.events.EndElement;
import javax.xml.stream.events.StartElement;
import javax.xml.stream.events.XMLEvent;

public class EventCreator {

	private static XMLEventFactory eventFactory = XMLEventFactory.newInstance();
	private static XMLEvent tab = eventFactory.createDTD("\t");
	private static XMLEvent end = eventFactory.createDTD("\n");

	
	public static List<XMLEvent> createEventWithAttribute(String elementName, String attributeName, String attributeValue, String character){
		List<XMLEvent> events = new LinkedList<>(); 
		
		Attribute att = eventFactory.createAttribute(attributeName, attributeValue);
		List<Attribute> att_list = new ArrayList<>();
		att_list.add(att);
		StartElement sElement = eventFactory.createStartElement(new QName(elementName), att_list.iterator(), null);

		Characters characters = eventFactory.createCharacters(character);
		EndElement eElement = eventFactory.createEndElement("", "", elementName);
		
		events.add(tab);
		events.add(sElement);
		events.add(tab);
		events.add(characters);
		events.add(tab);
		events.add(eElement);
		events.add(end);
		
		return events;
	}
	
	public static List<XMLEvent> addEventWithTabAndEnd(XMLEvent event) {
		List<XMLEvent> events = new LinkedList<>();
		events.add(tab);
		events.add(event);
		events.add(end);
		return events;
	}
	
}
