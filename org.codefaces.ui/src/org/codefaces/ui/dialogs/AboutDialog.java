package org.codefaces.ui.dialogs;

import java.util.HashMap;
import java.util.Map;

import org.codefaces.ui.Images;
import org.codefaces.ui.commands.CommandUtils;
import org.codefaces.ui.commands.OpenEditorHandler;
import org.codefaces.ui.editors.SupportEditor;
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
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;


public class AboutDialog extends IconAndMessageDialog {
	
	private static final int MESSAGE_WIDTH = 200;
	
	private static final String TITLE = "About";
	private static final String LOGO_TOOLTIPS_TEXT = "contact us";
	
	private static final String MESSAGE = "CodeFaces is a source control client for browsers "
			+ "based on Ajax RIA technology. We are currently in beta and looking for your feedback. "
			+ "To contact us, click the icon on the left.";
	
	public AboutDialog(Shell parentShell) {
		super(parentShell);
		setShellStyle(getShellStyle() | SWT.SHEET);
	}
	
	@Override
	protected void configureShell(Shell shell){
		super.configureShell(shell);
		shell.setText(TITLE);
	}
	
	//create only OK button
	@Override
	protected void createButtonsForButtonBar(Composite parent) {
		Button button = createButton(parent, IDialogConstants.OK_ID, IDialogConstants.get().OK_LABEL,
				true);
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
		
		//create contact us button
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
				//small optimization, create a map with smaller-than-default capacity
				Map<String, String> parameterMap = new HashMap<String, String>(4);
				parameterMap.put(OpenEditorHandler.PARAMETER_EDITOR_ID, SupportEditor.ID);
				CommandUtils.executeCommand(OpenEditorHandler.ID, parameterMap, null);
				close();
			}
		});
		
		// create message
		Label messageLabel = new Label(dialogAreaComposite, SWT.WRAP);
		messageLabel.setText(MESSAGE);
		GridDataFactory.fillDefaults().align(SWT.FILL, SWT.BEGINNING)
				.grab(true, false)
				.hint(convertHorizontalDLUsToPixels(MESSAGE_WIDTH), SWT.DEFAULT)
				.applyTo(messageLabel);
		
		return composite;
	}

	@Override
	protected Image getImage() {
		return Images.getImageFromRegistry(Images.IMG_FAVICON_48);
	}
}
