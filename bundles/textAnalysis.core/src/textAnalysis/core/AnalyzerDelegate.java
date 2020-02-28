/**
 *
 */
package textAnalysis.core;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.ServiceLoader;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
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

    // TODO hier evtl. erst die analysis-objekte laden, im tab nur static constanten zu namen abrufen.
    // TODO oder eigene launchconfig schreiben, die analyse-objekte speichern kann.
    // TODO basic test �berlegen / implementieren; es gibt auch SWTBot tests (siehe vogella), ui test als bonus

    // TODO bis 13.1. analyse loading, maven tycho verbessern als prio2

    @Override
    public void launch(ILaunchConfiguration configuration, String arg1, ILaunch arg2, IProgressMonitor arg3)
            throws CoreException {

        System.out.println("hello");

        List<String> filenames = configuration.getAttribute(AnalyzerAttributes.FILE_NAME, new ArrayList<String>());
        ServiceLoader<AProvider> executionServices = ServiceLoader.load(AProvider.class);

        // f�r jedes file eigene analyse:
        for (String filename : filenames) {
            String subString = filename.substring(0, filename.length() - 4); // TODO 4 rausziehen
            String outputFileName = subString + "_analysis.xml";

            try {

                // ---- Preprocessing
                Document doc = setupDocument();
                Element root = setupRootInDoc(filename, doc);

                List<String> linesInFile = getLinesInFile(filename);

                // ---- Analysis
                Map<String, String> checkbox_activation = configuration.getAttribute(
                        AnalyzerAttributes.CHECKBOX_ACTIVATION, new HashMap<String, String>());

                for (Map.Entry<String, String> entry : checkbox_activation.entrySet()) {
                    if (Boolean.valueOf(entry.getValue())) {
                        // root.appendChild(AnalyzerAttributes.AnalysisRegistry.get(entry.getKey())
                        // .printInXML(doc, linesInFile));
                        // TODO FIXME!
                        System.out.println("FIXME!!");
                    }
                }

                // ---- Services // two options to load:
                // 1: (hat ein Problem! TODO)
                List<AProvider> services1 = new LinkedList<>();
                // List<String> serviceClassNames = configuration TODO
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
                // Map<String, String> servicesCheckboxes = configuration
                // .getAttribute(AnalyzerAttributes.SERVICE_CHECKBOX_VALUES, new HashMap<String, String>());
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
                // // ---- execute Services (k�nnte man auch direkt zum Laden bei 2 dazu packen)
                // for (ExecutionServiceProvider ser : services2) {
                // root.appendChild(ser.printInXML(doc, linesInFile));
                // }

                saveAnalysisFile(outputFileName, doc);

            } catch (ParserConfigurationException e) {
                e.printStackTrace();
            } catch (TransformerException tfe) {
                tfe.printStackTrace();
            }
        }
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
