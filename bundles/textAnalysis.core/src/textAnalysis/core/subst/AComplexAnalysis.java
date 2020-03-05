package textAnalysis.core.subst;

import java.util.LinkedList;
import java.util.List;

import javax.xml.stream.XMLEventFactory;
import javax.xml.stream.events.Characters;
import javax.xml.stream.events.EndElement;
import javax.xml.stream.events.StartElement;
import javax.xml.stream.events.XMLEvent;

import textAnalysis.provider.AProvider;

public class AComplexAnalysis implements AProvider {

	private static final String NAME = "A Complex Analysis";

	@Override
	public String getName() {
		return NAME;
	}

	@Override
	public boolean isValid() {
		return true;
	}

	@Override
	public List<XMLEvent> getXMLEvents(List<String> textToAnalyze) {
		List<XMLEvent> events = new LinkedList<>();
		XMLEventFactory eventFactory = XMLEventFactory.newInstance();
		XMLEvent end = eventFactory.createDTD("\n");
		XMLEvent tab = eventFactory.createDTD("\t");
		events.add(end);

		// create Start node
		StartElement sElement = eventFactory.createStartElement("", "", "analysis");
		events.add(tab);
		events.add(sElement);
		
		// create Content
		Characters characters = eventFactory.createCharacters(NAME);
		events.add(characters);
		
		// create End node
		EndElement eElement = eventFactory.createEndElement("", "", "analysis");
		events.add(eElement);
		events.add(end);
		return events;
	}

	
}
