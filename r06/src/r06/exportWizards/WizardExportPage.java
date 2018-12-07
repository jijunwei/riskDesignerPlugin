/*     */ package r06.exportWizards;
/*     */ 
/*     */ import java.util.ArrayList;
/*     */ import java.util.Arrays;
/*     */ import java.util.Iterator;
/*     */ import java.util.List;
/*     */ import java.util.StringTokenizer;
/*     */ import java.util.Vector;

/*     */ import org.eclipse.core.resources.IContainer;
/*     */ import org.eclipse.core.resources.IFile;
/*     */ import org.eclipse.core.resources.IProject;
/*     */ import org.eclipse.core.resources.IResource;
/*     */ import org.eclipse.core.resources.IWorkspace;
/*     */ import org.eclipse.core.resources.IWorkspaceRoot;
/*     */ import org.eclipse.core.resources.ResourcesPlugin;
/*     */ import org.eclipse.core.runtime.CoreException;
/*     */ import org.eclipse.core.runtime.IAdaptable;
/*     */ import org.eclipse.core.runtime.IPath;
/*     */ import org.eclipse.core.runtime.IStatus;
/*     */ import org.eclipse.jface.dialogs.IDialogSettings;
/*     */ import org.eclipse.jface.dialogs.MessageDialog;
/*     */ import org.eclipse.jface.viewers.ArrayContentProvider;
/*     */ import org.eclipse.jface.viewers.IStructuredSelection;
/*     */ import org.eclipse.jface.viewers.StructuredSelection;
/*     */ import org.eclipse.osgi.util.NLS;
/*     */ import org.eclipse.swt.layout.GridData;
/*     */ import org.eclipse.swt.layout.GridLayout;
/*     */ import org.eclipse.swt.widgets.Button;
/*     */ import org.eclipse.swt.widgets.Combo;
/*     */ import org.eclipse.swt.widgets.Composite;
/*     */ import org.eclipse.swt.widgets.Event;
/*     */ import org.eclipse.swt.widgets.Label;
/*     */ import org.eclipse.swt.widgets.Text;
/*     */ import org.eclipse.swt.widgets.Widget;
/*     */ import org.eclipse.ui.IFileEditorMapping;
/*     */ import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.dialogs.FileEditorMappingLabelProvider;
import org.eclipse.ui.dialogs.ListSelectionDialog;
import org.eclipse.ui.dialogs.ResourceSelectionDialog;
import org.eclipse.ui.dialogs.WizardDataTransferPage;
/*     */ import org.eclipse.ui.internal.ide.IDEWorkbenchMessages;
/*     */ import org.eclipse.ui.internal.ide.IDEWorkbenchPlugin;
import org.eclipse.ui.internal.ide.misc.ResourceAndContainerGroup;
/*     */ 
/*     */ @Deprecated
/*     */ public abstract class WizardExportPage extends WizardDataTransferPage
/*     */ {
/*     */   private IStructuredSelection currentResourceSelection;
/*     */   private List selectedResources;
/*     */   private List selectedTypes;
/*  88 */   private boolean exportCurrentSelection = false;
/*     */   private ResourceAndContainerGroup resourceGroup;
            private String initialFileName;
/*  90 */   private boolean exportAllResourcesPreSet = false;
/*     */   private Combo typesToExportField;
/*     */   private Button typesToExportEditButton;
/*     */   private Button exportAllTypesRadio;
/*     */   private Button exportSpecifiedTypesRadio;
/*     */   private Button resourceDetailsButton;
/*     */   private Label resourceDetailsDescription;
/*     */   private Text resourceNameField;
/*     */   private Button resourceBrowseButton;
/* 110 */   private boolean initialExportAllTypesValue = true;
/*     */   private String initialExportFieldValue;
/*     */   private String initialTypesFieldValue;
/*     */   private static final String CURRENT_SELECTION = "<current selection>";
/*     */   private static final String TYPE_DELIMITER = ",";
/*     */   private static final String STORE_SELECTED_TYPES_ID = "WizardFileSystemExportPage1.STORE_SELECTED_TYPES_ID.";
/*     */   private static final String STORE_EXPORT_ALL_RESOURCES_ID = "WizardFileSystemExportPage1.STORE_EXPORT_ALL_RESOURCES_ID.";
/*     */ 
/*     */   protected WizardExportPage(String pageName, IStructuredSelection selection)
/*     */   {
/* 135 */     super(pageName);
/* 136 */     this.currentResourceSelection = selection;
/*     */   }
/*     */ 
/*     */   protected boolean allowNewContainerName()
/*     */   {
/* 146 */     return false;
/*     */   }
/*     */ 
/*     */   public void createControl(Composite parent)
/*     */   {
/* 154 */     Composite composite = new Composite(parent, 0);
/* 155 */     composite.setLayout(new GridLayout());
/* 156 */     composite.setLayoutData(new GridData(272));
/*     */ 
/* 159 */     createBoldLabel(composite, IDEWorkbenchMessages.WizardExportPage_whatLabel);
/* 160 */     createSourceGroup(composite);
/*     */ 
/* 162 */     createSpacer(composite);
/*     */ 
/* 164 */     createBoldLabel(composite, IDEWorkbenchMessages.WizardExportPage_whereLabel);
/* 165 */     createDestinationGroup(composite);
/*     */ 
/* 167 */     createSpacer(composite);
/*     */ 
/* 169 */     createBoldLabel(composite, IDEWorkbenchMessages.WizardExportPage_options);
/* 170 */     createOptionsGroup(composite);
/*     */ 
/* 172 */     restoreResourceSpecificationWidgetValues();
/* 173 */     restoreWidgetValues();
/* 174 */     if (this.currentResourceSelection != null) {
/* 175 */       setupBasedOnInitialSelections();
/*     */     }
/*     */ 
/* 178 */     updateWidgetEnablements();
/* 179 */     setPageComplete(determinePageCompletion());
/*     */ 
/* 181 */     setControl(composite);
/*     */   }
/*     */ 
/*     */   protected abstract void createDestinationGroup(Composite paramComposite);
/*     */ 
/*     */   protected final void createSourceGroup(Composite parent)
/*     */   {
/* 201 */     Composite sourceGroup = new Composite(parent, 0);
/* 202 */     GridLayout layout = new GridLayout();
/* 203 */     layout.numColumns = 3;
/* 204 */     sourceGroup.setLayout(layout);
/* 205 */     sourceGroup.setLayoutData(new GridData(272));
/*     */ 
/* 209 */     new Label(sourceGroup, 0).setText(IDEWorkbenchMessages.WizardExportPage_folder);
/*     */ 
/* 212 */     this.resourceNameField = new Text(sourceGroup, 2052);
/* 213 */     this.resourceNameField.addListener(1, this);
/* 214 */     GridData data = new GridData(768);
/*     */ 
/* 216 */     data.widthHint = 250;
/* 217 */     this.resourceNameField.setLayoutData(data);
/*     */ 
/* 220 */     this.resourceBrowseButton = new Button(sourceGroup, 8);
/* 221 */     this.resourceBrowseButton.setText(IDEWorkbenchMessages.WizardExportPage_browse);
/* 222 */     this.resourceBrowseButton.addListener(13, this);
/* 223 */     this.resourceBrowseButton.setLayoutData(
/* 224 */       new GridData(768));
/*     */ 
/* 227 */     this.exportAllTypesRadio = new Button(sourceGroup, 16);
/* 228 */     this.exportAllTypesRadio.setText(IDEWorkbenchMessages.WizardExportPage_allTypes);
/* 229 */     this.exportAllTypesRadio.addListener(13, this);
/* 230 */     data = new GridData(768);
/*     */ 
/* 232 */     data.horizontalSpan = 3;
/* 233 */     this.exportAllTypesRadio.setLayoutData(data);
/*     */ 
/* 236 */     this.exportSpecifiedTypesRadio = new Button(sourceGroup, 16);
/* 237 */     this.exportSpecifiedTypesRadio.setText(IDEWorkbenchMessages.WizardExportPage_specificTypes);
/* 238 */     this.exportSpecifiedTypesRadio.addListener(13, this);
/*     */ 
/* 241 */     this.typesToExportField = new Combo(sourceGroup, 0);
/* 242 */     data = new GridData(768);
/*     */ 
/* 244 */     data.widthHint = 250;
/* 245 */     this.typesToExportField.setLayoutData(data);
/* 246 */     this.typesToExportField.addListener(24, this);
/*     */ 
/* 249 */     this.typesToExportEditButton = new Button(sourceGroup, 8);
/* 250 */     this.typesToExportEditButton.setText(IDEWorkbenchMessages.WizardExportPage_edit);
/* 251 */     this.typesToExportEditButton.setLayoutData(
/* 252 */       new GridData(776));
/*     */ 
/* 254 */     this.typesToExportEditButton.addListener(13, this);
/*     */ 
/* 257 */     this.resourceDetailsButton = new Button(sourceGroup, 8);
/* 258 */     this.resourceDetailsButton.setText(IDEWorkbenchMessages.WizardExportPage_details);
/* 259 */     this.resourceDetailsButton.addListener(13, this);
/*     */ 
/* 262 */     this.resourceDetailsDescription = new Label(sourceGroup, 0);
/* 263 */     data = new GridData(768);
/*     */ 
/* 265 */     data.horizontalSpan = 2;
/* 266 */     this.resourceDetailsDescription.setLayoutData(data);
/*     */ 
/* 269 */     resetSelectedResources();
/* 270 */     this.exportAllTypesRadio.setSelection(this.initialExportAllTypesValue);
/* 271 */     this.exportSpecifiedTypesRadio.setSelection(!this.initialExportAllTypesValue);
/* 272 */     this.typesToExportField.setEnabled(!this.initialExportAllTypesValue);
/* 273 */     this.typesToExportEditButton.setEnabled(!this.initialExportAllTypesValue);
/*     */ 
/* 275 */     if (this.initialExportFieldValue != null) {
/* 276 */       this.resourceNameField.setText(this.initialExportFieldValue);
/*     */     }
/* 278 */     if (this.initialTypesFieldValue != null)
/* 279 */       this.typesToExportField.setText(this.initialTypesFieldValue);
/*     */   }
/*     */ 
/*     */   protected void displayErrorDialog(String message)
/*     */   {
/* 290 */     MessageDialog.open(1, getContainer().getShell(), IDEWorkbenchMessages.WizardExportPage_errorDialogTitle, message, 268435456);
/*     */   }
/*     */ 
/*     */   protected void displayResourcesSelectedCount(int selectedResourceCount)
/*     */   {
/* 300 */     if (selectedResourceCount == 1)
/* 301 */       this.resourceDetailsDescription.setText(IDEWorkbenchMessages.WizardExportPage_oneResourceSelected);
/*     */     else
/* 303 */       this.resourceDetailsDescription
/* 304 */         .setText(NLS.bind(IDEWorkbenchMessages.WizardExportPage_resourceCountMessage, new Integer(selectedResourceCount)));
/*     */   }
/*     */ 
/*     */   @Deprecated
/*     */   protected boolean ensureResourcesLocal(List resources)
/*     */   {
/* 318 */     return true;
/*     */   }
/*     */ 
/*     */   protected List extractNonLocalResources(List originalList)
/*     */   {
/* 331 */     Vector result = new Vector(originalList.size());
/* 332 */     Iterator resourcesEnum = originalList.iterator();
/*     */ 
/* 334 */     while (resourcesEnum.hasNext()) {
/* 335 */       IResource currentResource = (IResource)resourcesEnum.next();
/* 336 */       if (!currentResource.isLocal(0)) {
/* 337 */         result.addElement(currentResource);
/*     */       }
/*     */     }
/*     */ 
/* 341 */     return result;
/*     */   }
/*     */ 
/*     */   public boolean getExportAllTypesValue()
/*     */   {
/* 352 */     if (this.exportAllTypesRadio == null) {
/* 353 */       return this.initialExportAllTypesValue;
/*     */     }
/*     */ 
/* 356 */     return this.exportAllTypesRadio.getSelection();
/*     */   }
/*     */ 
/*     */   public String getResourceFieldValue()
/*     */   {
/* 368 */     if (this.resourceNameField == null) {
/* 369 */       return this.initialExportFieldValue;
/*     */     }
/*     */ 
/* 372 */     return this.resourceNameField.getText();
/*     */   }
/*     */ 
/*     */   protected IPath getResourcePath()
/*     */   {
/* 380 */     return getPathFromText(this.resourceNameField);
/*     */   }
/*     */ 
/*     */   protected List getSelectedResources()
/*     */   {
/* 392 */     if (this.selectedResources == null) {
/* 393 */       IResource sourceResource = getSourceResource();
/*     */ 
/* 395 */       if (sourceResource != null) {
/* 396 */         selectAppropriateResources(sourceResource);
/*     */       }
/*     */     }
/*     */ 
/* 400 */     return this.selectedResources;
/*     */   }
/*     */ 
/*     */   protected IResource getSourceResource()
/*     */   {
/* 411 */     IWorkspace workspace = IDEWorkbenchPlugin.getPluginWorkspace();
/*     */ 
/* 413 */     IPath testPath = getResourcePath();
/*     */ 
/* 415 */     IStatus result = workspace.validatePath(testPath.toString(), 
/* 416 */       15);
/*     */ 
/* 419 */     if ((result.isOK()) && (workspace.getRoot().exists(testPath))) {
/* 420 */       return workspace.getRoot().findMember(testPath);
/*     */     }
/*     */ 
/* 423 */     return null;
/*     */   }
/*     */ 
/*     */   public String getTypesFieldValue()
/*     */   {
/* 434 */     if (this.typesToExportField == null) {
/* 435 */       return this.initialTypesFieldValue;
/*     */     }
/*     */ 
/* 438 */     return this.typesToExportField.getText();
/*     */   }
/*     */ 
/*     */   protected List getTypesToExport()
/*     */   {
/* 448 */     List result = new ArrayList();
/* 449 */     StringTokenizer tokenizer = new StringTokenizer(this.typesToExportField
/* 450 */       .getText(), ",");
/*     */ 
/* 452 */     while (tokenizer.hasMoreTokens()) {
/* 453 */       String currentExtension = tokenizer.nextToken().trim();
/* 454 */       if (!currentExtension.equals("")) {
/* 455 */         result.add(currentExtension);
/*     */       }
/*     */     }
/*     */ 
/* 459 */     return result;
/*     */   }
/*     */ 
/*     */   public void handleEvent(Event event)
/*     */   {
/* 469 */     Widget source = event.widget;
/*     */ 
/* 471 */     if ((source == this.exportAllTypesRadio) || (source == this.typesToExportField) || 
/* 472 */       (source == this.resourceNameField)) {
/* 473 */       resetSelectedResources();
/* 474 */     } else if (source == this.exportSpecifiedTypesRadio) {
/* 475 */       resetSelectedResources();
/* 476 */       this.typesToExportField.setFocus();
/* 477 */     } else if (source == this.resourceDetailsButton) {
/* 478 */       handleResourceDetailsButtonPressed();
/* 479 */     } else if (source == this.resourceBrowseButton) {
/* 480 */       handleResourceBrowseButtonPressed();
/* 481 */     } else if (source == this.typesToExportEditButton) {
/* 482 */       handleTypesEditButtonPressed();
/*     */     }
/*     */ 
/* 485 */     setPageComplete(determinePageCompletion());
/* 486 */     updateWidgetEnablements();
/*     */   }
/*     */ 
/*     */   protected void handleResourceBrowseButtonPressed()
/*     */   {
/* 494 */     IResource currentFolder = getSourceResource();
/* 495 */     if ((currentFolder != null) && (currentFolder.getType() == 1)) {
/* 496 */       currentFolder = currentFolder.getParent();
/*     */     }
/*     */ 
/* 499 */     IPath containerPath = queryForContainer((IContainer)currentFolder, 
/* 500 */       IDEWorkbenchMessages.WizardExportPage_selectResourcesToExport);
/* 501 */     if (containerPath != null) {
/* 502 */       String relativePath = containerPath.makeRelative().toString();
/* 503 */       if (!relativePath.toString().equals(this.resourceNameField.getText())) {
/* 504 */         resetSelectedResources();
/* 505 */         this.resourceNameField.setText(relativePath);
/*     */       }
/*     */     }
/*     */   }
/*     */ 
/*     */   protected void handleResourceDetailsButtonPressed()
/*     */   {
/* 515 */     IAdaptable source = getSourceResource();
/*     */ 
/* 517 */     if (source == null) {
/* 518 */       source = ResourcesPlugin.getWorkspace().getRoot();
/*     */     }
/*     */ 
/* 521 */     selectAppropriateResources(source);
/*     */ 
/* 523 */     if ((source instanceof IFile)) {
/* 524 */       source = ((IFile)source).getParent();
/* 525 */       setResourceToDisplay((IResource)source);
/*     */     }
/*     */ 
/* 528 */     Object[] newlySelectedResources = queryIndividualResourcesToExport(source);
/*     */ 
/* 530 */     if (newlySelectedResources != null) {
/* 531 */       this.selectedResources = Arrays.asList(newlySelectedResources);
/* 532 */       displayResourcesSelectedCount(this.selectedResources.size());
/*     */     }
/*     */   }
/*     */ 
/*     */   protected void handleTypesEditButtonPressed()
/*     */   {
/* 541 */     Object[] newSelectedTypes = queryResourceTypesToExport();
/*     */ 
/* 543 */     if (newSelectedTypes != null) {
/* 544 */       List result = new ArrayList(newSelectedTypes.length);
/* 545 */       for (int i = 0; i < newSelectedTypes.length; i++) {
/* 546 */         result.add(((IFileEditorMapping)newSelectedTypes[i])
/* 547 */           .getExtension());
/*     */       }
/* 549 */       setTypesToExport(result);
/*     */     }
/*     */   }
/*     */ 
/*     */   protected boolean hasExportableExtension(String resourceName)
/*     */   {
/* 562 */     if (this.selectedTypes == null) {
/* 563 */       return true;
/*     */     }
/*     */ 
/* 566 */     int separatorIndex = resourceName.lastIndexOf(".");
/* 567 */     if (separatorIndex == -1) {
/* 568 */       return false;
/*     */     }
/*     */ 
/* 571 */     String extension = resourceName.substring(separatorIndex + 1);
/*     */ 
/* 573 */     Iterator it = this.selectedTypes.iterator();
/* 574 */     while (it.hasNext()) {
/* 575 */       if (extension.equalsIgnoreCase((String)it.next())) {
/* 576 */         return true;
/*     */       }
/*     */     }
/*     */ 
/* 580 */     return false;
/*     */   }
/*     */ 
/*     */   protected void internalSaveWidgetValues()
/*     */   {
/*     */   }
/*     */ 
/*     */   protected Object[] queryIndividualResourcesToExport(IAdaptable rootResource)
/*     */   {
/* 604 */     ResourceSelectionDialog dialog = new ResourceSelectionDialog(
/* 605 */       getContainer().getShell(), rootResource, IDEWorkbenchMessages.WizardExportPage_selectResourcesTitle);
/* 606 */     dialog.setInitialSelections(this.selectedResources
/* 607 */       .toArray(new Object[this.selectedResources.size()]));
/* 608 */     dialog.open();
/* 609 */     return dialog.getResult();
/*     */   }
/*     */ 
/*     */   protected Object[] queryResourceTypesToExport()
/*     */   {
/* 621 */     IFileEditorMapping[] editorMappings = PlatformUI.getWorkbench()
/* 622 */       .getEditorRegistry().getFileEditorMappings();
/*     */ 
/* 624 */     int mappingsSize = editorMappings.length;
/* 625 */     List selectedTypes = getTypesToExport();
/* 626 */     List initialSelections = new ArrayList(selectedTypes.size());
/*     */ 
/* 628 */     for (int i = 0; i < mappingsSize; i++) {
/* 629 */       IFileEditorMapping currentMapping = editorMappings[i];
/* 630 */       if (selectedTypes.contains(currentMapping.getExtension())) {
/* 631 */         initialSelections.add(currentMapping);
/*     */       }
/*     */     }
/*     */ 
/* 635 */     ListSelectionDialog dialog = new ListSelectionDialog(getContainer()
/* 636 */       .getShell(), editorMappings, 
/* 637 */       ArrayContentProvider.getInstance(), 
/* 638 */       FileEditorMappingLabelProvider.INSTANCE, IDEWorkbenchMessages.WizardExportPage_selectionDialogMessage)
/*     */     {
/*     */       protected int getShellStyle() {
/* 641 */         return super.getShellStyle() | 0x10000000;
/*     */       }
/*     */     };
/* 645 */     dialog.setTitle(IDEWorkbenchMessages.WizardExportPage_resourceTypeDialog);
/* 646 */     dialog.open();
/*     */ 
/* 648 */     return dialog.getResult();
/*     */   }
/*     */ 
			public void setFileName(String value)
/*     */   {
/* 761 */     if (this.resourceGroup == null)
/* 762 */       this.initialFileName = value;
/*     */     else
/* 764 */       this.resourceGroup.setResource(value);
/*     */   }
/*     */   protected void resetSelectedResources()
/*     */   {
/* 656 */     this.resourceDetailsDescription.setText(IDEWorkbenchMessages.WizardExportPage_detailsMessage);
/* 657 */     this.selectedResources = null;
/*     */ 
/* 659 */     if (this.exportCurrentSelection) {
/* 660 */       this.exportCurrentSelection = false;
/*     */ 
/* 663 */       if (this.resourceNameField.getText().length() > "<current selection>"
/* 663 */         .length())
/* 664 */         this.resourceNameField.setText(this.resourceNameField.getText()
/* 665 */           .substring("<current selection>".length()));
/*     */       else
/* 667 */         this.resourceNameField.setText("");
/*     */     }
/*     */   }
/*     */ 
/*     */   protected void restoreResourceSpecificationWidgetValues()
/*     */   {
/* 678 */     IDialogSettings settings = getDialogSettings();
/* 679 */     if (settings != null) {
/* 680 */       String pageName = getName();
/* 681 */       boolean exportAllResources = settings
/* 682 */         .getBoolean("WizardFileSystemExportPage1.STORE_EXPORT_ALL_RESOURCES_ID." + pageName);
/*     */ 
/* 685 */       if (!this.exportAllResourcesPreSet) {
/* 686 */         this.exportAllTypesRadio.setSelection(exportAllResources);
/* 687 */         this.exportSpecifiedTypesRadio.setSelection(!exportAllResources);
/*     */       }
/*     */ 
/* 691 */       if (this.initialTypesFieldValue == null) {
/* 692 */         String[] selectedTypes = settings
/* 693 */           .getArray("WizardFileSystemExportPage1.STORE_SELECTED_TYPES_ID." + pageName);
/* 694 */         if (selectedTypes != null) {
/* 695 */           if (selectedTypes.length > 0) {
/* 696 */             this.typesToExportField.setText(selectedTypes[0]);
/*     */           }
/* 698 */           for (int i = 0; i < selectedTypes.length; i++)
/* 699 */             this.typesToExportField.add(selectedTypes[i]);
/*     */         }
/*     */       }
/*     */     }
/*     */   }
/*     */ 
/*     */   protected void saveWidgetValues()
/*     */   {
/* 714 */     IDialogSettings settings = getDialogSettings();
/* 715 */     if (settings != null) {
/* 716 */       String pageName = getName();
/*     */ 
/* 719 */       String[] selectedTypesNames = settings
/* 720 */         .getArray("WizardFileSystemExportPage1.STORE_SELECTED_TYPES_ID." + pageName);
/* 721 */       if (selectedTypesNames == null) {
/* 722 */         selectedTypesNames = new String[0];
/*     */       }
/*     */ 
/* 725 */       if (this.exportSpecifiedTypesRadio.getSelection()) {
/* 726 */         selectedTypesNames = addToHistory(selectedTypesNames, 
/* 727 */           this.typesToExportField.getText());
/*     */       }
/*     */ 
/* 730 */       settings
/* 731 */         .put("WizardFileSystemExportPage1.STORE_SELECTED_TYPES_ID." + pageName, selectedTypesNames);
/*     */ 
/* 734 */       settings.put("WizardFileSystemExportPage1.STORE_EXPORT_ALL_RESOURCES_ID." + pageName, 
/* 735 */         this.exportAllTypesRadio.getSelection());
/*     */     }
/*     */ 
/* 739 */     internalSaveWidgetValues();
/*     */   }
/*     */ 
/*     */   protected void selectAppropriateFolderContents(IContainer resource)
/*     */   {
/*     */     try
/*     */     {
/* 751 */       IResource[] members = resource.members();
/*     */ 
/* 753 */       for (int i = 0; i < members.length; i++) {
/* 754 */         if (members[i].getType() == 1) {
/* 755 */           IFile currentFile = (IFile)members[i];
/*     */ 
/* 757 */           if (hasExportableExtension(currentFile.getFullPath()
/* 757 */             .toString())) {
/* 758 */             this.selectedResources.add(currentFile);
/*     */           }
/*     */         }
/* 761 */         if (members[i].getType() == 2)
/* 762 */           selectAppropriateFolderContents((IContainer)members[i]);
/*     */       }
/*     */     }
/*     */     catch (CoreException localCoreException)
/*     */     {
/*     */     }
/*     */   }
/*     */ 
/*     */   protected void selectAppropriateResources(Object resource)
/*     */   {
/* 777 */     if (this.selectedResources == null)
/*     */     {
/* 779 */       if (this.exportSpecifiedTypesRadio.getSelection())
/* 780 */         this.selectedTypes = getTypesToExport();
/*     */       else {
/* 782 */         this.selectedTypes = null;
/*     */       }
/*     */ 
/* 785 */       this.selectedResources = new ArrayList();
/* 786 */       if ((resource instanceof IWorkspaceRoot)) {
/* 787 */         IProject[] projects = ((IWorkspaceRoot)resource).getProjects();
/* 788 */         for (int i = 0; i < projects.length; i++)
/* 789 */           selectAppropriateFolderContents(projects[i]);
/*     */       }
/* 791 */       else if ((resource instanceof IFile)) {
/* 792 */         IFile file = (IFile)resource;
/* 793 */         if (hasExportableExtension(file.getFullPath().toString()))
/* 794 */           this.selectedResources.add(file);
/*     */       }
/*     */       else {
/* 797 */         selectAppropriateFolderContents((IContainer)resource);
/*     */       }
/*     */     }
/*     */   }
/*     */ 
/*     */   public void setExportAllTypesValue(boolean value)
/*     */   {
/* 809 */     if (this.exportAllTypesRadio == null) {
/* 810 */       this.initialExportAllTypesValue = value;
/* 811 */       this.exportAllResourcesPreSet = true;
/*     */     } else {
/* 813 */       this.exportAllTypesRadio.setSelection(value);
/* 814 */       this.exportSpecifiedTypesRadio.setSelection(!value);
/*     */     }
/*     */   }
/*     */ 
/*     */   public void setResourceFieldValue(String value)
/*     */   {
/* 825 */     if (this.resourceNameField == null)
/* 826 */       this.initialExportFieldValue = value;
/*     */     else
/* 828 */       this.resourceNameField.setText(value);
/*     */   }
/*     */ 
/*     */   protected void setResourceToDisplay(IResource resource)
/*     */   {
/* 837 */     setResourceFieldValue(resource.getFullPath().makeRelative().toString());
/*     */   }
/*     */ 
/*     */   public void setTypesFieldValue(String value)
/*     */   {
/* 847 */     if (this.typesToExportField == null)
/* 848 */       this.initialTypesFieldValue = value;
/*     */     else
/* 850 */       this.typesToExportField.setText(value);
/*     */   }
/*     */ 
/*     */   protected void setTypesToExport(List typeStrings)
/*     */   {
/* 862 */     StringBuffer result = new StringBuffer();
/* 863 */     Iterator typesEnum = typeStrings.iterator();
/*     */ 
/* 865 */     while (typesEnum.hasNext()) {
/* 866 */       result.append(typesEnum.next());
/* 867 */       result.append(",");
/* 868 */       result.append(" ");
/*     */     }
/*     */ 
/* 871 */     this.typesToExportField.setText(result.toString());
/*     */   }
/*     */ 
/*     */   protected void setupBasedOnInitialSelections()
/*     */   {
/* 878 */     if (this.initialExportFieldValue != null)
/*     */     {
/* 881 */       IResource specifiedSourceResource = getSourceResource();
/* 882 */       if (specifiedSourceResource == null)
/* 883 */         this.currentResourceSelection = new StructuredSelection();
/*     */       else {
/* 885 */         this.currentResourceSelection = 
/* 886 */           new StructuredSelection(specifiedSourceResource);
/*     */       }
/*     */     }
/*     */ 
/* 890 */     if (this.currentResourceSelection.isEmpty()) {
/* 891 */       return;
/*     */     }
/*     */ 
/* 894 */     List selections = new ArrayList();
/* 895 */     Iterator it = this.currentResourceSelection.iterator();
/* 896 */     while (it.hasNext()) {
/* 897 */       IResource currentResource = (IResource)it.next();
/*     */ 
/* 899 */       if (currentResource.isAccessible()) {
/* 900 */         selections.add(currentResource);
/*     */       }
/*     */     }
/*     */ 
/* 904 */     if (selections.isEmpty()) {
/* 905 */       return;
/*     */     }
/*     */ 
/* 908 */     int selectedResourceCount = selections.size();
/* 909 */     if (selectedResourceCount == 1) {
/* 910 */       IResource resource = (IResource)selections.get(0);
/* 911 */       setResourceToDisplay(resource);
/*     */     } else {
/* 913 */       this.selectedResources = selections;
/* 914 */       this.exportAllTypesRadio.setSelection(true);
/* 915 */       this.exportSpecifiedTypesRadio.setSelection(false);
/* 916 */       this.resourceNameField.setText("<current selection>");
/* 917 */       this.exportCurrentSelection = true;
/* 918 */       displayResourcesSelectedCount(selectedResourceCount);
/*     */     }
/*     */   }
/*     */ 
/*     */   protected void updateWidgetEnablements()
/*     */   {
/* 927 */     if (this.exportCurrentSelection) {
/* 928 */       this.resourceDetailsButton.setEnabled(true);
/*     */     } else {
/* 930 */       IResource resource = getSourceResource();
/* 931 */       this.resourceDetailsButton.setEnabled((resource != null) && 
/* 932 */         (resource.isAccessible()));
/*     */     }
/*     */ 
/* 935 */     this.exportSpecifiedTypesRadio.setEnabled(!this.exportCurrentSelection);
/* 936 */     this.typesToExportField.setEnabled(this.exportSpecifiedTypesRadio.getSelection());
/* 937 */     this.typesToExportEditButton.setEnabled(this.exportSpecifiedTypesRadio
/* 938 */       .getSelection());
/*     */   }
/*     */ 
/*     */   protected final boolean validateSourceGroup()
/*     */   {
/* 943 */     if (this.exportCurrentSelection) {
/* 944 */       return true;
/*     */     }
/*     */ 
/* 947 */     String sourceString = this.resourceNameField.getText();
/* 948 */     if (sourceString.equals("")) {
/* 949 */       setErrorMessage(null);
/* 950 */       return false;
/*     */     }
/*     */ 
/* 953 */     IResource resource = getSourceResource();
/*     */ 
/* 955 */     if (resource == null) {
/* 956 */       setErrorMessage(IDEWorkbenchMessages.WizardExportPage_mustExistMessage);
/* 957 */       return false;
/*     */     }
/*     */ 
/* 960 */     if (!resource.isAccessible()) {
/* 961 */       setErrorMessage(IDEWorkbenchMessages.WizardExportPage_mustBeAccessibleMessage);
/* 962 */       return false;
/*     */     }
/*     */ 
/* 965 */     return true;
/*     */   }
/*     */ }

/* Location:           D:\soft\eclipse-jee-mars-R-win32-x86_64\eclipse\plugins\org.eclipse.ui.ide_3.11.0.v20150510-1749.jar
 * Qualified Name:     org.eclipse.ui.dialogs.WizardExportPage
 * JD-Core Version:    0.6.0
 */