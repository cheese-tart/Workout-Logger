package ui.console;

import model.WorkoutLog;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Scanner;
import persistence.JsonReader;
import persistence.JsonWriter;

import model.Exercise;
import model.Set;

// Workout logger application
public class WorkoutLogApp {

    private static final String JSON_STORE = "./data/workoutsave.json";
    private WorkoutLog workout;
    private Exercise exercise;
    private Set set;
    private Scanner input;
    private JsonWriter jsonWriter;
    private JsonReader jsonReader;

    // EFFECTS: runs workout logger application
    public WorkoutLogApp() throws FileNotFoundException {
        jsonWriter = new JsonWriter(JSON_STORE);
        jsonReader = new JsonReader(JSON_STORE);
        runWorkoutApp();
    }

    // MODIFIES: this
    // EFFECTS: processes user input
    private void runWorkoutApp() {
        workout = new WorkoutLog();
        input = new Scanner(System.in);
        boolean keepRunning = true;
        String order = null;

        while (keepRunning) {
            displayMenu();
            order = input.next();
            order = order.toLowerCase();

            if (order.equals("x")) {
                keepRunning = false;
            } else {
                processOrder(order);
            }
        }
        System.out.println("\nGoodbye.");
    }

    // MODIFIES: this
    // EFFECTS: sets name of workout as inputted line
    private void nameWorkout() {
        String name = "";
        while (name.equals("")) {
            System.out.println("\nEnter a name for your workout:");
            name = input.nextLine();
        }
        workout.setWorkoutName(name);
    }

    // MODIFIES: this
    // EFFECTS: processes user orders
    private void processOrder(String order) {
        if (order.equals("+")) {
            addExercise();
        } else if (order.equals("-")) {
            removeExercise();
        } else if (order.equals("v")) {
            viewWorkout();
        } else if (order.equals("a")) {
            addSets();
        } else if (order.equals("r")) {
            removeSets();
        } else if (order.equals("s")) {
            saveWorkoutLog();
        } else if (order.equals("l")) {
            loadWorkoutLog();
        } else {
            System.out.println("\nInvalid input.");
        }
    }

    // EFFECTS: displays menu of choices to user
    private void displayMenu() {
        System.out.println("\nEnter + to add exercise.");
        System.out.println("Enter - to remove an exercise.");
        System.out.println("Enter v to view workout.");
        System.out.println("Enter a to add a set.");
        System.out.println("Enter r to remove a set.");
        System.out.println("Enter s to save workout.");
        System.out.println("Enter l to load workout.");
        System.out.println("Enter x to quit.");
    }

    // MODIFIES: this
    // EFFECTS: creates new exercise and adds it to workout log
    private void addExercise() {
        exercise = new Exercise();
        giveExerciseName();
        giveMuscleHit();
        workout.addExercise(exercise);
    }

    // EFFECTS: prints a log of the entire workout
    private void viewWorkout() {
        for (Exercise exercise : workout.getExercises()) {
            System.out.println("");
            System.out.println(exercise.getExerciseName());
            for (Set set : exercise.getSet()) {
                System.out.println(set.getSetNumber() + ". Weight used: " + set.getWeight() + "lbs"
                        + " Reps done: " + set.getReps());
            }
        }
        System.out.println("");
        for (String muscleWorked : workout.getMusclesWorked()) {
            System.out.println("Muscle worked in workout: " + muscleWorked);
        }
    }

    // MODIFIES: this
    // EFFECTS: Finds exercise in workout log from given user inputted line
    //          Removes exercise if exercise found
    //          Does nothing and returns to menu if otherwisee
    private void removeExercise() {
        String exerciseName = "";
        while (exerciseName.equals("")) {
            System.out.println("\nEnter the name of exercise you want to delete:");
            exerciseName = input.nextLine();
        }
        if (workout.findExercise(exerciseName) != null) {
            workout.getExercises().remove(workout.findExercise(exerciseName));
            System.out.println("\nExercise removed.");
        } else if (workout.findExercise(exerciseName) == null) {
            System.out.println("\nNo exercise found.");
        }
    }

