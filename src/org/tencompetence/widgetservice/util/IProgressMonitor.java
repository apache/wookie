package org.tencompetence.widgetservice.util;

/**
 * Interface to monitor Progress
 * 
 * @author Phillip Beauvoir
 * @version $$
 */
public interface IProgressMonitor {

    /**
     * Set a note
     * @param name
     */
    void setNote(String name);

    /**
     * Cancel the operation
     * @return
     */
    boolean isCanceled();

    /**
     * Close it
     */
    void close();
}