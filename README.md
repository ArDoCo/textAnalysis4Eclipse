[![Build Status](https://travis-ci.org/maikefer/textAnalysis4Eclipse.svg?branch=master)](https://travis-ci.org/maikefer/textAnalysis4Eclipse)
# textAnalysis4Eclipse
This Project was coded for my Lab "Tools for Agile Modeling" at the KIT in the Wintersemester 2019/2020. 

The documentation is split up into 
- this README
  which describes the overall Motivation and Setup,
- the [README in the ExampleAnalysis project](https://github.com/maikefer/textAnalysis4Eclipse/tree/master/ExampleAnalysis/README.md)
   which describes the provided ExampleAnalysis that should be a guide how a user can implement their own Analysis,
- a README in each of the [TextAnaysis4Eclipse/bundles](https://github.com/maikefer/textAnalysis4Eclipse/tree/master/TextAnalysis4Eclipse/bundles)
  - a [README in the textAnalysis.core](https://github.com/maikefer/textAnalysis4Eclipse/blob/master/TextAnalysis4Eclipse/bundles/textAnalysis.core/README.md)
    which describes the core functionality of the plugin, including input and output files, how analysis loading works and how to handle possible errors,
  - a [README in the textAnalysis.provider](https://github.com/maikefer/textAnalysis4Eclipse/blob/master/TextAnalysis4Eclipse/bundles/textAnalysis.provider/README.md)
    which describes how to implement an Analysis as a Service and some more information about the ServiceProvider interface. 
 
## Motivation
This project provides an Eclipse plugin that provides a run configuration with which text files can be loaded and analyzed. This is a first step for a framework that can be used to check consistencies between documentation and code. The analysis are not hard coded but will be dynamically loaded with the help of the Java ServiceLoader. Find more info on how to provide an analysis in the [TextAnaysis4Eclipse/bundles/provider/Readme.md](https://github.com/maikefer/textAnalysis4Eclipse/blob/master/TextAnalysis4Eclipse/bundles/textAnalysis.provider/README.md)

This is how the run configuration looks:
![screenshot of the run configuration dialog](https://github.com/maikefer/textAnalysis4Eclipse/blob/master/docu/screen1.PNG "Run Configuration Dialog of textAnalysis4Eclipse")

## Setup
This project is managed with maven tycho. In the [TextAnaysis4Eclipse/bundles/ folder](https://github.com/maikefer/textAnalysis4Eclipse/tree/master/TextAnalysis4Eclipse/bundles), you find two eclipse plugins:
- [TextAnalysis.core](https://github.com/maikefer/textAnalysis4Eclipse/tree/master/TextAnalysis4Eclipse/bundles/textAnalysis.core)
- [TextAnalysis.provider](https://github.com/maikefer/textAnalysis4Eclipse/tree/master/TextAnalysis4Eclipse/bundles/textAnalysis.provider)
The core-module handles basically everything. The provider-module provides the analysis-interface that is needed when a user wants to implement her own analysis.

You find examplary, very simple Analysis in the [*ExampleAnalysis* folder](https://github.com/maikefer/textAnalysis4Eclipse/tree/master/ExampleAnalysis). Have a look at how these are implemented. The *jar* that is extracted from these analysis can be found in [example_jars](https://github.com/maikefer/textAnalysis4Eclipse/tree/master/example_jars). *Test files* that can be used to have a first look on the run configuration can be found in [*test_files*](https://github.com/maikefer/textAnalysis4Eclipse/tree/master/test_files). 

## How To Run
You can open the [TextAnalysis4Eclipse/bundles/textAnalysis.core project](https://github.com/maikefer/textAnalysis4Eclipse/tree/master/TextAnalysis4Eclipse/bundles/textAnalysis.core) in your eclipe workspace and just "run" it as "Eclipse Application". It will open an inner Eclipse instance where the new run configuration is available.
