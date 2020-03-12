package textAnalysis.core;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.MalformedURLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Optional;

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

/***
 * This class handles what happens when the runConfig is run. It calls the
 * Analysis and collects their output in the output file
 * 
 * @author Maike Rees (uoxix)
 *
 */
public class Delegate extends LaunchConfigurationDelegate {

	// TODO (2) basic test ueberlegen / implementieren;
	// es gibt auch SWTBot tests (siehe vogella), ui test als bonus

	private static final String ANALYSIS_XML_TAG = "textAnalysis";
	private static final String ANALYSIS_FILE_ENDING = "_analysis.xml";
	private static final String ERROR_FILE_ENDING = "_error.txt";

	@Override
	public void launch(ILaunchConfiguration configuration, String arg1, ILaunch arg2, IProgressMonitor arg3)
			throws CoreException {

		List<String> fileNames = configuration.getAttribute(PluginAttributes.FILE_NAME, new ArrayList<String>());
		List<AProvider> analysisObjs = getAnalysisObjects(fileNames.get(0));

		List<AProvider> analysisToCompute = new LinkedList<>();
		Map<String, String> analysisCheckboxesStatus = configuration
				.getAttribute(PluginAttributes.CHECKBOX_ACTIVATION_STATUS, new HashMap<String, String>());

		for (Map.Entry<String, String> entry : analysisCheckboxesStatus.entrySet()) {
			if (Boolean.valueOf(entry.getValue())) {
				Optional<AProvider> s = AnalysisLoader.getAnalysisFrom(analysisObjs, entry.getKey());
				if (s.isPresent()) {
					analysisToCompute.add(s.get());
				}
			}
		}

		// ------ Compute Analysis for each input file
		for (String fileName : fileNames) {

			// create file name of output File
			String outputFileName = getFileNameWithoutExtension(fileName) + ANALYSIS_FILE_ENDING;

			// Setup XML
			XMLOutputFactory outputFactory = XMLOutputFactory.newInstance();

			try {
				// create XMLEventWriter
				XMLEventWriter eventWriter = outputFactory.createXMLEventWriter(new FileOutputStream(outputFileName));

				// create an EventFactory
				XMLEventFactory eventFactory = XMLEventFactory.newInstance();
				XMLEvent end = eventFactory.createDTD("\n");

				// create and write Start Tag
				StartDocument startDocument = eventFactory.createStartDocument();
				eventWriter.add(startDocument);

				// create analysis open tag
				eventWriter.add(end);
				StartElement configStartElement = eventFactory.createStartElement("", "", ANALYSIS_XML_TAG);
				eventWriter.add(configStartElement);
				eventWriter.add(end);

				// Get text that should be analyzed
				List<String> textToAnalyze = getLinesInFile(fileName);

				// Add Analysis outputs
				for (AProvider aP : analysisToCompute) {
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
				eventWriter.add(eventFactory.createEndElement("", "", ANALYSIS_XML_TAG));
				eventWriter.add(end);
				eventWriter.add(eventFactory.createEndDocument());
				eventWriter.close();
			} catch (FileNotFoundException | XMLStreamException e1) {
				String error = "Problem with xml Generation: " + e1.toString();
				saveErrorFile(getFileNameWithoutExtension(fileName), error);
			}
		}
	}

	/***
	 * Load Analysis Objects and save an error file if loading is not successful.
	 * 
	 * @param fileName a FileName (and path) for the possible error file.
	 * @return The Analysis Objects, or an empty List if there was an error or no
	 *         Analysis were found.
	 */
	private List<AProvider> getAnalysisObjects(String fileName) {
		String folder = null;
		List<AProvider> analysisProviders = new LinkedList<>();

		String errorFile = getFileNameWithoutExtension(fileName);

		try {
			folder = AnalysisLoader.getAnalysisSrcDirectory();
			if (folder == null) {
				String error = "The path and / or folder name does not exists or is not a directory";
				saveErrorFile(errorFile, error);
			} else {
				analysisProviders = AnalysisLoader.loadAnalysis(folder);
			}
		} catch (MalformedURLException e2) {
			String error = "The analysis files in the given directory can not be converted to URLs: \n" + e2.toString();
			saveErrorFile(errorFile, error);
		} catch (IOException e2) {
			String error = "IO Exception when loading the Analysis Source Directory: \n" + e2.toString();
			saveErrorFile(errorFile, error);
		} catch (ClassNotFoundException e2) {
			String error = "The PROVIDER_INTERFACE can not be found: \n" + e2.toString();
			saveErrorFile(errorFile, error);
		}
		return analysisProviders;
	}

	/***
	 * Extract the filename without the file extension.
	 * 
	 * @param fileName The specific FileName
	 * @return The fileName without extension, e.g. /example/directory/file.txt ->
	 *         /example/directory/file
	 */
	private String getFileNameWithoutExtension(String fileName) {
		int i = fileName.lastIndexOf('.');
		int extensionLength = (i > 0) ? fileName.substring(i).length() : 0;
		return fileName.substring(0, fileName.length() - extensionLength);
	}

	/***
	 * Creates a file with the specified name and error message.
	 * 
	 * @param fileNameWithoutExtension The name and path of the file
	 * @param error                    The error Message
	 */
	private void saveErrorFile(String fileNameWithoutExtension, String error) {
		try {
			PrintWriter out = new PrintWriter(fileNameWithoutExtension + ERROR_FILE_ENDING);
			out.println(error);
			out.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
	}

	/***
	 * Read lines of file into a String-List. Saves an error file in case of error.
	 * 
	 * @param filename The path and filename to read
	 * @return A List of the lines in the file. An empty list if the file is empty
	 *         or couldn't be read.
	 */
	private List<String> getLinesInFile(String filename) {
		List<String> linesInFile = new LinkedList<>();

		try {
			BufferedReader reader = new BufferedReader(new FileReader(filename));
			String line;
			while ((line = reader.readLine()) != null) {
				linesInFile.add(line);
			}
			reader.close();
		} catch (Exception e) {
			saveErrorFile(getFileNameWithoutExtension(filename),
					"Exception occurred trying to read" + filename + ":\n" + e.toString());
		}
		return linesInFile;
	}

}
