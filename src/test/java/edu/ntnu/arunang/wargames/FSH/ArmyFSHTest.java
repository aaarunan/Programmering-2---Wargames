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

        ArmyFSH armyFSH = new ArmyFSH(army);
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

        ArmyFSH armyFSH = new ArmyFSH(army);

        armyFSH.write(army);

        Army armyFromFile = ArmyFSH.toArmy(armyFSH.getFile());

        assertEquals(army, armyFromFile);
    }


    @Test
    @DisplayName("Test file reading, when no units are specified")
    void testFileReadingOnOnlyArmyName() {
        Army army = new Army("TestReading");

        ArmyFSH armyFSH = new ArmyFSH(army);

        armyFSH.write(army);
        Army armyFromFile = ArmyFSH.toArmy(armyFSH.getFile());

        assertEquals(army, armyFromFile);
    }

    @Test
    @DisplayName("Test file reading, on empty file")
    void testFileReadingOnEmpty() {

        File file = new File(FileSystems.getDefault().getPath("src", "main", "resources", "tests", "blank.csv").toString());

        try{
            ArmyFSH.toArmy(file);
        } catch (Exception e) {
            assertEquals(IllegalStateException.class, e.getClass());
        }

    }

    @Test
    @DisplayName("Test on reading non-supported Unit type")
    void testOnReadingNonSupportedType() {
        File file = new File(FileSystems.getDefault().getPath("src", "main", "resources", "tests", "army", "NotaUnit.csv").toString());

        try{
            ArmyFSH.toArmy(file);
        } catch (Exception e) {
           assertEquals(IllegalArgumentException.class, e.getClass());
        }
    }
}
