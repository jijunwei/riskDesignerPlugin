package r06.views.word;

import org.eclipse.jface.action.IAction;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.ui.*;

public class BlueActionDelegate implements IViewActionDelegate {

	/**
	 * Constructor for BlueActionDelegate
	 */
	public BlueActionDelegate() {
		super();
	}

	/**
	 * @see IViewActionDelegate#init(IViewPart)
	 */
	public void init(IViewPart view) {
	}

	/**
	 * @see IActionDelegate#run(IAction)
	 */
	public void run(IAction action) {
		System.out.println("Blue Action Called");
	}

	/**
	 * @see IActionDelegate#selectionChanged(IAction, ISelection)
	 */
	public void selectionChanged(IAction action, ISelection selection) {
	}

}

