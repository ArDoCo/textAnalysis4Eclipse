package textAnalysis.core;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileFilter;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.LinkedList;
import java.util.List;
import java.util.ServiceLoader;
import java.lang.ClassLoader;

import textAnalysis.provider.AProvider;

public class AnalyzerAttributes {
	
	private static final String PROVIDER_INTERFACE = "textAnalysis.provider.AProvider";

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
        URL[] urls = new URL[flist.length];
        
        try {
        	for (int i = 0; i < flist.length; i++) {
                urls[i] = flist[i].toURI().toURL();
        	}
        	
			ClassLoader cLoader = Class.forName(PROVIDER_INTERFACE).getClassLoader();
		    URLClassLoader cl = new URLClassLoader(urls, cLoader);
		    return cl;	 
		    
		} catch (ClassNotFoundException | MalformedURLException e) {
			e.printStackTrace();
		}
		return null; // TODO can we make the error handling a little nicer?   
    }
   
    /***
     * Loads services as jars from  folder 
     * @param folder: Where the jars are
     * @return a list of services
     */
    protected static List<AProvider> loadServices(String folder) {
    	ServiceLoader<AProvider> analysisServices;
        List<AProvider> providerList = new LinkedList<>();

        ClassLoader classloader = AnalyzerAttributes.getURLCL(folder);
	    analysisServices = ServiceLoader.load(textAnalysis.provider.AProvider.class, classloader);
	    for (AProvider service : analysisServices) { 
		    providerList.add(service);
	    }	
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
