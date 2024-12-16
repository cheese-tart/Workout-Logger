package persistence;

import model.WorkoutLog;
import model.Exercise;
import model.Set;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.stream.Stream;

import org.json.*;

// I "referenced" code from JsonSerializationDemo... link:
// https://github.students.cs.ubc.ca/CPSC210/JsonSerializationDemo

// Represents a reader that reads workout log from JSON data stored in file
public class JsonReader {

    private String source;

    // EFFECTS: Constructs a reader to read from source file
    public JsonReader(String source) {
        this.source = source;
    }

    // EFFECTS: reads workout log from file and returns it;
    // throws IOException if an error occurs reading data from file
    public WorkoutLog read() throws IOException {
        String jsonData = readFile(source);
        JSONObject jsonObject = new JSONObject(jsonData);
        return parseWorkoutLog(jsonObject);
    }

    // EFFECTS: reads source file as string and returns it
    private String readFile(String source) throws IOException {
        StringBuilder contentBuilder = new StringBuilder();

        try (Stream<String> stream = Files.lines(Paths.get(source), StandardCharsets.UTF_8)) {
            stream.forEach(s -> contentBuilder.append(s));
        }

        return contentBuilder.toString();
    }

    // EFFECTS: parses workout log from JSON object and returns it
    private WorkoutLog parseWorkoutLog(JSONObject jsonWorkoutLog) {
        String name = jsonWorkoutLog.getString("workout name");
        WorkoutLog wl = new WorkoutLog();
        wl.setWorkoutName(name);
        addExercises(wl, jsonWorkoutLog);
        addMusclesWorked(wl, jsonWorkoutLog);
        return wl;
    }

    // MODIFIES: wl
    // EFFECTS: parses exercises from JSON object and adds them to workout log
    private void addExercises(WorkoutLog wl, JSONObject jsonWorkoutLog) {
        JSONArray exercises = jsonWorkoutLog.getJSONArray("exercises");
        for (Object json : exercises) {
            JSONObject nextExercise = (JSONObject) json;
            addExercise(wl, nextExercise);
        }
    }

    // MODIFIES: wl
    // EFFECTS: parses exercise from JSON object and adds it to workout log
    private void addExercise(WorkoutLog wl, JSONObject jsonExercise) {
        String name = jsonExercise.getString("exercise name");
        String muscleHit = jsonExercise.getString("primary muscle hit");
        Exercise ex = new Exercise();
        ex.setExerciseName(name);
        ex.setMuscleHit(muscleHit);
        addSets(ex, jsonExercise);
        wl.addExercise(ex);
    }

    // MODIFIES: ex
    // EFFECTS: parses sets from JSON object and adds them to exercise, then adds exercise to workout log
    private void addSets(Exercise ex, JSONObject jsonExercise) {
        JSONArray sets = jsonExercise.getJSONArray("sets");
        for (Object json : sets) {
            JSONObject nextSet = (JSONObject) json;
            addSet(ex, nextSet);
        }
    }

    // MODIFIES: ex
    // EFFECTS: parses set from JSON object and adds it to exerciise
    private void addSet(Exercise ex, JSONObject jsonSet) {
        int setPos = jsonSet.getInt("set position");
        int reps = jsonSet.getInt("reps");
        int weight = jsonSet.getInt("weight");
        Set set = new Set();
        set.setSetNumber(setPos);
        set.setReps(reps);
        set.setWeight(weight);
        ex.addSet(set, setPos - 1);
    }

    // MODIFIES: wl
    // EFFECTS: parses muscle worked from JSON object and adds them to workout log
    private void addMusclesWorked(WorkoutLog wl, JSONObject jsonWorkoutLog) {
        JSONArray musclesWorked = jsonWorkoutLog.getJSONArray("muscles worked");
        for (Object json : musclesWorked) {
            String muscleWorked = (String) json;
            wl.addMuscleWorked(muscleWorked);
        }
    }

}
