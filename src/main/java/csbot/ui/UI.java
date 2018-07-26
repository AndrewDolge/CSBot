package csbot.ui;
import java.io.File;
import java.io.IOException;

import csbot.core.BotProperties;
import csbot.core.CSBot;
import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.application.Platform;



public class UI extends Application{

        
    @FXML
    private TextArea textArea;
    @FXML
    private Button startButton;
    @FXML
    private Button stopButton;
    @FXML
    private Button fileButton;

    private CSBot csBot;
    private Stage stage;
    private boolean botReady; 

    public CSBot getBot(){
        return this.csBot;
    }
    public Stage getStage(){
        return this.stage;
    }

    public boolean botReady(){
        return this.botReady;
    }

    public void setBotReady(boolean botReady) {
        this.botReady = botReady;
    }

    @Override
    public void start(Stage stage){

        try{

        //load the UI from the fxml
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation( getClass().getClassLoader().getResource("botUI.fxml"));
        Parent root = loader.load();


        //set up the sceen and show the UI
        Scene scene = new Scene(root);
        this.stage = stage;
        stage.setScene(scene);
        stage.show();

        
        }catch(IOException ioe){
            ioe.printStackTrace();
        }

    }//start




@FXML
private void onStartButtonClicked(ActionEvent event){
    textArea.clear();

    if( botReady() && !getBot().isRunning() ){
        textArea.appendText("starting bot...\n");
        
        csBot.start();
        textArea.appendText("bot started!\n");

    }

}

@FXML
private void onStopButtonClicked(ActionEvent event){

    if( botReady() && getBot().isRunning()){
        textArea.appendText("shutting down bot...\n");
        csBot.shutdown();
        textArea.appendText("bot shutdown!\n");
        setBotReady(false);
        

    }

}

@FXML
private void onFileButtonClicked(ActionEvent event){

       //get the bot properties file from the User
       FileChooser fileChooser = new FileChooser();
       fileChooser.setTitle("Open Bot Properties File");
       File file = fileChooser.showOpenDialog(getStage());

       if(file != null){
           this.csBot = new CSBot(new BotProperties(file));

           if(this.getBot() != null){
            textArea.appendText("bot initialized!");
            setBotReady(true);
           }else{
            textArea.appendText("bad file selection! bot not initialized!");
            setBotReady(false);
           }


       }else{
         textArea.appendText("no file selected.");
         setBotReady(false);
       }

}


@FXML
private void initialize(){

 

}



    public static void main(String[] args){
        launch(args);
    }


}//class