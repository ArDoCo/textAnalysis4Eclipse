package textAnalysis.core;

import org.eclipse.debug.ui.AbstractLaunchConfigurationTabGroup;
import org.eclipse.debug.ui.ILaunchConfigurationDialog;
import org.eclipse.debug.ui.ILaunchConfigurationTab;

public class AnalyzerTabGroup extends AbstractLaunchConfigurationTabGroup {
	
    @Override
    public void createTabs(ILaunchConfigurationDialog arg0, String arg1) {
        setTabs(new ILaunchConfigurationTab[] { new ChoseFileTab() });

        // This should be doable with:
        // https://stackoverflow.com/questions/45387473/java-serviceloader-explanation
        // https://stackoverflow.com/questions/12728985/dynamically-loading-classes-which-adhere-to-an-interface/12729334
        // https://stackoverflow.com/questions/6219829/method-to-dynamically-load-java-class-files

    }
}
