


# CSBot- A Plugin Based Chat Bot in Java for Discord

## About
This program implements a chat bot for the Chat Program Discord using the Java Discord API.


## Prerequisites

* Java 10 SDK
* Gradle 4.9 
* A Discord Account with a Bot Account Application( [See the Discord Documentation](https://discordapp.com/developers/docs/intro) )


## Run Instructions

You can run the bot by executing the runnable jar file `CSBot-all.jar` found in the build folder of the project.

* In the build folder, open a terminal or command prompt.
* run the command `java -jar CSBot-all.jar`
  * This should generate the necessary directories and files for the bot to run. These should include the  directories `plugins` and `data`, as well as the file `bot.properties`.
* Paste the token for your application to the file `bot.properties`. It should look like: `token=YourTokenHere`
* If you have any plugins jar files, add them to the `plugins` directory. (See the [ExamplePlugin project](https://github.com/AndrewDolge/ExamplePlugin) for an example on how to create one.)
* run the command `java -jar CSBot-all.jar`
* When you want to shut down the bot, type `Ctrl-c` into the terminal.
  

 

