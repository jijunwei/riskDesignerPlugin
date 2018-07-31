package r06.perspectives;

import org.eclipse.ui.IFolderLayout;
import org.eclipse.ui.IPageLayout;
import org.eclipse.ui.IPerspectiveFactory;
import org.eclipse.ui.console.IConsoleConstants;
import org.eclipse.jdt.ui.JavaUI;


/**
 *  This class is meant to serve as an example for how various contributions 
 *  are made to a perspective. Note that some of the extension point id's are
 *  referred to as API constants while others are hardcoded and may be subject 
 *  to change. 
 */
public class RiskDesignerPerspective implements IPerspectiveFactory {

	private IPageLayout factory;

	public RiskDesignerPerspective() {
		super();
	}

	public void createInitialLayout(IPageLayout factory) {
		this.factory = factory;
		addViews();
		addActionSets();
		addNewWizardShortcuts();
		addPerspectiveShortcuts();
		addViewShortcuts();
	}

	private void addViews() {
		// Creates the overall folder layout. 
		// Note that each new Folder uses a percentage of the remaining EditorArea.
		
		IFolderLayout bottom =
			factory.createFolder(
				"bottomRight", //NON-NLS-1
				IPageLayout.BOTTOM,
				0.75f,
				factory.getEditorArea());
		bottom.addView(IPageLayout.ID_PROBLEM_VIEW);
		bottom.addView("org.eclipse.team.ui.GenericHistoryView"); //NON-NLS-1
		bottom.addPlaceholder(IConsoleConstants.ID_CONSOLE_VIEW);
		bottom.addPlaceholder("org.eclipse.ui.views.ProgressView");
		bottom.addPlaceholder("org.eclipse.search.ui.views.SearchView");

		IFolderLayout topLeft =
			factory.createFolder(
				"topLeft", //NON-NLS-1
				IPageLayout.LEFT,
				0.25f,
				factory.getEditorArea());
		topLeft.addView(IPageLayout.ID_RES_NAV);
		topLeft.addView("org.eclipse.jdt.junit.ResultView"); //NON-NLS-1
		
		factory.addFastView("org.eclipse.team.ccvs.ui.RepositoriesView",0.50f); //NON-NLS-1
		factory.addFastView("org.eclipse.team.sync.views.SynchronizeView", 0.50f); //NON-NLS-1
	}

	private void addActionSets() {
		factory.addActionSet("org.eclipse.debug.ui.launchActionSet"); //NON-NLS-1
		factory.addActionSet("org.eclipse.debug.ui.debugActionSet"); //NON-NLS-1
		factory.addActionSet("org.eclipse.debug.ui.profileActionSet"); //NON-NLS-1
		factory.addActionSet("org.eclipse.jdt.debug.ui.JDTDebugActionSet"); //NON-NLS-1
		factory.addActionSet("org.eclipse.jdt.junit.JUnitActionSet"); //NON-NLS-1
		factory.addActionSet("org.eclipse.team.ui.actionSet"); //NON-NLS-1
		factory.addActionSet("org.eclipse.team.cvs.ui.CVSActionSet"); //NON-NLS-1
		factory.addActionSet("org.eclipse.ant.ui.actionSet.presentation"); //NON-NLS-1
		factory.addActionSet(JavaUI.ID_ACTION_SET);
		//factory.addActionSet("org.eclipse.jdt.ui.JavaActionSet");
		//factory.addActionSet("org.eclipse.jdt.ui.JavaElementCreationActionSet");
		factory.addActionSet(JavaUI.ID_ELEMENT_CREATION_ACTION_SET);
		factory.addActionSet(IPageLayout.ID_NAVIGATE_ACTION_SET); //NON-NLS-1
	}

	private void addPerspectiveShortcuts() {
		factory.addPerspectiveShortcut("org.eclipse.team.ui.TeamSynchronizingPerspective"); //NON-NLS-1
		factory.addPerspectiveShortcut("org.eclipse.team.cvs.ui.cvsPerspective"); //NON-NLS-1
		factory.addPerspectiveShortcut("org.eclipse.ui.resourcePerspective"); //NON-NLS-1
		factory.addPerspectiveShortcut("org.eclipse.jdt.ui.JavaPerspective"); //NON-NLS-1
		factory.addPerspectiveShortcut(JavaUI.ID_BROWSING_PERSPECTIVE);
		factory.addPerspectiveShortcut("org.eclipse.debug.ui.DebugPerspective");
		
	}

