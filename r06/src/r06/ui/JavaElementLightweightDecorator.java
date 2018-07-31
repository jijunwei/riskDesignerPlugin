/*     */ package r06.ui;
/*     */ 
/*     */ import java.util.HashSet;
/*     */ import java.util.Set;
/*     */ import org.eclipse.core.filebuffers.FileBuffers;
/*     */ import org.eclipse.core.filebuffers.IFileBuffer;
/*     */ import org.eclipse.core.filebuffers.IFileBufferListener;
/*     */ import org.eclipse.core.filebuffers.ITextFileBufferManager;
/*     */ import org.eclipse.core.filebuffers.LocationKind;
/*     */ import org.eclipse.core.resources.IFile;
/*     */ import org.eclipse.core.resources.IResource;
/*     */ import org.eclipse.core.runtime.IPath;
/*     */ import org.eclipse.core.runtime.IProgressMonitor;
/*     */ import org.eclipse.core.runtime.IStatus;
/*     */ import org.eclipse.core.runtime.Status;
/*     */ import org.eclipse.jdt.core.IClassFile;
/*     */ import org.eclipse.jdt.core.ICompilationUnit;
/*     */ import org.eclipse.jdt.core.IJavaElement;
/*     */ import org.eclipse.jface.resource.FontRegistry;
/*     */ import org.eclipse.jface.viewers.IDecoration;
/*     */ import org.eclipse.jface.viewers.ILightweightLabelDecorator;
/*     */ import org.eclipse.jface.viewers.LabelProvider;
/*     */ import org.eclipse.jface.viewers.LabelProviderChangedEvent;
/*     */ import org.eclipse.swt.graphics.Color;
/*     */ import org.eclipse.swt.graphics.Font;
/*     */ import org.eclipse.swt.widgets.Display;
/*     */ import org.eclipse.ui.IWorkbench;
/*     */ import org.eclipse.ui.PlatformUI;
/*     */ import org.eclipse.ui.progress.UIJob;
/*     */ import org.eclipse.ui.themes.ITheme;
/*     */ import org.eclipse.ui.themes.IThemeManager;
/*     */ 
/*     */ public class JavaElementLightweightDecorator extends LabelProvider
/*     */   implements ILightweightLabelDecorator
/*     */ {
/*     */   private Color fColor;
/*     */   private Font fBold;
/*     */   private FileBufferListener fListener;
/*     */   private UIJob fNotifierJob;
/*     */   private Set fChangedResources;
/*     */ 
/*     */   public JavaElementLightweightDecorator()
/*     */   {
/* 126 */     FontRegistry fontRegistry = PlatformUI.getWorkbench().getThemeManager().getCurrentTheme().getFontRegistry();
/* 127 */     Display.getDefault().syncExec(null);
/* 133 */     this.fListener = new FileBufferListener();
/* 134 */     FileBuffers.getTextFileBufferManager().addFileBufferListener(this.fListener);
/*     */ 
/* 136 */     this.fChangedResources = new HashSet();
/*     */   }
/*     */ 
/*     */   public void decorate(Object element, IDecoration decoration)
/*     */   {
/* 143 */     IPath path = null;
/*     */ 
/* 145 */     if ((element instanceof IResource))
/* 146 */       path = ((IResource)element).getFullPath();
/* 147 */     else if (((element instanceof IClassFile)) || ((element instanceof ICompilationUnit))) {
/* 148 */       path = ((IJavaElement)element).getPath();
/*     */     }
/* 150 */     if ((path != null) && 
/* 151 */       (FileBuffers.getTextFileBufferManager().getFileBuffer(path, LocationKind.NORMALIZE) != null))
/* 152 */       decoration.setFont(this.fBold);
/*     */   }
/*     */ 
/*     */   private void update(IPath location)
/*     */   {
/* 158 */     IFile file = FileBuffers.getWorkspaceFileAtLocation(location);
/* 159 */     if (file != null) {
/* 160 */       boolean hasChanges = false;
/* 161 */       synchronized (this) {
/* 162 */         hasChanges = this.fChangedResources.add(file);
/*     */       }
/* 164 */       if (hasChanges) {
/* 165 */         if (this.fNotifierJob == null) {
/* 166 */           this.fNotifierJob = new UIJob(Display.getDefault(), "Update Java test decorations") {
/*     */             public IStatus runInUIThread(IProgressMonitor monitor) {
/* 168 */               JavaElementLightweightDecorator.this.runPendingUpdates();
/* 169 */               return Status.OK_STATUS;
/*     */             }
/*     */           };
/* 172 */           this.fNotifierJob.setSystem(true);
/*     */         }
/* 174 */         this.fNotifierJob.schedule();
/*     */       }
/*     */     }
/*     */   }
/*     */ 
/*     */   private void runPendingUpdates() {
/* 180 */     Object[] resourceToUpdate = null;
/* 181 */     synchronized (this) {
/* 182 */       resourceToUpdate = this.fChangedResources.toArray();
/* 183 */       this.fChangedResources.clear();
/*     */     }
/* 185 */     if (resourceToUpdate.length > 0) {
/* 186 */       LabelProviderChangedEvent event = new LabelProviderChangedEvent(this, resourceToUpdate);
/* 187 */       fireLabelProviderChanged(event);
/*     */     }
/*     */   }
/*     */ 
/*     */   public void dispose()
/*     */   {
/* 195 */     this.fColor.dispose();
/* 196 */     FileBuffers.getTextFileBufferManager().removeFileBufferListener(this.fListener);
/*     */   }
/*     */ 
/*     */   private class FileBufferListener
/*     */     implements IFileBufferListener
/*     */   {
/*     */     private FileBufferListener()
/*     */     {
/*     */     }
/*     */ 
/*     */     public void bufferCreated(IFileBuffer buffer)
/*     */     {
/*  57 */       if (buffer.getLocation() != null)
/*  58 */         JavaElementLightweightDecorator.this.update(buffer.getLocation());
/*     */     }
/*     */ 
/*     */     public void bufferDisposed(IFileBuffer buffer)
/*     */     {
/*  65 */       if (buffer.getLocation() != null)
/*  66 */         JavaElementLightweightDecorator.this.update(buffer.getLocation());
/*     */     }
/*     */ 
/*     */     public void bufferContentAboutToBeReplaced(IFileBuffer buffer)
/*     */     {
/*     */     }
/*     */ 
/*     */     public void bufferContentReplaced(IFileBuffer buffer)
/*     */     {
/*     */     }
/*     */ 
/*     */     public void stateChanging(IFileBuffer buffer)
/*     */     {
/*     */     }
/*     */ 
/*     */     public void dirtyStateChanged(IFileBuffer buffer, boolean isDirty)
/*     */     {
/*     */     }
/*     */ 
/*     */     public void stateValidationChanged(IFileBuffer buffer, boolean isStateValidated)
/*     */     {
/*     */     }
/*     */ 
/*     */     public void underlyingFileMoved(IFileBuffer buffer, IPath path)
/*     */     {
/*     */     }
/*     */ 
/*     */     public void underlyingFileDeleted(IFileBuffer buffer)
/*     */     {
/*     */     }
/*     */ 
/*     */     public void stateChangeFailed(IFileBuffer buffer)
/*     */     {
/*     */     }
/*     */   }
/*     */ }

/* Location:           C:\Users\jjw8610\Desktop\EclipsePlugin\org.eclipse.jdt.ui.tests_3.10.101.v20151123-1510\
 * Qualified Name:     org.eclipse.jdt.ui.examples.JavaElementLightweightDecorator
 * JD-Core Version:    0.6.0
 */