package org.codefaces.ui.lookandfeel.spica;


import org.eclipse.jface.action.CoolBarManager;
import org.eclipse.jface.action.ICoolBarManager;
import org.eclipse.jface.action.MenuManager;
import org.eclipse.jface.internal.provisional.action.ICoolBarManager2;
import org.eclipse.jface.window.ApplicationWindow;
import org.eclipse.rap.ui.interactiondesign.IWindowComposer;
import org.eclipse.rap.ui.interactiondesign.layout.model.Layout;
import org.eclipse.rwt.lifecycle.WidgetUtil;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.CBanner;
import org.eclipse.swt.custom.StackLayout;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.layout.FormAttachment;
import org.eclipse.swt.layout.FormData;
import org.eclipse.swt.layout.FormLayout;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.layout.RowLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.application.IWorkbenchWindowConfigurer;
import org.eclipse.ui.internal.FastViewBar;
import org.eclipse.ui.internal.PerspectiveSwitcher;
import org.eclipse.ui.internal.WindowTrimProxy;
import org.eclipse.ui.internal.WorkbenchMessages;
import org.eclipse.ui.internal.WorkbenchWindow;
import org.eclipse.ui.internal.layout.CacheWrapper;
import org.eclipse.ui.internal.layout.IWindowTrim;
import org.eclipse.ui.internal.layout.LayoutUtil;
import org.eclipse.ui.internal.layout.TrimLayout;
import org.eclipse.ui.internal.menus.TrimBarManager2;
import org.eclipse.ui.internal.menus.TrimContributionManager;
import org.eclipse.ui.internal.progress.ProgressRegion;

public class SpicaWindowComposer implements IWindowComposer {


	
	@Override
	public Composite createWindowContents(final Shell shell, IWorkbenchWindowConfigurer configurer){
		IWorkbench workbench = PlatformUI.getWorkbench();
	    IWorkbenchWindow activeWbWindow = workbench.getActiveWorkbenchWindow();
	    ApplicationWindow window = ( ApplicationWindow ) activeWbWindow;
	    
	    shell.setLayout(new FormLayout());
	    shell.setData(WidgetUtil.CUSTOM_VARIANT, Style.SHELL_OUTERMOST);
	    
	    Composite mainComposite = new Composite(shell, SWT.NONE);
	    mainComposite.setData(WidgetUtil.CUSTOM_VARIANT, Style.COMPOSITE_MAIN);
	    mainComposite.setLayout(new FormLayout());
    	FormData mainCompositelayoutData = new FormData();
    	mainCompositelayoutData.left = new FormAttachment(0, 0);
    	mainCompositelayoutData.right = new FormAttachment(100, 0);
    	mainCompositelayoutData.top = new FormAttachment(0, 0);
    	mainCompositelayoutData.bottom = new FormAttachment(100, 0);
	    mainComposite.setLayoutData(mainCompositelayoutData);
	    
	    Composite topElement = null;
	    
	    if(configurer.getShowMenuBar()){
	    	Composite menuBarArea = createMenuBar(mainComposite, window, configurer);
	    	FormData layoutData = new FormData();
	    	layoutData.left = new FormAttachment(0, 0);
	    	layoutData.right = new FormAttachment(100, 0);
	    	menuBarArea.setLayoutData(layoutData);
	    	if(topElement != null ) layoutData.top = new FormAttachment(topElement);
	    	topElement = menuBarArea;
	    }
	    
	    if(configurer.getShowCoolBar()){
	    	Composite coolBarArea = createCoolBar(mainComposite, window, configurer);	
	    	FormData layoutData = new FormData();
	    	layoutData.left = new FormAttachment(0, 0);
	    	layoutData.right = new FormAttachment(100, 0);
	    	if(topElement != null ) layoutData.top = new FormAttachment(topElement);
	    	coolBarArea.setLayoutData(layoutData);
	    	topElement = coolBarArea;
	    }

	    Composite clientArea = createClientArea(mainComposite, window, configurer);
    	FormData layoutData = new FormData();
    	layoutData.left = new FormAttachment(0, 0);
    	layoutData.right = new FormAttachment(100, 0);
    	layoutData.bottom = new FormAttachment(100, 0);
    	if(topElement != null ) layoutData.top = new FormAttachment(topElement);
    	clientArea.setLayoutData(layoutData);
    	topElement = clientArea;
	    
    	
	    shell.layout( true, true );
	    return clientArea;
	}
	
