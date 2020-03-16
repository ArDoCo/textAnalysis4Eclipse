package exampleanalysis;

import java.util.ArrayList;
import java.util.Arrays;
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

public class KeywordExtractionAnalysis implements AnalysisProvider {

	private List<String> keywords = Arrays.asList("Lorem", "ipsum", "dolor"); 
	
	@Override
	public String getName() {
		return "KeywordExtractionAnalysis";
	}

	@Override
	public boolean isValid() {
		return true;
	}

	@Override
	public List<XMLEvent> getXMLEvents(List<String> textToAnalyze) {
		Map<String, List<Integer>> keywordInLines = new HashMap<>();
		
		for (String k : keywords) {
			keywordInLines.put(k, new LinkedList<>());
		}
		
		for (int i = 0; i < textToAnalyze.size(); i++) {
			String[] words = textToAnalyze.get(i).split(" ");
			for (String w : words) {
				if (keywords.contains(w)) {
					keywordInLines.get(w).add(i);
				}
			}
		}
		
		List<XMLEvent> events = new LinkedList<>();
		XMLEventFactory eventFactory = XMLEventFactory.newInstance();
		XMLEvent end = eventFactory.createDTD("\n");
		XMLEvent tab = eventFactory.createDTD("\t");
		events.add(end);
		
		for (Map.Entry<String, List<Integer>> entry : keywordInLines.entrySet()) {

			Attribute att = eventFactory.createAttribute("keyword", entry.getKey());
			List<Attribute> att_list = new ArrayList<>();
			att_list.add(att);
			StartElement sElement = eventFactory
					.createStartElement(new QName("word"), att_list.iterator(), null); 
			Characters characters = eventFactory.createCharacters(entry.getValue().toString());
			EndElement eElement = eventFactory.createEndElement("", "", "word");
			events.add(tab);
			events.add(sElement);
			events.add(tab);
			events.add(characters);
			events.add(tab);
			events.add(eElement);
			events.add(end);
		}			
		
		return events;
	}

}
