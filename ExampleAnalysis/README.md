# ExampleAnalysis
This Project contains 4 Analysis that work as example Analysis for the TextAnalysis4Eclipse Plugin.

You can have a look at the implementation, especially the META-INF/services folder, to understand how to code your own analysis. 
The convenience.ElementCreator helps to easily create XML-Tags with the javax.xml.stream API.

## Provided Analysis
The implemented Analysis provide very simple functions. The are implemented rather naiv and don't take care of extrem use cases.
- _WordCount_: Counts the amount of Word per line.
- _CharCount_: Counts the amount of Characters per line and creates a Warning if it is above a certain threshold.
- _WordFrequency_: Counts the frequency of all words and outputs the ones which have frequencies lower or greater than two thresholds.
- _KeywordExtraction_: Reports how often certain keywords appear and in which lines.

