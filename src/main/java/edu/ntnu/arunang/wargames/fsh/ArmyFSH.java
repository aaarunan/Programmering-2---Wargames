package edu.ntnu.arunang.wargames.fsh;

import edu.ntnu.arunang.wargames.model.army.Army;
import edu.ntnu.arunang.wargames.model.unit.Unit;
import edu.ntnu.arunang.wargames.model.unit.UnitFactory;

import java.io.*;
import java.nio.file.FileSystems;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

/**
 * ArmyFSH is a file system handler (FSH) class for an Army.
 * This class manages how armies are stored and read from the file system.
 * <p>
 * The units are stored in the following manner:
 * unitType, unitName, unitHealth, count.
 * <p>
 * count is the amount of the units in the army.
 * <p>
 * Specific details can be found in the readme document.
 * <p>
 * ArmyFSH implements FSH which is the basic FileSystemHandling interface for every FSH in War games.
 * <p>
 * The FSH uses Buffered- Reader and Writer, for easily reading and writing to a line.
 */

public class ArmyFSH implements FSH {
    public final static String FILETYPE = "csv";
    private int lineNr = 1;

    /**
     * Constructs the FSH with no parameters.
     * The class can be instantiated and the constructor is therefore public.
     */

    public ArmyFSH() {

    }

    /**
     * Helper method to get the path of the army. This is by default in resources root /army.
     *
     * @param fileName the name of the army is the name of the file
     * @return the full system path
     */

    public static String getPath(String fileName) {
        return getDir() + "/" + fileName + "." + FILETYPE;
    }

    /**
     * Get the directory of were the army files are saved.
     *
     * @return directory as string.
     */

    public static String getDir() {
        return System.getProperty("user.home") + "/Wargames/Army";
    }

    /**
     * Get the test path of the army this is by default in test recources root folder in /army.
     *
     * @param armyName armyname is the filename.
     * @return full path
     */

    protected static String getTestPath(String armyName) {
        return FileSystems.getDefault().getPath("src", "test", "resources", "army", armyName + ".csv").toString();
    }

    /**
     * Writes to a file that is given by the armyName. The file is stored in: /src/main/resources/army
     * <p>
     * If the file is not found or not accessible, an IOException will be thrown.
     *
     * @param army army that is going to be written
     * @throws IOException if the file is unavailable or cannot be written to.
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
     * @throws IllegalArgumentException if the filetype is not supported for writing to
     * @throws IOException              if the file is not writable or unavailable.
     */

    public void writeArmyTo(File file, Army army) throws IllegalArgumentException, IOException {
        //check if file is csv
        if (!isCsv(file.toString())) {
            throw new IllegalArgumentException("Filetype is not supported");
        }

        write(file, army);
    }

    /**
     * Load an Army from a specific file. It constructs a fully reset army.
     *
     * <p>
     *
     * @param file file that is parsed
     * @return The army parsed from the file
     * @throws FileFormatException If the file is wrongly formatted
     * @throws IOException         If the file can not be found or is not accessible
     */

    public Army loadFromFile(File file) throws FileFormatException, IOException {
        //Check if file is csv
        if (!isCsv(file.toString())) {
            throw new FileFormatException(String.format("File '%s' is not csv.", file.getName()));
        }

        Army army;
        String line;

        BufferedReader reader = new BufferedReader(new FileReader(file));
        String armyName = reader.readLine();

        //check if file is empty
        if (armyName == null) {
            throw new FileFormatException(String.format("File '%s' is empty", file));
        }

        //add army name
        army = new Army(armyName);

        //parse the units
        lineNr = 1;
        while ((line = reader.readLine()) != null) {
            army.add((ArrayList<Unit>) parseLine(line));
            parseLine(line);
            lineNr++;
        }
        reader.close();

        return army;
    }

    /**
     * Deletes an army from resources /army.
     * This will delete the file corresponding to the
     * army name.
     *
     * @param army army that is being deleted
     * @return true if successful
     */

    public boolean deleteArmy(Army army) {
        File file = new File(ArmyFSH.getPath(army.getName()));
        return file.delete();
    }

    /**
     * A helper function that parses a line from a file. It uses regex to clean the values and checks if the health and
     * count values can be parsed to int.
     *
     * @param line line that is being parsed
     * @return pares values
     * @throws FileFormatException if the line is wrongly formatted.
     */

    private List<Unit> parseLine(String line) throws FileFormatException {
        String[] values;
        String type, name;
        int health, count;

        values = line.split(",");

        //try to parse the values
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

        //try to construct the units
        try {
            units = UnitFactory.constructUnitsFromString(type, name, health, count);
        } catch (IllegalArgumentException e) {
            throw new FileFormatException(String.format("%s on Line: %d", e.getMessage(), lineNr));
        }

        return units;
    }

    /**
     * Helper method for checking if file is the correct filetype.
     *
     * @param fileName file that is checked
     * @return true if csv
     */

    protected boolean isCsv(String fileName) {
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
        writer.write(army.getName() + '\n');

        for (Map.Entry<Unit, Integer> entry : army.getMap().entrySet()) {
            writer.append(unitToCsv(entry.getKey())).append(',').append(entry.getValue().toString()).append('\n');
        }

        writer.flush();
        writer.close();
    }

    /**
     * Get all the army files and stored armies.
     *
     * @return the files
     */

    public File[] getAllArmyFiles() {
        File folder = new File(getDir());
        return folder.listFiles();
    }

    /**
     * Converts the Unit to a string that represents how Units are stored in a file: unitType,unitName,health,count
     *
     * @return string that represents the unit
     */

    public String unitToCsv(Unit unit) {
        return unit.getClass().getSimpleName() + ',' + unit.getName() + ',' + unit.getHealthPoints();
    }
}
