package textAnalysis.core;

import org.eclipse.ui.plugin.AbstractUIPlugin;
import org.osgi.framework.Bundle;
import org.osgi.framework.BundleContext;
import org.osgi.framework.BundleActivator;

/**
 * The activator class controls the plug-in life cycle
 */
//public class Activator extends AbstractUIPlugin {
public class Activator implements BundleActivator {

	// The plug-in ID
	public static final String PLUGIN_ID = "PluginTest4"; //$NON-NLS-1$

	// The shared instance
	private static Activator plugin;
	
	/**
	 * The constructor
	 */
	public Activator() {
	}

	@Override
	public void start(BundleContext context) throws Exception {
		super.start(context);
		Bundle[] bundles = context.getBundles(); // TODO hier vielleicht schauen welche plugins installiert sind
		System.out.print("In Activator start: Bundles from Context length: ");
		System.out.println(bundles.length);
		plugin = this;
	}

	@Override
	public void stop(BundleContext context) throws Exception {
		plugin = null;
		super.stop(context);
	}

	/**
	 * Returns the shared instance
	 *
	 * @return the shared instance
	 */
	public static Activator getDefault() {
		return plugin;
	}

}
