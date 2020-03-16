package exampleanalysis;

import java.util.LinkedList;
import java.util.List;

import javax.xml.stream.XMLEventFactory;
import javax.xml.stream.events.XMLEvent;

import convenience.EventCreator;
import textAnalysis.provider.AnalysisProvider;

/***
 * For each line in the input file, this element creates a node specifying the amount of characters.
 * If this amount is greater than the maximum threshold, it also produces a warning tag.
 * @author Maike Rees (uoxix)
 *
 */
public class CharCountAnalyis implements AnalysisProvider {

	private static final int MAX_CHARS = 80;

	@Override
	public String getName() {
		return "Char-Count";
	}

	@Override
	public boolean isValid() {
		return true;
	}

	@Override
	public List<XMLEvent> getXMLEvents(List<String> textToAnalyze) {
		List<XMLEvent> events = new LinkedList<>();
		List<XMLEvent> warnElements = new LinkedList<>();

		XMLEventFactory eventFactory = XMLEventFactory.newInstance();
		warnElements.addAll(EventCreator.addEventWithTabAndEnd(eventFactory.createStartElement("", "", "warnings")));

		for (int i = 0; i < textToAnalyze.size(); i++) {
			int lengthOfLine = textToAnalyze.get(i).length();

			events.addAll(EventCreator.createEventWithAttribute("line", "number", String.valueOf(i),
					String.valueOf(lengthOfLine)));

			if (lengthOfLine > MAX_CHARS) {
				List<XMLEvent> attributeWarningEvents = EventCreator.createEventWithAttribute("line", "number",
						String.valueOf(i), "Line is too long (more than 80 characters)");

				warnElements.add(eventFactory.createDTD("\t"));
				warnElements.addAll(attributeWarningEvents);
			}
		}
		warnElements.addAll(EventCreator.addEventWithTabAndEnd(eventFactory.createEndElement("", "", "warnings")));
		events.addAll(warnElements);

		return events;
	}

}
