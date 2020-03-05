package textAnalysis.core.subst;

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

import textAnalysis.provider.AProvider;

public class CharCountAnalyis implements AProvider{

    @Override
    public String getName() {
        return "Char-Count";
    }

    // @Override
//    public Element printInXML(Document doc, List<String> linesInFile) {
//
//        Element charCount = doc.createElement("Char-Count");
//        Element warnings = doc.createElement("Warnings");
//        charCount.appendChild(warnings);
//
//        for (int i = 0; i < linesInFile.size(); i++) {
//            Element line = doc.createElement("line");
//            line.setAttribute("number", Integer.toString(i));
//            int lengthOfLine = linesInFile.get(i).length();
//            line.appendChild(doc.createTextNode(Integer.toString(lengthOfLine)));
//            charCount.appendChild(line);
//
//            if (lengthOfLine > 80) {
//                Element warn = doc.createElement("warn");
//                warn.setAttribute("line-number", Integer.toString(i));
//                warn.appendChild(doc.createTextNode("Line is too long (more than 80 characters)"));
//                warnings.appendChild(warn);
//            }
//        }
//        return charCount;
//    }

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
		List<XMLEvent> warnElements = new LinkedList<>(); 
		StartElement wElement = eventFactory.createStartElement("", "", "warnings");
		warnElements.add(tab);
		warnElements.add(wElement);
		warnElements.add(end);
		
		events.add(end);
		
		for (int i = 0; i < textToAnalyze.size(); i++) {
	    		Attribute att = eventFactory.createAttribute("number", String.valueOf(i));
	    		List<Attribute> att_list = new ArrayList<>();
	    		att_list.add(att);
	    		StartElement sElement = eventFactory
	    				.createStartElement(new QName("line"), att_list.iterator(), null); 

	            int lengthOfLine = textToAnalyze.get(i).length();
	    		Characters characters = eventFactory.createCharacters(String.valueOf(lengthOfLine));
	    		EndElement eElement = eventFactory.createEndElement("", "", "line");

	    		if (lengthOfLine > 80) {
	    			StartElement sElementW = eventFactory
		    				.createStartElement(new QName("line"), att_list.iterator(), null);
		    		Characters charactersW = eventFactory
		    				.createCharacters("Line is too long (more than 80 characters)");
		    		EndElement eElementW = eventFactory.createEndElement("", "", "line");
		    		
		    		warnElements.add(tab); 
		    		warnElements.add(tab);
		    		warnElements.add(sElementW);
		    		warnElements.add(tab);
		    		warnElements.add(charactersW);
		    		warnElements.add(tab);
		    		warnElements.add(eElementW);
		    		warnElements.add(end);
	    		}
	    			    		
	    		events.add(tab);
	    		events.add(sElement);
	    		events.add(tab);
	    		events.add(characters);
	    		events.add(tab);
	    		events.add(eElement);
	    		events.add(end);
		}	            
		EndElement weElement = eventFactory.createEndElement("", "", "warnings");
		warnElements.add(tab);
		warnElements.add(weElement);
		warnElements.add(end);
		events.addAll(warnElements);
			
		return events;
	}

}
