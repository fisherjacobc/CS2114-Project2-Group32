# TSASimulation
## Virginia Tech | CS2114 | Project 2 | Group 32
### Members

- Jacob Fisher ([@fisherjacobc](https://github.com/fisherjacobc))
- Emerson Molina ([@Emolina6416](https://github.com/Emolina6416))
- Sofia Moreno ([@sofia-m06](https://github.com/sofia-m06))
- Austin Quesenberry ([@aqberry2506](https://github.com/aqberry2506))

## Overview
The TSA Simulation is an interactive, console-based queue management system written in Java that simulates a airport security line. The system manages passengers across multiple lines (PreCheck, Standard, and Secondary Screening). It uses a custom generic data structure (`LinkedChain<T>`) to store and process different types of individuals (`Passenger` and `Crew`).

## Features
* **Interactive Menu System**: Users can easily choose from actions `A`, `B`, `C`, or `D` to run the simulation, check passengers, or inspect simulation statistics upon exit.
  * Option `A`: Randomly generates passengers and crew members with attributes.
  * Option `B`: Allows manual creation of custom passengers or crew members with specific attributes.
  * Option `C`: Searches through lines by passenger name to display passenger status and item details.
  * Option `D`: Ends simulation and exits program, with a provided overview.
* **Crew Priority Sorting**: Automatically puts captain and crew members directly to the front of the line.
* **Input Validation & Error Handling**: Custom helper methods validate integers, booleans, and menu selections, catching bad user input without crashing.
* **Item & Detain Management**: Features a dedicated `Item` class and a manual detain system to log flagged passengers for security review.

## How to Compile & Run
Open up VS Code with the Java Language Support Extension Pack, and head to [`Main.java`](/src/simulation/Main.java). From there, you can click on 'Run' and go through the simulation.

VS Code will automatically compile for you, but if you wish to compile manually, you can use `javac **/*.java`.

To run tests, head to the Testing tab (looks like a beaker icon), and you can run the full suite of tests all at once and get a code coverage report.

## System Diagram
Here is our TSA Line Simulation System Diagram showing the routing of Passengers and Crew through the generic LinkedChain<T> lines (PreCheck, Standard, and Secondary Screening).

<img width="404" height="338" alt="System_Diagram_P2" src="https://github.com/user-attachments/assets/afa9e226-c1ff-4893-9e83-8a9d58824b48" />

## Attribution
Ideas on organization came from some GenAI (Gemini), but mostly through discussion with the team

Main, TSASimulation, and tests work: Emerson worked on it with revisions and lookovers by Jacob

LogEntry and LinkedChain work: Jacob worked on it with lookovers by Emerson

Passenger, Crew, and Person classes: Austin worked on this with revisions by Jacob
