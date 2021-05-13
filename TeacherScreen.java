package screen;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.*;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.scene.image.Image;
import javafx.scene.text.Font;


import java.io.*;
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

        Button add_grade_button=new Button("Add Grade");

        Button view_my_schedule=new Button("View My Schedule");

        Button back_button1=new Button("Back");
        Button back_button2=new Button("Back");

        Button quit_button=new Button("QUIT");
        quit_button.setOnAction(e-> primaryStage.close());

        VBox layout_main=new VBox(30);

        layout_main.getChildren().addAll(label, add_grade_button, view_my_schedule,
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
        Label genre_input=new Label("Add comment");
        GridPane.setConstraints(genre_input, 0,3);
        Label price_input=new Label("Add grade");
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

        Button add_grade_confirm_button=new Button("Add grade");
        GridPane.setConstraints(add_grade_confirm_button, 1,5);

        grid.getChildren().addAll(back_button1, label_add, title_input,
                genre_input, price_input, pass_title, pass_genre, pass_price,
                add_grade_confirm_button);

        grid.setAlignment(Pos.CENTER);

        scene_add=new Scene(grid, 1200, 800);

        try {
            FileInputStream input_img_add = new FileInputStream("src/main/resources/images/addgametime.jpg");
            Image image_add = new Image(input_img_add);
            BackgroundImage bgi_add=new BackgroundImage(image_add,
                    BackgroundRepeat.NO_REPEAT,
                    BackgroundRepeat.NO_REPEAT,
                    BackgroundPosition.DEFAULT,
                    BackgroundSize.DEFAULT);
            Background add_dev_bg=new Background(bgi_add);
            grid.setBackground(add_dev_bg);

        }catch (IOException e){}

        add_grade_button.setOnAction(e-> primaryStage.setScene(scene_add));

        add_grade_confirm_button.setOnAction(event -> {
            if(Validate_Game_Info(pass_title.getText(), pass_genre.getText(),
                                                      pass_price.getText())) {
                String title_wo_spaces=pass_title.getText();
                title_wo_spaces.replaceAll("\\s", "");
                game_aux=new Game();
                game_aux.setStudent_name(title_wo_spaces);
                game_aux.setGenre(pass_genre.getText());
                game_aux.setPrice(Double.parseDouble(pass_price.getText()));
                //Copies sold and rating will automatically be randomized
                game_list.add(game_aux);

                Popup.Display("Grade uploaded",
                        "Your grade has been uploaded to out database" +
                                ", please relog.");

                try{
                    BufferedWriter writer=new BufferedWriter(new FileWriter(
                            "src/main/resources/database/GameList.db", true));

                    writer.write(title_wo_spaces
                            +"\n"+name+"\n");
                    writer.flush();
                    writer.close();

                    BufferedWriter game_writer=new BufferedWriter(new FileWriter(
                            "src/main/resources/database/"+
                                    title_wo_spaces
                                    +".db",
                            true));

                    game_writer.write(
                            pass_title.getText()
                            +"\n"+ pass_genre.getText()+
                            "\n"+pass_price.getText()+"\n"+game_aux.getCopies_sold()+
                            "\n"+game_aux.getRating()+"\n"+name+"\n");
                    game_writer.flush();
                    game_writer.close();

                }catch (IOException e){
                    e.printStackTrace();
                }

            }
           /* else {
                Popup.Display("Game info wrong", "Price must be a number. Title must be unique");
            }*/
        });


        //Layout for view games

        //Columns for the table
        TableColumn<Game, String> Student_name=new TableColumn<>("Student name");
        Student_name.setMinWidth(100);
        Student_name.setCellValueFactory(new PropertyValueFactory<>("title"));

        TableColumn<Game, String> Add_comments=new TableColumn<>("Comments");
        Add_comments.setMinWidth(80);
        Add_comments.setCellValueFactory(new PropertyValueFactory<>("genre"));

        TableColumn<Game, String> Add_grade=new TableColumn<>("Grades");
        Add_grade.setMinWidth(50);
        Add_grade.setCellValueFactory(new PropertyValueFactory<>("price"));





        back_button2.setOnAction(e-> primaryStage.setScene(scene_default));

        table=new TableView<>();
        table.setItems(Get_Games(name));
        table.getColumns().addAll(Student_name, Add_comments,
                Add_grade);

        VBox layout_view=new VBox(10);


        Label label_view=new Label("Here is the info about your games, "+name);
        label_view.setFont(new Font("Arial Rounded MT Bold", 30));
        Label edit_game_label=new Label("In case you'd like to edit your game");
        edit_game_label.setFont(new Font("Arial Rounded MT Bold", 30));
        TextField pass_edit=new TextField();
        pass_edit.setPromptText("Name of game to edit");
        Button edit_button=new Button("Edit game");

        label_view.setPadding(new Insets(10,10,10,10));
        layout_view.getChildren().addAll(label_view, back_button2, edit_game_label,
                pass_edit, edit_button, table);
        layout_view.setAlignment(Pos.CENTER);

        edit_button.setOnAction(e->{



            Change_Stuff(pass_edit.getText(), name);
        });

        scene_view=new Scene(layout_view, 1600, 1200);

        try {
            FileInputStream input_view = new FileInputStream("src/main/resources/images/viewgamestime.jpeg");
            Image image_view = new Image(input_view);
            BackgroundImage bgi_view=new BackgroundImage(image_view,
                    BackgroundRepeat.NO_REPEAT,
                    BackgroundRepeat.NO_REPEAT,
                    BackgroundPosition.DEFAULT,
                    BackgroundSize.DEFAULT);
            Background view_dev_bg=new Background(bgi_view);
            layout_view.setBackground(view_dev_bg);

        }catch (IOException e){

        }

        view_my_schedule.setOnAction(e-> primaryStage.setScene(scene_view));


        primaryStage.setScene(scene_default);
        primaryStage.show();

    }



    private static void Change_Stuff(String game_title, String dev) {
        boolean found=false;
        try{
            if(Files.exists(Paths.get("src/main/resources/database/" + game_title
                    + ".db")))

                for(Game g : game_list)
                    if(g.getStudent_name().equals(game_title)){

                        //list
                        found=true;

                        Stage window=new Stage();

                        window.initModality(Modality.APPLICATION_MODAL);
                        window.setTitle("Editting Game");

                        Label label=new Label("You may change "+game_title+
                                "'s genre and price");
                        label.setFont(new Font("Arial Rounded MT Bold", 30));
                        Button okbutton=new Button("OK");

                        TextField pass_new_genre=new TextField(g.getGenre());
                        TextField pass_new_price=new TextField(String.valueOf(g.getPrice()));

                        okbutton.setOnAction(e->{
                            g.setGenre(pass_new_genre.getText());
                            g.setPrice(Double.parseDouble(pass_new_price.getText()));
                            Popup.Display("Game edited", "Please relog to save changes");
                        });

                        VBox layout=new VBox(10);
                        layout.getChildren().addAll(label, pass_new_genre,
                                pass_new_price, okbutton);
                        layout.setAlignment(Pos.CENTER);

                        Scene scene=new Scene(layout, 1600, 1000);

                        try {
                            FileInputStream input_edit = new FileInputStream("src/main/resources/images/editgametime.jpg");
                            Image image_view = new Image(input_edit);
                            BackgroundImage bgi_edit=new BackgroundImage(image_view,
                                    BackgroundRepeat.NO_REPEAT,
                                    BackgroundRepeat.NO_REPEAT,
                                    BackgroundPosition.DEFAULT,
                                    BackgroundSize.DEFAULT);
                            Background edit_dev_bg=new Background(bgi_edit);
                            layout.setBackground(edit_dev_bg);

                        }catch (IOException e){}

                        window.setScene(scene);
                        window.showAndWait();


                        BufferedWriter edit_file_writer=new BufferedWriter(new FileWriter(
                                "src/main/resources/database/" + game_title
                                        + ".db", false));
                        edit_file_writer.write(g.getStudent_name()+"\n"+g.getGenre()+"\n"+
                                g.getPrice()+"\n"+g.getCopies_sold()+"\n"+g.getRating()+
                                "\n"+dev);

                        edit_file_writer.close();
                    }

            if(!found)
                Popup.Display("Uh oh!", "No game with that name");

        } catch (IOException e){

        }
    }

    private static void Read_From_File(String dev) {
        try{
            File games_info=new File("src/main/resources/database/GameList.db");
            Scanner reader=new Scanner((games_info));
            while(reader.hasNextLine()){

                String path_name=reader.nextLine();
                if(Files.exists(Paths.get("src/main/resources/database/" +
                        path_name.replaceAll("\\s","") + ".db"))) {

                    File individual_game = new File("src/main/resources/database/" +
                            path_name.replaceAll("\\s","")
                            + ".db");

                    Scanner game_reader=new Scanner(individual_game);

                    game_aux = new Game();

                    //Name of game
                    game_aux.setStudent_name(game_reader.nextLine());
                    //Genre
                    if (game_reader.hasNextLine())
                        game_aux.setGenre(game_reader.nextLine());
                    //Price
                    if (game_reader.hasNextLine())
                        game_aux.setPrice(Double.parseDouble(game_reader.nextLine()));
                    //Copies sold
                    if (game_reader.hasNextLine())
                        game_aux.setCopies_sold(Integer.parseInt(game_reader.nextLine()));
                    //Avg rating
                    if (game_reader.hasNextLine())
                        game_aux.setRating(Double.parseDouble(game_reader.nextLine()));
                    //Owner
                    if (game_reader.hasNextLine())
                        game_aux.setOwner(game_reader.nextLine());

                    //Adding game to dev's list of games
                    try {
                        if (game_aux.getOwner().equals(dev))
                            game_list.add(game_aux);
                    } catch (NullPointerException e) {

                    }
                    game_reader.close();
                }
                //Getting rid of the username
                reader.hasNextLine();
            }

            reader.close();
        }catch (FileNotFoundException e){
            System.out.println("File not found");
            e.printStackTrace();
        }
    }

    private static boolean Validate_Game_Info(String name, String genre, String price) {
        try{
            double price_is_double=Double.parseDouble(price);

        }catch (NumberFormatException e){
            return  false;
        }

        for(Game g : game_list)
            if(g.getStudent_name().equals(name))
                return false;
        return true;
    }

    public static ObservableList<Game> Get_Games(String dev) {
        ObservableList<Game> my_games = FXCollections.observableArrayList();
        for (Game game : game_list)
            my_games.add(game);
        return my_games;
    }
}
