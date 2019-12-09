/**
 * 
 */
package plugintest4;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.List;
import java.util.StringTokenizer;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.w3c.dom.Attr;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.debug.core.ILaunch;
import org.eclipse.debug.core.ILaunchConfiguration;
import org.eclipse.debug.core.model.LaunchConfigurationDelegate;

/**
 * @author maike
 *
 */
public class AnalyzerDelegate extends LaunchConfigurationDelegate {

	@Override
	public void launch(ILaunchConfiguration configuration, String arg1, ILaunch arg2, IProgressMonitor arg3)
			throws CoreException {
		System.out.println("hello");
		String filename = configuration.getAttribute(AnalyzerAttributes.FILE_NAME, "none");
		System.out.println(filename);
		
		String subString = filename.substring(0, filename.length()-4);
		String outputFileName = subString + "_analysis.xml";
		
		Document doc;
		try {
			doc = setupDocument();
			Element root = setupRootInDoc(filename, doc);
			
			List<String> linesInFile = getLinesInFile(filename);
			addCharOutput(root, linesInFile, doc);		
			
			if (configuration.getAttribute(AnalyzerAttributes.WORD_COUNT, false)) {
				addWordCountOutput(root, doc, linesInFile);
			}
			
			saveAnalysisFile(outputFileName, doc);
			
		} catch (ParserConfigurationException e) {
			e.printStackTrace();
		} catch (TransformerException tfe) {
			tfe.printStackTrace();
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
		List<String> linesInFile = new ArrayList<String>();
		
		try
		  {
		    BufferedReader reader = new BufferedReader(new FileReader(filename));
		    String line;
		    while ((line = reader.readLine()) != null)
		    {
		    	linesInFile.add(line);
		    }
		    reader.close();
		  }
		  catch (Exception e)
		  {
		    System.err.format("Exception occurred trying to read '%s'.", filename);
		    e.printStackTrace();
		  }
		return linesInFile;
	}
	
	private void addCharOutput(Element rootElement, List<String> linesInFile, Document doc) {
		
		Element charCount = doc.createElement("Char-Count");
		rootElement.appendChild(charCount);
		
		Element warnings = doc.createElement("Warnings");
		charCount.appendChild(warnings);
		
		for (int i = 0; i < linesInFile.size(); i++) {
			Element line = doc.createElement("line");
			line.setAttribute("number", Integer.toString(i));
			int lengthOfLine = linesInFile.get(i).length();
			line.appendChild(doc.createTextNode(Integer.toString(lengthOfLine)));
			charCount.appendChild(line);
			
			if (lengthOfLine > 80) {
				Element warn = doc.createElement("warn");
				warn.setAttribute("line-number", Integer.toString(i));
				warn.appendChild(doc.createTextNode("Line is too long (more than 80 characters)"));
				warnings.appendChild(warn);
			}
		}
	}

	private void addWordCountOutput(Element root, Document doc, List<String> linesInFile) {
		Element wordCount = doc.createElement("Word-Count");
		root.appendChild(wordCount);
		
		for (int i = 0; i < linesInFile.size(); i++) {
			Element line = doc.createElement("line");
			line.setAttribute("number", Integer.toString(i));
			
			// count words:
		    StringTokenizer tokens = new StringTokenizer(linesInFile.get(i));
		    int count = tokens.countTokens();
		    line.appendChild(doc.createTextNode(Integer.toString(count)));
			wordCount.appendChild(line);
		}
	}

	
}
