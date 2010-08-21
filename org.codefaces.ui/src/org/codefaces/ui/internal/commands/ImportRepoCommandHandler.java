package org.codefaces.ui.internal.commands;

import org.codefaces.ui.internal.wizards.ImportRepoWizard;
import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.jface.wizard.WizardDialog;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.handlers.HandlerUtil;

public class ImportRepoCommandHandler extends AbstractHandler {

	@Override
	public Object execute(ExecutionEvent event) throws ExecutionException {
		Shell shell = HandlerUtil.getActiveShell(event);

		ImportRepoWizard wizard = new ImportRepoWizard();
		WizardDialog dialog = new WizardDialog(shell, wizard);
		dialog.open();

		return null;
	}
}
