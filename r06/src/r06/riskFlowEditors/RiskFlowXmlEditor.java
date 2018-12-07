package r06.riskFlowEditors;

import org.eclipse.ui.editors.text.TextEditor;

import r06.cfgXmlEditors.ColorManager;
import r06.cfgXmlEditors.XMLConfiguration;
import r06.cfgXmlEditors.XMLDocumentProvider;

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
