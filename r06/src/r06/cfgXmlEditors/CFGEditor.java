package r06.cfgXmlEditors;

import org.eclipse.ui.editors.text.TextEditor;

public class CFGEditor extends TextEditor {

	private ColorManager colorManager;

	public CFGEditor() {
		super();
		colorManager = new ColorManager();
		setSourceViewerConfiguration(new XMLConfiguration(colorManager));
		setDocumentProvider(new XMLDocumentProvider());
	}
	public void dispose() {
		colorManager.dispose();
		super.dispose();
	}

}
