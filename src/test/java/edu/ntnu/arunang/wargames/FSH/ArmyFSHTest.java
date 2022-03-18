package edu.ntnu.arunang.wargames.FSH;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

import edu.ntnu.arunang.wargames.Army;
import edu.ntnu.arunang.wargames.Unit.*;

import java.io.File;
import java.nio.file.FileSystem;
import java.nio.file.FileSystems;


public class ArmyFSHTest {

    CavalryUnit cavUnit = new CavalryUnit("cavUnit", 20);
    InfantryUnit infUnit = new InfantryUnit("infUnit", 40);
    CommanderUnit comUnit = new CommanderUnit("opUnit", 10000);

    @Test
    @DisplayName("Test file writing")
    void testFileWriting() {
        Army army = new Army("Test");
        army.add(cavUnit, 50);
        army.add(infUnit, 23);

        ArmyFSH armyFSH = new ArmyFSH();
        armyFSH.write(army);

        //not finished
    }


    @Test
    @DisplayName("Test file reading")
    void testFileReading() {
        Army army = new Army("TestReading");

        army.add(cavUnit, 2);
        army.add(infUnit, 2);
        army.add(comUnit, 2);

        ArmyFSH armyFSH = new ArmyFSH();

        armyFSH.write(army);
        Army armyFromFile = armyFSH.loadFromFile(new File(ArmyFSH.getPath(army)));

        assertEquals(army, armyFromFile);
    }


    @Test
    @DisplayName("Test file reading, when no units are specified")
    void testFileReadingOnOnlyArmyName() {
        Army army = new Army("TestReading");

        ArmyFSH armyFSH = new ArmyFSH();

        armyFSH.write(army);
        Army armyFromFile = armyFSH.loadFromFile(new File(ArmyFSH.getPath(army)));

        assertEquals(army, armyFromFile);
    }

    @Test
    @DisplayName("Test file reading, on empty file")
    void testFileReadingOnEmpty() {
        ArmyFSH armyFSH = new ArmyFSH();
        try {
            armyFSH.loadFromFile(new File(ArmyFSH.getPathFromFileName("blank.csv")));
        } catch (Exception e) {
            assertEquals(IllegalStateException.class, e.getClass());
        }

    }

    @Test
    @DisplayName("Test on reading non-supported Unit type")
    void testOnReadingNonSupportedType() {
        ArmyFSH armyFSH = new ArmyFSH();

        try {
            armyFSH.loadFromFile(new File(ArmyFSH.getPathFromFileName("NotAUnit.csv")));
        } catch (Exception e) {
            assertEquals(IllegalArgumentException.class, e.getClass());
        }
    }

    @Test
    @DisplayName("Test on reading file that has spaces")
    void testOnReadingFileWithSpaces() {
        ArmyFSH armyFSH = new ArmyFSH();

        Army armyFromFile = armyFSH.loadFromFile(new File(ArmyFSH.getPathFromFileName("Test With Spaces.csv")));

        System.out.println(armyFromFile.toString());
    }

    @Test
    @DisplayName("Test on reading file that is wrongly formatted")
    void testWithWrongFormattedFile() {
        ArmyFSH armyFSH = new ArmyFSH();

        Army armyFromFile = armyFSH.loadFromFile(new File(ArmyFSH.getPathFromFileName("Test With Spaces.csv")));
    }

    @Test
    @DisplayName("Test on reading file that has blank fields")
    void testOnFileWithBlankFields() {
        ArmyFSH armyFSH = new ArmyFSH();

        Army armyFromFile = armyFSH.loadFromFile(new File(ArmyFSH.getPathFromFileName("Test With Spaces.csv")));
    }
}

