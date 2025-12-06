# SYSC3110-Group14-UnoFlip

Team Members:
Mark Bowerman
Aidan Cartier
Lawrence Chen
Joshua Heinze

Project Overview
This project involves developing a digital version of Uno Flip, a variation of the classic Uno card game. The work is divided into four milestones with each adding more functionality and features. The project will follow the Model-View-Controller (MVC) architecture and use GitHub for version control and collaboration.

Milestone 4 Objective:
In milestone 4 we aim to implement the features of saving game states, undo and redo actions, and save and load games.
The Controller handler all the game state saving and loading by listening to whenever the game state in GameManager changed.
We did this by creating another class named Snapshot which held the state of the game by copying the GameManager and all 
necessary attributes required to keep the game stable.

Deliverables
All items are submitted together in a single .zip file and include the following:
- README file (this document)
- Source code and executable (.jar)
- UML Diagrams
    - Class diagrams with complete attributes and method signatures
- Documentation
    - UML changes and reasons for said changes
 
Changes Made Since Last Milestone:
- Implementation of GameState enum
- Implementation of StateListener interface
- Implementation of more UI buttons
- Implementation of saving states with undo and redo functions
- Implementation of saving the game as a binary

Known Issues:
- Game is missing functionality to call UNO when a player is down to one card. This seems like it is not required for this milestone and was intentionally ommited.
- You can not see how many cards oppenents have.

  
Contributions:

Mark Bowerman
- Initial implementation of deepCopy() functions
- Initial implementation of save/load and undo/redo
- Class UML Diagram
  
Joshua Heinze
- Implemented test cases for save/load and undo/redo
- Modified Controller to facilitate testing

Lawrence Chen 


Aidan Cartier
- Implemented StateListener, GameState.
- Implemented logic for saving snapshots in gameManager and how to execute when reloading a snapshot.
- Implemented all GUI functionality for undo and redo buttons.
- Fixed bugs surrounding the undo and redo implementations.

Roadmap:
- Milestone 1 - Oct 27 - Text-based playable Uno game
- Milestone 2 - Nov 10 - GUI version with unit testing
- Milestone 3 - Nov 24 - Uno Flip rules + AI players
- Milestone 4 - Dec5 - Undo/redo, replay, save/load functions 

Additional Notes:

