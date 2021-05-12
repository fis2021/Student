package model;

import javafx.stage.Stage;
import screen.StudentScreen;

public class Student {
    public static void openStudent(String Student_name, Stage primaryStage)
    {
        StudentScreen.Load_Student_Screen(primaryStage, Student_name);
        return;
    }
}
