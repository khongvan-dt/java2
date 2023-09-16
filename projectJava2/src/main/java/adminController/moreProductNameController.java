package adminController;

import java.io.File;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import db.connect;
import java.io.IOException;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
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
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.SelectionMode;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.util.Callback;
import main.Main;

public class moreProductNameController {

    @FXML
    private TextField productNameField;

    @FXML
    public void addProductName() throws IOException {
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
                getFromfromMoreProductName();
            } else {
                showAlert("Failed to add product name.");
            }

        } catch (SQLException e) {
            showAlert("An error occurred while adding the product name.");
            e.printStackTrace();
        }
    }

    //in dữ liệu ra bảng 
    public class ProductName {

        private int productId;
        private String productName;

        public ProductName(int productId, String productName) {
            this.productId = productId;
            this.productName = productName;
        }

        public int getProductId() {
            return productId;
        }

        public String getProductName() {
            return productName;
        }
    }

    private List<ProductName> fetchDataFromDatabase() {
        List<ProductName> productNames = new ArrayList<>();

        try {
            Connection connection = connect.getConnection();
            Statement statement = connection.createStatement();
            String query = "SELECT * FROM ProductsName";
            ResultSet resultSet = statement.executeQuery(query);

            while (resultSet.next()) {
                int productNameId = resultSet.getInt("ProductNameId");
                String productName = resultSet.getString("ProductName");
                ProductName product = new ProductName(productNameId, productName);
                productNames.add(product);
            }

            resultSet.close();
            statement.close();
            connection.close();
        } catch (Exception e) {
            e.printStackTrace();
        }

        return productNames;
    }
    @FXML
    private TableView<ProductName> productNameTable; //id bảng

    @FXML
    private TableColumn<ProductName, String> productNameColumn; //id cột 

    public void initialize() {
        // Khởi tạo các cột để hiển thị dữ liệu từ lớp ProductName
        productNameColumn.setCellValueFactory(new PropertyValueFactory<>("productName"));// tên cột trong db

        // Truy vấn dữ liệu từ cơ sở dữ liệu và điền vào TableView
        ObservableList<ProductName> productNames = FXCollections.observableArrayList(fetchDataFromDatabase());
        productNameTable.setItems(productNames);
    }

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

    public void getFromAddcategory() throws IOException {
        Main.setRoot("/admin/addCategory.fxml");

    }

    public void getFromAddProduct() throws IOException {
        Main.setRoot("/admin/addProduct.fxml");

    }

    public void getFromfromAddSupplier() throws IOException {
        Main.setRoot("/admin/addSupplier.fxml");

    }

    public void getFromImportGoods() throws IOException {
        Main.setRoot("/admin/importGoods.fxml");
    }

    public void getFromfromMoreProductName() throws IOException {
        Main.setRoot("/admin/addProductName.fxml");

    }

    public void handleLogout(ActionEvent event) throws IOException {
        // Tạo một thể hiện của lớp logOut và thiết lập tham chiếu đến loginController
        loginController logoutHandler = new loginController();
        logoutHandler.handleLogout();
    }

}
