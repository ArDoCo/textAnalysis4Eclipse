package analysisasplugin;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

import javax.xml.namespace.QName;
import javax.xml.stream.XMLEventFactory;
import javax.xml.stream.events.Attribute;
import javax.xml.stream.events.Characters;
import javax.xml.stream.events.EndElement;
import javax.xml.stream.events.StartElement;
import javax.xml.stream.events.XMLEvent;

import textAnalysis.provider.AProvider;

public class FullstopSpaceChecker implements AProvider {

	@Override
	public String getName() {
		return "Fullstop-Space-Checker";
	}

	@Override
	public boolean isValid() {
		return true;
	}

	@Override
	public List<XMLEvent> getXMLEvents(List<String> textToAnalyze) {
		// returns count of ". ", will not falsify "... " but "...<nospace>" 
		List<XMLEvent> events = new LinkedList<>();
		XMLEventFactory eventFactory = XMLEventFactory.newInstance();
		XMLEvent end = eventFactory.createDTD("\n");
		XMLEvent tab = eventFactory.createDTD("\t");
		
		StartElement wElement = eventFactory.createStartElement("", "", "warnings");
		events.add(end);
		events.add(tab);
		events.add(wElement);
		events.add(end);

		
		int correctFullstopCount = 0;

		for (int i = 0; i < textToAnalyze.size(); i++) {
			
			String[] substrings = textToAnalyze.get(i).split(".");
			String[] substr = Arrays.copyOfRange(substrings, 1, substrings.length);
			for (String s : substr) {
				if (s.charAt(0) == ' ') { 
					correctFullstopCount++;
				} else {
		    		Attribute att = eventFactory.createAttribute("line-number", String.valueOf(i));
		    		Attribute pos = eventFactory.createAttribute("position", String.valueOf(textToAnalyze.get(i).indexOf(s)));
		    		StartElement startE = eventFactory.createStartElement(new QName("wrongFullStopUsage"), Arrays.asList(att, pos).iterator(), null);
		    		EndElement endE = eventFactory.createEndElement("", "", "wrongFullStopUsage");
		    		events.add(tab);
		    		events.add(tab);
		    		events.add(startE);
		    		events.add(endE);
		    		events.add(end);
				}
			}
		}
		
		StartElement correctCount = eventFactory.createStartElement("CorrectCount", "", "");
		Characters chara = eventFactory.createCharacters(String.valueOf(correctFullstopCount));
		EndElement corrEnd = eventFactory.createEndElement("CorrectCount", "", "");
		events.add(tab);
		events.add(correctCount);
		events.add(chara);
		events.add(corrEnd);
					
		return events;
	}

}
