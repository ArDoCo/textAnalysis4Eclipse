package plugintest4.analysis;

import java.util.List;
import java.util.StringTokenizer;

import org.w3c.dom.Document;
import org.w3c.dom.Element;

import provider.AProvider;


public class WordCountAnalysis implements AProvider {

	@Override
	public String getName() {
		return "Word-Count";
	}

	@Override
	public Element printInXML(Document doc, List<String> linesInFile) {
		Element wordCount = doc.createElement("Word-Count");

		for (int i = 0; i < linesInFile.size(); i++) {
			Element line = doc.createElement("line");
			line.setAttribute("number", Integer.toString(i));

			// count words:
			StringTokenizer tokens = new StringTokenizer(linesInFile.get(i));
			int count = tokens.countTokens();

			line.appendChild(doc.createTextNode(Integer.toString(count)));
			wordCount.appendChild(line);
		}
		return wordCount;
	}

	@Override
	public boolean isValid() {
		return true;
	}

}