    // MODIFIES: this
    // EFFECTS: sets name of exercise as user inputted line
    private void giveExerciseName() {
        String name = "";
        while (name.equals("")) {
            System.out.println("\nEnter exercise name:");
            name = input.nextLine();
        }
        exercise.setExerciseName(name);
    }

    // MODIFIES: this
    // EFFECTS: sets muscle worked of exercise as user inputted line
    //          adds the muscle hit to list of muscles hit in workout
    private void giveMuscleHit() {
        String muscle = "";
        while (muscle.equals("")) {
            System.out.println("\nEnter primary muscle targetted:");
            muscle = input.nextLine();
        }
        exercise.setMuscleHit(muscle);
        workout.addMuscleWorked(exercise.getMuscleHit());
    }

    // REQUIRES: position is 1 bigger than set list size
    // MODIFIES: this
    // EFFECTS: adds set to given position in an exercise's list of sets
    //          if exercise is found
    //          otherwise prints message for user and returns to menu
    private void addSets() {
        set = new Set();
        int position = 0;
        String exerciseName = "";
        while (exerciseName.equals("")) {
            System.out.println("\nEnter the name of exercise you want to add set to:");
            exerciseName = input.nextLine();
        }
        if (workout.findExercise(exerciseName) != null) {
            System.out.println("\nExercise found.");
            while (position == 0) {
                System.out.println("\nEnter the nth position of this set:");
                position = input.nextInt();
            }
            set.setSetNumber(position);
            giveSetWeight();
            giveSetReps();
            workout.findExercise(exerciseName).addSet(set, position - 1);
            System.out.println("\nSet added.");
        } else if (workout.findExercise(exerciseName) == null) {
            System.out.println("\nNo exercise found.");
        }
    }

    // MODIFIES: this
    // EFFECTS: sets weight used in set to user inputted int
    private void giveSetWeight() {
        int weight = 0;
        while (weight == 0) {
            System.out.println("\nEnter how much weight you used:");
            weight = input.nextInt();
        }
        set.setWeight(weight);
    }

    // MODIFIES: this
    // EFFECTS: sets number of reps done in set to user inputted int
    private void giveSetReps() {
        int reps = 0;
        while (reps == 0) {
            System.out.println("\nEnter how many reps you did:");
            reps = input.nextInt();
        }
        set.setReps(reps);
    }

    // MODIFIES: this
    // EFFECTS: Removes set from exercise if exercise is found AND
    //          setPos <= size of list of sets AND list is not empty
    //          Otherwise prints a message for user and returns to menu
    private void removeSets() {
        int setPos = 0;
        String exerciseName = "";
        while (exerciseName.equals("")) {
            System.out.println("\nEnter the name of exercise you want to remove set from:");
            exerciseName = input.nextLine();
        }
        if (workout.findExercise(exerciseName) != null) {
            System.out.println("\nExercise found.");
            while (setPos == 0) {
                System.out.println("\nEnter the position of the set you want to remove:");
                setPos = input.nextInt();
            }
            if (workout.findExercise(exerciseName).getSet().isEmpty()) {
                System.out.println("\nThere are no sets to remove.");
            } else if (workout.findExercise(exerciseName).getSet().size() < setPos) {
                System.out.println("\nSet does not exist.");
            } else if (workout.findExercise(exerciseName)
                    .removeSet(workout.findExercise(exerciseName).getSet().get(setPos - 1))) {
                System.out.println("\nSet has been removed.");
            }
        } else if (workout.findExercise(exerciseName) == null) {
            System.out.println("\nNo exercise found.");
        }
    }

    // EFFECTS: saves the workroom to file
    private void saveWorkoutLog() {
        nameWorkout();
        try {
            jsonWriter.open();
            jsonWriter.write(workout);
            jsonWriter.close();
            System.out.println("Saved " + workout.getWorkoutName() + " to " + JSON_STORE);
        } catch (FileNotFoundException e) {
            System.out.println("Unable to write to file: " + JSON_STORE);
        }
    }

    // MODIFIES: this
    // EFFECTS: loads workroom from file
    private void loadWorkoutLog() {
        try {
            workout = jsonReader.read();
            System.out.println("Loaded " + workout.getWorkoutName() + " from " + JSON_STORE);
        } catch (IOException e) {
            System.out.println("Unable to read from file: " + JSON_STORE);
        }
    }

}
