package main;
//
import javafx.application.Application;
import javafx.stage.Stage;
import javafx.scene.Scene;
import javafx.scene.Parent;
import javafx.fxml.FXMLLoader;
import java.io.IOException;

public class Main extends Application {

    @Override
    public void start(Stage primaryStage) {
        try {
            // Tạo giao diện form tạo tài khoản
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/admin/login.fxml"));
            Parent root = loader.load();
            Scene scene = new Scene(root);

            // Đặt CSS cho giao diện form tạo tài khoản
            scene.getStylesheets().add(getClass().getResource("/admin/loginCss.css").toExternalForm());

            // Đặt độ rộng và độ dài tối thiểu của cửa sổ
            primaryStage.setMinWidth(800); // Độ rộng tối thiểu
            primaryStage.setMinHeight(600); // Độ dài tối thiểu

            primaryStage.setScene(scene);
            primaryStage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        launch(args);
    }
}
