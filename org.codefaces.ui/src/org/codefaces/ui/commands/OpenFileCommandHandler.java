package org.codefaces.ui.commands;

import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;

public class OpenFileCommandHandler extends AbstractHandler {
	public static final String PARAM_FILE_ID = "org.codefaces.ui.commands.parameters.openFileCommand.filename";
	@Override
	public Object execute(ExecutionEvent event) throws ExecutionException {
		String file = event.getParameter(PARAM_FILE_ID);
		System.out.println(file);
		return null;
	}

}
