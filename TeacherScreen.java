package screen;

import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.*;
import javafx.stage.Modality;
import javafx.stage.PopupWindow;
import javafx.stage.Stage;
import javafx.scene.image.ImageView;
import javafx.scene.image.Image;
import javafx.scene.text.Font;




import java.awt.*;
import java.io.*;
import java.lang.reflect.Array;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class TeacherScreen {

    private static Game game_aux;
    private static List<Game> game_list=new ArrayList<>(100);
    private static TableView<Game> table;

    public static void Load_Teacher_screen(Stage primaryStage, String teacher_name){

        String name=teacher_name;

        //Reads all games
        Read_From_File(name);

        primaryStage.setTitle("School management application- Teacher Mode");
        Scene scene_default, scene_add, scene_view;

        //Layout for main dev page with view or add games

        Label label=new Label("Welcome, "+name+" ,what would you like to do?");
        label.setFont(new Font("Arial Rounded MT Bold", 30));
        label.setStyle("-fx-font-weight: bold;");

        Button add_game_button=new Button("Add Grade");

        Button view_my_schedule=new Button("View My Schedule");

        Button back_button1=new Button("Back");
        Button back_button2=new Button("Back");

        Button quit_button=new Button("QUIT");
        quit_button.setOnAction(e-> primaryStage.close());

        VBox layout_main=new VBox(30);

        layout_main.getChildren().addAll(label, add_game_button, view_my_schedule,
                quit_button);
        layout_main.setPadding(new Insets(10,10,10,10));
        layout_main.setAlignment(Pos.CENTER);

        scene_default=new Scene(layout_main, 1200,800);

        //Background for Main dev page
        try {
            FileInputStream input_img = new FileInputStream("src/main/resources/images/devtime.jpg");
            Image image = new Image(input_img);
            BackgroundImage bgi=new BackgroundImage(image,
                    BackgroundRepeat.NO_REPEAT,
                    BackgroundRepeat.NO_REPEAT,
                    BackgroundPosition.DEFAULT,
                    BackgroundSize.DEFAULT);
            Background main_dev_bg=new Background(bgi);
            layout_main.setBackground(main_dev_bg);

        }catch (IOException e){}

        back_button1.setOnAction(e-> primaryStage.setScene(scene_default));


        //Layout for add game

        //VBox layout_add=new VBox(10);
        Label label_add=new Label("Please input your grade:");
        label_add.setFont(new Font("Arial Rounded MT Bold", 30));

        GridPane grid=new GridPane();
        grid.setPadding(new Insets(10,10,10,10));
        grid.setVgap(5);
        grid.setHgap(10);

        GridPane.setConstraints(label_add, 0,1);
        GridPane.setConstraints(back_button1, 0,0);

        Label title_input=new Label("Student Name");
        GridPane.setConstraints(title_input, 0,2);
        Label genre_input=new Label("Add grade");
        GridPane.setConstraints(genre_input, 0,3);
        Label price_input=new Label("Add comment");
        GridPane.setConstraints(price_input, 0,4);
        title_input.setFont(new Font("Arial Rounded MT Bold", 20));
        genre_input.setFont(new Font("Arial Rounded MT Bold", 20));
        price_input.setFont(new Font("Arial Rounded MT Bold", 20));

        TextField pass_title=new TextField();

        TextField pass_genre=new TextField();

        TextField pass_price=new TextField();

        GridPane.setConstraints(pass_title, 1,2);
        GridPane.setConstraints(pass_genre, 1,3);
        GridPane.setConstraints(pass_price, 1,4);

        Button add_game_confirm_button=new Button("Add grade");
        GridPane.setConstraints(add_game_confirm_button, 1,5);

        grid.getChildren().addAll(back_button1, label_add, title_input,
                genre_input, price_input, pass_title, pass_genre, pass_price,
                add_game_confirm_button);
        /*layout_add.getChildren().addAll(back_button1, label_add, title_input,
                genre_input, price_input, pass_title, pass_genre, pass_price,
                add_game_confirm_button);*/
        grid.setAlignment(Pos.CENTER);

        scene_add=new Scene(grid, 1200, 800);
