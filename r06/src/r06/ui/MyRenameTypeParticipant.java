/*     */ package r06.ui;
/*     */ 
/*     */ import java.util.Collection;
/*     */ import java.util.HashMap;
/*     */ import java.util.Iterator;
import java.util.Map;
/*     */ import java.util.regex.Pattern;
/*     */ import org.eclipse.core.resources.IFile;
/*     */ import org.eclipse.core.resources.IResource;
/*     */ import org.eclipse.core.runtime.CoreException;
/*     */ import org.eclipse.core.runtime.IProgressMonitor;
/*     */ import org.eclipse.jdt.core.IJavaProject;
/*     */ import org.eclipse.jdt.core.IType;
/*     */ import org.eclipse.ltk.core.refactoring.Change;
/*     */ import org.eclipse.ltk.core.refactoring.CompositeChange;
/*     */ import org.eclipse.ltk.core.refactoring.RefactoringStatus;
/*     */ import org.eclipse.ltk.core.refactoring.TextChange;
/*     */ import org.eclipse.ltk.core.refactoring.TextFileChange;
/*     */ import org.eclipse.ltk.core.refactoring.participants.CheckConditionsContext;
/*     */ import org.eclipse.ltk.core.refactoring.participants.RenameArguments;
/*     */ import org.eclipse.ltk.core.refactoring.participants.RenameParticipant;
/*     */ import org.eclipse.search.core.text.TextSearchEngine;
/*     */ import org.eclipse.search.core.text.TextSearchMatchAccess;
/*     */ import org.eclipse.search.core.text.TextSearchRequestor;
/*     */ import org.eclipse.search.ui.text.FileTextSearchScope;
import org.eclipse.swt.widgets.List;
/*     */ import org.eclipse.text.edits.MultiTextEdit;
/*     */ import org.eclipse.text.edits.ReplaceEdit;
/*     */ import org.eclipse.text.edits.TextEditGroup;
/*     */ 
/*     */ public class MyRenameTypeParticipant extends RenameParticipant
/*     */ {
/*     */   private IType fType;
/*     */ 
/*     */   protected boolean initialize(Object element)
/*     */   {
/*  76 */     this.fType = ((IType)element);
/*  77 */     return true;
/*     */   }
/*     */ 
/*     */   public String getName()
/*     */   {
/*  84 */     return "My special file participant";
/*     */   }
/*     */ 
/*     */   public RefactoringStatus checkConditions(IProgressMonitor pm, CheckConditionsContext context)
/*     */   {
/*  91 */     return new RefactoringStatus();
/*     */   }
/*     */ 
/*     */   public Change createChange(IProgressMonitor pm)
/*     */     throws CoreException
/*     */   {
/*  99 */     HashMap changes = new HashMap();
/* 100 */     String newName = getArguments().getNewName();
/*     */ 
/* 105 */     IResource[] roots = { this.fType.getJavaProject().getProject() };
/* 106 */     String[] fileNamePatterns = { "*.special" };
/* 107 */     FileTextSearchScope scope = FileTextSearchScope.newSearchScope(roots, fileNamePatterns, false);
/* 108 */     Pattern pattern = Pattern.compile(this.fType.getElementName());
/*     */ 
/* 110 */     TextSearchRequestor collector = new TextSearchRequestor() {
              private Map<Object,Object> val$changes;
			  private String val$newName;

/*     */       public boolean acceptPatternMatch(TextSearchMatchAccess matchAccess) throws CoreException {
/* 112 */         IFile file = matchAccess.getFile();
/* 113 */         TextFileChange change = (TextFileChange)this.val$changes.get(file);
/* 114 */         if (change == null) {
/* 115 */           TextChange textChange = MyRenameTypeParticipant.this.getTextChange(file);
/* 116 */           if (textChange != null) {
/* 117 */             return false;
/*     */           }
/* 119 */           change = new TextFileChange(file.getName(), file);
/* 120 */           change.setEdit(new MultiTextEdit());
/* 121 */           this.val$changes.put(file, change);
/*     */         }
/* 123 */         ReplaceEdit edit = new ReplaceEdit(matchAccess.getMatchOffset(), matchAccess.getMatchLength(), this.val$newName);
/* 124 */         change.addEdit(edit);
/* 125 */         change.addTextEditGroup(new TextEditGroup("Update type reference", edit));
/* 126 */         return true;
/*     */       }
/*     */     };
/* 129 */     TextSearchEngine.create().search(scope, collector, pattern, pm);
/*     */ 
/* 131 */     if (changes.isEmpty()) {
/* 132 */       return null;
/*     */     }
/* 134 */     CompositeChange result = new CompositeChange("My special file updates");
/* 135 */     for (Iterator iter = changes.values().iterator(); iter.hasNext(); ) {
/* 136 */       result.add((Change)iter.next());
/*     */     }
/* 138 */     return result;
/*     */   }
/*     */ }

/* Location:           C:\Users\jjw8610\Desktop\EclipsePlugin\org.eclipse.jdt.ui.tests_3.10.101.v20151123-1510\
 * Qualified Name:     org.eclipse.jdt.ui.examples.MyRenameTypeParticipant
 * JD-Core Version:    0.6.0
 */