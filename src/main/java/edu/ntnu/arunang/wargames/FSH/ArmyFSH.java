package edu.ntnu.arunang.wargames.FSH;

import edu.ntnu.arunang.wargames.Army;
import edu.ntnu.arunang.wargames.Unit.*;

import java.io.*;
import java.nio.file.FileSystems;

public class ArmyFSH {

    private final String location;

    public ArmyFSH(Army army) {
        location = FileSystems.getDefault().getPath("src", "main", "resources", "army", army.getName() + ".csv").toString();
    }

    public File getFile() {
        return new File(location);
    }

    public void write(Army army) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(location))) {
            writer.write(army.getName());
            writer.write(army.toCsv());
            writer.flush();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void writeTo(File file, Army army) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(file))) {
            writer.write(army.getName());
            writer.write(army.toCsv());
            writer.flush();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static Army toArmy(File file) throws IllegalStateException {

        Army army = null;

        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            String armyName = reader.readLine();

            if (armyName == null) {
                //kankjse lage egen exception
                throw new IllegalStateException("File is empty");
            }

            army = new Army(armyName);

            String[] values;
            String line, type, name;
            int health;

            while ((line = reader.readLine()) != null) {
                values = line.split(",");

                type = values[0];
                name = values[1];
                health = Integer.parseInt(values[2].strip());

                army.add(ArmyFSH.constructUnitFromString(type, name, health));
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return army;
    }

    private static Unit constructUnitFromString(String type, String name, int health) throws IllegalArgumentException{
        return switch (type) {
            case "CavalryUnit" -> new CavalryUnit(name, health);
            case "CommanderUnit" -> new CommanderUnit(name, health);
            case "InfantryUnit" -> new InfantryUnit(name, health);
            case "RangedUnit" -> new RangedUnit(name, health);
            default -> throw new IllegalArgumentException(String.format("The object %s is not a Unit", type));
        };
    }
}
