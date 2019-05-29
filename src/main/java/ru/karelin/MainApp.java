package ru.karelin;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import ru.karelin.gui.StageLoader;

@SpringBootApplication
@EnableFeignClients
public class MainApp extends AbstractJavaFxApplicationSupport {


    private static ClassPathXmlApplicationContext context;

    public static void main(String[] args) throws Exception {
        launchApp(MainApp.class, args);
    }


    public void start(Stage stage) throws Exception {
        StageLoader.loadMain().show();
    }
}
