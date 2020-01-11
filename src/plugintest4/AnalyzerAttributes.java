package plugintest4;

import java.util.Map;

public class AnalyzerAttributes {
	
	// Names of the Attributes
	public static final String FILE_NAME = "edu.kit.analyzer.FILE_TEXT";
	
	// Was da reinkommt ist eine Map<String,String>: Name auf 'true'/ 'false'.
	// TODO man könnte auch nur eine Liste mit allen Namen machen von denen, die ausgeführt werden sollen, 
	//      wenn eine abgeklickt wird müsste man sie halt immer aus der Liste löschen, das ist wahrscheinlich zu aufwändig.
	public static final String CHECKBOX_ACTIVATION = "edu.kit.analyzer.CHECKBOX_ACTIVATION";
	public static final String SERVICE_CHECKBOX_VALUES = "edu.kit.analyzer.SERVICE_CHECKBOX_VALUES";
	
	public static final String EXECUTION_SERVICE_CLASS_NAMES = "edu.kit.analyzer.EXECUTION_SERVICE_CLASS_NAMES";
	
	// TODO is there a more pretty Way to register the Analysis so the Delegate can use them?
	public static Map<String, IAnalysis> AnalysisRegistry;
	
    private AnalyzerAttributes() {
    }

}
