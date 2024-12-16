package model;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class WorkoutLogTest {

    private WorkoutLog workout;
    private Exercise ex1;
    private Exercise ex2;
    private Exercise ex3;

    @BeforeEach
    void runBefore() {
        workout = new WorkoutLog();
        ex1 = new Exercise();
        ex2 = new Exercise();
        ex3 = new Exercise();
        ex1.setExerciseName("benchpress");
        ex2.setExerciseName("squat");
        ex3.setExerciseName("deadlift");
    }

    @Test
    void testConstructor() {
        assertEquals(null, workout.getWorkoutName());
        assertEquals(0, workout.getExercises().size());
        assertEquals(0, workout.getMusclesWorked().size());
    }

    @Test
    void testNameSetter() {
        workout.setWorkoutName("push");
        assertEquals("push", workout.getWorkoutName());
    }

    @Test
    void testMuscleAdder() {
        assertEquals(0, workout.getMusclesWorked().size());
        workout.addMuscleWorked("chest");
        assertEquals(1, workout.getMusclesWorked().size());
        workout.addMuscleWorked("chest");
        assertEquals(1, workout.getMusclesWorked().size());
    }

    @Test
    void testExerciseAdder() {
        workout.addExercise(ex1);
        workout.addExercise(ex2);
        assertEquals(2, workout.getExercises().size());
        workout.addExercise(ex3);
        assertEquals(3, workout.getExercises().size());
    }

    @Test
    void testExerciseRemover() {
        workout.addExercise(ex1);
        workout.addExercise(ex2);
        workout.addExercise(ex3);
        assertEquals(3, workout.getExercises().size());
        workout.removeExercise(ex2);
        assertEquals(2, workout.getExercises().size());
    }

    @Test
    void testExerciseFinder() {
        assertEquals(null, workout.findExercise("benchpress"));
        workout.addExercise(ex1);
        assertEquals(ex1, workout.findExercise("benchpress"));
        assertEquals(null, workout.findExercise("squat"));
        workout.addExercise(ex2);
        workout.addExercise(ex3);
        assertEquals(ex2, workout.findExercise("squat"));
        assertEquals(ex3, workout.findExercise("deadlift"));
    }

}
