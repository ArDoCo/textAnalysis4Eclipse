package plugintest4.serviceproviders;

import java.util.List;

import org.w3c.dom.Document;
import org.w3c.dom.Element;

public class CountAsExecutionServiceProvider implements ExecutionServiceProvider {

	/***
	 * This Service counts the total amount of 'A' and 'a' in a file. 
	 */
	
	// Der hier könnte ja auch den NameServiceProvider kennen, oder es gibt eine gemeinsame Attribute Datei, 
	// was vor allem für die Analysen von Vorteil wäre (bzw benötigt ist), die tatsächlich Einstellungen etc haben.
	// aber woher wissen die dann, dass das die gleichen sind? bzw welche Attribut-Objekte... 
	
	@Override
	public String getName() {
		return "Count all As"; 
	}

	@Override
	public Element printInXML(Document doc, List<String> linesInFile) {
		Element aCount = doc.createElement("ACount");		
		
		int aCounter = 0;
		for (String line: linesInFile) {
			long count = line.chars().filter(ch -> (ch == 'a' || ch == 'A')).count();
			aCounter += count;
		}
		aCount.appendChild(doc.createTextNode(Integer.toString(aCounter)));
		return aCount;
	}
}
