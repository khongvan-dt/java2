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
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
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

        // Kiểm tra độ dài của tên sản phẩm
        int maxProductNameLength = 500;
        if (productName.length() > maxProductNameLength) {
            showAlert("Product name is too long. Maximum length is " + maxProductNameLength + " characters.");
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

    private boolean deleteProductNameFromDatabase(int productId) {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
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

        public void setProductName(String productName) {
            this.productName = productName;
        }

        private Object getItems() {
            throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
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

    //edit Supplier
    @FXML
    private void showeditProductName(ActionEvent event) throws IOException {
        // Lấy hàng đã chọn từ TableView
        ProductName selectedProductName = productNameTable.getSelectionModel().getSelectedItem();

        if (selectedProductName != null) {
            // Tải cảnh "Sửa nhà cung cấp" và chuyển dữ liệu nhà cung cấp đã chọn
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/admin/editProductName.fxml"));
            Parent root = loader.load();

            // để trỏ đến controller của editSupplierController giống như liên kết 
            editProductNameController editController = loader.getController();

            // Truyền dữ liệu nhà cung cấp đã chọn cho controller của editSupplierController, initData là hàm trong editSupplierController
            //selectedSupplier là giá trị đã được chọn ở trong table
            editController.initData(selectedProductName);

            Stage stage = new Stage();
            Scene scene = new Scene(root);
            stage.setScene(scene);
            stage.show();

        } else {
            showAlert("Please select the information you want to edit!");
        }
    }
    //xóa

    @FXML
    private void DeleteProductName(ActionEvent event) throws IOException {
        ProductName selectedProductName = productNameTable.getSelectionModel().getSelectedItem();

        if (selectedProductName != null) {
            if (isSupplierUsed(selectedProductName.getProductId())) {
                // Hiển thị thông báo rằng nhà cung cấp không thể bị xóa do được sử dụng ở nơi khác
                showAlert("This Product Name cannot be deleted as it is used in other tables.");
            } else {
                Alert confirmation = new Alert(AlertType.CONFIRMATION);
                confirmation.setTitle("Confirm Delete");
                confirmation.setHeaderText("Are you sure you want to delete this Product Name?");
                confirmation.getButtonTypes().setAll(ButtonType.YES, ButtonType.NO);

                ButtonType result = confirmation.showAndWait().orElse(ButtonType.NO);

                if (result == ButtonType.YES) {
                    if (deleteSupplierFromDatabase(selectedProductName.getProductId())) {
                        productNameTable.getItems().remove(selectedProductName);
                        showSuccessAlert("Supplier deleted successfully!");
                    } else {
                        showAlert("Failed to delete Product Name.");
                    }
                }
            }
        } else {
            showAlert("Please select the supplier you want to delete!");
        }
    }

    // ... (other methods and code)
    private boolean isSupplierUsed(int supplierId) {
        String checkSQL = "SELECT COUNT(*) FROM importgoods WHERE ProductNameId = ?";

        try (Connection connection = connect.getConnection(); PreparedStatement preparedStatement = connection.prepareStatement(checkSQL)) {

            preparedStatement.setInt(1, supplierId);
            ResultSet resultSet = preparedStatement.executeQuery();

            if (resultSet.next()) {
                int count = resultSet.getInt(1);
                return count > 0;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return false;
    }

    private boolean deleteSupplierFromDatabase(int ProductNameId) {
        // Implement the logic to delete the supplier from the database
        String deleteSQL = "DELETE FROM ProductsName WHERE ProductNameId = ?";

        try (Connection connection = connect.getConnection(); PreparedStatement preparedStatement = connection.prepareStatement(deleteSQL)) {

            preparedStatement.setInt(1, ProductNameId);
            int rowsAffected = preparedStatement.executeUpdate();

            return rowsAffected > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    private boolean isProductNameUsed(int productId) {
        String checkSQL = "SELECT COUNT(*) FROM productsname WHERE ProductNameId = ?";

        try (Connection connection = connect.getConnection(); PreparedStatement preparedStatement = connection.prepareStatement(checkSQL)) {

            preparedStatement.setInt(1, productId);
            ResultSet resultSet = preparedStatement.executeQuery();

            if (resultSet.next()) {
                int count = resultSet.getInt(1);
                return count > 0;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return false;
    }

    private void showWarningAlert(String message) {
        Alert alert = new Alert(AlertType.WARNING);
        alert.setTitle("Warning");
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
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

    public void handleLogout(ActionEvent event) throws IOException {
        // Tạo một thể hiện của lớp logOut và thiết lập tham chiếu đến loginController
        loginController logoutHandler = new loginController();
        logoutHandler.handleLogout();
    }

}
