package plugintest4.analysis;

import java.util.List;

import org.w3c.dom.Document;
import org.w3c.dom.Element;

import plugintest4.IAnalysis;

public class CharCountAnalyis implements IAnalysis {

	@Override
	public String getName() {
		return "Char-Count";
	}

	@Override
	public Element printInXML(Document doc, List<String> linesInFile) {
		
		Element charCount = doc.createElement("Char-Count");		
		Element warnings = doc.createElement("Warnings");
		charCount.appendChild(warnings);
		
		for (int i = 0; i < linesInFile.size(); i++) {
			Element line = doc.createElement("line");
			line.setAttribute("number", Integer.toString(i));
			int lengthOfLine = linesInFile.get(i).length();
			line.appendChild(doc.createTextNode(Integer.toString(lengthOfLine)));
			charCount.appendChild(line);
			
			if (lengthOfLine > 80) {
				Element warn = doc.createElement("warn");
				warn.setAttribute("line-number", Integer.toString(i));
				warn.appendChild(doc.createTextNode("Line is too long (more than 80 characters)"));
				warnings.appendChild(warn);
			}
		}
		return charCount;
	}

	@Override
	public boolean isValid() {
		return true;
	}

}
