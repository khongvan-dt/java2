package adminController;

import com.mysql.cj.conf.IntegerProperty;
import com.mysql.cj.conf.StringProperty;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import javafx.util.StringConverter;
import db.connect;
import java.io.File;
import java.io.IOException;
import java.lang.annotation.Annotation;
import java.net.URL;
import java.sql.ResultSet;
import java.util.HashMap;
import java.util.Map;
import java.util.ResourceBundle;
import javafx.application.Application;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.TextField;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.layout.VBox;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import jdk.jfr.Category;
import main.Main;

public class addProductController extends Application {


    @FXML
    private void initialize() {
        try (Connection connection = connect.getConnection()) {
            // Tạo một truy vấn SQL để lấy dữ liệu danh mục sản phẩm từ cơ sở dữ liệu
            String selectCategory = "SELECT categoryId, categoryName FROM category";

            // Tạo một PreparedStatement để thực thi truy vấn SQL
            PreparedStatement preparedStatement = connection.prepareStatement(selectCategory);

            // Thực thi truy vấn và lấy kết quả
            ResultSet resultSet = preparedStatement.executeQuery();
            ObservableList<String> categoryNames = FXCollections.observableArrayList();

            // Duyệt qua kết quả truy vấn và thêm tên danh mục vào danh sách
            while (resultSet.next()) {
                String categoryName = resultSet.getString("categoryName");
                categoryNames.add(categoryName);
            }

            // Đặt danh sách tên danh mục vào ComboBox để hiển thị trong giao diện người dùng
            fieldViewProductCategoryId.setItems(categoryNames);

            // Tạo một truy vấn SQL để lấy dữ liệu tên sản phẩm từ cơ sở dữ liệu
            String selectProduct = "SELECT ProductNameId, ProductName FROM ProductsName";

            // Tạo một PreparedStatement để thực thi truy vấn SQL
            PreparedStatement preparedStatement2 = connection.prepareStatement(selectProduct);

            // Thực thi truy vấn và lấy kết quả
            ResultSet resultSet2 = preparedStatement2.executeQuery();
            ObservableList<String> productNames = FXCollections.observableArrayList();

            // Duyệt qua kết quả truy vấn và thêm tên sản phẩm vào danh sách
            while (resultSet2.next()) {
                String productsName = resultSet2.getString("ProductName");
                productNames.add(productsName);
            }

            // Đặt danh sách tên sản phẩm vào ComboBox để hiển thị trong giao diện người dùng
            fieldViewProductName.setItems(productNames);

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

//insert 
    @FXML
    private ComboBox<String> fieldViewProductCategoryId;

    @FXML
    private ComboBox<String> fieldViewProductName;

    @FXML
    private TextField fieldViewProductPrice;

    @FXML
    private TextField fieldViewProductDescriptions;

    @FXML
    private TextField ImportPrice;

    private File selectedImageFile;

    @FXML
    public void moreProduct() {
        String productPrice = fieldViewProductPrice.getText();
        String description = fieldViewProductDescriptions.getText();
        String importPrice = ImportPrice.getText();
        String selectedCategory = fieldViewProductCategoryId.getValue();
        String selectedProductName = fieldViewProductName.getValue();

        if (productPrice.isEmpty()) {
            showAlert("Vui lòng nhập đủ giá sản phẩm.");
            return;
        }

        if (description.isEmpty()) {
            showAlert("Vui lòng nhập đủ mô tả sản phẩm.");
            return;
        }
        if (description.length()
                > 1000) {
            showAlert("Product descriptions cannot be longer than 1000 characters.");
            return;
        }

        // Check if an image was selected
        if (selectedImageFile == null) {
            showAlert("Vui lòng chọn ảnh sản phẩm.");
            return;
        }

        // Prepare the SQL statement to insert the product into the database
        String insertSQL = "INSERT INTO product (categoryId, ProductNameId, productImportPrice, price, img, Description) VALUES (?,?,?,?,?,?)";

        try (Connection connection = connect.getConnection(); PreparedStatement preparedStatement = connection.prepareStatement(insertSQL)) {
            // Set parameters for the SQL statement
            preparedStatement.setInt(1, Integer.parseInt(selectedCategory));
            preparedStatement.setInt(2, Integer.parseInt(selectedProductName));
            preparedStatement.setDouble(3, Double.parseDouble(importPrice));
            preparedStatement.setDouble(4, Double.parseDouble(productPrice));
            preparedStatement.setString(5, selectedImageFile.getAbsolutePath());
            preparedStatement.setString(6, description);

            // Execute the SQL statement
            int rowsAffected = preparedStatement.executeUpdate();

            if (rowsAffected > 0) {
                System.out.println("Thêm sản phẩm thành công!");
                showSuccessAlert("Thêm sản phẩm thành công!");
                fieldViewProductPrice.clear();
                fieldViewProductDescriptions.clear();
                // Clear the selected image file
                selectedImageFile = null;
            } else {
                showAlert("Thêm sản phẩm không thành công.");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

// Modify the eventImg method to set the selectedImageFile
    @FXML
    public void eventImg(ActionEvent event) {
        Stage primaryStage = new Stage();
        primaryStage.setTitle("Image File Chooser Example");

        FileChooser fileChooser = new FileChooser();
        fileChooser.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter("Image Files", "*.jpg", "*.png", "*.jpeg", "*.gif")
        );

        File selectedFile = fileChooser.showOpenDialog(primaryStage);
        if (selectedFile != null) {
            selectedImageFile = selectedFile;
        }
        primaryStage.close();
        System.out.println("Đường dẫn ảnh đã chọn: " + selectedImageFile);

    }

    // Hiển thị thông báo thành công
    private void showSuccessAlert(String message) {
        Alert alert = new Alert(AlertType.INFORMATION);
        alert.setTitle("Thành công");
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    // Hiển thị thông báo lỗi
    private void showAlert(String message) {
        Alert alert = new Alert(AlertType.ERROR);
        alert.setTitle("Lỗi");
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    // các hàm gọi giao diện
    public void getFromAddcategory() throws IOException {
        Main.setRoot("/admin/addCategory.fxml");

    }

    public void getFromAddProduct() throws IOException {
        Main.setRoot("/admin/addProduct.fxml");

    }

    public void getFromfromAddSupplier() throws IOException {
        Main.setRoot("/admin/addSupplier.fxml");

    }

    public void getFromfromMoreProductName() throws IOException {
        Main.setRoot("/admin/addProductName.fxml");

    }

    public void getFromImportGoods() throws IOException {
        Main.setRoot("/admin/importGoods.fxml");
    }

    public void getFromProductDelivery() throws IOException {
        Main.setRoot("/admin/productDelivery.fxml");
    }

    public void getFromInventory() throws IOException {
        Main.setRoot("/admin/inventory.fxml");
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public void handleLogout(ActionEvent event) throws IOException {
        // Tạo một thể hiện của lớp logOut và thiết lập tham chiếu đến loginController
        loginController logoutHandler = new loginController();
        logoutHandler.handleLogout();
    }
}
