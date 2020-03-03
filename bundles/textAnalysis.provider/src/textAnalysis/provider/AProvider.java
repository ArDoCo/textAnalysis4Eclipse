package textAnalysis.provider;

//import org.w3c.dom.Document;
//import org.w3c.dom.Element;   
// das geht wohl ab java 9 nicht mehr. javax xml anschauen, weil das java internas sind.
// javax xml, apache xml serializer, selbst was schreiben?
// oder java module example um alte dinger zu verwenden

public interface AProvider {
    // Element printInXML(Document doc, List<String> linesInFile);

    String getName();

    boolean isValid();

}
