package ballgame.state;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class BallStateTest {

    private void assertEmptySpace(int expectedEmptyRow, int expectedEmptyCol, BallState state) {
        assertAll(
                () -> assertEquals(expectedEmptyRow, state.getEmptyRow()),
                () -> assertEquals(expectedEmptyCol, state.getEmptyCol())
        );
    }

    @Test
    void testOneArgConstructor_InvalidArg() {
        assertThrows(IllegalArgumentException.class, () -> new BallState(null));
        assertThrows(IllegalArgumentException.class, () -> new BallState(new int[][] {
                {1, 1},
                {1, 0}})
        );
        assertThrows(IllegalArgumentException.class, () -> new BallState(new int[][] {
                {0},
                {1, 2},
                {3, 4, 5}})
        );
        assertThrows(IllegalArgumentException.class, () -> new BallState(new int[][] {
                {1, 2, 3},
                {4, 5, 6},
                {7, 8, 9}})
        );
        assertThrows(IllegalArgumentException.class, () -> new BallState(new int[][] {
                {1, 1, 1},
                {1, 1, 1},
                {1, 1, 1}})
        );
        assertThrows(IllegalArgumentException.class, () -> new BallState(new int[][] {
                {0, 1, 1},
                {1, 1, 1},
                {1, 1, 0}})
        );
    }

    @Test
    void testOneArgConstructor_ValidArg() {
        int[][] a = new int[][] {
                {1, 1, 1},
                {1, 0, 1},
                {1, 1, 1}
        };
        BallState state = new BallState(a);
        assertArrayEquals(new Ball[][] {
                {Ball.CUBE1, Ball.CUBE1, Ball.CUBE1},
                {Ball.CUBE1, Ball.EMPTY, Ball.CUBE1},
                {Ball.CUBE1, Ball.CUBE1, Ball.CUBE1}
        }, state.getTray());
        assertEmptySpace(1, 1, state);
    }

    @Test
    void testIsSolved() {
        assertFalse(new BallState().isSolved());
        assertTrue(new BallState(new int[][] {
                {6, 6, 6},
                {6, 0, 6},
                {6, 6, 6}}).isSolved());
        assertTrue(new BallState(new int[][] {
                {6, 6, 6},
                {6, 6, 6},
                {6, 6, 0}}).isSolved());
    }

    @Test
    void testCanRollToEmptySpace() {
        BallState state = new BallState();
        assertEmptySpace(1, 1, state);
        assertFalse(state.canRollToEmptySpace(0, 0));
        assertTrue(state.canRollToEmptySpace(0, 1));
        assertFalse(state.canRollToEmptySpace(0, 2));
        assertTrue(state.canRollToEmptySpace(1, 0));
        assertFalse(state.canRollToEmptySpace(1, 1));
        assertTrue(state.canRollToEmptySpace(1, 2));
        assertFalse(state.canRollToEmptySpace(2, 0));
        assertTrue(state.canRollToEmptySpace(2, 1));
        assertFalse(state.canRollToEmptySpace(2, 2));
    }

    @Test
    void testGetRollDirection() {
        BallState state = new BallState();
        assertEmptySpace(1, 1, state);
        assertEquals(Direction.UP, state.getRollDirection(2, 1));
        assertEquals(Direction.RIGHT, state.getRollDirection(1, 0));
        assertEquals(Direction.DOWN, state.getRollDirection(0, 1));
        assertEquals(Direction.LEFT, state.getRollDirection(1, 2));
        assertThrows(IllegalArgumentException.class, () -> state.getRollDirection(0, 0));
        assertThrows(IllegalArgumentException.class, () -> state.getRollDirection(0, 2));
        assertThrows(IllegalArgumentException.class, () -> state.getRollDirection(1, 1));
        assertThrows(IllegalArgumentException.class, () -> state.getRollDirection(2, 0));
        assertThrows(IllegalArgumentException.class, () -> state.getRollDirection(2, 2));
    }

    @Test
    void testRollToEmptySpace() {
        BallState state = new BallState();
        assertEmptySpace(1, 1, state); //just to make sure that the empty space is in the middle of the tray
        Ball ball = state.getTray()[0][1]; // the cube at (0, 1) will be rolled to the empty space
        state.rollToEmptySpace(0, 1); // let's roll the cube at (0, 1) to the empty space
        assertEmptySpace(0, 1, state); // the empty space now should be at (0, 1)
        assertEquals(ball.rollTo(Direction.DOWN), state.getTray()[1][1]); // the cube rolled now should be at (1, 1) and its orientation should have been changed accordingly
        state.rollToEmptySpace(1, 1); // let's roll the cube to the empty space again
        assertEmptySpace(1, 1, state); // the empty space now should be at (1, 1) again
        assertEquals(ball, state.getTray()[0][1]); // the cube rolled now should be at (0, 1) again in its original orientation
    }

    @Test
    void testToString() {
        BallState state = new BallState();
        assertEquals("1 1 1 \n"
                + "1 0 1 \n"
                + "1 1 1 \n", state.toString());
    }

}
