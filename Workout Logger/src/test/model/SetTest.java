package model;

import static org.junit.Assert.assertEquals;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class SetTest {

    private Set set;

    @BeforeEach
    void runBefore() {
        this.set = new Set();
    }

    @Test
    void testTestConstructor() {
        assertEquals(0, set.getSetNumber());
        assertEquals(0, set.getWeight());
        assertEquals(0, set.getReps());
    }

    @Test
    void testNumberSetter() {
        set.setSetNumber(1);
        assertEquals(1, set.getSetNumber());
    }

    @Test
    void testWeightSetter() {
        set.setWeight(225);
        assertEquals(225, set.getWeight());
    }

    @Test
    void testRepsSetter() {
        set.setReps(8);
        assertEquals(8, set.getReps());
    }

}
