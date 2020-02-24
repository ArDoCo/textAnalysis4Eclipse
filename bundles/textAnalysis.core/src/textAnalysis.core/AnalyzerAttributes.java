package textAnalysis.core;

import java.util.Map;

import provider.AProvider;

import java.io.File;
import java.io.FileFilter;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;

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
	public static Map<String, AProvider> AnalysisRegistry;
	
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
	     
	     //URL[] classLoaderUrls = new URL[]{new URL("file:///C://Maike//KIT//PraktikumWfAM//Analysis//bean.jar")};
//	    URL[] classLoaderUrls = null;
//		try {
//			classLoaderUrls = new URL[]{new URL("file:///C://Maike//KIT//PraktikumWfAM//Analysis/dcountservice-0.0.1-SNAPSHOT.jar")};
//		} catch (MalformedURLException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
	     
	     System.out.println("url list: " + urls.length);
	     System.out.println(flist[0]);
	     System.out.println(urls[0]);
	     URLClassLoader ucl = new URLClassLoader(urls);
	     
	     return ucl;
	}
	
	
    private AnalyzerAttributes() {
    }

}
