package test;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IResourceChangeEvent;
import org.eclipse.core.resources.IResourceChangeListener;
import org.eclipse.core.resources.IResourceDelta;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.ui.IEditorInput;
import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.IFileEditorInput;
import org.eclipse.ui.IStartup;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.PlatformUI;

import r06.Activator;

public class AutoUpdateYear implements IStartup

{

    private static final int year = Calendar.getInstance().get(Calendar.YEAR);

    private static final String regEx = "Copyright\\s+\\(C\\):\\s+(\\d{4})?(\\s*-\\s*)?(\\d{4})?";

    private static Pattern p = null;

    private static final List<String> extensions = new ArrayList<String>();

    private ResurceDeltaVisitorImpl resourceDelta = new ResurceDeltaVisitorImpl();

   

 

    @Override

    public void earlyStartup()

    {

       p = Pattern.compile(regEx);

       extensions.add("java");

       extensions.add("js");

       extensions.add("jsp");

       resourceDelta.setExtensions(extensions);

       a();

    }

 

    private void a()

    {

       IResourceChangeListener resourceChangeListener = new IResourceChangeListener()

       {

 

           @Override

           public void resourceChanged(IResourceChangeEvent event)

           {

              try

              {

                  final IFile activeFile = getActiveFile();

                  if (activeFile == null)

                     return;

 

                  if (!extensions.contains(activeFile.getFileExtension()))

                  {

                     return;

                  }

 

                  IResourceDelta rootDelta = event.getDelta();

                  IResourceDelta findMember = rootDelta.findMember(activeFile.getFullPath());

                 

                  findMember.accept(resourceDelta);

                 

                  if (!resourceDelta.isCanUpdate())

                     return;

                 

                 

                  InputStream contents = activeFile.getContents();

                  InputStreamReader reader = new InputStreamReader(contents);

                  BufferedReader br = new BufferedReader(reader);

                  String s;

                  StringBuilder sb = new StringBuilder();

                  while ((s = br.readLine()) != null)

                  {

                     sb.append(s).append("\n");

 

                  }

                  String str = sb.toString();

                 

                  Matcher m = p.matcher(str);

                 

                  if(!m.find())

                     return;

                 

                  String group = m.group().trim();

                  int groupCount = m.groupCount();

                  String tmp;

                 

                  for (int i = 1; i <= groupCount; i++)

                  {

                     tmp = m.group(i);

                     if (tmp == null)

                     {

                         continue;

                     }

                     if ("-".equals(tmp))

                     {

                         continue;

                     }

                     if (Integer.valueOf(tmp) == year)

                     {

                         return;

                     }

                  }

                 

                  if (group.indexOf('-') > 0)

                  {

                     group = group.substring(0, group.indexOf('-'));

                  }

                  str = str.replaceAll(regEx, group + "-" + year);

                 

                 

                 

                  InputStream inputStream = new ByteArrayInputStream(str.getBytes());

                  run(inputStream, activeFile, str, this);

              }

              catch (Exception e)

              {

                  e.printStackTrace();

              }

           }

 

       };

 

       // add resourceChangeListener

       ResourcesPlugin.getWorkspace().addResourceChangeListener(resourceChangeListener);

    }

 

    private void run(final InputStream inputStream, final IFile activeFile, final String sb,

           final IResourceChangeListener resourceChangeListener)

    {

       Activator.getDefault().getWorkbench().getDisplay().asyncExec(new Runnable()

       {

           @SuppressWarnings("restriction")

           public void run()

           {

              //final IWorkbench workbench = PlatformUI.getWorkbench();

              //IWorkbenchWindow window = workbench.getActiveWorkbenchWindow();

 

              IProgressMonitor nullMonitor = org.eclipse.core.internal.utils.Policy.monitorFor(null);

              // ResourcesPlugin.getWorkspace().copy(airesource, ipath, flag, iprogressmonitor)

              try

              {

                  // remove listener, because setContents will notify listener

 

                  //ResourcesPlugin.getWorkspace().removeResourceChangeListener(resourceChangeListener);

                  activeFile.setContents(inputStream, sb.length(), nullMonitor);

 

                  //activeFile.appendContents(inputStream, IResource.FORCE, nullMonitor);

 

              }

              catch (CoreException e)

              {

                  e.printStackTrace();

              }

              finally{

                  try

                  {

                     if (inputStream != null)

                     {

                         inputStream.close();

                     }

                  }

                  catch (IOException e)

                  {

                     e.printStackTrace();

                  }

              }

              // after change the code, and then add the Listener

               //ResourcesPlugin.getWorkspace().addResourceChangeListener(resourceChangeListener);

           }

       });

    }

 

    //

    public static IWorkbenchWindow getActiveWindow()

    {

       IWorkbench workbench = PlatformUI.getWorkbench();

       if (workbench != null)

       {

 

           return workbench.getActiveWorkbenchWindow();

       }

       return null;

    }

 

    public static IWorkbenchPage getActivePage()

    {

       IWorkbenchWindow window = getActiveWindow();

       if (window != null)

       {

           return window.getActivePage();

       }

       return null;

    }

 

    public static IEditorPart getActiveEditor()

    {

       IWorkbenchPage page = getActivePage();

       if (page != null)

       {

           return page.getActiveEditor();

       }

       return null;

    }

 

    public static IFile getActiveFile()

    {

       IEditorPart editorPart = getActiveEditor();

      

       if (editorPart != null)

       {

           IEditorInput input = editorPart.getEditorInput();

           if (input instanceof IFileEditorInput)

           {

              IFileEditorInput fileEditorInput = (IFileEditorInput) editorPart.getEditorInput();

              if (fileEditorInput != null)

              {

                  IFile file = fileEditorInput.getFile();

                  return file;

              }

           }

       }

       return null;

    }

 

}
