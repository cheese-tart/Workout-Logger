package ui;

import javax.swing.*;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

// References:

// How to make frames:
// https://docs.oracle.com/javase/tutorial/uiswing/components/frame.html

// How to use panels:
// https://docs.oracle.com/javase/tutorial/uiswing/components/panel.html

// How to make buttons:
// https://docs.oracle.com/javase/tutorial/uiswing/components/button.html

// How to make dialogs:
// https://docs.oracle.com/javase/tutorial/uiswing/components/dialog.html

// How to use BoxLayout:
// https://docs.oracle.com/javase%2Ftutorial%2Fuiswing%2F%2F/layout/box.html

// How to use GridLayout:
// https://docs.oracle.com/javase%2Ftutorial%2Fuiswing%2F%2F/layout/grid.html

// How to use labels:
// https://docs.oracle.com/javase/tutorial/uiswing/components/label.html

// How to use icons:
// https://docs.oracle.com/javase/tutorial/uiswing/components/icon.html

// Home menu of workout logger with GUI
public class Home extends JFrame {
    private static final int WIDTH = 800;
    private static final int HEIGHT = 600;
    private JLabel title;
    private JButton startButton;
    private JButton loadButton;
    private JLabel visual;
    private Workout workout;
    private JPanel cover;
    private JPanel buttons;
    private JPanel home;

    // Method is "based on" createAndShowGui() in FrameDemo:
    // https://docs.oracle.com/javase/tutorial/displayCode.html?code=https://docs.oracle.com/javase/tutorial/uiswing/examples/components/FrameDemoProject/src/components/FrameDemo.java
    // MODIFIES: this
    // EFFECTS: makes cover and buttons and boots up home menu
    public Home() {
        home = new JPanel();
        home.setLayout(new BoxLayout(home, BoxLayout.Y_AXIS));
        add(home);

        makeCoverPanel();
        layoutButtons();

        home.add(cover);
        home.add(buttons);

        setSize(WIDTH, HEIGHT);
        pack();
        setVisible(true);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
    }

    // MODIFIES: this
    // EFFECTS: puts title and visual onto cover panel
    public void makeCoverPanel() {
        cover = new JPanel();
        cover.setLayout(new BoxLayout(cover, BoxLayout.Y_AXIS));

        title = new JLabel("The Ultimate Workout Logger");
        title.setFont(new Font("Sans Serif", Font.BOLD, 50));
        visual = new JLabel(makeImageIcon("visuals/mooscles.gif"));

        addThingToBox(title, cover);
        addThingToBox(visual, cover);   
    }

    // MODIFIES: this
    // EFFECTS: lays out buttons and adds them to button panel
    public void layoutButtons() {
        buttons = new JPanel();
        buttons.setLayout(new GridLayout(1, 2));

        makeButtons();

        buttons.add(startButton);
        buttons.add(loadButton);
    }

    // I "referenced" code from traffic light lecture starter:
    // https://github.students.cs.ubc.ca/CPSC210/C3-LectureLabStarter
    // MODIFIES: this
    // EFFECTS: makes buttons and gives functions to them
    public void makeButtons() {
        startButton = new JButton("Start new workout");
        startButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent ae) {
                setVisible(false);
                workout = new Workout();
                workout.setVisible(true);
            }
        });

        loadButton = new JButton("Load saved workout");
        loadButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent ae) {
                setVisible(false);
                workout = new Workout();
                workout.loadWorkout();
                workout.setVisible(true);
            }
        });
    }

    // Method is "based on" addAButton in BoxLayoutDemo:
    // https://docs.oracle.com/javase%2Ftutorial%2Fuiswing%2F%2F/examples/layout/BoxLayoutDemoProject/src/layout/BoxLayoutDemo.java
    // MODIFIES: this
    // EFFECTS: aligns component and adds to container
    public void addThingToBox(JComponent component, Container container) {
        component.setAlignmentX(Component.CENTER_ALIGNMENT);
        container.add(component);
    }

    // Method is "based on" createImageIcon in IconDemoApp:
    // https://docs.oracle.com/javase/tutorial/displayCode.html?code=https://docs.oracle.com/javase/tutorial/uiswing/examples/components/IconDemoProject/src/components/IconDemoApp.java
    // EFFECTS: returns image found by given path, or null if no image found
    protected ImageIcon makeImageIcon(String path) {
        java.net.URL imgURL = getClass().getResource(path);
        if (imgURL != null) {
            return new ImageIcon(imgURL);
        } else {
            System.err.println("Couldn't find file: " + path);
            return null;
        }
    }
    
}
