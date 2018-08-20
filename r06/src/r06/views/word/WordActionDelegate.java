package r06.views.word;

import org.eclipse.jface.action.IAction;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.ui.*;

public class WordActionDelegate implements IViewActionDelegate {

	/**
	 * Constructor for WordActionDelegate
	 */
	public WordActionDelegate() {
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
		System.out.println("Word Action Called");
	}

	/**
	 * @see IActionDelegate#selectionChanged(IAction, ISelection)
	 */
	public void selectionChanged(IAction action, ISelection selection) {
	}

}

