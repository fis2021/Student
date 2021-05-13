package screen;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.stage.Stage;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class StudentScreen {
//    private static Scene CreateLibraryScene()
//    {
//        Scene scene = new Scene();
//        Button view_library=new Button("View My Games");
//
//        return scene;
//    }

    private static String name;
    private static Scene landingScene;
    private static Scene libraryScene;
    private static Scene storeScene;
    private static Scene fundsScene;
    private static List<Game> library = new ArrayList<>();
    private static TableView<Game> table;
    private static List<Game> store = new ArrayList<>();
    private static TableView<Game> storeTable;
    private static double credit;

    private static void GetLibrary()
    {
        try {
            File libraryDatabase = new File("src/main/resources/database/" + name + "_library.db");
            BufferedReader reader = new BufferedReader(new FileReader(
                    libraryDatabase));
            String line;
            while ((line = reader.readLine()) != null) {
                Game game = new Game();
                if(line == "\n")
                {
                    continue;
                }
                // read next line
                String[] game_info = line.split(" ");
                // title genre dev
                int game_title_words = 0;
                String gameTitle = "";
                for(int i = 2; i < game_info.length; ++i)
                {
                    gameTitle += game_info[game_title_words++];
                    if(i != game_info.length - 1)
                    {
                        gameTitle += " ";
                    }
                }
                game.setStudent_name(gameTitle);
                game.setGenre(game_info[game_info.length - 2]);
                game.setDevname(game_info[game_info.length - 1]);
                game.setPrice(20.2);
                library.add(game);
            }
            reader.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static void GetStore()
    {
        try {
            File gameDatabase = new File("src/main/resources/database/GameList.db");
            BufferedReader reader = new BufferedReader(new FileReader(
                    gameDatabase));
            String line;
            String devname;
            while ((line = reader.readLine()) != null) {
                Game game = new Game();
                game.setStudent_name(line);
                devname = reader.readLine();
                game.setDevname(devname);
                File gameInfo = new File("src/main/resources/database/" + line.replaceAll("\\s","") + ".db");
                BufferedReader gameInfoReader = new BufferedReader(new FileReader(
                        gameInfo));
                String infoline;
                infoline = gameInfoReader.readLine();
                // game name
                infoline = gameInfoReader.readLine();
                // genre
                game.setGenre(infoline);
                infoline = gameInfoReader.readLine();
                // price
                game.setPrice(Double.parseDouble(infoline));
                infoline = gameInfoReader.readLine();
                // number of reviews
                game.setNumRatings(Integer.parseInt(infoline));
                infoline = gameInfoReader.readLine();
                // average review
                game.setAverageRating(Double.parseDouble(infoline));

                gameInfoReader.close();
                store.add(game);
            }
            reader.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static void CreateLibraryScene(Stage stage) {
        Button back_button = new Button("Back");
        back_button.setOnAction(e -> stage.setScene(landingScene));

        VBox layout_main = new VBox(10);

        HBox label_box = new HBox(10);
        HBox button_box = new HBox(10);
        HBox hbox = new HBox(10);
        Label local_label = new Label("Game library");
        local_label.setTextFill(Color.BLACK);
        label_box.setAlignment(Pos.BASELINE_LEFT);
        label_box.getChildren().addAll(local_label);
        button_box.setAlignment(Pos.BASELINE_RIGHT);
        button_box.getChildren().addAll(back_button);
        hbox.getChildren().addAll(label_box, button_box);

        TableColumn<Game, String> title_column = new TableColumn<>("Student name");
        title_column.setMinWidth(150);
        title_column.setCellValueFactory(new PropertyValueFactory<>("title"));

        TableColumn<Game, String> genre_column = new TableColumn<>("Game genre");
        genre_column.setMinWidth(150);
        genre_column.setCellValueFactory(new PropertyValueFactory<>("genre"));

        TableColumn<Game, String> developer_column = new TableColumn<>("Teacher");
        developer_column.setMinWidth(150);
        developer_column.setCellValueFactory(new PropertyValueFactory<>("devname"));

        ObservableList<Game> my_games = FXCollections.observableArrayList();
        GetLibrary();
        for (Game game : library) {
            my_games.add(game);
        }
        table = new TableView<>();
        table.setItems(my_games);
        table.getColumns().addAll(title_column, genre_column, developer_column);
        table.setPrefHeight(600);
        TextField game_name = new TextField("Game");
        TextField game_rate = new TextField("10.00");
        Button rate_button = new Button("Rate");
        rate_button.setOnAction(e -> {
            String game_chosen = game_name.getText();
            double game_rating = Math.floor(Double.parseDouble(game_rate.getText()) * 100) / 100;
            boolean valid = true;
            if(game_rating < 1.00 || game_rating > 10.00)
            {
                valid = false;
                Popup.Display("Error", "Rating must be between 1.00 and 10.00!", "OK");
            }
            if(valid) {
                boolean found = false;
                for (Game game : library) {
                    if (game.getStudent_name().equals(game_chosen)) {
                        found = true;
                        break;
                    }

                }

                if (!found) {
                    Popup.Display("Error", game_chosen + " is not owned!", "OK");
                } else {
                    for (Game game : store) {
                        if (game_chosen.equals(game.getStudent_name())) {
                            try {
                                File gameInfo = new File("src/main/resources/database/" + game_chosen.replaceAll("\\s", "") + ".db");
                                BufferedReader reader = new BufferedReader(new FileReader(gameInfo));

                                String game_genre, game_price, game_num_ratings, game_avg_rating, game_devname;
                                String game_title;
                                game_title = reader.readLine();
                                game_genre = reader.readLine();
                                game_price = reader.readLine();
                                game_num_ratings = reader.readLine();
                                game_avg_rating = reader.readLine();
                                game_devname = reader.readLine();
                                reader.close();

                                int num_ratings = Integer.parseInt(game_num_ratings);
                                double avg_rating = (Double.parseDouble(game_avg_rating) * num_ratings + game_rating) / ++num_ratings;
                                avg_rating = Math.floor(avg_rating * 100) / 100;

                                BufferedWriter writer = new BufferedWriter(new FileWriter(gameInfo, false));
                                writer.write(game_title + "\n" + game_genre + "\n" + game_price + "\n" + num_ratings + "\n" + avg_rating + "\n" + game_devname + "\n");
                                writer.close();
                                store.clear();
                                CreateStoreScene(stage);
                                Popup.Display("Success", "Successfully rated " + game_chosen + "!", "Cool!");
                                break;
                            } catch (IOException except) {
                                except.printStackTrace();
                            }
                        }
                    }
                }
            }
        });
        layout_main.getChildren().addAll(hbox, table, game_name, game_rate, rate_button);
        table.setPlaceholder(new Label("No games owned!"));
        layout_main.setPadding(new Insets(10, 10, 10, 10));
        back_button.setOnAction(e-> stage.setScene(landingScene));

        try {
            FileInputStream input_view = new FileInputStream("src/main/resources/images/library_customer.jpeg");
            Image image_view = new Image(input_view);
            BackgroundImage bgi_view=new BackgroundImage(image_view,
                    BackgroundRepeat.NO_REPEAT,
                    BackgroundRepeat.NO_REPEAT,
                    BackgroundPosition.DEFAULT,
                    new BackgroundSize(BackgroundSize.AUTO, BackgroundSize.AUTO, true, true ,true ,true));
            Background view_dev_bg=new Background(bgi_view);
            layout_main.setBackground(view_dev_bg);
        }catch(IOException except)
        {
            except.printStackTrace();
        }
        libraryScene = new Scene(layout_main, 750, 750);
    }

    private static void CreateStoreScene(Stage stage)
    {
        Button back_button = new Button("Back");
        back_button.setOnAction(e-> stage.setScene(landingScene));

        VBox layout_main = new VBox(10);

        HBox label_box = new HBox(10);
        HBox button_box = new HBox(10);
        HBox hbox = new HBox(10);
        Label local_label = new Label("Game store");
        local_label.setTextFill(Color.RED);
        label_box.setAlignment(Pos.BASELINE_LEFT);
        label_box.getChildren().addAll(local_label);
        button_box.setAlignment(Pos.BASELINE_RIGHT);
        button_box.getChildren().addAll(back_button);
        hbox.getChildren().addAll(label_box, button_box);

        TableColumn<Game, String> Matter_name = new TableColumn<>("Matter name");
        Matter_name.setMinWidth(150);
        Matter_name.setCellValueFactory(new PropertyValueFactory<>("title"));

        TableColumn<Game, String> genre_column = new TableColumn<>("Comments");
        genre_column.setMinWidth(150);
        genre_column.setCellValueFactory(new PropertyValueFactory<>("genre"));

        TableColumn<Game, String> developer_column = new TableColumn<>("Teacher");
        developer_column.setMinWidth(150);
        developer_column.setCellValueFactory(new PropertyValueFactory<>("devname"));

        TableColumn<Game, String> price_column = new TableColumn<>("Grade");
        price_column.setMinWidth(150);
        price_column.setCellValueFactory(new PropertyValueFactory<>("price"));



        ObservableList<Game> my_games = FXCollections.observableArrayList();
        GetStore();
        for(Game game : store)
        {
            my_games.add(game);
        }
        storeTable = new TableView<>();
        storeTable.setItems(my_games);
        storeTable.getColumns().addAll(Matter_name, genre_column, developer_column, price_column);
        storeTable.setPrefHeight(600);
        TextField game_buy = new TextField("Game");
        Button buy_button = new Button("Buy");
        buy_button.setOnAction(e->{
            String game_chosen = game_buy.getText();
            boolean found = false;
            for(Game game : store)
            {
                if(game_chosen.equals(game.getStudent_name()))
                {
                    found = true;
                    boolean valid = true;
                    for(Game game_from_lib : library)
                    {
                        if(game_from_lib.getStudent_name().equals(game.getStudent_name()))
                        {
                            Popup.Display("Error", game.getStudent_name() + " is already owned!", "OK");
                            valid = false;
                            break;
                        }
                    }
                    if(valid)
                    {
                        if(game.getPrice() <= credit)
                        {
                            try {
                                File userLibrary = new File("src/main/resources/database/" + name + "_library.db");
                                BufferedWriter writer = new BufferedWriter(new FileWriter(userLibrary, true));
                                writer.write(game.getStudent_name() + " " + game.getGenre() + " " + game.getDevname() + "\n");
                                writer.close();

                                File userInfo = new File("src/main/resources/database/" + name + "_info.db");
                                writer = new BufferedWriter(new FileWriter(userInfo, false));
                                credit -= game.getPrice();
                                credit = Math.floor(credit * 100) / 100;
                                writer.write(Double.toString(credit) + "\n");
                                writer.close();
                                Popup.Display("Success", "Successfully bought " + game.getStudent_name() + "!", "Cool!");
                                library.clear();
                                CreateLibraryScene(stage);
                                CreateLandingScene(stage);
                            } catch (IOException except) {
                                except.printStackTrace();
                            }
                        }
                        else
                        {
                            Popup.Display("Error", "Insufficient funds!", "OK");
                        }
                    }
                }
            }
            if(!found)
            {
                Popup.Display("Error", "Could not find game " + game_chosen  + "!", "OK");
            }
        });
        layout_main.getChildren().addAll(hbox, storeTable, game_buy, buy_button);
        storeTable.setPlaceholder(new Label("No games owned!"));
        layout_main.setPadding(new Insets(10, 10, 10, 10));
        back_button.setOnAction(e-> stage.setScene(landingScene));
        try {
            FileInputStream input_view = new FileInputStream("src/main/resources/images/store_customer.jpg");
            Image image_view = new Image(input_view);
            BackgroundImage bgi_view=new BackgroundImage(image_view,
                    BackgroundRepeat.NO_REPEAT,
                    BackgroundRepeat.NO_REPEAT,
                    BackgroundPosition.DEFAULT,
                    new BackgroundSize(BackgroundSize.AUTO, BackgroundSize.AUTO, true, true ,true ,true));
            Background view_dev_bg=new Background(bgi_view);
            layout_main.setBackground(view_dev_bg);
        }catch(IOException except)
        {
            except.printStackTrace();
        }
        storeScene = new Scene(layout_main, 1000, 750);
    }

    private static void CreateFundsScene(Stage stage)
    {
        Button back_button = new Button("Back");
        back_button.setOnAction(e-> stage.setScene(landingScene));
        VBox main_layout = new VBox(10);
        VBox secondary_layout = new VBox(10);
        TextField amount = new TextField("Amount");
        TextField card_info = new TextField("Card info (12 digits)");
        Button purchase = new Button("Purchase");
        purchase.setOnAction(e->{
            double money = Double.parseDouble(amount.getText());
            String code = card_info.getText();
            boolean valid = true;

            if(money <= 0.0)
            {
                valid = false;
                Popup.Display("Error", "Please input a positive amount!", "OK");
            }

            if(valid && code.length() != 12)
            {
                valid = false;
                Popup.Display("Error", "Code must have 12 digits!", "OK");
            }

            if(valid)
            {
                for(int i = 0; i < code.length(); ++i)
                {
                    if(code.charAt(i) < '0' || code.charAt(i) > '9')
                    {
                        valid = false;
                        Popup.Display("Error", "Code must be composed of digits only!", "OK", 400, 150);
                        break;
                    }
                }
            }

            if(valid && (code.charAt(1) <= code.charAt(0) || code.charAt(2) <= code.charAt(1) || code.charAt(3) <= code.charAt(2)))
            {
                valid = false;
                Popup.Display("Error", "First 4 digits of code must be in ascending order!", "OK", 400, 150);
            }

            if(valid && (code.charAt(4) >= '4' || (code.charAt(4) == '3' && code.charAt(5) > '1')))
            {
                valid = false;
                Popup.Display("Error", "Last 8 digits must represent a valid date!", "OK", 400, 150);
            }

            if(valid && (code.charAt(6) >= '2' || (code.charAt(6) == '1' && code.charAt(7) > '2')))
            {
                valid = false;
                Popup.Display("Error", "Last 8 digits must represent a valid date!", "OK", 400, 150);
            }

            if(valid && (code.charAt(8) > '2' || code.charAt(8) < '1'))
            {
                valid = false;
                Popup.Display("Error", "Last 8 digits must represent a valid date!", "OK", 400, 150);
            }

            if(valid) {
                credit += money;
                try {
                    File userInfo = new File("src/main/resources/database/" + name + "_info.db");
                    BufferedWriter writer = new BufferedWriter(new FileWriter(userInfo, false));
                    credit = Math.floor(credit * 100) / 100;
                    writer.write(Double.toString(credit) + "\n");
                    writer.close();
                } catch (IOException except) {
                    except.printStackTrace();
                }
                Popup.Display("Success", money + " funds successfully added to account!", "Cool!");
                CreateLandingScene(stage);
            }
        });

        HBox amount_box = new HBox(10);
        amount_box.setAlignment(Pos.CENTER);
        HBox code_box = new HBox(10);
        code_box.setAlignment(Pos.CENTER);
        HBox purchase_box = new HBox(10);
        purchase_box.setAlignment(Pos.CENTER);
        amount_box.getChildren().addAll(amount);
        code_box.getChildren().addAll(card_info);
        purchase_box.getChildren().addAll(purchase);

        TextField gift_card = new TextField("Gift card");
        Button verify = new Button("Submit gift card");
        verify.setOnAction(e->{
            String code = gift_card.getText();
            boolean valid = true;

            if(valid && code.length() != 8)
            {
                valid = false;
                Popup.Display("Error", "Code must have 8 digits!", "OK");
            }

            if(valid)
            {
                for(int i = 0; i < code.length(); ++i)
                {
                    if(code.charAt(i) < '0' || code.charAt(i) > '9')
                    {
                        valid = false;
                        Popup.Display("Error", "Code must be composed of digits only!", "OK", 400, 150);
                        break;
                    }
                }
            }

            if(valid) {
                try {
                    boolean found = false;
                    double money = 0.0;
                    File userDatabase = new File("src/main/resources/database/giftcards.db");
                    BufferedReader reader = new BufferedReader(new FileReader(
                            userDatabase));
                    String line;
                    while ((line = reader.readLine()) != null) {
                        String[] gift_card_info = line.split(" ");
                        String gift_card_code = gift_card_info[0];
                        double gift_card_value = Double.parseDouble(gift_card_info[1]);
                        if(code.equals(gift_card_code))
                        {
                            found = true;
                            money = gift_card_value;
                            break;
                        }
                    }

                    reader.close();
                    if(found) {
                        credit += money;
                        File userInfo = new File("src/main/resources/database/" + name + "_info.db");
                        BufferedWriter writer = new BufferedWriter(new FileWriter(userInfo, false));
                        credit = Math.floor(credit * 100) / 100;
                        writer.write(Double.toString(credit) + "\n");
                        writer.close();
                        CreateLandingScene(stage);
                        Popup.Display("Success", money + " funds successfully added to account!", "Cool!", 400, 150);
                    }
                    else
                    {
                        Popup.Display("Error", "Gift card was not found in the database!", "OK", 400, 150);
                    }
                } catch (IOException except) {
                    except.printStackTrace();
                }
            }
        });

        HBox gift_card_code_box = new HBox(10);
        gift_card_code_box.setAlignment(Pos.CENTER);
        HBox verify_box = new HBox(10);
        verify_box.setAlignment(Pos.CENTER);
        gift_card_code_box.getChildren().addAll(gift_card);
        verify_box.getChildren().addAll(verify);

        secondary_layout.setAlignment(Pos.CENTER);
        HBox back_button_box = new HBox(10);
        back_button_box.setAlignment(Pos.TOP_LEFT);
        back_button_box.getChildren().addAll(back_button);
        Label label_card = new Label("Purchase credit with credit card");
        label_card.setTextFill(Color.WHITE);
        label_card.setFont(new Font("Arial", 20));
        Label label_gift = new Label("Purchase credit with gift card");
        label_gift.setTextFill(Color.WHITE);
        label_gift.setFont(new Font("Arial", 20));

        secondary_layout.getChildren().addAll(label_card, amount_box, code_box, purchase_box, label_gift, gift_card_code_box, verify_box);
        main_layout.getChildren().addAll(back_button_box, secondary_layout);
        main_layout.setPadding(new Insets(10, 10, 10, 10));
        try {
            FileInputStream input_view = new FileInputStream("src/main/resources/images/mist.jpeg");
            Image image_view = new Image(input_view);
            BackgroundImage bgi_view=new BackgroundImage(image_view,
                    BackgroundRepeat.NO_REPEAT,
                    BackgroundRepeat.NO_REPEAT,
                    BackgroundPosition.DEFAULT,
                    new BackgroundSize(BackgroundSize.AUTO, BackgroundSize.AUTO, true, true ,true ,true));
            Background view_dev_bg=new Background(bgi_view);
            main_layout.setBackground(view_dev_bg);
        }catch(IOException except)
        {
            except.printStackTrace();
        }
        fundsScene = new Scene(main_layout, 500, 350);
    }

    private static void CreateLandingScene(Stage stage)
    {
        Label welcome_label = new Label("Welcome " + name + "!");
        welcome_label.setFont(new Font("Arial MT Rounded Bold", 30));
        Button view_library_button = new Button("My Library");
        Button view_all_games_button = new Button("Game Store");
        Button add_funds = new Button("Purchase credit");

        HBox layout_main = new HBox(10);
        layout_main.getChildren().addAll(view_library_button, view_all_games_button, add_funds);
        layout_main.setAlignment(Pos.CENTER);
        Label label = new Label("Credit: " + credit);
        VBox vlayout = new VBox(10);
        vlayout.getChildren().addAll(welcome_label, layout_main, label);

        view_library_button.setOnAction(e-> stage.setScene(libraryScene));
        view_all_games_button.setOnAction(e-> stage.setScene(storeScene));
        add_funds.setOnAction(e-> stage.setScene(fundsScene));
        vlayout.setPadding(new Insets(25, 25, 25, 25));
        vlayout.setAlignment(Pos.CENTER);
        try {
            FileInputStream input_view = new FileInputStream("src/main/resources/images/landing_customer.jpeg");
            Image image_view = new Image(input_view);
            BackgroundImage bgi_view=new BackgroundImage(image_view,
                    BackgroundRepeat.NO_REPEAT,
                    BackgroundRepeat.NO_REPEAT,
                    BackgroundPosition.DEFAULT,
                    new BackgroundSize(BackgroundSize.AUTO, BackgroundSize.AUTO, true, true ,true ,true));
            Background view_dev_bg=new Background(bgi_view);
            vlayout.setBackground(view_dev_bg);
        }catch(IOException except)
        {
            except.printStackTrace();
        }
        landingScene = new Scene(vlayout, 1280, 720);

    }

    public static void Load_Student_Screen(Stage primaryStage, String Student_name){
        primaryStage.setTitle("Mist - " + Student_name);
        name = Student_name;
        try{
            File customerInfo = new File("src/main/resources/database/" + Student_name + "_info.db");
            BufferedReader customerInfoReader = new BufferedReader(new FileReader(
                    customerInfo));
            String line;
            line = customerInfoReader.readLine();
            credit = Double.parseDouble(line);
        } catch (IOException e) {
            e.printStackTrace();
        }

        CreateLibraryScene(primaryStage);
        CreateStoreScene(primaryStage);
        CreateFundsScene(primaryStage);
        CreateLandingScene(primaryStage);

        primaryStage.setScene(landingScene);
        primaryStage.show();

    }
}
