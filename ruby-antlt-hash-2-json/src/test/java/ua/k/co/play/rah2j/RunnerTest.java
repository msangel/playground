package ua.k.co.play.rah2j;

import org.junit.Test;

import static org.junit.Assert.*;

public class RunnerTest {

    @Test
    public void add() {
        // given
        Runner r = new Runner();

        // when
        int с = r.add(1, 2);

        // then
        assertEquals(3, с);
    }
}
