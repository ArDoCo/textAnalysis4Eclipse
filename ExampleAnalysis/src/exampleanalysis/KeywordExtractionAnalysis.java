package exampleanalysis;

import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import javax.xml.stream.events.XMLEvent;

import convenience.EventCreator;
import textAnalysis.provider.AnalysisProvider;

/***
 * 
 * This Analysis counts and outputs how often certain keywords appear and in
 * which lines.
 * 
 * @author Maike Rees (uoxix)
 *
 */
public class KeywordExtractionAnalysis implements AnalysisProvider {

	private static final List<String> KEYWORDS = Arrays.asList("Lorem", "ipsum", "dolor");

	@Override
	public String getName() {
		return "KeywordExtractionAnalysis";
	}

	@Override
	public boolean isValid() {
		return true;
	}

	@Override
	public List<XMLEvent> getXMLEvents(List<String> textToAnalyze) {
		Map<String, List<Integer>> keywordInLines = new HashMap<>();

		for (String k : KEYWORDS) {
			keywordInLines.put(k, new LinkedList<>());
		}

		for (int i = 0; i < textToAnalyze.size(); i++) {
			String[] words = textToAnalyze.get(i).split(" ");
			for (String w : words) {
				if (KEYWORDS.contains(w)) {
					keywordInLines.get(w).add(i);
				}
			}
		}

		List<XMLEvent> events = new LinkedList<>();

		for (Map.Entry<String, List<Integer>> entry : keywordInLines.entrySet()) {
			events.addAll(EventCreator.createEventWithAttribute("word", "keyword", entry.getKey(),
					entry.getValue().toString()));
		}

		return events;
	}

}
