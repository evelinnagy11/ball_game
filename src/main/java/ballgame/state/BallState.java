package ballgame.state;

import lombok.AccessLevel;
import lombok.Data;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

/**
 * Class representing the state of the puzzle.
 */
@Data
@Slf4j
public class BallState implements Cloneable {

    /**
     * The array representing the initial configuration of the tray.
     */
    public static final int[][] INITIAL = {
            {2, 4, 10, 3, 4},
            {5, 0, 3, 0, 8},
            {6, 0, 0, 0, 4},
            {2, 0, 7, 0, 9},
            {6, 7, 11, 6, 8}
    };

    /**
     * The array representing a near-goal configuration of the tray.
     */
    public static final int[][] NEAR_GOAL = {
            {2, 4, 10, 3, 4},
            {5, 0, 3, 0, 8},
            {6, 0, 0, 0, 4},
            {2, 0, 7, 0, 9},
            {6, 7, 11, 6, 8}
    };

    /**
     * The array storing the current configuration of the tray.
     */
    @Setter(AccessLevel.NONE)
    private Ball[][] tray;

    /**
     * The row of the empty space.
     */
    @Setter(AccessLevel.NONE)
    private int emptyRow;

    /**
     * The column of the empty space.
     */
    @Setter(AccessLevel.NONE)
    private int emptyCol;

    /**
     * Creates a {@code RollingCubesState} object representing the (original)
     * initial state of the puzzle.
     */
    public BallState() {
        this(INITIAL);
    }

    /**
     * Creates a {@code RollingCubesState} object that is initialized it with
     * the specified array.
     *
     * @param a an array of size 3&#xd7;3 representing the initial configuration
     *          of the tray
     * @throws IllegalArgumentException if the array does not represent a valid
     *                                  configuration of the tray
     */
    public BallState(int[][] a) {
        if (!isValidTray(a)) {
            throw new IllegalArgumentException();
        }
        initTray(a);
    }

    private boolean isValidTray(int[][] a) {
        if (a == null || a.length != 5) {
            return false;
        }
        boolean foundEmpty = false;
        for (int[] row : a) {
            if (row == null || row.length != 5) {
                return false;
            }
            for (int space : row) {
                if (space < 0 || space >= Ball.values().length) {
                    return false;
                }
                if (space == Ball.EMPTY.getValue()) {
                    if (foundEmpty) {
                        return false;
                    }
                    foundEmpty = true;
                }
            }
        }
        return foundEmpty;
    }

    private void initTray(int[][] a) {
        this.tray = new Ball[5][5];
        for (int i = 0; i < 5; ++i) {
            for (int j = 0; j < 5; ++j) {
                if ((this.tray[i][j] = Ball.of(a[i][j])) == Ball.EMPTY) {
                    emptyRow = i;
                    emptyCol = j;
                }
            }
        }
    }

    /**
     * Checks whether the puzzle is solved.
     *
     * @return {@code true} if the puzzle is solved, {@code false} otherwise
     */
    public boolean isSolved() {
        for (Ball[] row : tray) {
            for (Ball ball : row) {
                if (ball != Ball.CUBE6 && ball != Ball.EMPTY) {
                    return false;
                }
            }
        }
        return true;
    }

    /**
     * Returns whether the cube at the specified position can be rolled to the
     * empty space.
     *
     * @param row the row of the cube to be rolled
     * @param col the column of the cube to be rolled
     * @return {@code true} if the cube at the specified position can be rolled
     * to the empty space, {@code false} otherwise
     */
    public boolean canRollToEmptySpace(int row, int col) {
        return 0 <= row && row <= 4 && 0 <= col && col <= 4 &&
                Math.abs(emptyRow - row) + Math.abs(emptyCol - col) == 1;
    }

    /**
     * Returns the direction to which the cube at the specified position is
     * rolled to the empty space.
     *
     * @param row the row of the cube to be rolled
     * @param col the column of the cube to be rolled
     * @return the direction to which the cube at the specified position is
     * rolled to the empty space
     * @throws IllegalArgumentException if the cube at the specified position
     * can not be rolled to the empty space
     */
    public Direction getRollDirection(int row, int col) {
        if (! canRollToEmptySpace(row, col)) {
            throw new IllegalArgumentException();
        }
        return Direction.of(emptyRow - row, emptyCol - col);
    }

    /**
     * Rolls the cube at the specified position to the empty space.
     *
     * @param row the row of the cube to be rolled
     * @param col the column of the cube to be rolled
     * @throws IllegalArgumentException if the cube at the specified position
     * can not be rolled to the empty space
     */
    public void rollToEmptySpace(int row, int col) {
        Direction direction = getRollDirection(row, col);
        log.info("Cube at ({},{}) is rolled to {}", row, col, direction);
        tray[emptyRow][emptyCol] = tray[row][col].rollTo(direction);
        tray[row][col] = Ball.EMPTY;
        emptyRow = row;
        emptyCol = col;
    }

    public BallState clone() {
        BallState copy = null;
        try {
            copy = (BallState) super.clone();
        } catch (CloneNotSupportedException e) {
        }
        copy.tray = new Ball[tray.length][];
        for (int i = 0; i < tray.length; ++i) {
            copy.tray[i] = tray[i].clone();
        }
        return copy;
    }

    public String toString() {
        StringBuilder sb = new StringBuilder();
        for (Ball[] row : tray) {
            for (Ball ball : row) {
                sb.append(ball).append(' ');
            }
            sb.append('\n');
        }
        return sb.toString();
    }

    public static void main(String[] args) {
        BallState state = new BallState();
        System.out.println(state);
        state.rollToEmptySpace(0, 1);
        System.out.println(state);
    }

}
