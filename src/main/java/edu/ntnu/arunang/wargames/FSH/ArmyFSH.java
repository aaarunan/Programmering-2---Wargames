package edu.ntnu.arunang.wargames.FSH;

import edu.ntnu.arunang.wargames.Army;
import edu.ntnu.arunang.wargames.Unit.*;

import java.io.*;
import java.nio.file.FileSystems;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

/**
 * ArmyFSH is a fileSystemHandling class for Army. This class saves armies in
 * csv files, and reads from csv files. The units are stored in the following manner:
 * unitType, unitName, unitHealth, count.
 * <p>
 * count is the amount of the units in the army.
 * <p>
 * More information about how the units are stored is written in the readme.
 * <p>
 * ArmyFSH implements FSH which is the basic FileSystemHandling interface for every FSH in wargames.
 * <p>
 * ArmyFSH uses Buffered- Reader and Writer, for easily reading and writing to lines.
 */

public class ArmyFSH implements FSH {

    private int lineNr;

    /**
     * Constructs the FSH with no parameters.
     */

    public ArmyFSH() {

    }


    /**
     * Helper method to get the path of the army file. This is by default stored in:
     * /src/main/resources/army
     *
     * @param army armyName is the filename
     * @return the full system path
     */

    private static String getPath(Army army) {
        return FileSystems.getDefault().getPath("src", "main", "resources", "army", army.getName() + ".csv").toString();
    }

    /**
     * Writes to a file that is given by the armyName. The file is stored in:
     * /src/main/resources/army
     *
     * @param army
     */

    public void write(Army army) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(getPath(army)))) {
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
     * @throws IllegalStateException if the file is wrongly formatted or empty.
     */

    public Army loadFromFile(File file) throws IllegalStateException {
        Army army = null;
        String line;

        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            String armyName = reader.readLine();

            if (armyName == null) {
                //kankjse lage egen exception
                throw new IllegalStateException("File is empty or wrongly formatted");
            }

            army = new Army(armyName);

            lineNr = 1;

            while ((line = reader.readLine()) != null) {
                String[] values = parseLine(line);
                army.add(this.constructUnitFromString(values[0], values[1], Integer.parseInt(values[2])), Integer.parseInt(values[3]));
                lineNr++;
            }

        } catch (Exception e) {
            e.printStackTrace();
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
     * @throws IllegalStateException if the line is wrongly formatted.
     */

    private String[] parseLine(String line) throws IllegalStateException {
        String[] values;
        String type, name;
        int health, count;

        values = line.split(",");

        try {
            type = values[0].replaceAll("\\s+", "");
            name = values[1].replaceAll("\\s+", "");
            health = Integer.parseInt(values[2].replaceAll("\\s+", ""));
            count = Integer.parseInt(values[3].replaceAll("\\s+", ""));

        } catch (Exception e) {
            throw new IllegalStateException("File is wrongly formatted on line:" + lineNr);
        }
        return new String[]{type, name, Integer.toString(health), Integer.toString(count)};
    }

    /**
     * Constructs a unit from a parsed line. The army is created reset.
     *
     * @param type   unit type
     * @param name   unit name
     * @param health unit health
     * @return A constructed Unit.
     * @throws IllegalStateException if the Unit type is not defined or wrong.
     */

    private Unit constructUnitFromString(String type, String name, int health) throws IllegalStateException {
        return switch (type) {
            case "CavalryUnit" -> new CavalryUnit(name, health);
            case "CommanderUnit" -> new CommanderUnit(name, health);
            case "InfantryUnit" -> new InfantryUnit(name, health);
            case "RangedUnit" -> new RangedUnit(name, health);
            default -> throw new IllegalStateException(String.format("The object %s on line %d, is not a Unit", type, lineNr));
        };
    }
}
