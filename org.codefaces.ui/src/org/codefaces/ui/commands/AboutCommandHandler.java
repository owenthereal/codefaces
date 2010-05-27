package org.codefaces.ui.commands;

import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.ui.PlatformUI;

public class AboutCommandHandler extends AbstractHandler {

	private static final String TITLE = "About CodeFaces";

	private static final String MESSAGE = "Copyright © 2010 CodeFaces.org.";

	@Override
	public Object execute(ExecutionEvent event) throws ExecutionException {
		MessageDialog.openInformation(PlatformUI.getWorkbench()
				.getActiveWorkbenchWindow().getShell(), TITLE, MESSAGE);

		return null;
	}

}
