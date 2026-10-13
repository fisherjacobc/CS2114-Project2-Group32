Reflection:

Added helper methods like isValidBoolean, readValidBoolean, isValidFlightNumber, readValidFlightNumber to help with user input handling in TSASimulation. isProhibitedItem and checkPassengerItems were also added to help with the search and check feature of the passengers.

The line shifts are not fully working yet.

LinkedChain was mostly left as is for now while we figure out shifts

An unknown we have is the LogEntry working on some devices and not on others. 

Ideas on organization cam from GenAI (Gemini), and some discussion with the team

Main, TSASimulation, and tests work: Emerson worked on it with revisions and lookovers by Jacob

LogEntry and LinkedChain work: Jacob worked on it with lookovers by Emerson

Passenger, Crew, and Person classes: Austin worked on this with revisions by Jacob


Next Steps:

Our next steps is to fully implement the line movements of the simulation so that every addition moves the lines in their own ways for every line. 