	/**
	 * Create a composite which filled with the menu bar
	 */
	private Composite createMenuBar(final Composite parent,
			ApplicationWindow window, IWorkbenchWindowConfigurer configurer) {
		Composite menuBarArea = new Composite(parent, SWT.NONE);
		menuBarArea.setData(WidgetUtil.CUSTOM_VARIANT, Style.COMPOSITE_MENUBAR_MAIN);
		menuBarArea.setLayout(new RowLayout());
		
		MenuManager manager = window.getMenuBarManager();
		manager.fill(menuBarArea);
		return menuBarArea;
	}
	
	/**
	 * Create a composite which filled with the main cool bar
	 */
	@SuppressWarnings("restriction")
	private Composite createCoolBar(final Composite parent, 
			ApplicationWindow window, IWorkbenchWindowConfigurer configurer) {
		Composite coolbarArea = new Composite(parent, SWT.NONE);
		coolbarArea.setData(WidgetUtil.CUSTOM_VARIANT, Style.COMPOSITE_COOLBAR_MAIN);
		ICoolBarManager manager = window.getCoolBarManager2();
		coolbarArea.setLayout(new FillLayout());
		
		ICoolBarManager2 coolbarManager2 = (ICoolBarManager2) manager;
		coolbarManager2.createControl2(coolbarArea);
    	return coolbarArea;
	}
	
