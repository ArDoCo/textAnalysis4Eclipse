package textAnalysis.provider;

import java.util.List;

import org.w3c.dom.Document;
import org.w3c.dom.Element;

public interface AProvider {
    Element printInXML(Document doc, List<String> linesInFile);

    String getName();

    boolean isValid();

}
