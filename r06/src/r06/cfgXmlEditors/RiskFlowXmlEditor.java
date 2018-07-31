package r06.cfgXmlEditors;

import org.eclipse.ui.editors.text.TextEditor;

public class RiskFlowXmlEditor extends TextEditor {

	private ColorManager colorManager;

	public RiskFlowXmlEditor() {
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
