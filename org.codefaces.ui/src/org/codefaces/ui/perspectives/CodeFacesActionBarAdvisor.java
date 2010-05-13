package org.codefaces.ui.perspectives;

import org.eclipse.jface.action.IMenuManager;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.application.ActionBarAdvisor;
import org.eclipse.ui.application.IActionBarConfigurer;

public class CodeFacesActionBarAdvisor extends ActionBarAdvisor {
	public CodeFacesActionBarAdvisor(IActionBarConfigurer configurer) {
		super(configurer);
	}
	
	 // Actions - important to allocate these only in makeActions, and then use them
    // in the fill methods.  This ensures that the actions aren't recreated
    // when fillActionBars is called with FILL_PROXY.
    /*private IWorkbenchAction exitAction;
    private IAction aboutAction;
    private OpenViewAction openViewAction;
    private Action messagePopupAction;
    
    private ShowRepoDialogAction showRepoDialogAction;*/

    protected void makeActions(final IWorkbenchWindow window) {
        // Creates the actions and registers them.
        // Registering is needed to ensure that key bindings work.
        // The corresponding commands keybindings are defined in the plugin.xml file.
        // Registering also provides automatic disposal of the actions when
        // the window is closed.

        /*exitAction = ActionFactory.QUIT.create(window);
        register(exitAction);
        
        aboutAction = new AboutAction(window);
        register(aboutAction);
        
        openViewAction = new OpenViewAction(window, "Open Another Message View", ProjectExplorerViewPart.ID);
        register(openViewAction);
        
        messagePopupAction = new MessagePopupAction("Open Message", window);
        register(messagePopupAction);
        
		showRepoDialogAction = new ShowRepoDialogAction("&Open Repository",
				ProjectExplorerViewPart.ID);
		register(showRepoDialogAction);*/
    }
    
    protected void fillMenuBar(IMenuManager menuBar) {
        /*MenuManager fileMenu = new MenuManager("&File", IWorkbenchActionConstants.M_FILE);
        MenuManager helpMenu = new MenuManager("&Help", IWorkbenchActionConstants.M_HELP);
        
        menuBar.add(fileMenu);
        // Add a group marker indicating where action set menus will appear.
        menuBar.add(new GroupMarker(IWorkbenchActionConstants.MB_ADDITIONS));
        menuBar.add(helpMenu);*/
        
        // File
        /*fileMenu.add(showRepoDialogAction);
        
        fileMenu.add(messagePopupAction);
        fileMenu.add(openViewAction);
        fileMenu.add(new Separator());
        fileMenu.add(exitAction);
        
        // Help
        helpMenu.add(aboutAction);*/
    }
    
    /*protected void fillCoolBar(ICoolBarManager coolBar) {
        IToolBarManager toolbar = new ToolBarManager(SWT.FLAT | SWT.RIGHT);
        coolBar.add(new ToolBarContributionItem(toolbar, "main"));   
        toolbar.add(openViewAction);
        toolbar.add(messagePopupAction);
    }*/
}
