package plugintest4;

import java.util.Map;
import java.io.File;
import java.io.FileFilter;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;

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
	
	protected static URLClassLoader getURLCL() {
		 File loc = new File("C:\\Maike\\KIT\\PraktikumWfAM\\Analysis");

	     File[] flist = loc.listFiles(new FileFilter() {
	     	public boolean accept(File file) {return file.getPath().toLowerCase().endsWith(".jar");}
	     });
	     System.out.println("file list: " + flist.length);
	     
	     URL[] urls = new URL[flist.length];
	     for (int i = 0; i < flist.length; i++)
			try {
				urls[i] = flist[i].toURI().toURL();
			} catch (MalformedURLException e) {
				e.printStackTrace();
			}
	     
	     System.out.println("url list: " + urls.length);
	     System.out.println(flist[0]);
	     System.out.println(urls[0]);
	     URLClassLoader ucl = new URLClassLoader(urls);
	     
	     return ucl;
	}
	
	
    private AnalyzerAttributes() {
    }

}
