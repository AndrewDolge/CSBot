package csbot.ui;

import csbot.core.BotProperties;
import csbot.core.CSBot;

import java.io.File;

public class Main{


    public static void main(String[] args){

        BotProperties properties = new BotProperties(new File("C:\\Users\\Curious Sight\\Desktop\\bot.properties"));

        CSBot bot = new CSBot(properties);

        bot.start();

    }


}