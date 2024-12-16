package ui;

import java.io.IOException;

import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import model.Exercise;
import model.Set;
import model.WorkoutLog;

import persistence.JsonReader;

// How to use split panes:
// https://docs.oracle.com/javase%2Ftutorial%2Fuiswing%2F%2F/components/splitpane.html

// How to use scroll panes:
// https://docs.oracle.com/javase%2Ftutorial%2Fuiswing%2F%2F/components/scrollpane.html

// How to use lists:
// https://docs.oracle.com/javase%2Ftutorial%2Fuiswing%2F%2F/components/list.html

// JList, ListSelectionListener and DefaultListModel tutorial:
// https://www.youtube.com/watch?v=KOI1WbkKUpQ

// How to write a list selection listener:
// https://docs.oracle.com/javase%2Ftutorial%2Fuiswing%2F%2F/events/listselectionlistener.html

// Guide to DefaultListModel:
// https://docs.oracle.com/javase/8/docs/api/javax/swing/DefaultListModel.html

// Represents view of all exercises and sets in each exercise
public class WorkoutDisplay {
    private JSplitPane workout;
    private JScrollPane exPane;
    private JScrollPane setPane;
    private DefaultListModel<Exercise> exercises;
    private JList<Exercise> exList;
    private DefaultListModel<Set> sets;
    private JList<Set> setList;
    private ListSelectionModel listSelectionModel;
    private WorkoutLog workoutLog;
    private static final String JSON_STORE = "./data/workoutsave.json";

    // I "referenced" code from here:
    // https://docs.oracle.com/javase%2Ftutorial%2Fuiswing%2F%2F/examples/events/ListSelectionDemoProject/src/events/ListSelectionDemo.java
    // MODIFIES: this
    // EFFECTS: creates split pane with empty lists of exercises and sets
    public WorkoutDisplay() {
        workoutLog = new WorkoutLog();

        exercises = new DefaultListModel<>();
        exList = new JList<>(exercises);
        exPane = new JScrollPane(exList);
        exList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

        sets = new DefaultListModel<>();
        setList = new JList<>(sets);
        setPane = new JScrollPane(setList);
        setList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

        listSelectionModel = exList.getSelectionModel();
        listSelectionModel.addListSelectionListener(new SharedListSelectionHandler());

        workout = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, exPane, setPane);
        workout.setDividerLocation(300);
    }

    // MODIFIES: this
    // EFFECTS: loads workout onto workout log
    public void loadWorkout() {
        try {
            JsonReader jsonReader = new JsonReader(JSON_STORE);
            workoutLog = jsonReader.read();
        } catch (IOException e) {
            JOptionPane.showMessageDialog(null, "Unable to read from file: " + JSON_STORE);
        }
    }

    // MODIFIES: this
    // EFFECTS: creates new exercise with name from user input
    //          and adds exercise to workout log
    public void addExerciseToWorkout() {
        Exercise exercise = new Exercise();
        String name = JOptionPane.showInputDialog(null,
                "Enter name of exercise", null);
        String muscle = JOptionPane.showInputDialog(null,
                "Enter main muscle targetted", null);
        if (name == null || muscle == null) {
            // do nothing lmao
        } else {
            exercise.setExerciseName(name);
            exercise.setMuscleHit(muscle);
            workoutLog.addMuscleWorked(muscle);
            workoutLog.addExercise(exercise);
            exercises.addElement(exercise);
        }
    }

    // Guide to turning string to int:
    // https://sentry.io/answers/how-do-i-convert-a-string-to-an-int-in-java/
    // Guide to convert int to string:
    // https://www.javatpoint.com/java-int-to-string#:~:text=We%20can%20convert%20int%20to,method%2C%20string%20concatenation%20operator%20etc.
    // MODIFIES: this
    // EFFECTS: adds set with user inputted set position, weight, and reps done
    //          to workout log
    public void addSetToExercise() {
        Set set = new Set();
        int setPos = Integer.parseInt(JOptionPane.showInputDialog(null, 
                "Enter position of set", null));
        int weight = Integer.parseInt(JOptionPane.showInputDialog(null, 
                "Enter amount of weight used (in lbs)", null));
        int reps = Integer.parseInt(JOptionPane.showInputDialog(null, 
                "Enter number of reps done", null));
        if (String.valueOf(setPos) == null || String.valueOf(weight) == null
                || String.valueOf(reps) == null) {
                    // do nothing lmao
        } else {
            set.setSetNumber(setPos);
            set.setWeight(weight);
            set.setReps(reps);
            workoutLog.getExercises().get(exList.getSelectedIndex()).addSet(set, setPos - 1);
            sets.addElement(set);
        }
    }

    // "Based on" SharedListSelectionHandler class in ListSelectionDemo:
    class SharedListSelectionHandler implements ListSelectionListener {

        // MODIFIES: this
        // EFFECTS: changes list of sets displayed every time a different exercise is selected
        @Override
        public void valueChanged(ListSelectionEvent e) {
            Exercise exercise = exercises.getElementAt(exList.getSelectedIndex());
            sets.clear();
            for (Set s : exercise.getSet()) {
                sets.addElement(s);
            }
        }
    }

    // MODIFIES: this
    // EFFECTS: removes selected exercises and the sets in it
    public void removeExerciseFromWorkout() {
        Exercise jexercise = exercises.getElementAt(exList.getSelectedIndex());
        Exercise exercise = workoutLog.getExercises().get(exList.getSelectedIndex());

        sets.clear();
        workoutLog.removeExercise(exercise);
        exercises.removeElement(jexercise);
    }

    // MODIFIES: this
    // EFFECTS: removes selected set from exercise
    public void removeSetFromExercise() {
        Exercise exercise1 = workoutLog.getExercises().get(exList.getSelectedIndex());
        int i = exercise1.getSet().size();
        if (setList.getSelectedIndex() < i - 1) {
            fixSetOrder();
        }

        Exercise exercise2 = workoutLog.getExercises().get(exList.getSelectedIndex());
        Set set = exercise2.getSet().get(setList.getSelectedIndex());

        workoutLog.getExercises().get(exList.getSelectedIndex()).removeSet(set);
        sets.clear();
        for (Set s : workoutLog.getExercises().get(exList.getSelectedIndex()).getSet()) {
            sets.addElement(s);
        }
    }

    public void fixSetOrder() {
        Exercise exercise = workoutLog.getExercises().get(exList.getSelectedIndex());
        for (int i = setList.getSelectedIndex() + 1; i < exercise.getSet().size(); i++) {
            int setPos = exercise.getSet().get(i).getSetNumber();
            workoutLog.getExercises().get(exList.getSelectedIndex()).getSet().get(i).setSetNumber(setPos - 1);
        }
    }

    // EFFECTS: returns workout log
    public WorkoutLog getWorkoutLog() {
        return workoutLog;
    }

    // EFFECTS: returns split pane of workout
    public JSplitPane getSplitPane() {
        return workout;
    }

    // EFFECTS: returns exercises
    public DefaultListModel<Exercise> getExercises() {
        return exercises;
    }

}
