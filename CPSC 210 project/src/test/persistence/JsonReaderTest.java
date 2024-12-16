package persistence;

import model.WorkoutLog;
import model.Exercise;
import model.Set;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

import java.io.IOException;
import java.util.ArrayList;

public class JsonReaderTest extends JsonTest {

    @Test
    void testReaderNonExistentFile() {
        JsonReader reader = new JsonReader("./data/nonExistantFile.json");
        try {
            WorkoutLog wl = reader.read();
            fail("IOException was not caught");
        } catch (IOException e) {
            // pass
        }
    }

    @Test
    void testReaderEmptyWorkout() {
        JsonReader reader = new JsonReader("./data/testReaderEmptyWorkout.json");
        try {
            WorkoutLog wl = reader.read();
            assertEquals("empty workout", wl.getWorkoutName());
            assertEquals(0, wl.getExercises().size());
            assertEquals(0, wl.getMusclesWorked().size());
        } catch (IOException e) {
            fail("An error occurred when fetching workout from file");
        }
    }

    @Test
    void testReaderWorkoutWithExercises() {
        JsonReader reader = new JsonReader("./data/testReaderWorkoutWithExercises.json");
        try {
            WorkoutLog wl = reader.read();
            ArrayList<Exercise> exercises = wl.getExercises();

            Exercise firstExercise = exercises.get(0);
            ArrayList<Set> firstSets = firstExercise.getSet();

            assertEquals("workout with exercises", wl.getWorkoutName());
            assertEquals("chest", wl.getMusclesWorked().get(0));
            assertEquals("back", wl.getMusclesWorked().get(1));

            checkExercise("benchpress", "chest", firstSets, firstExercise);
            assertEquals("benchpress", firstExercise.getExerciseName());
            assertEquals("chest", firstExercise.getMuscleHit());
            assertEquals(1, firstExercise.getSet().size());
            checkSet(1, 245, 3, firstSets.get(0));
            assertEquals(1, firstSets.get(0).getSetNumber());
            assertEquals(245, firstSets.get(0).getWeight());
            assertEquals(3, firstSets.get(0).getReps());
        } catch (IOException e) {
            fail("An error occurred when fetching workout from file");
        }
    }

}