	private void addNewWizardShortcuts() {
		factory.addNewWizardShortcut("org.eclipse.team.cvs.ui.newProjectCheckout");//NON-NLS-1
		factory.addNewWizardShortcut("org.eclipse.ui.wizards.new.folder");//NON-NLS-1
		factory.addNewWizardShortcut("org.eclipse.ui.wizards.new.file");//NON-NLS-1
		factory.addNewWizardShortcut("org.eclipse.jdt.ui.wizards.JavaProjectWizard");
		factory.addNewWizardShortcut("org.eclipse.jdt.ui.wizards.NewPackageCreationWizard");
		factory.addNewWizardShortcut("org.eclipse.jdt.ui.wizards.NewClassCreationWizard");
		factory.addNewWizardShortcut("org.eclipse.jdt.ui.wizards.NewInterfaceCreationWizard");
		factory.addNewWizardShortcut("org.eclipse.jdt.ui.wizards.NewEnumCreationWizard");
		factory.addNewWizardShortcut("org.eclipse.jdt.ui.wizards.NewAnnotationCreationWizard");
		factory.addNewWizardShortcut("org.eclipse.jdt.ui.wizards.NewSourceFolderCreationWizard");
		factory.addNewWizardShortcut("org.eclipse.jdt.ui.wizards.NewSnippetFileCreationWizard");
		factory.addNewWizardShortcut("org.eclipse.jdt.ui.wizards.NewJavaWorkingSetWizard");
		
		factory.addNewWizardShortcut("org.eclipse.ui.editors.wizards.UntitledTextFileWizard");
	}

	private void addViewShortcuts() {
		factory.addShowViewShortcut("org.eclipse.ant.ui.views.AntView"); //NON-NLS-1
		factory.addShowViewShortcut("org.eclipse.team.ccvs.ui.AnnotateView"); //NON-NLS-1
		factory.addShowViewShortcut("org.eclipse.pde.ui.DependenciesView"); //NON-NLS-1
		factory.addShowViewShortcut("org.eclipse.jdt.junit.ResultView"); //NON-NLS-1
		factory.addShowViewShortcut("org.eclipse.team.ui.GenericHistoryView"); //NON-NLS-1
		factory.addShowViewShortcut(IConsoleConstants.ID_CONSOLE_VIEW);
		factory.addShowViewShortcut(JavaUI.ID_PACKAGES);
		factory.addShowViewShortcut(IPageLayout.ID_RES_NAV);
		factory.addShowViewShortcut(IPageLayout.ID_PROBLEM_VIEW);
		factory.addShowViewShortcut(IPageLayout.ID_OUTLINE);
		
		//factory.addShowViewShortcut("org.eclipse.jdt.ui.PackageExplorer");
		factory.addShowViewShortcut("org.eclipse.jdt.ui.TypeHierarchy");
		factory.addShowViewShortcut("org.eclipse.jdt.ui.SourceView");
		factory.addShowViewShortcut("org.eclipse.jdt.ui.JavadocView");

		factory.addShowViewShortcut("org.eclipse.search.ui.views.SearchView");

		//factory.addShowViewShortcut("org.eclipse.ui.console.ConsoleView");

		factory.addShowViewShortcut("org.eclipse.ui.views.ContentOutline");
		//factory.addShowViewShortcut("org.eclipse.ui.views.ProblemView");
		factory.addShowViewShortcut("org.eclipse.ui.views.ResourceNavigator");
		factory.addShowViewShortcut("org.eclipse.ui.views.TaskList");
		factory.addShowViewShortcut("org.eclipse.ui.views.ProgressView");
		factory.addShowViewShortcut("org.eclipse.ui.navigator.ProjectExplorer");
		factory.addShowViewShortcut("org.eclipse.ui.texteditor.TemplatesView");
		factory.addShowViewShortcut("org.eclipse.pde.runtime.LogView");
	}

}
