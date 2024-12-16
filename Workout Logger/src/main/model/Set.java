package model;

import org.json.JSONObject;

import persistence.Writable;

// Represents a set with number representing nth set in exercise
// how much weight used and how many reps done
public class Set implements Writable {

    private int set;
    private int weight;
    private int reps;

    // EFFECTS: creates a set with position setNumber
    //          weight used, and reps done
    public Set() {
        this.set = 0;
        this.weight = 0;
        this.reps = 0;
    }

    // REQUIRES: s > 0
    // MODIFIES: this
    // EFFECTS: sets set number to given int
    public void setSetNumber(int s) {
        this.set = s;
    }

    // EFFECTS: returns set number
    public int getSetNumber() {
        return this.set;
    }

    // REQUIRES: w > 0
    // MODIFIES: this
    // EFFECTS: sets weight used in set to given int
    public void setWeight(int w) {
        this.weight = w;
    }

    // EFFECTS: returns weight
    public int getWeight() {
        return this.weight;
    }

    // REQUIRES: r > 0
    // MODIFIES: this
    // EFFECTS: sets number of reps done to givn int
    public void setReps(int r) {
        this.reps = r;
    }

    // EFFECTS: returns reps
    public int getReps() {
        return this.reps;
    }

    @Override
    // EFFECTS: returns this set as a JSON object
    public JSONObject toJson() {
        JSONObject json = new JSONObject();
        json.put("set position", set);
        json.put("weight", weight);
        json.put("reps", reps);
        return json;
    }

    @Override
    public String toString() {
        return set + ". Weight used: " + weight + "lbs " + " Reps done: " + reps;
    }

}
