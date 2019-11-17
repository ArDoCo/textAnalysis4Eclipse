package plugintest4;

import org.eclipse.debug.ui.AbstractLaunchConfigurationTabGroup;
import org.eclipse.debug.ui.ILaunchConfigurationDialog;
import org.eclipse.debug.ui.ILaunchConfigurationTab;


public class AnalyzerTabGroup extends AbstractLaunchConfigurationTabGroup {
	
	  @Override
	  public void createTabs(ILaunchConfigurationDialog arg0, String arg1) {
	       setTabs(new ILaunchConfigurationTab[] { new ChoseFileTab(), new AnalysisTab() });
	  }
	  
	  public void createTabs() {
	        setTabs(new ILaunchConfigurationTab[] { new ChoseFileTab() });
	    }

}
