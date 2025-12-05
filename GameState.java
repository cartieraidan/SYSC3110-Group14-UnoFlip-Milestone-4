/**
 * Enum for all major game states for the initial hand, after player draws and once a new round or game starts.
 * Has methods to execute those game states for the Snapshot class.
 *
 * @author Aidan Cartier
 * @version December 5, 2025
 */
public enum GameState {

    HANDLE_INITIAL_HAND {
        @Override
        void runState(GameManager engine) {
            engine.handleInitialHand();
        }
    },
    HANDLE_AFTER_DRAW {
        @Override
        void runState(GameManager engine) {
            engine.handleAfterDraw();
        }
    },
    NEW_ROUND {
        @Override
        void runState(GameManager engine) {
            engine.startGame();
        }
    };


    abstract void runState(GameManager engine);
}
