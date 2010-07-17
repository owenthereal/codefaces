package org.codefaces.ui.internal.widgets;

import org.eclipse.core.runtime.IProgressMonitor;

public interface IProgressMonitorInputValidator {
	/**
	 * Validates the given string. Returns an error message to display if the
	 * new text is invalid. Returns <code>null</code> if there is no error. Note
	 * that the empty string is not treated the same as <code>null</code>; it
	 * indicates an error state but with no message to display.
	 * 
	 * @param newText
	 *            the text to check for validity
	 * 
	 * @return an error message or <code>null</code> if no error
	 */
	String validate(String newText, IProgressMonitor monitor);
	
	
}
