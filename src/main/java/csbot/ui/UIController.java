package csbot.ui;

import csbot.core.BotProperties;
import csbot.core.CSBot;
import java.io.File;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;

public class UIController{


@FXML
private TextArea textArea;

@FXML
private Button startButton;

@FXML
private Button stopButton;

private CSBot csBot;

public UIController(){
    
    BotProperties properties = new BotProperties(new File("C:\\Users\\Curious Sight\\Desktop\\bot.properties"));

    this.csBot =  new CSBot(properties);


}




@FXML
private void onStartButtonClicked(ActionEvent event){
    textArea.clear();

    if(!csBot.isRunning()){
        textArea.appendText("starting bot...\n");
        
        csBot.start();
        textArea.appendText("bot started!\n");

    }

}

@FXML
private void onStopButtonClicked(ActionEvent event){

    if(csBot.isRunning()){
        textArea.appendText("shutting down bot...\n");
        csBot.shutdown();
        textArea.appendText("bot shutdown!\n");
        

    }

}


@FXML
private void initialize(){}



}