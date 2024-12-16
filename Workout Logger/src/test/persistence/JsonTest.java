package persistence;

import model.Exercise;
import model.Set;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class JsonTest {

    protected void checkExercise(String name, String muscleHit, 
            ArrayList<Set> sets, Exercise exercise) {
        assertEquals(name, exercise.getExerciseName());
        assertEquals(muscleHit, exercise.getMuscleHit());
        sets = exercise.getSet();
        int count = 0;
        for (Set s : sets) {
            Set actualSet = sets.get(count);
            assertTrue(s.equals(actualSet));
            count++;
        }
    }

    protected void checkSet(int setPos, int weight, int reps, Set set) {
        assertEquals(setPos, set.getSetNumber());
        assertEquals(weight, set.getWeight());
        assertEquals(reps, set.getReps());
    }

}
