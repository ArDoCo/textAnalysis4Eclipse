/**
 * 
 */
package plugintest4;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.List;

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
		
		List<Integer> charsPerLine = getFileContent(filename);
		writeXML(charsPerLine, outputFileName, filename);
		
	}

	private List<Integer> getFileContent(String filename) {
		List<Integer> charsPerLine = new ArrayList<Integer>();
		try
		  {
		    BufferedReader reader = new BufferedReader(new FileReader(filename));
		    String line;
		    while ((line = reader.readLine()) != null)
		    {
		    	int lengthOfLine = line.length();
		    	charsPerLine.add(lengthOfLine);
		    }
		    reader.close();
		  }
		  catch (Exception e)
		  {
		    System.err.format("Exception occurred trying to read '%s'.", filename);
		    e.printStackTrace();
		  }
		return charsPerLine;
	}
	
	private void writeXML(List<Integer> charsPerLine, String outputFileName, String sourceFile) {

		  try {

			DocumentBuilderFactory docFactory = DocumentBuilderFactory.newInstance();
			DocumentBuilder docBuilder = docFactory.newDocumentBuilder();

			// root elements
			Document doc = docBuilder.newDocument();
			Element rootElement = doc.createElement("Analysis");
			doc.appendChild(rootElement);
			
			Element source = doc.createElement("Source");
			source.setAttribute("file", sourceFile);
			rootElement.appendChild(source);
			
			Element charCount = doc.createElement("Char-Count");
			rootElement.appendChild(charCount);
			
			for (int i = 0; i < charsPerLine.size(); i++) {
				Element line = doc.createElement("line");
				line.setAttribute("number", Integer.toString(i));
				line.appendChild(doc.createTextNode(Integer.toString(charsPerLine.get(i))));
				charCount.appendChild(line);
			}
			
			TransformerFactory transformerFactory = TransformerFactory.newInstance();
			Transformer transformer = transformerFactory.newTransformer();
			DOMSource dmSource = new DOMSource(doc);
			StreamResult result = new StreamResult(new File(outputFileName));

			transformer.transform(dmSource, result);

			System.out.println("File saved!");

		  } catch (ParserConfigurationException pce) {
			pce.printStackTrace();
		  } catch (TransformerException tfe) {
			tfe.printStackTrace();
		  }
		  
	}		

}
