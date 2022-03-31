package edu.ntnu.arunang.wargames.exception;

/**
 * Exception to be thrown when the file is wrongly formatted or is empty.
 * This should be handled as a checked Exception, because a file that is wrongly
 * formatted or empty can be given by the user, and is not related to the program itself.
 */

public class FileFormatException extends Exception {

    /**
     * Constructs the Exception with a message
     *
     * @param string
     */

    public FileFormatException(String string) {
        super(string);
    }
}
