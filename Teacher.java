package model;

import javafx.stage.Stage;
import screen.TeacherScreen;

public class Teacher {
    public static void openTeacher(String teacher_name, Stage primaryStage)
    {
        TeacherScreen.Load_Teacher_screen(primaryStage, teacher_name);
        return;
    }
}
