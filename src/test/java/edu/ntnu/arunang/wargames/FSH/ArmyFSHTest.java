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
        armyFSH.writeTo(new File(ArmyFSH.getTestPath(army.getName())), army);

        assertTrue(armyFSH.fileExists(new File(ArmyFSH.getTestPath(army.getName()))));


    }


    @Test
    @DisplayName("Test file reading")
    void testFileReading() {
        Army army = new Army("TestReading");

        army.add(cavUnit, 2);
        army.add(infUnit, 2);
        army.add(comUnit, 2);

        ArmyFSH armyFSH = new ArmyFSH();

        armyFSH.writeTo(new File(ArmyFSH.getTestPath(army.getName())), army);
        Army armyFromFile = armyFSH.loadFromFile(new File(ArmyFSH.getTestPath(army.getName())));

        assertEquals(army, armyFromFile);
    }


    @Test
    @DisplayName("Test file reading, when no units are specified")
    void testFileReadingOnOnlyArmyName() {
        Army army = new Army("TestReading");

        ArmyFSH armyFSH = new ArmyFSH();

        armyFSH.writeTo(new File(ArmyFSH.getTestPath(army.getName())), army);
        Army armyFromFile = armyFSH.loadFromFile(new File(ArmyFSH.getTestPath(army.getName())));

        assertEquals(army, armyFromFile);
    }

    @Test
    @DisplayName("Test file reading, on empty file")
    void testFileReadingOnEmpty() {
        ArmyFSH armyFSH = new ArmyFSH();
        try {
            armyFSH.loadFromFile(new File(ArmyFSH.getTestPath("Blank")));
        } catch (Exception e) {
            assertEquals(IllegalStateException.class, e.getClass());
        }

    }

    @Test
    @DisplayName("Test on reading non-supported Unit type")
    void testOnReadingNonSupportedType() {
        ArmyFSH armyFSH = new ArmyFSH();

        try {
            armyFSH.loadFromFile(new File(ArmyFSH.getTestPath("NotAUnit")));
        } catch (Exception e) {
            assertEquals(IllegalArgumentException.class, e.getClass());
        }
    }

    @Test
    @DisplayName("Test on reading file that has spaces")
    void testOnReadingFileWithSpaces() {
        ArmyFSH armyFSH = new ArmyFSH();
        Army army = new Army("Test With Spaces");
        army.add(new InfantryUnit("test with spaces", 101), 13);

        Army armyFromFile = armyFSH.loadFromFile(new File(ArmyFSH.getTestPath("Test With Spaces")));
        System.out.println(armyFromFile.toCsv());
        System.out.println(army.toCsv());

        assertEquals(army, armyFromFile);

    }

    @Test
    @DisplayName("Test on reading file that has blank fields")
    void testOnFileWithBlankFields() {
        ArmyFSH armyFSH = new ArmyFSH();

        try {
            Army armyFromFile = armyFSH.loadFromFile(new File(ArmyFSH.getTestPath("BlankFields")));
        } catch (Exception e) {
            assertEquals(IllegalStateException.class, e.getClass());
        }

    }
}

