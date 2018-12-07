package r06;

import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.jface.text.rules.RuleBasedScanner;
import org.eclipse.ui.plugin.AbstractUIPlugin;
import org.osgi.framework.BundleContext;

import r06.drlEditor.JavaPartitionScanner;
import r06.drlEditor.java.JavaCodeScanner;
import r06.drlEditor.javadoc.JavaDocScanner;
import r06.drlEditor.util.JavaColorProvider;

/**
 * The activator class controls the plug-in life cycle
 */
public class Activator extends AbstractUIPlugin {
	// The plug-in ID
	public static final String PLUGIN_ID = "r06"; //$NON-NLS-1$

	// The shared instance
	private static Activator plugin;
	
	public final static String JAVA_PARTITIONING= "__java_partitioning"; //$NON-NLS-1$


	private JavaPartitionScanner fPartitionScanner;

	private JavaColorProvider fColorProvider;

	private JavaCodeScanner fCodeScanner;

	private JavaDocScanner fDocScanner;
	
	/**
	 * The constructor
	 */
	public Activator() {
		
	}

	/*
	 * (non-Javadoc)
	 * @see org.eclipse.ui.plugin.AbstractUIPlugin#start(org.osgi.framework.BundleContext)
	 */
	public void start(BundleContext context) throws Exception {
		super.start(context);
		plugin = this;
	}

	/*
	 * (non-Javadoc)
	 * @see org.eclipse.ui.plugin.AbstractUIPlugin#stop(org.osgi.framework.BundleContext)
	 */
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

	/**
	 * Returns an image descriptor for the image file at the given
	 * plug-in relative path
	 *
	 * @param path the path
	 * @return the image descriptor
	 */
	public static ImageDescriptor getImageDescriptor(String path) {
		return imageDescriptorFromPlugin(PLUGIN_ID, path);
	}
	
	/**
	 * Return a scanner for creating Java partitions.
	 * 
	 * @return a scanner for creating Java partitions
	 */
	public JavaPartitionScanner getJavaPartitionScanner() {
		if (fPartitionScanner == null)
			fPartitionScanner= new JavaPartitionScanner();
		return fPartitionScanner;
	}

	/**
	 * Returns the singleton Java code scanner.
	 * 
	 * @return the singleton Java code scanner
	 */
	public RuleBasedScanner getJavaCodeScanner() {
		if (fCodeScanner == null)
			fCodeScanner= new JavaCodeScanner(getJavaColorProvider());
		return fCodeScanner;
	}

	/**
	 * Returns the singleton Java color provider.
	 * 
	 * @return the singleton Java color provider
	 */
	public JavaColorProvider getJavaColorProvider() {
		if (fColorProvider == null)
			fColorProvider= new JavaColorProvider();
		return fColorProvider;
	}

	/**
	 * Returns the singleton Javadoc scanner.
	 * 
	 * @return the singleton Javadoc scanner
	 */
	public RuleBasedScanner getJavaDocScanner() {
		if (fDocScanner == null)
			fDocScanner= new JavaDocScanner(fColorProvider);
		return fDocScanner;
	}

	public static void log(IStatus status) {
		getDefault().getLog().log(status);
	}

	public static void log(Throwable e) {
		log(new Status(IStatus.ERROR, PLUGIN_ID, 0, "Plugin: internal error", e)); //$NON-NLS-1$
	}
}
