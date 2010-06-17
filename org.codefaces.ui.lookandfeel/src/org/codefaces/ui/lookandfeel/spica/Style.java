package org.codefaces.ui.lookandfeel.spica;

/**
 * This interface define the CustomVariant CSS tags that can be used in the 
 * theme CSS.
 * 
 * Personally, I am not a fan of Hungarian Notation, but it seems that it is 
 * quite suitable for defining RAP CSS theme due to its lack of selectors. 
 * 
 * @author kklo
 */
public interface Style {
	
	/** It is the outermost shell that contain the main composite */
	public static final String SHELL_OUTERMOST = "shellOutermost";
	
	/** The composite that contains all other workbench elements*/
	public static final String COMPOSITE_MAIN = "compositeMain"; 
	
	/** The composite that define the area containing the main menu bar */
	public static final String COMPOSITE_MENUBAR_MAIN = "compositeMenubarMain";
	/* TODO: Decompose this tag into a number of smaller tags */
	public static final String MENUBAR_MAIN = "mainMenuBar";
	
	
	/** The composite that define the area containing the main cool bar */
	public static final String COMPOSITE_COOLBAR_MAIN = "compositeCoolbarMain";
	
	/** the toolbar inside the cool bar area */
	public static final String TOOLBAR_COOLBAR_MAIN = "toolbarCoolbarMain";

	/** the composite that contains the main client area */
	public static final String COMPOSITE_CLIENT_AREA = "compositeClientArea";
}
