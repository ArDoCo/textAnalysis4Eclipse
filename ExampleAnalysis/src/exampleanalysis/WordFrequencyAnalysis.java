package exampleanalysis;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import javax.xml.namespace.QName;
import javax.xml.stream.XMLEventFactory;
import javax.xml.stream.events.Attribute;
import javax.xml.stream.events.Characters;
import javax.xml.stream.events.EndElement;
import javax.xml.stream.events.StartElement;
import javax.xml.stream.events.XMLEvent;

import textAnalysis.provider.AnalysisProvider;

public class WordFrequencyAnalysis implements AnalysisProvider {

	private final int upperLimit = 15;
	private final int lowerLimit = 5;
	
	@Override
	public String getName() {
		return "WordFrequencyCount";
	}

	@Override
	public boolean isValid() {
		return true;
	}

	@Override
	public List<XMLEvent> getXMLEvents(List<String> textToAnalyze) {
		Map<String, Integer> wordFrequency = new HashMap<>();
		
		// Count word frequencies
		for (String line : textToAnalyze) {
			String[] words = line.split(" ");
			for (String w: words) {
				if (wordFrequency.containsKey(w)) {
					Integer oldInt = wordFrequency.get(w);
					Integer newInt = oldInt + 1;
					wordFrequency.replace(w, newInt);
				} else {
					wordFrequency.put(w, 1);
				}
			}
		}
		
		// Create xml events for word frequencies over and under the set limits
		List<XMLEvent> events = new LinkedList<>();
		XMLEventFactory eventFactory = XMLEventFactory.newInstance();
		XMLEvent end = eventFactory.createDTD("\n");
		XMLEvent tab = eventFactory.createDTD("\t");
		events.add(end);
		
		for (Map.Entry<String, Integer> entry : wordFrequency.entrySet()) {
			if (entry.getValue() > upperLimit || entry.getValue() < lowerLimit) {
	    		Attribute att = eventFactory.createAttribute("string", entry.getKey());
	    		List<Attribute> att_list = new ArrayList<>();
	    		att_list.add(att);
	    		StartElement sElement = eventFactory
	    				.createStartElement(new QName("word"), att_list.iterator(), null); 
	    		Characters characters = eventFactory.createCharacters(String.valueOf(entry.getValue()));
	    		EndElement eElement = eventFactory.createEndElement("", "", "word");
	    		events.add(tab);
	    		events.add(sElement);
	    		events.add(tab);
	    		events.add(characters);
	    		events.add(tab);
	    		events.add(eElement);
	    		events.add(end);
			}
		}
		
		return events;
	}

}
