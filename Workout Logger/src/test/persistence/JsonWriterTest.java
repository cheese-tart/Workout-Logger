package persistence;

import model.WorkoutLog;
import model.Exercise;
import model.Set;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

import java.io.IOException;
import java.util.ArrayList;

public class JsonWriterTest extends JsonTest {

    @Test
    void testWriterInvalidFile() {
        try {
            WorkoutLog wl = new WorkoutLog();
            JsonWriter writer = new JsonWriter("./data/my\0illegal:fileName.json");
            writer.open();
            fail("IOException was expected");
        } catch (IOException e) {
            // pass
        }
    }

    @Test
    void testWriterEmptyWorkout() {
        try {
            WorkoutLog wl = new WorkoutLog();
            wl.setWorkoutName("empty workout");
            JsonWriter writer = new JsonWriter("./data/testWriterEmptyWorkout.json");
            writer.open();
            writer.write(wl);
            writer.close();

            JsonReader reader = new JsonReader("./data/testWriterEmptyWorkout.json");
            wl = reader.read();
            assertEquals("empty workout", wl.getWorkoutName());
            assertEquals(0, wl.getExercises().size());
            assertEquals(0, wl.getMusclesWorked().size());
        } catch (IOException e) {
            fail("Exception should not have been thrown");
        }
    }

    @Test
    void testWriterWorkoutWithExercises() {
        try {
            WorkoutLog wl = new WorkoutLog();
            wl.setWorkoutName("workout with exercises");
            JsonWriter writer = new JsonWriter("./data/testWriterWorkoutWithExercises.json");

            Exercise ex1 = new Exercise();
            ex1.setExerciseName("benchpress");
            ex1.setMuscleHit("chest");
            wl.addExercise(ex1);

            writer.open();
            writer.write(wl);
            writer.close();

            JsonReader reader = new JsonReader("./data/testWriterWorkoutWithExercises.json");
            wl = reader.read();
            assertEquals("workout with exercises", wl.getWorkoutName());
            ArrayList<Exercise> exercises = wl.getExercises();
            Exercise firstExercise = exercises.get(0);
            ArrayList<Set> sets = firstExercise.getSet();

            assertEquals(1, exercises.size());
            assertEquals(0, sets.size());
            assertEquals(0, wl.getMusclesWorked().size());
        } catch (IOException e) {
            fail();
        }
    }

}
