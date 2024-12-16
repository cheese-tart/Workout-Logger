package model;

import org.json.JSONArray;
import org.json.JSONObject;
import persistence.Writable;

import java.util.ArrayList;

// Represents a workout with a name
// a list of muscles worked
// and a list of exercises
public class WorkoutLog implements Writable {
    
    private ArrayList<String> musclesWorked;
    private String workoutName;
    private ArrayList<Exercise> exercises;

    // EFFECTS: creates workout with a given name
    //          empty list of exercises and empty list of muscles worked
    public WorkoutLog() {
        this.workoutName = null;
        this.exercises = new ArrayList<>();
        this.musclesWorked = new ArrayList<>();
        EventLog.getInstance().logEvent(new Event("Workout has been started"));
    }

    // MODIFIES: this
    // EFFECTS: sets workout name to given string
    public void setWorkoutName(String s) {
        this.workoutName = s;
    }

    // EFFECTS: returns workout name
    public String getWorkoutName() {
        return this.workoutName;
    }

    // MODIFIES: this
    // EFFECTS: adds muscles worked to list of muscles worked
    //          if muscle is not already in list
    public void addMuscleWorked(String m) {
        if (!musclesWorked.contains(m)) {
            musclesWorked.add(m);
        }
    }

    // EFFECTS: returns list of muscles worked in workout 
    public ArrayList<String> getMusclesWorked() {
        return this.musclesWorked;
    }

    // MODIFIES: this
    // EFFECTS: adds given exercise to list of exercises
    public void addExercise(Exercise e) {
        exercises.add(e);
        EventLog.getInstance().logEvent(new Event(e.getExerciseName() + " has been added to workout log"));
    }

    // EFFECTS: returns list of exercises in workout
    public ArrayList<Exercise> getExercises() {
        return this.exercises;
    }

    public Exercise findExercise(String s) {
        for (Exercise exercise : exercises) {
            if (s.equals(exercise.getExerciseName())) {
                return exercise;
            }
        }
        return null;
    }

    // MODIFIES: this
    // EFFECTS: removes given exercise from list of exercises
    public void removeExercise(Exercise e) {
        exercises.remove(e);
        EventLog.getInstance().logEvent(new Event(e.getExerciseName() + " has been removed from workout log"));
    }

    @Override
    // EFFECTS: returns this workout log as a JSON object
    public JSONObject toJson() {
        JSONObject json = new JSONObject();
        json.put("workout name", workoutName);
        json.put("exercises", exercisesToJson());
        json.put("muscles worked", musclesWorkedToJson());
        return json;
    }

    // EFFECTS: returns exercises in this workout log as a JSON array
    private JSONArray exercisesToJson() {
        JSONArray jsonArray = new JSONArray();

        for (Exercise e : exercises) {
            jsonArray.put(e.toJson());
        }

        return jsonArray;
    }

    // EFFECTS: returns muscles worked in this workout log as a JSON array
    private JSONArray musclesWorkedToJson() {
        JSONArray jsonArray = new JSONArray();
    
        for (String s : musclesWorked) {
            jsonArray.put(s);
        }
    
        return jsonArray;
    }

    // EFFECTS: prints all logged events
    public void printEvents() {
        for (Event e : EventLog.getInstance()) {
            System.out.println(e.getDescription());
        }
    }
    
}
