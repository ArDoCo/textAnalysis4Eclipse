package plugintest4;

import java.util.List;
import org.w3c.dom.Element;
import org.w3c.dom.Document;

public interface IAnalysis {

	public String getName();
	
	public Element printInXML(Document doc, List<String> linesInFile); 
	
	public boolean isValid(); // TODO braucht wohl noch Parameter 
	
}
