package org.codefaces.ui.lookandfeel;

/**
 * this interface defines all the custom variant CSS tags that applicable 
 *  
 * @author kklo
 */
public interface Style {

	/** It is the outermost shell that contain the main composite */
	public static final String SHELL_OUTERMOST = "shellOutermost";
	
	/** The composite that contains all other workbench elements*/
	public static final String COMPOSITE_MAIN = "compositeMain";

	/** The composite that define the area containing the main menu bar */
	public static final String COMPOSITE_MENUBAR_MAIN = "compositeMenuBarMain"; 

	/* TODO: Decompose this tag into a number of smaller tags */
	public static final String MENUBAR_MAIN = "mainMenuBar";
	
	/** the composite that contains the main client area */
	public static final String COMPOSITE_CLIENT_AREA = "compositeClientArea";
}
