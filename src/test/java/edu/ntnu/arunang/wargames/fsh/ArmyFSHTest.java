package edu.ntnu.arunang.wargames.fsh;

import edu.ntnu.arunang.wargames.model.Army;
import edu.ntnu.arunang.wargames.model.unit.CavalryUnit;
import edu.ntnu.arunang.wargames.model.unit.CommanderUnit;
import edu.ntnu.arunang.wargames.model.unit.InfantryUnit;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

import static org.junit.jupiter.api.Assertions.*;

public class ArmyFSHTest {

    CavalryUnit cavUnit = new CavalryUnit("cavUnit", 20);
    InfantryUnit infUnit = new InfantryUnit("infUnit", 40);
    CommanderUnit comUnit = new CommanderUnit("opUnit", 10000);

    @Test
    @DisplayName("Test file is created")
    void testFileCreation() throws IOException {
        ArmyFSH armyFSH = new ArmyFSH();
        File file = new File(ArmyFSH.getTestPath("fileCreation"));
        armyFSH.writeArmyTo(file, new Army("fileCreation"));

        assertTrue(armyFSH.fileExists(file));
    }

    @Test
    @DisplayName("Test file writing, by checking that the bytes are the same for a pre-made Army.")
    void testFileWriting() throws IOException {
        Army army = new Army("Test");
        army.add(cavUnit, 50);
        army.add(infUnit, 23);

        ArmyFSH armyFSH = new ArmyFSH();
        armyFSH.writeArmyTo(new File(ArmyFSH.getTestPath(army.getName())), army);
        assertEquals(-1,
                Files.mismatch(Paths.get(ArmyFSH.getTestPath("TestFasit")), Paths.get(ArmyFSH.getTestPath("Test"))));
    }

    @Test
    @DisplayName("Test file reading")
    void testFileReading() throws FileFormatException, IOException {
        Army army = new Army("testReading");

        army.add(cavUnit, 2);
        army.add(infUnit, 2);
        army.add(comUnit, 2);

        ArmyFSH armyFSH = new ArmyFSH();

        armyFSH.writeArmyTo(new File(ArmyFSH.getTestPath(army.getName())), army);
        Army armyFromFile = armyFSH.loadFromFile(new File(ArmyFSH.getTestPath(army.getName())));

        assertEquals(army, armyFromFile);
    }

    @Test
    @DisplayName("Test file reading, when no units are specified")
    void testFileReadingOnOnlyArmyName() throws FileFormatException, IOException {
        Army army = new Army("testReading");

        ArmyFSH armyFSH = new ArmyFSH();

        armyFSH.writeArmyTo(new File(ArmyFSH.getTestPath(army.getName())), army);
        Army armyFromFile = armyFSH.loadFromFile(new File(ArmyFSH.getTestPath(army.getName())));

        assertEquals(army, armyFromFile);
    }

    @Test
    @DisplayName("Test file reading, on empty file")
    void testFileReadingOnEmpty() {
        Throwable exception = assertThrows(FileFormatException.class, () -> {
            ArmyFSH armyFSH = new ArmyFSH();
            armyFSH.loadFromFile(new File(ArmyFSH.getTestPath("blank")));
        });

        assertEquals("File '" + ArmyFSH.getTestPath("blank") + "' is empty", exception.getMessage());
    }

    @Test
    @DisplayName("Test on reading non-supported Unit type")
    void testOnReadingNonSupportedType() {
        Throwable exception = assertThrows(FileFormatException.class, () -> {
            ArmyFSH armyFSH = new ArmyFSH();
            armyFSH.loadFromFile(new File(ArmyFSH.getTestPath("notUnit")));
        });

        assertEquals("Unittype NotaUnit does not exist on Line: 1", exception.getMessage());
    }

    @Test
    @DisplayName("Test on reading file that has spaces")
    void testOnReadingFileWithSpaces() throws FileFormatException, IOException {
        ArmyFSH armyFSH = new ArmyFSH();
        Army army = new Army("Test With Spaces");
        army.add(new InfantryUnit("test with spaces", 101), 13);

        Army armyFromFile = armyFSH.loadFromFile(new File(ArmyFSH.getTestPath("Test With Spaces")));

        assertEquals(army, armyFromFile);
    }

    @Test
    @DisplayName("Test on reading file that has blank fields")
    void testOnFileWithBlankFields() {
        Throwable exception = assertThrows(FileFormatException.class, () -> {
            ArmyFSH armyFSH = new ArmyFSH();
            armyFSH.loadFromFile(new File(ArmyFSH.getTestPath("blankFields")));
        });

        assertEquals("Too few fields on line: 1", exception.getMessage());
    }
}
