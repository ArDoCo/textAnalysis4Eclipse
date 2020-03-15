# textAnalysis4Eclipse
This Project was coded for my Lab "Tools for agile modeling". The Documentation is split up into this Readme and one Readme for every bundle.

## Motivation
This Project provides an Eclipse plugin that provides a run configuration with wicht text files can be loaded and analyzed. This is a first step for a framework that can be used to check consistencies between documentation and code. The Analysis are not hard coded but can be dynamically loaded with the help of the Java ServiceLoader. Find more info on how to provide an analysis in the bundles/provider/Readme.md

## Setup
This Project is managed with maven tycho. In the bundles/ folder, you find two eclipse plugins:
- TextAnalysis.core
- TextAnalysis.provider
The core-module handles basically everything. The provider-module provides the analysis-interface that is needed when a user wants to implement her own analysis.

## How To Run
You can open the core project in your eclipe workspace and just "run" it. It will open an inner eclipse where the new run configuration is available.

## Prerequesites
Java Version, Eclipse SDK
