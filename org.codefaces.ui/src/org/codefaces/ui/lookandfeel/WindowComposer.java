package org.codefaces.ui.lookandfeel;

import org.eclipse.jface.action.MenuManager;
import org.eclipse.jface.window.ApplicationWindow;
import org.eclipse.rwt.lifecycle.WidgetUtil;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.layout.FormAttachment;
import org.eclipse.swt.layout.FormData;
import org.eclipse.swt.layout.FormLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.application.IWorkbenchWindowConfigurer;


public class WindowComposer {

	
	public void createWindowContent(Shell shell,
			IWorkbenchWindowConfigurer configurer) {
		IWorkbench workbench = PlatformUI.getWorkbench();
	    IWorkbenchWindow activeWbWindow = workbench.getActiveWorkbenchWindow();
	    ApplicationWindow window = ( ApplicationWindow ) activeWbWindow;
		
		shell.setLayout(new FormLayout());
		shell.setData(WidgetUtil.CUSTOM_VARIANT, Style.SHELL_OUTERMOST);
		
		Composite mainComposite = createAndSetMainComposite(shell, configurer);
		
		// the top element so far
		Composite topElement = null;
		
		if(configurer.getShowMenuBar()){
			Composite menuBarArea = createAndSetMenuBar(mainComposite, window,
					configurer, topElement);
	    	topElement = menuBarArea;
		}
		
		Composite clientArea = createAndSetClientArea(mainComposite, window,
				configurer, topElement);
		
	    shell.layout( true, true );
	}

	/**
	 * Create and set the main composite than contains all other workbrench 
	 * elements
	 * @param parent the parent objct
	 * @return a configured composite object
	 */
	private Composite createAndSetMainComposite(Composite parent,
			IWorkbenchWindowConfigurer configurer) {
	    Composite mainComposite = new Composite(parent, SWT.NONE);
	    mainComposite.setData(WidgetUtil.CUSTOM_VARIANT, Style.COMPOSITE_MAIN);
	    mainComposite.setLayout(new FormLayout());
    	FormData mainCompositelayoutData = new FormData();
    	mainCompositelayoutData.left = new FormAttachment(0, 0);
    	mainCompositelayoutData.right = new FormAttachment(100, 0);
    	mainCompositelayoutData.top = new FormAttachment(0, 0);
    	mainCompositelayoutData.bottom = new FormAttachment(100, 0);
	    mainComposite.setLayoutData(mainCompositelayoutData);
	    return mainComposite;
	}
	

	private Composite createAndSetMenuBar(Composite parent,
			ApplicationWindow window, IWorkbenchWindowConfigurer configurer, Composite topElement) {
		Composite menuBarArea = new Composite(parent, SWT.NONE);
		menuBarArea.setData(WidgetUtil.CUSTOM_VARIANT, Style.COMPOSITE_MENUBAR_MAIN);
		menuBarArea.setLayout(new FillLayout());
		MenuManager manager = window.getMenuBarManager();
		
    	FormData layoutData = new FormData();
    	layoutData.left = new FormAttachment(0, 0);
    	layoutData.right = new FormAttachment(100, 0);
    	menuBarArea.setLayoutData(layoutData);
    	manager.fill(menuBarArea);
		if(topElement != null ) layoutData.top = new FormAttachment(topElement);
		return menuBarArea;
	}

	private Composite createAndSetClientArea(Composite parent,
			ApplicationWindow window, IWorkbenchWindowConfigurer configurer,
			Composite topElement) {
		Composite clientArea = new Composite(parent, SWT.NONE);
		clientArea.setData(WidgetUtil.CUSTOM_VARIANT, Style.COMPOSITE_CLIENT_AREA);
		//by reverse engineering of the WorkbenchWindow code, need to use 
		//StackLayout
		clientArea.setLayout(new FillLayout() );
    	FormData layoutData = new FormData();
    	layoutData.left = new FormAttachment(0, 0);
    	layoutData.right = new FormAttachment(100, 0);
    	layoutData.bottom = new FormAttachment(100, 0);
    	if(topElement != null ) layoutData.top = new FormAttachment(topElement);
    	clientArea.setLayoutData(layoutData);
		
		
		configurer.createPageComposite(clientArea);
		return clientArea;
	}
}
