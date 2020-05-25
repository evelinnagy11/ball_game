package ballgame.state;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class BallTest {

    @Test
    void testOf() {
        assertThrows(IllegalArgumentException.class, () -> Ball.of(-1));
        assertEquals(Ball.EMPTY, Ball.of(0));
        assertEquals(Ball.CUBE1, Ball.of(1));
        assertEquals(Ball.CUBE2, Ball.of(2));
        assertEquals(Ball.CUBE3, Ball.of(3));
        assertEquals(Ball.CUBE4, Ball.of(4));
        assertEquals(Ball.CUBE5, Ball.of(5));
        assertEquals(Ball.CUBE6, Ball.of(6));
        assertThrows(IllegalArgumentException.class, () -> Ball.of(7));
    }

    @Test
    void testRollTo() {
        assertThrows(UnsupportedOperationException.class, () -> Ball.EMPTY.rollTo(Direction.UP));
        assertEquals(Ball.CUBE1, Ball.CUBE1.rollTo(Direction.UP).rollTo(Direction.DOWN));
        assertEquals(Ball.CUBE1, Ball.CUBE1.rollTo(Direction.RIGHT).rollTo(Direction.LEFT));
        assertEquals(Ball.CUBE1, Ball.CUBE1.rollTo(Direction.DOWN).rollTo(Direction.UP));
        assertEquals(Ball.CUBE1, Ball.CUBE1.rollTo(Direction.LEFT).rollTo(Direction.RIGHT));
        assertEquals(Ball.CUBE1, Ball.CUBE1
                .rollTo(Direction.UP)
                .rollTo(Direction.UP)
                .rollTo(Direction.UP)
                .rollTo(Direction.UP)
        );
    }

}