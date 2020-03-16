# textAnalysis4Eclipse
This Project was coded for my Lab "Tools for agile modeling". The Documentation is split up into this Readme, one Readme for every bundle in TextAnaysis4Eclipse/bundles and one Readme in the ExampleAnalysis project.

## Motivation
This Project provides an Eclipse plugin that provides a run configuration with which text files can be loaded and analyzed. This is a first step for a framework that can be used to check consistencies between documentation and code. The analysis are not hard coded but can be dynamically loaded with the help of the Java ServiceLoader. Find more info on how to provide an analysis in the TextAnaysis4Eclipse/bundles/provider/Readme.md
This is how the run configuration looks:
![screenshot of the run configuration dialog](https://github.com/maikefer/textAnalysis4Eclipse/blob/master/docu/screen1.PNG "Run Configuration Dialog of textAnalysis4Eclipse")

## Setup
This Project is managed with maven tycho. In the TextAnaysis4Eclipse/bundles/ folder, you find two eclipse plugins:
- TextAnalysis.core
- TextAnalysis.provider
The core-module handles basically everything. The provider-module provides the analysis-interface that is needed when a user wants to implement her own analysis.

You find examplary, very simple Analysis in the *ExampleAnalysis* folder. Have a look at how these are implemented. The *jar* that is extracted from there analysis is to be found in example_jars. *Test files* that can be used to have a first look on the run configuration can be found in *test_files*. 

## How To Run
You can open the TextAnalysis4Eclipse/bundles/textAnalysis.core project in your eclipe workspace and just "run" it. It will open an inner eclipse where the new run configuration is available.