	/**
	 * Create the client area composite
	 */
	private Composite createClientArea(final Composite parent, 
			ApplicationWindow window, IWorkbenchWindowConfigurer configurer){
		Composite clientArea = new Composite(parent, SWT.NONE);
		clientArea.setData(WidgetUtil.CUSTOM_VARIANT, Style.COMPOSITE_CLIENT_AREA);
		//by reverse engineering of the WorkbenchWindow code, need to use 
		//StackLayout
		clientArea.setLayout(new FillLayout() );
		
		return clientArea;
	}
	
	
	public Composite createWindowContentsbak(final Shell shell,
			IWorkbenchWindowConfigurer configurer) {
		TrimLayout defaultLayout = new TrimLayout();
		shell.setLayout(defaultLayout);
		configurer.createMenuBar();
		
		// set menubar
        if (configurer.getShowMenuBar()) {
        	Menu menuBar = configurer.createMenuBar();
            shell.setMenuBar(menuBar);
        }
        
        // Create the CBanner widget which parents both the Coolbar
        // and the perspective switcher, and supports some configurations
        // on the left right and bottom
        CBanner topBar = new CBanner(shell, SWT.NONE);
        WindowTrimProxy topBarTrim = new WindowTrimProxy(topBar,
                "org.eclipse.ui.internal.WorkbenchWindow.topBar", //$NON-NLS-1$  
                WorkbenchMessages.get().TrimCommon_Main_TrimName, SWT.NONE, true);
        
        topBar.setSimple(true);
        
        CacheWrapper coolbarCacheWrapper = new CacheWrapper(topBar);
        IWorkbench workbench = PlatformUI.getWorkbench();
        IWorkbenchWindow activeWbWindow = workbench.getActiveWorkbenchWindow();
        WorkbenchWindow window = ( WorkbenchWindow ) activeWbWindow;
        
        
        if(configurer.getShowCoolBar()){
			ICoolBarManager coolbarManager = window.getCoolBarManager2();
			final Control coolBar = createCoolBarControl(coolbarManager,
					coolbarCacheWrapper.getControl());
			// need to resize the shell, not just the coolbar's immediate
			// parent, if the coolbar wants to grow or shrink

			final Point lastShellSize = new Point(0, 0);

			coolBar.addListener(SWT.Resize, new Listener() {
				public void handleEvent(Event event) {
					// If the user is dragging the sash then we will need to
					// force
					// a resize. However, if the coolbar was resized
					// programatically
					// then everything is already layed out correctly. There is
					// no
					// direct way to tell the difference between these cases,
					// however
					// we take advantage of the fact that dragging the sash does
					// not
					// change the size of the shell, and only force another
					// layout
					// if the shell size is unchanged.
					Rectangle clientArea = shell.getClientArea();

					if (lastShellSize.x == clientArea.width
							&& lastShellSize.y == clientArea.height) {
						LayoutUtil.resize(coolBar);
					}

					lastShellSize.x = clientArea.width;
					lastShellSize.y = clientArea.height;
				}
			});
			
	        
	        topBar.setLeft(coolbarCacheWrapper.getControl());
        }
        
        
        //status line
        configurer.createStatusLineControl(shell);
        IWindowTrim statusLineTrim = new WindowTrimProxy(
                window.getStatusLineManager().getControl(),
                "org.eclipse.jface.action.StatusLineManager", //$NON-NLS-1$
                WorkbenchMessages.get().TrimCommon_StatusLine_TrimName, SWT.NONE,
                true);
        
        FastViewBar fastViewBar = new FastViewBar(window);
        fastViewBar.createControl(shell);
        
        //perspective bar
        PerspectiveSwitcher perspectiveSwitcher = null;
        if (configurer.getShowPerspectiveBar()) {
        	perspectiveSwitcher = new PerspectiveSwitcher(window, topBar, SWT.FLAT | SWT.WRAP | SWT.RIGHT | SWT.HORIZONTAL);
            perspectiveSwitcher.createControl(shell);
        }
        
        //Progress bar
        ProgressRegion progressRegion = null;
        if (configurer.getShowProgressIndicator()) {
        	progressRegion = new ProgressRegion();
            progressRegion.createContents(shell, window);
        }
        
        // Insert any contributed trim into the layout
        // Legacy (3.2) trim
        TrimBarManager2 trimMgr2 = new TrimBarManager2(window);
        
        // 3.3 Trim contributions
        TrimContributionManager trimContributionMgr = new TrimContributionManager(window);
        
        //client composite area
        Composite pageComposite = new Composite(shell, SWT.NONE);
        // use a StackLayout instead of a FillLayout (see bug 81460 [Workbench]
        // (regression) Close all perspectives, open Java perspective, layout
        // wrong)
        pageComposite.setLayout(new StackLayout());
        
        updateLayoutDataForContents(defaultLayout, configurer, statusLineTrim, pageComposite, fastViewBar, statusLineTrim, window, progressRegion, pageComposite, trimMgr2, trimContributionMgr);
        
        return shell;
	}
	
    /**
     * Creates the control for the cool bar manager.
     * <p>
     * Subclasses may override this method to customize the cool bar manager.
     * </p>
     * @param composite the parent used for the control
     * 
     * @return an instance of <code>CoolBar</code>
     * @since 1.0
     */
    protected Control createCoolBarControl(final ICoolBarManager coolBarManager, Composite composite) {
        if (coolBarManager != null) {
        	if (coolBarManager instanceof ICoolBarManager2) {
				return ((ICoolBarManager2) coolBarManager).createControl2(composite);
			}
        	if (coolBarManager instanceof CoolBarManager) {
				return ((CoolBarManager) coolBarManager).createControl(composite);
			}
        }
        return null;
    }

