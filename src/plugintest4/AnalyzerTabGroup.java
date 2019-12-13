package plugintest4;

import org.eclipse.debug.ui.AbstractLaunchConfigurationTabGroup;
import org.eclipse.debug.ui.ILaunchConfigurationDialog;
import org.eclipse.debug.ui.ILaunchConfigurationTab;


public class AnalyzerTabGroup extends AbstractLaunchConfigurationTabGroup {
	
	  @Override
	  public void createTabs(ILaunchConfigurationDialog arg0, String arg1) {
	       setTabs(new ILaunchConfigurationTab[] { new ChoseFileTab()});
	       // (TODO) add other tab into list
	       // TODO geht das dynamisch? (ServiceLoader.load(Interface.class)) es gibt da nen stackoverflow 
	       // java plugin loader 
	       // muss an der rihctigen stelle suchen
	       // URL Class Loader anschauen
	  }

}
