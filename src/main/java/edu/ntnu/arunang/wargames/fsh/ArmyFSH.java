package edu.ntnu.arunang.wargames.fsh;

import edu.ntnu.arunang.wargames.Army;
import edu.ntnu.arunang.wargames.exception.FileFormatException;
import edu.ntnu.arunang.wargames.unit.Unit;
import edu.ntnu.arunang.wargames.unit.UnitFactory;

import java.io.*;
import java.nio.file.FileSystems;
import java.util.ArrayList;

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

    private int lineNr = 1;

    /**
     * Constructs the FSH with no parameters. The class can be instantiated
     * and the constructor is therefore public.
     */

    public ArmyFSH() {

    }

    /**
     * Helper method to get the path of the army file. This is by default:
     * /src/main/resources/army
     *
     * @param armyName armyName is the filename
     * @return the full system path
     */

    public static String getPath(String armyName) {
        return FileSystems.getDefault().getPath("src", "main", "resources", "army", armyName + ".csv").toString();
    }

    protected static String getTestPath(String armyName) {
        return FileSystems.getDefault().getPath("src", "test", "resources", "army", armyName + ".csv").toString();
    }

    /**
     * Writes to a file that is given by the armyName. The file is stored in:
     * /src/main/resources/army
     *
     * @param army
     */

    public void write(Army army) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(getPath(army.getName())))) {
            writer.write(army.getName());
            writer.write(army.toCsv());
            writer.flush();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Write an Army to a specific file or directory.
     *
     * @param file the directory or file.
     * @param army army that is written.
     */

    public void writeTo(File file, Army army) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(file))) {
            writer.write(army.getName());
            writer.write(army.toCsv());
            writer.flush();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Load an Army from a specific file. It constructs a
     * fully reset army. The method will throw an IllegalStateException if
     * the file was wrongly formatted. it will also throw an IOException if the file
     * was not found or does not exist.
     *
     * @param file file that is parsed
     * @return The army parsed from the file
     */

    public Army loadFromFile(File file) throws FileFormatException {
        Army army = null;
        String line;

        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            String armyName = reader.readLine();

            if (armyName == null) {
                throw new FileFormatException("File is empty");
            }

            army = new Army(armyName);

            lineNr = 1;

            while ((line = reader.readLine()) != null) {
                army.add(parseLine(line));
                parseLine(line);
                lineNr++;
            }

        } catch (IOException error) {
            error.printStackTrace();
        }

        return army;
    }

    /**
     * A helper function that parses a line from a file.
     * It uses regex to clean the values and checks if the
     * health and count values can be parsed to int.
     *
     * @param line
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

        ArrayList<Unit> units = new ArrayList<>();


        for (int i = 0; i < count; i++) {
            try {
                units.add(UnitFactory.constructUnitFromString(type, name, health));
            } catch (IllegalArgumentException e) {
                throw new FileFormatException(String.format("%s on Line: %d", e.getMessage(), lineNr));
            }
        }
        return units;
    }
}
