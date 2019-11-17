/**
 * 
 */
package plugintest4;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.debug.core.ILaunch;
import org.eclipse.debug.core.ILaunchConfiguration;
import org.eclipse.debug.core.model.LaunchConfigurationDelegate;

/**
 * @author maike
 *
 */
public class AnalyzerDelegate extends LaunchConfigurationDelegate {

	@Override
	public void launch(ILaunchConfiguration arg0, String arg1, ILaunch arg2, IProgressMonitor arg3)
			throws CoreException {
		// TODO Auto-generated method stub
		System.out.println("hello");
		
	}

}
