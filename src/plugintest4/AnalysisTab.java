package plugintest4;

import org.eclipse.debug.core.ILaunch;
import org.eclipse.debug.core.ILaunchConfiguration;
import org.eclipse.debug.core.ILaunchConfigurationWorkingCopy;
import org.eclipse.debug.ui.ILaunchConfigurationDialog;
import org.eclipse.debug.ui.ILaunchConfigurationTab;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Display;

public class AnalysisTab implements ILaunchConfigurationTab {

	@Override
	public void activated(ILaunchConfigurationWorkingCopy arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public boolean canSave() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public void createControl(Composite arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void deactivated(ILaunchConfigurationWorkingCopy arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void dispose() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public Control getControl() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getErrorMessage() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Image getImage() {
		// return new Image(new Display(), "C:\\Users\\maike\\eclipse-workspace\\PluginTest4\\icons\\analysis.png");
		return null;
	}

	@Override
	public String getMessage() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getName() {
		return "Analysis";
	}

	@Override
	public void initializeFrom(ILaunchConfiguration arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public boolean isValid(ILaunchConfiguration arg0) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public void launched(ILaunch arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void performApply(ILaunchConfigurationWorkingCopy arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void setDefaults(ILaunchConfigurationWorkingCopy arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void setLaunchConfigurationDialog(ILaunchConfigurationDialog arg0) {
		// TODO Auto-generated method stub
		
	}

}
