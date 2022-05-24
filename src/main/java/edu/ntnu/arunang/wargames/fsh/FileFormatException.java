package edu.ntnu.arunang.wargames.fsh;

/**
 * Exception to be thrown when the file is wrongly formatted or is empty.
 * This should be handled as a checked Exception,
 * because a file that is wrongly formatted or empty should not break the
 * program
 */

public class FileFormatException extends Exception {

    /**
     * Constructs the Exception with a message
     *
     * @param string message for the user about what has occurred
     */

    public FileFormatException(String string) {
        super(string);
    }
}
