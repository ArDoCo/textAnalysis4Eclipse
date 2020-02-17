package plugintest4;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

import org.eclipse.debug.ui.AbstractLaunchConfigurationTabGroup;
import org.eclipse.debug.ui.ILaunchConfigurationDialog;
import org.eclipse.debug.ui.ILaunchConfigurationTab;

import plugintest4.analysis.CharCountAnalyis;
import plugintest4.analysis.WordCountAnalysis;
 

public class AnalyzerTabGroup extends AbstractLaunchConfigurationTabGroup {
	
	  // TODO fancy analysen mit eigenem tab am ende.  
	
	  @Override
	  public void createTabs(ILaunchConfigurationDialog arg0, String arg1) {
		  List<IAnalysis> analysisList = Arrays.asList(new CharCountAnalyis(), new WordCountAnalysis());
		  AnalyzerAttributes.AnalysisRegistry = new HashMap<String, IAnalysis>();
		  
		  for (IAnalysis ana: analysisList) {
				// register Analysis so Delegate can use them later
		      	AnalyzerAttributes.AnalysisRegistry.put(ana.getName(), ana);
		  }
		  		  
	      setTabs(new ILaunchConfigurationTab[] { new ChoseFileTab(analysisList)});
	      
	       // (TODO) add other tab into list
	       // TODO geht das dynamisch? (ServiceLoader.load(Interface.class)) es gibt da nen stackoverflow 
	       // java plugin loader 
	       // muss an der rihctigen stelle suchen
	       // URL Class Loader anschauen
	      
	      // IAnalysis could also have an option "needs own Tab" 
	      // und dann könnte man abhängig davon neue Tabs in die Liste geben oder eben nur
	      // die Analysen in ein Default-Analysis-Tab laden. 
	      
	      // TODO Error Handling if there Are no analyis
	      
	      // This should be doable with:
	      // https://stackoverflow.com/questions/45387473/java-serviceloader-explanation
	      // https://stackoverflow.com/questions/12728985/dynamically-loading-classes-which-adhere-to-an-interface/12729334
	      // https://stackoverflow.com/questions/6219829/method-to-dynamically-load-java-class-files	
	  
	  }
}
