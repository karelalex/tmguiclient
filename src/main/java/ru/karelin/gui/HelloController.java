package ru.karelin.gui;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;
import ru.karelin.rest.LoginRestControllerI;

import java.io.IOException;

@Component
public class HelloController extends Controller implements ApplicationContextAware
{
    @Autowired
    LoginRestControllerI loginRestController;


    @FXML private TextField loginField;
    @FXML private PasswordField passwordField;
    @FXML private Label messageLabel;
    @FXML private Button projectButton;
    @FXML private Button loginButton;
    @FXML private Button taskButton;

    public void sayHello() {

        String login = loginField.getText();
        String password = passwordField.getText();


        if(loginRestController.login(login, password).isSuccess()){
            messageLabel.setText("Вы успешно вошли в систему");
            projectButton.setVisible(true);
            taskButton.setVisible(true);
            loginButton.setVisible(false);
            loginButton.setManaged(false);
        }
        else {
            messageLabel.setText("Войти не удалось");
        }


    }
    public void showProjects() throws IOException {
        Stage stage = (Stage) messageLabel.getScene().getWindow();
        stage.setTitle("Список проектов");
        stage.setScene(StageLoader.loadProjects());
    }

    public void showTasks() throws IOException {
        Stage stage = (Stage) messageLabel.getScene().getWindow();
        stage.setTitle("Список задач");
        stage.setScene(StageLoader.loadTasks());
    }

}
