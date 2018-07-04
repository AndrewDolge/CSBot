import java.io.IOException;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class UI extends Application{

    @Override
    public void start(Stage stage){

        try{

        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getResource("botUI.fxml"));
        Parent root = loader.load();

        
        
        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
        
        }catch(IOException ioe){
            ioe.printStackTrace();
        }

    }


    public static void main(String[] args){
        launch(args);
    }


}