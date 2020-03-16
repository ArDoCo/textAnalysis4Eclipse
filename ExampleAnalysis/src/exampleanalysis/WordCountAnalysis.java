package exampleanalysis;

import java.util.LinkedList;
import java.util.List;
import java.util.StringTokenizer;

import javax.xml.stream.events.XMLEvent;

import convenience.EventCreator;
import textAnalysis.provider.AnalysisProvider;

/***
 * This Analysis counts the words per line and outputs this perLineCount.
 * @author Maike Rees (uoxix)
 *
 */
public class WordCountAnalysis implements AnalysisProvider {

	@Override
	public String getName() {
		return "Word-Count";
	}

	@Override
	public boolean isValid() {
		return true;
	}

	@Override
	public List<XMLEvent> getXMLEvents(List<String> textToAnalyze) {
		List<XMLEvent> events = new LinkedList<>();

		for (int i = 0; i < textToAnalyze.size(); i++) {
			StringTokenizer tokens = new StringTokenizer(textToAnalyze.get(i));
			events.addAll(EventCreator.createEventWithCharacters("line", String.valueOf(tokens.countTokens())));
		}
		return events;
	}

}
