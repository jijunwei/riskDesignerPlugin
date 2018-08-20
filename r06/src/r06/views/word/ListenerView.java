package r06.views.word;

import org.eclipse.jface.viewers.*;
import org.eclipse.swt.widgets.*;
import org.eclipse.ui.*;
import org.eclipse.ui.part.ViewPart;

public class ListenerView extends ViewPart 
	implements ISelectionListener
{
	private Label label;
	public ListenerView() {
		super();
	}
	public void setFocus() {
		label.setFocus();
	}
	public void createPartControl(Composite parent) {
		label = new Label(parent, 0);
		label.setText("Hello World");
		getViewSite().getPage().addSelectionListener(this);
	}

	/**
	 * @see ISelectionListener#selectionChanged(IWorkbenchPart, ISelection)
	 */
	public void selectionChanged(IWorkbenchPart part, ISelection selection) {
		if (selection instanceof IStructuredSelection) {
			Object first = ((IStructuredSelection)selection).getFirstElement();
			if (first instanceof Word) {
				label.setText(((Word)first).toString());
			}
		}
	}
}

