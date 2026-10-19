# Card Game Coursework

## Requirements
The project was developed and tested using:
-Java JDK 17
-Maven Surefire 3.5.2
-JUnit 5.11.4

## Project Structure
- src/ - Production Java Source files
- test/ - JUnit test classes
- pom.xml - Maven build configuration

## Compiling the project

Open a terminal in the project root directory
Run:
"mvn clean compile"
This compiles the main Java classes

To compile the test classes run:
"mvn test-compile"

## Running Tests
To run the full JUnit test suite use:
"mvn clean test"

If all tests pass Maven should finish with:
BUILD SUCCESS

## Running the Card Game
Run:
mvn clean compile

Then Run:
java -cp target/classes CardGame

The program will then ask for 2 things:
1. The number of players
2. The location of a pack file

## Pack File Format
For n players, in the pack there must be exactly 8n cards.
Each card has to be on a seaparate line as a non negative integer (e.g 1,2,3,4,.....)

The program rejects invalid packs that have:
- Too many or few cards
- Negative card values
- Non number values
- Empty lines

If an invalid pack is entered then the program will keep asking for another pack location.

## Output files

For "n" players the game produces "2n" output files.

Player files:
player1_output.txt ....... up to
player (n) _output.txt

Deck files:
deck1_output.txt .... up to
deck (n) _output.txt

Player files contain the players initial hand, draws and discards, winner info, exit message and the final hand

Deck files contain the final contents of each deck

## Testing

This project uses JUnit 5.11

The test suite includes tests for:

-Card validation
-FIFO deck behaviour
-Pack validation
-Player hand management
-Winning hands
-Preferred card discard behaviour
-Card conservation
-Final hand sizes
-Winner declaration
-Output file generation

Run all the tests using:
"mvn clean test"