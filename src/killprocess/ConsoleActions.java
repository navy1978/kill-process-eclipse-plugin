package killprocess;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.Platform;
import org.eclipse.debug.core.ILaunch;
import org.eclipse.debug.core.model.IProcess;
import org.eclipse.debug.core.model.RuntimeProcess;
import org.eclipse.debug.internal.ui.views.console.ProcessConsole;
import org.eclipse.debug.ui.IDebugUIConstants;
import org.eclipse.jface.action.Action;
import org.eclipse.jface.action.IToolBarManager;
import org.eclipse.jface.action.Separator;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.swt.program.Program;
import org.eclipse.ui.IActionBars;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.console.IConsole;
import org.eclipse.ui.console.IConsoleConstants;
import org.eclipse.ui.console.IConsolePageParticipant;
import org.eclipse.ui.part.IPageBookViewPage;
import org.eclipse.ui.part.IPageSite;

public class ConsoleActions implements IConsolePageParticipant {

  private IPageBookViewPage page;
  private Action stop;
  private IActionBars bars;
  private IConsole console;

  @Override
  public void init(final IPageBookViewPage page, final IConsole console) {
    this.console = console;
    this.page = page;
    IPageSite site = page.getSite();
    this.bars = site.getActionBars();

    createTerminateAllButton();
    bars.getMenuManager().add(new Separator());

    IToolBarManager toolbarManager = bars.getToolBarManager();
    toolbarManager.appendToGroup(IConsoleConstants.LAUNCH_GROUP, stop);
    bars.updateActionBars();
  }

  private void createTerminateAllButton() {
    ImageDescriptor imageDescriptor =
        ImageDescriptor.createFromFile(getClass(), "/icons/terminate.gif");
    this.stop =
        new Action("Kill Process", imageDescriptor) {
          public void run() {
            if (console instanceof ProcessConsole) {
              RuntimeProcess runtimeProcess =
                  (RuntimeProcess)
                      ((ProcessConsole) console)
                          .getAttribute(IDebugUIConstants.ATTR_CONSOLE_PROCESS);
              ILaunch launch = runtimeProcess.getLaunch();
              stopProcess(launch);
            }
          }
        };
  }

  public void stopProcess(ILaunch launch) {
    if (launch == null || launch.isTerminated()) {
      Activator.log("Nothing to do , is the process still running? ");
      return;
    }

    try {
      for (IProcess process : launch.getProcesses()) {
        long pid = getProcessId(process);
        if (pid != -1) {
          killProcess(pid);
        }
      }
    } catch (CoreException | InterruptedException e) {
      Activator.log(e);
      e.printStackTrace();
    }
  }

  private int getProcessId(IProcess process) throws CoreException {
    int pid = -1;
    if (process.canTerminate()) {
      String label = process.getLabel();
      int pidStartIndex = label.indexOf("[pid:");
      int pidEndIndex = label.indexOf("]", pidStartIndex);
      if (pidStartIndex != -1 && pidEndIndex != -1) {
        String pidString = label.substring(pidStartIndex + 5, pidEndIndex).trim();
        try {
          pid = Integer.parseInt(pidString);
        } catch (NumberFormatException e) {
          Activator.log(e);
          e.printStackTrace();
        }
      }
    }
    return pid;
  }

  private void killProcess(long pid) throws InterruptedException, CoreException {
    ProcessBuilder processBuilder = getProcessBuilder((int) pid);
    Process process;
    try {
      process = processBuilder.start();
      process.waitFor();
      int exitValue = process.exitValue();
      if (exitValue == 0) {
        Activator.log("Process with PID " + pid + " killed successfully.");
      } else {
        Activator.log("Failed to kill process with PID " + pid + ". Exit value: " + exitValue);
      }
    } catch (Exception e) {
      Activator.log(e);
      e.printStackTrace();
    }
  }

  private ProcessBuilder getProcessBuilder(int pid) {
    ProcessBuilder processBuilder = null;

    if (PlatformUI.isWorkbenchRunning()) { // Check if the workbench is running
      if (Platform.OS_WIN32.equals(Platform.getOS())) {
        processBuilder = new ProcessBuilder("taskkill", "/F", "/PID", String.valueOf(pid));
      } else {
        processBuilder = new ProcessBuilder("kill", "-9", String.valueOf(pid));
      }
    } else { // If workbench is not running, consider it as non-Windows platform
      if (Program.findProgram("taskkill") != null) {
        processBuilder = new ProcessBuilder("taskkill", "/F", "/PID", String.valueOf(pid));
      } else {
        processBuilder = new ProcessBuilder("kill", "-9", String.valueOf(pid));
      }
    }

    return processBuilder;
  }

  @Override
  public void dispose() {
    stop = null;
    bars = null;
    page = null;
  }

  @Override
  public Object getAdapter(Class adapter) {
    return null;
  }

  @Override
  public void activated() {
    updateVis();
  }

  @Override
  public void deactivated() {
    updateVis();
  }

  private void updateVis() {

    if (page == null) return;
    boolean isEnabled = true;
    stop.setEnabled(isEnabled);
    bars.updateActionBars();
  }
}
