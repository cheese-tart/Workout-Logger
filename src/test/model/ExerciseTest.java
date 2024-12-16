package model;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class ExerciseTest {

    private Exercise exercise;
    private Set set1;
    private Set set2;
    private Set set3;

    @BeforeEach
    void runBefore() {
        this.exercise = new Exercise();
        set1 = new Set();
        set2 = new Set();
        set3 = new Set();
    }

    @Test
    void testConstructor() {
        assertEquals(null, exercise.getExerciseName());
        assertEquals(0, exercise.getSet().size());
        assertEquals(null, exercise.getMuscleHit());
    }

    @Test
    void testNameSetter() {
        exercise.setExerciseName("benchpress");
        assertEquals("benchpress", exercise.getExerciseName());
    }

    @Test
    void testMuscleSetter() {
        exercise.setMuscleHit("chest");
        assertEquals("chest", exercise.getMuscleHit());
    }

    @Test
    void testSetAdder() {
        exercise.addSet(set1, 0);
        exercise.addSet(set2, 1);
        assertTrue(exercise.getSet().size() == 2);
        exercise.addSet(set3, 2);
        assertTrue(exercise.getSet().size() == 3);
    }

    @Test
    void testSetRemover() {
        exercise.addSet(set1, 0);
        exercise.addSet(set2, 1);
        exercise.addSet(set3, 2);
        assertTrue(exercise.getSet().size() == 3);
        exercise.removeSet(set3);
        assertTrue(exercise.getSet().size() == 2);
    }

}
