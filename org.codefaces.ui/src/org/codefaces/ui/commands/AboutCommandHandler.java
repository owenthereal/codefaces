package org.codefaces.ui.commands;

import org.codefaces.ui.dialogs.AboutDialog;
import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.handlers.HandlerUtil;

public class AboutCommandHandler extends AbstractHandler {

	private static final String TITLE = "About CodeFaces";

	private static final String MESSAGE = "CodeFaces is a source control client for browsers" +
		"based on Ajax RIA technology. We are currently in beta and looking for your feedback. " +
		"To contact us, you can join our community forums or send us an email.\n\n" + 
		"- The CodeFaces Team";

	@Override
	public Object execute(ExecutionEvent event) throws ExecutionException {
		//MessageDialog.openInformation(PlatformUI.getWorkbench()
		//		.getActiveWorkbenchWindow().getShell(), TITLE, MESSAGE);
		Shell shell = HandlerUtil.getActiveShell(event);
		new AboutDialog(shell).open();
		return null;
	}

}
