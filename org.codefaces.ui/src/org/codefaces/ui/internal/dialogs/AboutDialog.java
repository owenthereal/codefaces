package org.codefaces.ui.internal.dialogs;

import java.util.HashMap;
import java.util.Map;

import org.codefaces.ui.internal.CodeFacesUIActivator;
import org.codefaces.ui.internal.Images;
import org.codefaces.ui.internal.commands.CommandExecutor;
import org.codefaces.ui.internal.commands.OpenEditorHandler;
import org.codefaces.ui.internal.editors.SupportEditor;
import org.codefaces.ui.internal.editors.WelcomeEditor;
import org.eclipse.jface.dialogs.IDialogConstants;
import org.eclipse.jface.dialogs.IconAndMessageDialog;
import org.eclipse.jface.layout.GridDataFactory;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Link;
import org.eclipse.swt.widgets.Shell;

public class AboutDialog extends IconAndMessageDialog {

	private static final int MESSAGE_WIDTH = 200;

	private static final String TITLE = "About";
	private static final String LOGO_TOOLTIPS_TEXT = "contact us";

	private static final String MESSAGE = "CodeFaces\n"
			+ "Version: "
			+ CodeFacesUIActivator.getDefault().getBundle().getVersion()
					.toString()
			+ "\n(c) Copyright CodeFaces 2010. All rights reserved. Visit <a>http://codefaces.org/</a>.";

	public AboutDialog(Shell parentShell) {
		super(parentShell);
		setShellStyle(getShellStyle() | SWT.SHEET);
	}

	@Override
	protected void configureShell(Shell shell) {
		super.configureShell(shell);
		shell.setText(TITLE);
	}

	@Override
	protected void createButtonsForButtonBar(Composite parent) {
		Button button = createButton(parent, IDialogConstants.OK_ID,
				IDialogConstants.get().OK_LABEL, true);
		button.setFocus();
	}

	@Override
	protected Control createDialogArea(Composite parent) {
		Composite composite = (Composite) super.createDialogArea(parent);
		GridLayout layout = new GridLayout();
		layout.marginHeight = 0;
		layout.marginWidth = 0;
		composite.setLayout(layout);

		Composite dialogAreaComposite = new Composite(composite, SWT.NONE);
		GridLayout dialogArealayout = new GridLayout();
		dialogArealayout.marginHeight = 0;
		dialogArealayout.marginWidth = 0;
		dialogArealayout.numColumns = 2;
		dialogAreaComposite.setLayout(dialogArealayout);

		// create contact us button
		Image image = getImage();
		Button contactUs = new Button(dialogAreaComposite, SWT.FLAT);
		contactUs.setImage(image);
		contactUs.setBounds(image.getBounds());
		contactUs.setToolTipText(LOGO_TOOLTIPS_TEXT);

		GridDataFactory.fillDefaults().align(SWT.CENTER, SWT.BEGINNING)
				.applyTo(contactUs);

		contactUs.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent event) {
				openEditor(SupportEditor.ID);
			}
		});

		Link linkText = new Link(dialogAreaComposite, SWT.NONE);
		linkText.setText(MESSAGE);
		linkText.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				openEditor(WelcomeEditor.ID);
			}
		});
		GridDataFactory
				.fillDefaults()
				.align(SWT.FILL, SWT.BEGINNING)
				.grab(true, false)
				.hint(convertHorizontalDLUsToPixels(MESSAGE_WIDTH), SWT.DEFAULT)
				.applyTo(linkText);

		return composite;
	}

	private void openEditor(String id) {
		Map<String, String> parameterMap = new HashMap<String, String>(4);
		parameterMap.put(OpenEditorHandler.PARAMETER_EDITOR_ID, id);
		CommandExecutor.execute(OpenEditorHandler.ID, parameterMap, null);
		close();
	}

	@Override
	protected Image getImage() {
		return Images.getImageFromRegistry(Images.IMG_FAVICON_48);
	}
}
