package test;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.IResourceDelta;
import org.eclipse.core.resources.IResourceDeltaVisitor;
import org.eclipse.core.runtime.CoreException;

public class ResurceDeltaVisitorImpl implements IResourceDeltaVisitor
{

 private List<String> extensions = new ArrayList<String>();
 private boolean canUpdate;
 
 
 
 public List<String> getExtensions()
 {
  return extensions;
 }



 public void setExtensions(List<String> extensions)
 {
  this.extensions = extensions;
 }



 public boolean isCanUpdate()
 {
  return canUpdate;
 }



 public void setCanUpdate(boolean canUpdate)
 {
  this.canUpdate = canUpdate;
 }


 @Override
 public boolean visit(IResourceDelta delta) throws CoreException
 {
  canUpdate = Boolean.FALSE;
  
  // deal with IResourceDelta.CHANGED event
  if (delta.getKind() != IResourceDelta.CHANGED)
   return true;

  if ((delta.getFlags() & IResourceDelta.CONTENT) == 0)
   return true;

  IResource resource = delta.getResource();
  if (resource.getType() == IResource.FILE && extensions.contains(resource.getFileExtension()))
  {
   canUpdate = Boolean.TRUE;
  }

  return true;

 }

}
