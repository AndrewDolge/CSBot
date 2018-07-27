package csbot.ui;

import ch.qos.logback.classic.spi.ILoggingEvent;
import ch.qos.logback.core.AppenderBase;
import javafx.scene.control.TextArea;

/**
 * implements a logback appender that ouptuts to a JavaFX textArea.
 * Reference:
 * http://www.rshingleton.com/javafx-log4j-textarea-log-appender/
 * https://logback.qos.ch/manual/appenders.html
 * 
 */
public class TextAreaAppender extends AppenderBase<ILoggingEvent> {

    private static TextArea textArea = null;

    public static void setTextArea(TextArea textArea) {
        TextAreaAppender.textArea = textArea;
    }

    public void append(ILoggingEvent event) {

        try {

            if (textArea != null) {

                String message = event.getMessage();

                if (message != null && message.length() > 0) {
                    TextAreaAppender.textArea.appendText(message);
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }// append
}// class