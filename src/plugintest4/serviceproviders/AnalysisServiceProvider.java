package plugintest4.serviceproviders;

import java.util.List;

import org.w3c.dom.Document;
import org.w3c.dom.Element;

public interface AnalysisServiceProvider {

	public Element printInXML(Document doc, List<String> linesInFile); 

	public String getName();
	
	public boolean isValid();
	
}
