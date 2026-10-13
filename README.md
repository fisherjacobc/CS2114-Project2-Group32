# TSASimulation
## Virginia Tech | CS2114 | Project 2 | Group 32
### Members

- Jacob Fisher ([@fisherjacobc](https://github.com/fisherjacobc))
- Emerson Molina ([@Emolina6416](https://github.com/Emolina6416))
- Sofia Moreno ([@sofia-m06](https://github.com/sofia-m06))
- Austin Quesenberry ([@aqberry2506](https://github.com/aqberry2506))

## How to Compile & Run

Open up VS Code with the Java Language Support Extension Pack, and head to [`Main.java`](/src/simulation/Main.java). From there, you can click on 'Run' and go through the simulation.

VS Code will automatically compile for you, but if you wish to compile manually, you can use `javac **/*.java`.

To run tests, head to the Testing tab (looks like a beaker icon), and you can run the full suite of tests all at once and get a code coverage report.

## Reflection

Added helper methods like isValidBoolean, readValidBoolean, isValidFlightNumber, readValidFlightNumber to help with user input handling in TSASimulation. isProhibitedItem and checkPassengerItems were also added to help with the search and check feature of the passengers.

The line shifts are not fully working yet.

LinkedChain was mostly left as is for now while we figure out shifts

An unknown we have is the LogEntry working on some devices and not on others.

## Next Steps

Our next step is to fully implement the line movements of the simulation so that every addition moves the lines in their own ways for every line.

1. Adding a more advanced items system and check different items with properties inside them
2. Screening the captain and crew for if they are fit to fly
3. Live visual of the simulation showing exactly how the line progresses over time

## Attribution

Ideas on organization came from some GenAI (Gemini), but mostly through discussion with the team

Main, TSASimulation, and tests work: Emerson worked on it with revisions and lookovers by Jacob

LogEntry and LinkedChain work: Jacob worked on it with lookovers by Emerson

Passenger, Crew, and Person classes: Austin worked on this with revisions by Jacob