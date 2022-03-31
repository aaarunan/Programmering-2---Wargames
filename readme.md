## What is Wargames?

Wargames is a simple battle simulator in Java. In a Battle two armies will fight against each other. An army consists of
units. Units are special troops that can have special abilities.
<br>
<br>
The project was created as an obligatory submission in the subject IDATT2001 on computer engineering at NTNU.

## To developers

### FSH - FileHandlingSystem

Classes with "FSH" in the name shows that the class is a "filehandling system class". This means that it is responsible
for reading and writing to files, and managing files.

#### ArmyFSH

The ArmyFSH stores and writes armies that are stored in memory. Units are stored in a csv file. The csv file always
starts with the name of the army. The file name should also be the name of the army.

The Units are stored in the following fashion: <br>
<em><b>unitType,unitName,health,count</b></em>

Where:

- <em>unitType</em> - type of Unit
- <em>unitName</em> - name of the Unit
- <em>health</em> - amount of healthPoints the unit has
- <em>count</em> - Amount of the unit in the Army

The count is there for saving space. Rather than having multiples of lines representing the same Unit, the count will
save unnecessary data duplication.

### UnitFactory

Unit Factory creates units from strings. This is usually done hand in hand when parsing files. If new types of Units are
added, they should also be added in the factory, if the Unit is wished to be used by the Factory.

### JavaFX

Todo

## System requirements

- Apache maven
- Java 16
