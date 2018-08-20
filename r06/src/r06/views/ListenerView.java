package r06.views;

import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.eclipse.ui.ISelectionListener;
import org.eclipse.ui.IWorkbenchPart;
import org.eclipse.ui.part.ViewPart;

import r06.views.word.WordView;

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
                if (first instanceof WordView) {
                        label.setText(((WordView)first).toString());
                }
        }
}
}
