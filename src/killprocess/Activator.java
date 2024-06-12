package killprocess;

import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.ui.plugin.AbstractUIPlugin;
import org.osgi.framework.BundleContext;

/** The activator class controls the plug-in life cycle */
public class Activator extends AbstractUIPlugin {

  // The plug-in ID
  public static final String PLUGIN_ID = "KillProcess"; // $NON-NLS-1$

  // The shared instance
  private static Activator plugin;

  /** The constructor */
  public Activator() {}

  @Override
  public void start(BundleContext context) throws Exception {
    super.start(context);
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

  public static String getId() {
    return PLUGIN_ID;
  }

  public static void log(Throwable e) {
    log(new Status(IStatus.ERROR, getId(), IStatus.ERROR, "Error", e)); // $NON-NLS-1$
  }

  public static void log(String e) {
    log(new Status(IStatus.INFO, getId(), e));
  }

  public static void log(IStatus status) {
    getDefault().getLog().log(status);
  }
}
