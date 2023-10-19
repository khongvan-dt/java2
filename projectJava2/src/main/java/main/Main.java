package main;

import javafx.application.Application;
import javafx.stage.Stage;
import javafx.scene.Scene;
import javafx.scene.Parent;
import javafx.fxml.FXMLLoader;
import java.io.IOException;
import java.net.URL;

public class Main extends Application {

    private static Scene scene;

    public static Object getPrimaryStage() {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }

    @Override
    public void start(Stage primaryStage) {
        try {
            // Lấy đường dẫn tới file FXML
            URL fxmlUrl = getClass().getResource("/web/home.fxml");
            if (fxmlUrl != null) {
                String fxmlPath = fxmlUrl.toExternalForm();

                // Kiểm tra xem đường dẫn có chứa "/admin/" không
                if (fxmlPath.contains("/admin/")) {
                    primaryStage.setWidth(900); // Đặt chiều rộng là 900
                    primaryStage.setHeight(800); // Đặt chiều cao là 800
                } else if (fxmlPath.contains("/web/")) {
                    primaryStage.setWidth(700); // Đặt chiều rộng là 700
                    primaryStage.setHeight(600); // Đặt chiều cao là 600
                }

                FXMLLoader loader = new FXMLLoader(fxmlUrl);
                Parent root = loader.load();
                scene = new Scene(root);

                // Đặt CSS cho giao diện form tạo tài khoản
                scene.getStylesheets().add(getClass().getResource("/admin/loginCss.css").toExternalForm());

                // Đặt độ rộng và độ dài tối thiểu của cửa sổ
                primaryStage.setScene(scene);
                primaryStage.show();
            } else {
                System.out.println("Không tìm thấy file FXML");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // Chuyển trang 
    public static void setRoot(String fxml) throws IOException {
        Parent root = loadFXML(fxml);
        scene.setRoot(root);
    }

    private static Parent loadFXML(String fxml) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(Main.class.getResource(fxml));
        Parent root = fxmlLoader.load();
        return root;
    }

    public static void main(String[] args) {
        launch(args);
    }
}