    /**
     * Note that this will only have an effect if the default implementation of
     * WorkbenchAdvisor.createWindowContents() has been invoked.
     * 
     * called IWorkbench
     * @param topBar 
     * @param fastViewBar 
     * @param statusLineTrim 
     * @param window 
     * @param progressRegion 
     * @param pageComposite 
     * @param trimMgr2 
     * @param trimContributionMgr 
     */
	private void updateLayoutDataForContents(TrimLayout defaultLayout,
			IWorkbenchWindowConfigurer configurer, IWindowTrim topBarTrim,
			Control topBar, FastViewBar fastViewBar,
			IWindowTrim statusLineTrim, WorkbenchWindow window,
			ProgressRegion progressRegion, Control pageComposite,
			TrimBarManager2 trimMgr2,
			TrimContributionManager trimContributionMgr) {
        if (defaultLayout == null) {
            return;
        }

        // @issue this is not ideal; coolbar and perspective shortcuts should be
        // separately configurable
        if ((configurer.getShowCoolBar())
                || (configurer.getShowPerspectiveBar())) {
            if (defaultLayout.getTrim(topBarTrim.getId()) == null) {
                defaultLayout.addTrim(SWT.TOP, topBarTrim);
            }
            topBar.setVisible(true);
        } else {
            defaultLayout.removeTrim(topBarTrim);
            topBar.setVisible(false);
        }

        if (fastViewBar != null) {
            if (configurer.getShowFastViewBars()) {
                int side = fastViewBar.getSide();

                if (defaultLayout.getTrim(fastViewBar.getId()) == null) {
                    defaultLayout.addTrim(side, fastViewBar);
                }
                fastViewBar.getControl().setVisible(true);
            } else {
                defaultLayout.removeTrim(fastViewBar);
                fastViewBar.getControl().setVisible(false);
            }
        }

        if (configurer.getShowStatusLine()) {
            if (defaultLayout.getTrim(statusLineTrim.getId()) == null) {
                defaultLayout.addTrim(SWT.BOTTOM, statusLineTrim);
            }
            window.getStatusLineManager().getControl().setVisible(true);
        } else {
            defaultLayout.removeTrim(statusLineTrim);
            window.getStatusLineManager().getControl().setVisible(false);
        }

// RAP [rh] HeapStatus not supported  
//      if (heapStatus != null) {
//          if (getShowHeapStatus()) {
//              if (heapStatus.getLayoutData() == null) {
//                  heapStatusTrim.setWidthHint(heapStatus.computeSize(
//                          SWT.DEFAULT, SWT.DEFAULT).x);
//                  heapStatusTrim
//                          .setHeightHint(getStatusLineManager().getControl()
//                                  .computeSize(SWT.DEFAULT, SWT.DEFAULT).y);
//              }
//
//              if (defaultLayout.getTrim(heapStatusTrim.getId()) == null) {
//                  defaultLayout.addTrim(SWT.BOTTOM, heapStatusTrim);
//              }
//              heapStatus.setVisible(true);
//          } else {
//
//              defaultLayout.removeTrim(heapStatusTrim);
//              heapStatus.setVisible(false);
//          }
//      }

        if (progressRegion != null) {
            if (configurer.getShowProgressIndicator()) {
                if (defaultLayout.getTrim(progressRegion.getId()) == null) {
                    defaultLayout.addTrim(SWT.BOTTOM, progressRegion);
                }
                progressRegion.getControl().setVisible(true);
            } else {
                defaultLayout.removeTrim(progressRegion);
                progressRegion.getControl().setVisible(false);
            }
        }
        
        defaultLayout.setCenterControl(pageComposite);

        // Re-populate the trim elements
        if (trimMgr2 != null)
            trimMgr2.update(true, false, !topBar.getVisible());
        if (trimContributionMgr != null)
            trimContributionMgr.update(true,  !topBar.getVisible());
    }
    

	@Override
	public void postWindowOpen(IWorkbenchWindowConfigurer configurer) {
	    final Shell windowShell = configurer.getWindow().getShell();
	    windowShell.setMaximized( true ); 
	}

	@Override
	public void preWindowOpen(IWorkbenchWindowConfigurer configurer) {
		configurer.setShellStyle( SWT.NO_TRIM  );
	}

}
