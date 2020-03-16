package exampleanalysis;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import javax.xml.stream.events.XMLEvent;

import convenience.EventCreator;
import textAnalysis.provider.AnalysisProvider;

/***
 * This analysis counts the frequency of all words and outputs if the frequency
 * is about an upper limit or below a lower limit.
 * 
 * @author Maike Rees (uoxix)
 *
 */
public class WordFrequencyAnalysis implements AnalysisProvider {

	private static final int UPPER_LIMIT = 15;
	private static final int LOWER_LIMIT = 5;

	@Override
	public String getName() {
		return "WordFrequencyCount";
	}

	@Override
	public boolean isValid() {
		return true;
	}

	@Override
	public List<XMLEvent> getXMLEvents(List<String> textToAnalyze) {
		Map<String, Integer> wordFrequency = new HashMap<>();

		// Count word frequencies
		for (String line : textToAnalyze) {
			String[] words = line.split(" ");
			for (String w : words) {
				if (wordFrequency.containsKey(w)) {
					Integer oldInt = wordFrequency.get(w);
					Integer newInt = oldInt + 1;
					wordFrequency.replace(w, newInt);
				} else {
					wordFrequency.put(w, 1);
				}
			}
		}

		// Create xml events for word frequencies over and under the set limits
		List<XMLEvent> events = new LinkedList<>();

		for (Map.Entry<String, Integer> entry : wordFrequency.entrySet()) {
			if (entry.getValue() > UPPER_LIMIT || entry.getValue() < LOWER_LIMIT) {
				events.addAll(EventCreator.createEventWithAttribute("word", "string", entry.getKey(),
						String.valueOf(entry.getValue())));
			}
		}

		return events;
	}

}
