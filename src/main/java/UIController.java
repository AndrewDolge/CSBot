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

public UIController(){}


@FXML
private void onStartButtonClicked(ActionEvent event){
    textArea.clear();
   
    
    CSBot csBot = CSBot.getInstance();

    if(!csBot.isRunning()){
        textArea.appendText("starting bot...\n");
        
        csBot.start(Token.getToken());
        textArea.appendText("bot started!\n");

    }

}

@FXML
private void onStopButtonClicked(ActionEvent event){
    CSBot csBot = CSBot.getInstance();

    if(csBot.isRunning()){
        textArea.appendText("shutting down bot...\n");
        csBot.shutdown();
        textArea.appendText("bot shutdown!\n");
        

    }

}


@FXML
private void initialize(){}



}