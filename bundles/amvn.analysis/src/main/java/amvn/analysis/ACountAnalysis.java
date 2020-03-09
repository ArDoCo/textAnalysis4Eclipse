package amvn.analysis;

import java.util.LinkedList;
import java.util.List;

import javax.xml.stream.events.XMLEvent;

import textAnalysis.provider.AProvider;

public class ACountAnalysis implements AProvider {

	@Override
	public String getName() {
		return "A-Count-Analysis";
	}

	@Override
	public boolean isValid() {
		return true;
	}

	@Override
	public List<XMLEvent> getXMLEvents(List<String> textToAnalyze) {
		return new LinkedList<XMLEvent>();
	}

}
