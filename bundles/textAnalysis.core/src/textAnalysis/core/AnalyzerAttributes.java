package textAnalysis.core;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileFilter;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.lang.reflect.Method;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URL;
import java.net.URLClassLoader;
//import java.net.URLClassLoader;
import java.util.LinkedList;
import java.util.List;
import java.util.ServiceLoader;
import java.lang.ClassLoader;
//import java.lang.Class;

import textAnalysis.provider.AProvider;

public class AnalyzerAttributes {
	
	private static final boolean cheatVersion = false;

    // Names of the Attributes
    public static final String FILE_NAME = "textAnalysis.core.FILE_TEXT";
    public static final String CHECKBOX_ACTIVATION = "textAnalysis.CHECKBOX_ACTIVATION";


    /**
     * Reads the config file and returns the name of the Folder where the Analysis-Jars are.
     * The config file is a file called .textanalysisconfig in the users home directory.
     * It has to contain the specific path to the folder.
     * 
     * If the config file can not be found, it is created with an entry to a default 
     * folder which is the folder '.textanalysis' in the user's home directory.
     * 
     * @return The folder name or "" if no folder is specified / the specified folder can not be found.
     * @throws IOException
     */
	protected static String getAnalysisSrcFolder() throws IOException {

		String homeDir = System.getProperty("user.home");
		File tmpDir = new File(homeDir + "\\.textanalysisconfig");
		if (!tmpDir.exists()) {
			String fileData = homeDir + "\\.textanalysis\\";
			FileOutputStream fos = new FileOutputStream(tmpDir);
			fos.write(fileData.getBytes());
			fos.flush();
			fos.close();
			File analysisDir = new File(fileData);
			if (!analysisDir.exists()) {
				analysisDir.mkdir();
			}
			System.out.println("successfull");
		}

		BufferedReader br = new BufferedReader(new FileReader(tmpDir));
		String configFolder = br.readLine();
		br.close();

		if (configFolder == null) {
			return "";
		}
		if (new File(configFolder).exists()) {
			return configFolder;
		} else {
			return "";
		}
	}
    
    
    /***
     * Creates a ClassLoader that loads the jar-files from the specified configFolder
     * @param configFolder
     * @return The created ClassLoader
     */
    protected static URLClassLoader getURLCL(String configFolder) {
    	
    	File loc = new File(configFolder);
        File[] flist = loc.listFiles((FileFilter) file -> file.getPath().toLowerCase().endsWith(".jar"));
        System.out.println("file list: " + flist.length);

        URL[] urls = new URL[flist.length];
        for (int i = 0; i < flist.length; i++) {
            try {
                urls[i] = flist[i].toURI().toURL();
            } catch (MalformedURLException e) {
                e.printStackTrace();
            }
        }

        System.out.println("found # urls: " + urls.length);
        
       // URLClassLoader ucl = new URLClassLoader(urls);

		try {
			Class cls = Class.forName("textAnalysis.provider.AProvider");
			ClassLoader cLoader = cls.getClassLoader();
		    URLClassLoader cl = new URLClassLoader(urls, cLoader);
		    return cl;	 
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
        
    }
    
    protected static URLClassLoader getBundleClassLoader() {
    	String folder = "C:\\Maike\\KIT\\PraktikumWfAM\\workspace\\textAnalysis4Eclipse\\bundles\\";
    	File loc1 = new File(folder + "textAnalysis.core\\");
    	//File loc2 = new File(folder + "textAnalysis.core\\");

    	try { // loc1.toURI().toURL(), 
			URL[] urls = new URL[] {loc1.toURI().toURL()};
			return new URLClassLoader(urls);
		} catch (MalformedURLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    	return null;	
    }
   
    
    protected static List<AProvider> loadServices(String folder) {
    	ServiceLoader<AProvider> analysisServices;
        List<AProvider> providerList = new LinkedList<>();

    	if (cheatVersion) {	
    		String fol = "C:\\Maike\\KIT\\PraktikumWfAM\\workspace\\textAnalysis4Eclipse\\"
    				+ "bundles\\analysisAsPlugin\\bin\\";
    		File fil = new File(fol);
    		// analysisasplugin/FullstopSpaceChecker (wrong name: FullstopSpaceChecker)
    		URL dirUrl;
    		URL provUrl;
    		String folProv = "C:\\Maike\\KIT\\PraktikumWfAM\\workspace\\textAnalysis4Eclipse\\"
    				+ "bundles\\textAnalysis.provider\\bin\\";
    		File fil2 = new File(folProv);
    		
			try {
				dirUrl = fil.toURL();
				provUrl = fil2.toURL();
				
				Class cls = Class.forName("textAnalysis.provider.AProvider");
		        ClassLoader cLoader = cls.getClassLoader();
		        URLClassLoader cl = new URLClassLoader(new URL[] {dirUrl}, cLoader);
			         
//	    		URLClassLoader cl = new URLClassLoader(new URL[] {dirUrl, provUrl}, 
//	    				ClassLoader.getSystemClassLoader());
	    		//, getClass().class.getClassLoader());  
	    		Class loadedClass = cl.loadClass("analysisasplugin.FullstopSpaceChecker");
	    		AProvider obj = (AProvider) loadedClass.newInstance();
	    		// gibt classcastexception weil whrschl in untersch. classloadern
	    		providerList.add(obj);
    		//obj.doSomething();
    		
			} catch (MalformedURLException | ClassNotFoundException | InstantiationException | IllegalAccessException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}  
    		//analysisServices = ServiceLoader.load(AProvider.class);
    		//analysisServices = ServiceLoader.load(AProvider.class, getBundleClassLoader());
//    		ResourceFinder finder = new ResourceFinder("META-INF/services/");
    	
//        	String folder2 = "analysisAsPlugin/META-INF/services/";
//    		ResourceFinder finderA = new ResourceFinder(folder2);
    		// finds if only one line in file in this project
//    		try {
    			// textAnalysis.core.subst.ASimpleAnalysis
//    			textAnalysis.core.subst.CharCountAnalyis
//    			textAnalysis.core.subst.WordCountAnalysis
    			
//				List<Class> impls = finder.findAllImplementations(AProvider.class);
//				for (Class i: impls) {
//					try {
//						AProvider prov = (AProvider) i.newInstance();
//						providerList.add(prov);
//					} catch (InstantiationException e) {
//						// TODO Auto-generated catch block
//						e.printStackTrace();
//					} catch (IllegalAccessException e) {
//						// TODO Auto-generated catch block
//						e.printStackTrace();
//					}
//					
//				}
//			} catch (ClassNotFoundException e) {
//				// TODO Auto-generated catch block
//				e.printStackTrace();
//			} catch (IOException e) {
//				// TODO Auto-generated catch block
//				e.printStackTrace();
//			}
    	} else { 
            ClassLoader classloader = AnalyzerAttributes.getURLCL(folder);
	        analysisServices = ServiceLoader.load(textAnalysis.provider.AProvider.class, classloader);
	        for (AProvider service : analysisServices) { // FIXME, throws ClassNotFoundDef
	        	providerList.add(service);
	        }
    	}
    	
    	System.out.println("Provider List Length " + String.valueOf(providerList.size()));
    	
        return providerList;
    }
    
    protected static AProvider getMatchingProvider(List<AProvider> list, String name) {
    	
    	for (AProvider s : list) {
    		if (s.getName().equals(name)) {
    			return s;
    		}
    	}
    	return null;
    }
    
    private AnalyzerAttributes() {
    }

}
