/**
 *
 */
package textAnalysis.core;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.ServiceLoader;
import java.util.Set;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.stream.XMLEventFactory;
import javax.xml.stream.XMLEventWriter;
import javax.xml.stream.XMLOutputFactory;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.events.StartDocument;
import javax.xml.stream.events.StartElement;
import javax.xml.stream.events.XMLEvent;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.debug.core.ILaunch;
import org.eclipse.debug.core.ILaunchConfiguration;
import org.eclipse.debug.core.model.LaunchConfigurationDelegate;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

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
	            StartElement configStartElement = eventFactory.createStartElement("", "", "textAnalysis");
	            eventWriter.add(configStartElement);
	            eventWriter.add(end);
	            
	            // Here: Add Analysis nodes
	            for (AProvider aP : analysisToCompute ) {
	            	List<XMLEvent> events = aP.getXMLEvents(textToAnalyze);
	            	for (XMLEvent e : events) { 
	            		// TODO (2) Whos responsibility is it, to check that the events are properly 
	            		// opened and closed? and formed?            	
	            		eventWriter.add(e);
	            	}
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
            
//            try {

                // ---- Preprocessing
//                Document doc = setupDocument();
//                Element root = setupRootInDoc(filename, doc);
//
            	
            	// ---- setup Document
  
                
                	
                // ---- Analysis
//                Map<String, String> checkbox_activation = configuration.getAttribute(
//                        AnalyzerAttributes.CHECKBOX_ACTIVATION, new HashMap<String, String>());
//
//                for (Map.Entry<String, String> entry : checkbox_activation.entrySet()) {
//                    if (Boolean.valueOf(entry.getValue())) {
//                        // root.appendChild(AnalyzerAttributes.AnalysisRegistry.get(entry.getKey())
//                        // .printInXML(doc, linesInFile));
//                        // TODO FIXME!
//                        System.out.println("FIXME!!");
//                    }
//                }

                // ---- Services // two options to load:
                // 1: (hat ein Problem! TODO)
//                List<AProvider> services1 = new LinkedList<>();
//                 List<String> serviceClassNames = configuration TODO
                // .getAttribute(AnalyzerAttributes.EXECUTION_SERVICE_CLASS_NAMES, new LinkedList<String>());
                // for (String s : serviceClassNames) {
                // try {
                // Object o = Class.forName(s).newInstance(); // braucht das noch ein .class ?
                // if (o instanceof ExecutionServiceProvider) {
                // ExecutionServiceProvider service = (ExecutionServiceProvider) o;
                // services1.add(service);
                // // jetzt sind halt alle services in dieser Liste, auch wenn sie gar nicht angechekt wurden.
                // }
                // }
                // catch(Exception e) {
                // e.printStackTrace();
                // }
                // }

                // 2:
//                 Map<String, String> servicesCheckboxes = 
//                		 configuration.getAttribute(AnalyzerAttributes.CHECKBOX_ACTIVATION, 
//                				 new HashMap<String, String>());
                //
                // List<ExecutionServiceProvider> services2 = new LinkedList<>();
                // ServiceLoader<ExecutionServiceProvider> eServices =
                // ServiceLoader.load(ExecutionServiceProvider.class);
                // for (ExecutionServiceProvider service : eServices) {
                // if (servicesCheckboxes.containsKey(service.getName())
                // && Boolean.valueOf(servicesCheckboxes.get(service.getName()))) {
                // services2.add(service);
                // }
                // }
                //
                // // ---- execute Services (kï¿½nnte man auch direkt zum Laden bei 2 dazu packen)
                // for (ExecutionServiceProvider ser : services2) {
                // root.appendChild(ser.printInXML(doc, linesInFile));
                // }

//                saveAnalysisFile(outputFileName, doc);

//            } catch (ParserConfigurationException e) {
//                e.printStackTrace();
//            } catch (TransformerException tfe) {
//                tfe.printStackTrace();
//            }
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
    
    private void saveAnalysisFile(String outputFileName, Document doc) throws TransformerException {

        TransformerFactory transformerFactory = TransformerFactory.newInstance();
        Transformer transformer = transformerFactory.newTransformer();
        DOMSource dmSource = new DOMSource(doc);
        StreamResult result = new StreamResult(new File(outputFileName));
        transformer.transform(dmSource, result);
    }

    private Document setupDocument() throws ParserConfigurationException {
        DocumentBuilderFactory docFactory = DocumentBuilderFactory.newInstance();
        DocumentBuilder docBuilder = docFactory.newDocumentBuilder();

        Document doc = docBuilder.newDocument();
        return doc;
    }

    private Element setupRootInDoc(String inputFileName, Document doc) {

        Element rootElement = doc.createElement("Analysis");
        doc.appendChild(rootElement);

        Element source = doc.createElement("Source");
        source.setAttribute("file", inputFileName);
        rootElement.appendChild(source);

        return rootElement;
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
