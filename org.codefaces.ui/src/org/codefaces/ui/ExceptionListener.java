package org.codefaces.ui;

public interface ExceptionListener {
    /**
     * This method is called when a recoverable exception has 
     * been caught. 
     *
     * @param e The exception that was caught. 
     * 
     */
	public void exceptionThrown(Exception e); 
}
