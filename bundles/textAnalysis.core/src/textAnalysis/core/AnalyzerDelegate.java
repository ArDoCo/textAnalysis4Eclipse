/**
 *
 */
package textAnalysis.core;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import javax.xml.stream.XMLEventFactory;
import javax.xml.stream.XMLEventWriter;
import javax.xml.stream.XMLOutputFactory;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.events.EndElement;
import javax.xml.stream.events.StartDocument;
import javax.xml.stream.events.StartElement;
import javax.xml.stream.events.XMLEvent;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.debug.core.ILaunch;
import org.eclipse.debug.core.ILaunchConfiguration;
import org.eclipse.debug.core.model.LaunchConfigurationDelegate;

import textAnalysis.provider.AProvider;

/**
 * @author maike
 *
 */
public class AnalyzerDelegate extends LaunchConfigurationDelegate {

    // TODO (2) basic test ueberlegen / implementieren; 
	// es gibt auch SWTBot tests (siehe vogella), ui test als bonus


    @Override
    public void launch(ILaunchConfiguration configuration, String arg1, ILaunch arg2, IProgressMonitor arg3)
            throws CoreException {

        System.out.println("hello");

        List<String> filenames = configuration.getAttribute(AnalyzerAttributes.FILE_NAME, new ArrayList<String>());

        // Load analysis that should be done:
        Map<String, String> analysisCheckboxes = configuration.getAttribute(AnalyzerAttributes.CHECKBOX_ACTIVATION, 
       				 														new HashMap<String, String>());
        
        String folder = ""; // TODO (2) handle if folder and analyzes change between loading and analyzing
		try {
			folder = AnalyzerAttributes.getAnalysisSrcFolder();
		} catch (IOException e2) {
			// TODO Auto-generated catch block
			e2.printStackTrace();
		}  // müsste eigentlich ioexeption schmeißen
        List<AProvider> analysisProviders = AnalyzerAttributes.loadServices(folder);
        // TODO (3) Analysis have to have unique ids, no same two names!
        List<AProvider> analysisToCompute = new LinkedList<>();

        for (Map.Entry<String,String> entry : analysisCheckboxes.entrySet()) {
        	if (Boolean.valueOf(entry.getValue())) {
        		AProvider s = getMatchingProvider(analysisProviders, entry.getKey());
        		if (s != null) {
        			analysisToCompute.add(s);
        		}
        	}
        }
        
        // do one analysis for each input file
        for (String filename : filenames) {
        	
        	List<String> textToAnalyze = getLinesInFile(filename); // thing that gets analyzed

        	int i = filename.lastIndexOf('.');
        	int extensionlength = (i > 0) ? filename.substring(i).length() : 0;
            String subString = filename.substring(0, filename.length() - extensionlength); 
            String outputFileName = subString + "_analysis.xml";

            // Setup XML
            XMLOutputFactory outputFactory = XMLOutputFactory.newInstance();
            // create XMLEventWriter
            try {
            	XMLEventWriter eventWriter = outputFactory
				        .createXMLEventWriter(new FileOutputStream(outputFileName));
			
	            // create an EventFactory
	            XMLEventFactory eventFactory = XMLEventFactory.newInstance();
	            XMLEvent end = eventFactory.createDTD("\n");
	            // create and write Start Tag
	            StartDocument startDocument = eventFactory.createStartDocument();
	            eventWriter.add(startDocument);
	
	            // create analysis open tag
	            eventWriter.add(end);
	            StartElement configStartElement = eventFactory.createStartElement("", "", "textAnalysis");
	            eventWriter.add(configStartElement);
	            eventWriter.add(end);
	            
	            // Here: Add Analysis nodes
	            for (AProvider aP : analysisToCompute ) {
            		StartElement analysisStartEl = eventFactory.createStartElement("", "", aP.getName());
            		eventWriter.add(analysisStartEl);
	            	List<XMLEvent> events = aP.getXMLEvents(textToAnalyze);
	            	for (XMLEvent e : events) {  
	            		eventWriter.add(e);
	            	}
	            	EndElement analysisEndEl = eventFactory.createEndElement("", "", aP.getName());
	            	eventWriter.add(analysisEndEl);
	            	eventWriter.add(end);
	            }
	            
	            // close XML
	            eventWriter.add(eventFactory.createEndElement("", "", "textAnalysis"));
	            eventWriter.add(end);
	            eventWriter.add(eventFactory.createEndDocument());
	            eventWriter.close();
            } catch (FileNotFoundException | XMLStreamException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
        }
    }

    private AProvider getMatchingProvider(List<AProvider> list, String name) {
    	
    	for (AProvider s : list) {
    		if (s.getName().equals(name)) {
    			return s;
    		}
    	}
    	return null;
    }
    

    private List<String> getLinesInFile(String filename) {
        List<String> linesInFile = new ArrayList<>();

        try {
            BufferedReader reader = new BufferedReader(new FileReader(filename));
            String line;
            while ((line = reader.readLine()) != null) {
                linesInFile.add(line);
            }
            reader.close();
        } catch (Exception e) {
            System.err.format("Exception occurred trying to read '%s'.", filename);
            e.printStackTrace();
        }
        return linesInFile;
    }

}
