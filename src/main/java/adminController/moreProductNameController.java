package adminController;

import java.io.File;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import db.connect;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.TextField;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.VBox;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;

public class moreProductNameController {

    @FXML
    private TextField productNameField; // Note the lowercase "p"

    @FXML
    public void addProductName() {
        String productName = productNameField.getText();

        if (productName.isEmpty()) {
            showAlert("Please fill in all fields.");
            return;
        }
        String insertSQL = "INSERT INTO productsname (ProductName) VALUES (?)";
        try (Connection connection = connect.getConnection(); PreparedStatement preparedStatement = connection.prepareStatement(insertSQL)) {

            preparedStatement.setString(1, productName);

            int rowsAffected = preparedStatement.executeUpdate();
            if (rowsAffected > 0) {
                System.out.println("Add category successfully!");

                // Success: Show a success message
                showSuccessAlert("Product name added successfully!");

                // Clear the input field after successful addition
                productNameField.clear();
            } else {
                showAlert("Failed to add product name.");
            }

        } catch (SQLException e) {
            showAlert("An error occurred while adding the product name.");
            e.printStackTrace();
        }
    }

//	 public void addImg(Stage primaryStage) {
//		primaryStage.setTitle("Image Uploader");
//
//		// Tạo một ImageView để hiển thị ảnh đã chọn
//		ImageView imageView = new ImageView();
//		imageView.setFitWidth(300); // Điều chỉnh kích thước ảnh
//		imageView.setFitHeight(300);
//
//		// Tạo nút để chọn ảnh
//		Button uploadButton = new Button("Chọn ảnh");
//		uploadButton.setOnAction(new EventHandler<ActionEvent>() {
//			@Override
//			public void handle(ActionEvent event) {
//				FileChooser fileChooser = new FileChooser();
//				fileChooser.getExtensionFilters()
//						.addAll(new FileChooser.ExtensionFilter("Hình ảnh", "*.jpg", "*.jpeg", "*.png"));
//				File selectedFile = fileChooser.showOpenDialog(primaryStage);
//
//				if (selectedFile != null) {
//					// Hiển thị ảnh đã chọn lên ImageView
//					Image image = new Image(selectedFile.toURI().toString());
//					imageView.setImage(image);
//				}
//			}
//		});
//
//		// Tạo layout
//		VBox vbox = new VBox(uploadButton, imageView);
//		Scene scene = new Scene(vbox, 400, 400);
//		primaryStage.setScene(scene);
//
//		primaryStage.show();
//	}
    // insert thành công sẽ hiện
    private void showSuccessAlert(String message) {
        Alert alert = new Alert(AlertType.INFORMATION);
        alert.setTitle("Success");
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    // insert không thành công sẽ hiện
    private void showAlert(String message) {
        Alert alert = new Alert(AlertType.ERROR);
        alert.setTitle("Error");
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    // các hàm gọi giao diện
    public void getFromAddcategory() {
        callLink link = new callLink();
        link.getFormAddCategory();
    }

    public void getFromAddProduct() {
        callLink link = new callLink();
        link.getFormAddProduct();
    }

    public void getFromfromAddSupplier() {
        callLink link = new callLink();
        link.getFormfromAddSupplier();
    }

    public void getFromfromMoreProductName() {
        callLink link = new callLink();
        link.getProductName();
    }
}
