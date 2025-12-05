# SYSC3110-Group14-UnoFlip

Team Members:
Mark Bowerman
Aidan Cartier
Lawrence Chen
Joshua Heinze

Project Overview
This project involves developing a digital version of Uno Flip, a variation of the classic Uno card game. The work is divided into four milestones with each adding more functionality and features. The project will follow the Model-View-Controller (MVC) architecture and use GitHub for version control and collaboration.

Milestone 2 Objective:
In milestone 2 we aim to implement a GUI based interface for the UNO Flip game we made in the previous milestone. This GUI will be based off of JFrames and will use a view and controller. The main gameplay of this iteration will remain mostly consistant with the milestone 1 iteration. The only difference is that the player will be prompted to press a button before moving to the next player. On the backend, we will implement JUnit testing for planned features that will be covered in future milestones. These tests will lay the groundwork for implementing these features and will ensure the project remains structured and well planned. These tests will all fail initially, but as we implement new features, they will slowly fill out.

Deliverables
All items are submitted together in a single .zip file and include the following:
- README file (this document)
- Source code and executable (.jar)
- UML Diagrams
    - Class diagrams with complete attributes and method signatures
- Documentation
    - UML changes and reasons for said changes
 
Changes Made Since Last Milestone:
- Implementation of Uno flip

Known Issues:
- Game is missing functionality to call UNO when a player is down to one card. This seems like it is not required for this milestone and was intentionally ommited.
- You can not see how many cards oppenents have.

  
Contributions:

Mark Bowerman


Joshua Heinze


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

