package plugintest4;

import java.util.Map;

public class AnalyzerAttributes {
	
	// Names of the Attributes
	public static final String FILE_NAME = "edu.kit.analyzer.FILE_TEXT";
	
	// Was da reinkommt ist eine Map<String,String>: Name auf 'true'/ 'false'.
	// TODO man k�nnte auch nur eine Liste mit allen Namen machen von denen, die ausgef�hrt werden sollen, 
	//      wenn eine abgeklickt wird m�sste man sie halt immer aus der Liste l�schen, das ist wahrscheinlich zu aufw�ndig.
	public static final String CHECKBOX_ACTIVATION = "edu.kit.analyzer.CHECKBOX_ACTIVATION";
	public static final String SERVICE_CHECKBOX_VALUES = "edu.kit.analyzer.SERVICE_CHECKBOX_VALUES";
	
	public static final String EXECUTION_SERVICE_CLASS_NAMES = "edu.kit.analyzer.EXECUTION_SERVICE_CLASS_NAMES";
	
	// TODO is there a more pretty Way to register the Analysis so the Delegate can use them?
	public static Map<String, IAnalysis> AnalysisRegistry;
	
    private AnalyzerAttributes() {
    }

}
