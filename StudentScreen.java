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
import java.math.BigDecimal;
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
                game.setTitle(gameTitle);
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
                game.setTitle(line);
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
