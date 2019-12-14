package plugintest4;

import java.util.Map;

public class AnalyzerAttributes {
	
	public static final String FILE_NAME = "edu.kit.analyzer.FILE_TEXT";
	public static final String CHECKBOX_ACTIVATION = "edu.kit.analyzer.CHECKBOX_ACTIVATION";
	
	// TODO is there a more pretty Way to register the Analysis so the Delegate can use them?
	public static Map<String, IAnalysis> AnalysisRegistry;
	
    private AnalyzerAttributes() {
    }

}
