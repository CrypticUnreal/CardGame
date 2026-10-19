Card Game Coursework

## Requirements

The project was developed and tested using:

- Java JDK 17
- Apache Maven
- JUnit 5.11.4
- Maven Surefire Plugin 3.5.2

JUnit is managed by Maven in the project with the dependencies specified in `pom.xml`. As with Maven download of dependencies for the project’s sources in `src/` and tests in `test/` an internet connection is required for the initial download.

## Project Structure

- src/ – Production Java source files
- test/ – JUnit test classes
- pom.xml – Maven build configuration
- README.md – Build, running and testing instructions
- pack.txt – Example valid input pack for four players

## Compiling the Project

Open a terminal in the project root directory, where `pom.xml` is located .

Run:

mvn clean compile

This compiles the main Java classes.

To compi le the test classes, run:

mvn test-compile

## Building the Executable JAR

Run:

mvn clean package

This compiles the project, runs the JUnit tests and creates an executable JAR at:

target/cards.jar

The production  excutable JAR contains all the compiled class files as well as the source Java files.

## Running the Card Game

After building the JAR, run:

java -jar target/cards.jar

The program will ask for:

1. The number of players
2. The location of a valid pack file

For example, the input could be 4 for the 4 players to be created, followed by pack.txt which is the supplied pack file.

Alternatively, you can run the main class after having compiled the classes of your project with the following command:

java -cp target/classes CardGame

## Pack File Format

For n players, the pack must contain exactly 8n cards.

Each card must be on a separate line as a non-negative integer.

The program rejects invalid parks containing:

- Too many or too few cards
- Negative card values
- Non-numeric values
- Empty lines

The program will prompt the user for an alternative location should it find an invalid pack.

## Output Files

For n players, the game generates 2n files.

Player files:

- player1_output.txt
- player2_output.txt
- ...up to playern_output.txt

Deck files:

- deck1_output.txt
- deck2_output.txt
- ... up to deckn_output.txt

The Player files contain: the initial hand, all draw and discard actions made by the player during the game, who won, exit messages and the end hand of the player.

For each deck, a corresponding output file is  created , containing the final contents of the deck.

## Running Tests

The project uses JUnit 5.11.4.

To run the full test suite, execute:

mvn clean test

Maven compiles the Java code and executes the JUnit tests.

A successful execution ends with:

BUILD SUCCESS

To run one test class, for example GameOutputTest , use:

mvn -Dtest=GameOutputTest test

## Testing Coverage

The JUnit test suite includes tests for:

- Card validation
- FIFO deck behaviour
- Pack validation
- Player hand management
- Winning hands
- Preferred-denomination discard behaviour
- Card conservation
- Final hand sizes
- Winner declaration
- Output-file generation

All 45 tests are currently passing. The complete test suite consists of 45 tests in 11 test classes. All tests are executed as part of the complete test suite.