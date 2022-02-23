package Unit;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class UnitTest {

    Unit test = new Unit("test", 20, 20,20) {
        @Override
        public Unit copy() {
            return null;
        }

        @Override
        public int getAttackBonus() {
            return 2;
        }

        @Override
        public int getResistBonus() {
            return 8;
        }
    };

    Unit testWithDifferentResistBonus = new Unit("test", 20, 20,20) {
        @Override
        public Unit copy() {
            return null;
        }

        @Override
        public int getAttackBonus() {
            return 6;
        }

        @Override
        public int getResistBonus() {
            return 8;
        }
    };

    Unit testWithDifferentHealthPoints = new Unit("test", 40, 20,20) {
        @Override
        public Unit copy() {
            return null;
        }

        @Override
        public int getAttackBonus() {
            return 2;
        }

        @Override
        public int getResistBonus() {
            return 8;
        }
    };

    Unit testSame = new Unit("test", 20, 20,20) {
        @Override
        public Unit copy() {
            return null;
        }

        @Override
        public int getAttackBonus() {
            return 2;
        }

        @Override
        public int getResistBonus() {
            return 8;
        }
    };

    @Test
    @DisplayName("Constructing Unit wrong should throw exception")
    void testConstructWrong() {
        try {
            Unit unit = new Unit("", -21, -20, -20) {
                @Override
                public Unit copy() {
                    return null;
                }

                @Override
                public int getAttackBonus() {
                    return -1;
                }

                @Override
                public int getResistBonus() {
                    return -1;
                }
            };

        } catch (Exception err) {
            assertEquals(IllegalArgumentException.class, err.getClass());
        }
    }

    @Test
    @DisplayName("Equals should check for subclass specific fields")
    void testEqualsOnSubclassSpecificFields() {

        assertNotEquals(test, testWithDifferentResistBonus);

    }

    @Test
    @DisplayName("Equal should be true where every field is the same")
    void testOnSameFields() {

        assertEquals(test, testSame);
    }

    @Test
    @DisplayName("Equals should check for abstract fields")
    void testEqualsOnAbstractFields() {
        assertNotEquals(test, testWithDifferentHealthPoints);
    }

    @Test
    @DisplayName("CompareTo should return 0 when the Units are the same")
    void testCompareToOnSameFields() {
        assertEquals(0, test.compareTo(testSame));
    }

    @Test
    @DisplayName("CompareTo should return less than 0 when other have lower stats")
    void testCompareToOnDifferentFields() {
        assertTrue(test.compareTo(testWithDifferentHealthPoints) < 0);
    }

    @Test
    @DisplayName("CompareTo should return less than 0 on subclass specific fields")
    void testCompareToOnSubclassSpecificFields() {
        assertTrue(test.compareTo(testWithDifferentResistBonus) < 0);
    }
}