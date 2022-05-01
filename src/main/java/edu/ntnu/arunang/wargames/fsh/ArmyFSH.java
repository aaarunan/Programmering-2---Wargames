package edu.ntnu.arunang.wargames.fsh;

import edu.ntnu.arunang.wargames.Army;
import edu.ntnu.arunang.wargames.unit.Unit;
import edu.ntnu.arunang.wargames.unit.UnitFactory;

import java.io.*;
import java.nio.file.FileSystems;
import java.util.ArrayList;
import java.util.List;

/**
 * ArmyFSH is a fileSystemHandling class for Army. This class saves armies in
 * csv files, and reads from csv files. The units are stored in the following manner:
 * unitType, unitName, unitHealth, count.
 * <p>
 * count is the amount of the units in the army.
 * <p>
 * More information about how the units are stored is written in the readme.
 * <p>
 * ArmyFSH implements FSH which is the basic FileSystemHandling interface for every FSH in Wargames.
 * <p>
 * ArmyFSH uses Buffered- Reader and Writer, for easily reading and writing to lines.
 */

public class ArmyFSH implements FSH {

    private final String FILETYPE = "csv";
    private int lineNr = 1;

    /**
     * Constructs the FSH with no parameters. The class can be instantiated
     * and the constructor is therefore public.
     */

    public ArmyFSH() {

    }

    /**
     * Helper method to get the path of the army. This is by default:
     * /src/main/resources/army
     *
     * @param armyName armyName is the filename
     * @return the full system path
     */

    public static String getPath(String armyName) {
        return FileSystems.getDefault().getPath("src", "main", "resources", "army", armyName + ".csv").toString();
    }

    /**
     * Get the test path of the army this is by default /src/test/resources/army
     *
     * @param armyName armyname is the filename.
     * @return full path
     */

    protected static String getTestPath(String armyName) {
        return FileSystems.getDefault().getPath("src", "test", "resources", "army", armyName + ".csv").toString();
    }

    /**
     * Writes to a file that is given by the armyName. The file is stored in:
     * /src/main/resources/army
     * <p>
     * If the file is not found or not accessible, an IOException will be thrown.
     *
     * @param army
     */

    public void writeArmy(Army army) throws IOException {
        BufferedWriter writer = new BufferedWriter(new FileWriter(getPath(army.getName())));
        write(new File(getPath(army.getName())), army);
        writer.flush();
        writer.close();
    }

    /**
     * Write an Army to a specific file or directory.
     *
     * @param file the directory or file.
     * @param army army that is written.
     */

    public void writeArmyTo(File file, Army army) throws IllegalArgumentException, IOException {
        if (!isCsv(file.toString())) {
            throw new IllegalArgumentException("Filetype is not supported");
        }

        write(file, army);
    }

    /**
     * Load an Army from a specific file. It constructs a
     * fully reset army. The method will throw an IllegalStateException if
     * the file was wrongly formatted. it will also throw an IOException if the file
     * was not found or does not exist.
     * <p>
     * Throws IOException if file cannot be found or is not accessible, which must be handled.
     *
     * @param file file that is parsed
     * @return The army parsed from the file
     * @throws FileFormatException If the file is wrongly formatted
     * @throws IOException         If the file can not be found or is not accessible
     */

    public Army loadFromFile(File file) throws FileFormatException, IOException {
        if (!isCsv(file.toString())) {
            throw new FileFormatException(String.format("File '%s' is not csv.", file));
        }

        Army army;
        String line;

        BufferedReader reader = new BufferedReader(new FileReader(file));
        String armyName = reader.readLine();

        if (armyName == null) {
            throw new FileFormatException(String.format("File '%s' is empty", file));
        }

        army = new Army(armyName);

        lineNr = 1;

        while ((line = reader.readLine()) != null) {
            army.add(parseLine(line));
            parseLine(line);
            lineNr++;
        }
        reader.close();

        return army;
    }

    /**
     * Load multiple files to a list.
     *
     * @param files files that are parsed
     * @return parsed armies
     * @throws IOException         if the file does not exist or could not be found
     * @throws FileFormatException if one of the file has formatting issues
     */

    public List<Army> loadFromFiles(File[] files) throws IOException, FileFormatException {
        List<Army> armies = new ArrayList<>();
        for (File file : files) {
            armies.add(this.loadFromFile(file));
        }

        return armies;
    }

    /**
     * Deletes an army from /resources/army.
     *
     * @param army army that is being deleted
     * @return true if successful
     */

    public boolean deleteArmy(Army army) {
        File file = new File(ArmyFSH.getPath(army.getName()));
        return file.delete();
    }

    /**
     * A helper function that parses a line from a file.
     * It uses regex to clean the values and checks if the
     * health and count values can be parsed to int.
     *
     * @param line line that is being parsed
     * @return pares values
     * @throws FileFormatException if the line is wrongly formatted.
     */

    private ArrayList<Unit> parseLine(String line) throws FileFormatException {
        String[] values;
        String type, name;
        int health, count;

        values = line.split(",");

        try {
            type = values[0].replaceAll("\\s+", "");
            name = values[1];
            health = Integer.parseInt(values[2].replaceAll("\\s+", ""));
            count = Integer.parseInt(values[3].replaceAll("\\s+", ""));

        } catch (NumberFormatException e) {
            throw new FileFormatException("Could not parse integers on line :" + lineNr);
        } catch (IndexOutOfBoundsException e) {
            throw new FileFormatException("Too few fields on line: " + lineNr);
        }

        List<Unit> units;

        try {
            units = UnitFactory.constructUnitsFromString(type, name, health, count);
        } catch (IllegalArgumentException e) {
            throw new FileFormatException(String.format("%s on Line: %d", e.getMessage(), lineNr));
        }
        return (ArrayList<Unit>) units;
    }

    /**
     * Helper method for checking if file is the correct filetype.
     *
     * @param fileName file that is checked
     * @return true if csv
     */

    private boolean isCsv(String fileName) {
        int index = fileName.lastIndexOf('.');

        if (index > 0) {
            String extension = fileName.substring(index + 1);
            return extension.equals(FILETYPE);
        }
        return false;
    }

    /**
     * Helper method for writing armies to increase cohesion.
     * <p>
     * throws IOException if the file can not be written to.
     *
     * @param file file that is being written
     * @param army army that is being written to the file
     * @throws IOException if the file can not be written to
     */

    private void write(File file, Army army) throws IOException {
        BufferedWriter writer = new BufferedWriter(new FileWriter(file));
        writer.write(army.getName());
        writer.write(army.toCsv());
        writer.flush();
        writer.close();
    }

    public File[] getAllArmyFiles() {
        File folder = new File(FSH.getPathFromResources("army"));
        File[] files = folder.listFiles();
        return files;
    }
}
