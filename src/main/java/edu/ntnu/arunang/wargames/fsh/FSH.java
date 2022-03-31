package edu.ntnu.arunang.wargames.fsh;

import java.io.File;
import java.nio.file.FileSystems;

/**
 * An interface for FileSystemHandlers in Wargames. This interface generalizes
 * FSH methods. This is an inteface every FSH class should have.
 */

public interface FSH {
    /**
     * Get the full system path from resources.
     *
     * @param string fileName or directory
     * @return full path
     */

    static String getPathFromResources(String string) {
        return FileSystems.getDefault().getPath("src", "main", "resources", string).toString();
    }

    /**
     * Check if a file exists. Useful for overwrite protection.
     *
     * @param file file that is being checked
     * @return true or false
     */

    default boolean fileExists(File file) {
        return file.exists() && !file.isDirectory();
    }

}