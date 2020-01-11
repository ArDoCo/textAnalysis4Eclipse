package plugintest4.serviceproviders;

import java.util.List;

import org.w3c.dom.Document;
import org.w3c.dom.Element;

public interface ExecutionServiceProvider {
	
	// TODO: Is there a case where we do not want to print the Analysis but just pass the results to something else? 
	// 		 The we need a doAnalysis / getAnalysis results. But what will be the return type?  
	
	public Element printInXML(Document doc, List<String> linesInFile); 

	public String getName();
	
}
