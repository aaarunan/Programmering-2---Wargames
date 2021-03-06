package edu.ntnu.arunang.wargames.fsh;

import java.io.File;
import java.nio.file.FileSystems;

/**
 * An interface for FileSystemHandlers in Wargames. This interface generalizes FSH methods. This is an interface every
 * FSH class should have.
 */

public interface FSH {

    /**
     * Check if a file exists. Useful for overwrite protection.
     *
     * @param file file that is being checked
     * @return true or false
     */

    default boolean fileExists(File file) {
        return file.exists() && !file.isDirectory();
    }

    /**
     * Get the file name without the extension.
     *
     * @param file file that is parsed
     * @return filename without extension
     */

    default String getFileNameWithoutExtension(File file) {
        try {
            return file.getName().substring(0, file.getName().lastIndexOf('.'));
        } catch (StringIndexOutOfBoundsException e) {
            return file.toString();
        }
    }
}
