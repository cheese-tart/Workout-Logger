package model;

import org.json.JSONArray;
import org.json.JSONObject;
import persistence.Writable;

import java.util.ArrayList;

// Represents an exercise with a primary muscle target
// name of exercise and a list of sets
public class Exercise implements Writable {

    private String primaryMuscleHit;
    private String name;
    private ArrayList<Set> sets;

    // EFFECTS: creates exercise with the given name
    //          muscles hit and empty list of sets
    public Exercise() {
        this.name = null;
        this.sets = new ArrayList<>();
        this.primaryMuscleHit = null;
    }

    // REQUIRES: is a weightlifting exercise
    // MODIFIES: this
    // EFFECTS: sets exercise name to given string
    public void setExerciseName(String s) {
        this.name = s;
    }

    // EFFECTS: returns name of exercise
    public String getExerciseName() {
        return this.name;
    }

    // REQUIRES: s is an actual muscle
    // MODIFIES: this
    // EFFECTS: sets muscle hit to given string
    public void setMuscleHit(String s) {
        this.primaryMuscleHit = s;
    }

    // EFFECTS: returns muscle hit by exercise
    public String getMuscleHit() {
        return this.primaryMuscleHit;
    }

    // MODIFIES: this
    // EFFECTS: adds set to given position inside a list of sets
    public void addSet(Set s, int i) {
        sets.add(i, s);
        EventLog.getInstance().logEvent(new Event("Set in position " + s.getSetNumber()
                    + " with " + s.getWeight() + "lbs used and " + s.getReps() 
                    + " reps done has been added to " + name));
    }

    // EFFECTS: returns sets done for an exercise
    public ArrayList<Set> getSet() {
        return this.sets;
    }

    // MODIFIES: this
    // EFFECTS: removes a set from a list of sets
    //          if the set is inside the list
    //          returns true if set is removed
    public boolean removeSet(Set s) {
        if (sets.contains(s)) {
            sets.remove(s);
            EventLog.getInstance().logEvent(new Event("Set in position " + s.getSetNumber()
                    + " with " + s.getWeight() + "lbs used and " + s.getReps() 
                    + " reps done has been removed from " + name));
            return true;
        }
        return false;
    }

    @Override
    // EFFECTS: returns this exercise as a JSON object
    public JSONObject toJson() {
        JSONObject json = new JSONObject();
        json.put("exercise name", name);
        json.put("primary muscle hit", primaryMuscleHit);
        json.put("sets", setsToJson());
        return json;
    }

    // EFFECTS: returns sets in this exercise as a JSON array
    private JSONArray setsToJson() {
        JSONArray jsonArray = new JSONArray();

        for (Set s : sets) {
            jsonArray.put(s.toJson());
        }

        return jsonArray;
    }

    @Override
    public String toString() {
        return name;
    }

}
