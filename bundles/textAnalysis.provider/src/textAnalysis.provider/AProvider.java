package textAnalysis.provider

import java.util.List;

import org.w3c.dom.Document;
import org.w3c.dom.Element;

public interface MyProvider {
	public Element printInXML(Document doc, List<String> linesInFile); 

	public String getName();
	
	public boolean isValid();

}
