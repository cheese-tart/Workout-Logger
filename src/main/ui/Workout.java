package ui;

import javax.swing.*;

import model.Exercise;
import model.WorkoutLog;

import persistence.JsonReader;
import persistence.JsonWriter;

import java.awt.*;
import java.awt.event.*;
import java.io.FileNotFoundException;
import java.io.IOException;

// References:

// How to use dialogs:
// https://docs.oracle.com/javase%2Ftutorial%2Fuiswing%2F%2F/components/dialog.html

// Represents view of entire workout window:
// contains split pane with exercises and sets
// and buttons to add and delete exercises and sets
// and button to save workout
public class Workout extends JFrame {
    private static final int WIDTH = 800;
    private static final int HEIGHT = 600;
    private WindowListener listener;
    private JPanel workout;
    private JPanel buttons;
    private JButton addEx;
    private JButton removeEx;
    private JButton addSet;
    private JButton removeSet;
    private JButton saveButton;
    private JButton muscleButton;
    private WorkoutDisplay listView;
    private JsonWriter jsonWriter;
    private JsonReader jsonReader;
    private static final String JSON_STORE = "./data/workoutsave.json";

    // MODIFIES: this
    // EFFECTS: boots up workout logger, with split pane displaying workout
    //          buttons to add and remove, and a save button
    public Workout() {
        setSize(WIDTH, HEIGHT);
        jsonReader = new JsonReader(JSON_STORE);
        jsonWriter = new JsonWriter(JSON_STORE);

        workout = new JPanel();
        workout.setLayout(new BoxLayout(workout, BoxLayout.Y_AXIS));
        add(workout);

        listView = new WorkoutDisplay();
        addInCenter(listView.getSplitPane(), workout);

        makeButtons();
        giveFnToButtons();
        makeButtonPanel();
        makeMusclesWorkedButton();
        makeSaveButton();

        workout.add(buttons);
        addInCenter(muscleButton, workout);
        addInCenter(saveButton, workout);

        makeWindowAdapter();
        addWindowListener(listener);

        setDefaultCloseOperation(EXIT_ON_CLOSE);
    }

    // Referenced code from here:
    // https://stackoverflow.com/questions/39845163/is-windowadapter-is-an-adapter-pattern-implementation-in-java-swing
    // MODIFIES: this
    // EFFECTS: makes WindowAdapter do what I want
    public void makeWindowAdapter() {
        listener = new WindowAdapter() {
            public void windowClosing(WindowEvent e) {
                listView.getWorkoutLog().printEvents();
            }
        };
    }

    // MODIFIES: this
    // EFFECTS: aligns component and adds to container
    public void addInCenter(JComponent component, Container container) {
        component.setAlignmentX(Component.CENTER_ALIGNMENT);
        container.add(component);
    }

    // MODIFIES: this
    // EFFECTS: makes buttons
    public void makeButtons() {
        addEx = new JButton("Add exercise");
        addSet = new JButton("Add set");
        removeEx = new JButton("Remove exercise");
        removeSet = new JButton("Remove set");
    }

    // MODIFIES: this
    // EFFECTS: gives functionality to buttons
    public void giveFnToButtons() {
        addEx.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent ae) {
                listView.addExerciseToWorkout();
            }
        });

        addSet.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent ae) {
                listView.addSetToExercise();
            }
        });

        removeEx.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent ae) {
                listView.removeExerciseFromWorkout();
            }
        });

        removeSet.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent ae) {
                listView.removeSetFromExercise();
            }
        });
    }

    // MODIFIES: this
    // EFFECTS: puts add and remove buttons in grid layout
    public void makeButtonPanel() {
        buttons = new JPanel();
        buttons.setLayout(new GridLayout(2, 2));
        buttons.add(addEx);
        buttons.add(addSet);
        buttons.add(removeEx);
        buttons.add(removeSet);
    }

    // MODIFIES: this
    // EFFECTS: makes button that allows you to see what muscles were worked in workout
    public void makeMusclesWorkedButton() {
        DefaultListModel<String> muscles = new DefaultListModel<>();
        muscleButton = new JButton("View muscles worked");
        muscleButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent ae) {
                for (String s : listView.getWorkoutLog().getMusclesWorked()) {
                    if (!muscles.contains(s)) {
                        muscles.addElement(s);
                    }
                }
                JList<String> muscleList = new JList<>(muscles);
                JFrame frame = new JFrame();
                JPanel panel = new JPanel();
                frame.add(panel);
                panel.add(muscleList);
                frame.pack();
                frame.setTitle("Muscles worked in workout");
                frame.setVisible(true);
                frame.setDefaultCloseOperation(HIDE_ON_CLOSE);
            }
        });
    }

    // MODIFIES: this
    // EFFECTS: makes and adds funcionality to save button
    public void makeSaveButton() {
        saveButton = new JButton("Save workout");
        saveButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent ae) {
                String name = JOptionPane.showInputDialog(null,
                        "Enter name of workout", null);
                listView.getWorkoutLog().setWorkoutName(name);
                try {
                    jsonWriter.open();
                    jsonWriter.write(listView.getWorkoutLog());
                    jsonWriter.close();
                    JOptionPane.showMessageDialog(null, "Saved " + listView.getWorkoutLog().getWorkoutName() 
                            + " to " + JSON_STORE);
                } catch (FileNotFoundException e) {
                    JOptionPane.showMessageDialog(null, "Unable to write to file: " + JSON_STORE);
                }
            }
        });
    }

    // MODIFIES: this
    // EFFECTS: loads saved workout onto workout logger
    public void loadWorkout() {
        try {
            listView.loadWorkout();
            WorkoutLog workout = jsonReader.read();
            for (Exercise e : workout.getExercises()) {
                listView.getExercises().addElement(e);
            }
            JOptionPane.showMessageDialog(null, "Loaded " + workout.getWorkoutName() + " from " + JSON_STORE);
        } catch (IOException e) {
            JOptionPane.showMessageDialog(null, "Unable to read from file: " + JSON_STORE);
        }
    }

}
