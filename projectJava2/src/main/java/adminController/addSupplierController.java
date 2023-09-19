package adminController;

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
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.TextField;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.ButtonType;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import main.Main;

public class addSupplierController {

    @FXML
    private TextField SupplierNameField;

    public void moreSupplier() throws IOException {
        String SupplierName = SupplierNameField.getText();

        if (SupplierName.isEmpty()) {
            showAlert("Please fill in all fields.");
            return;
        }

        String insertSQL = "INSERT INTO supplier (supplierName) VALUES (?)";
        try (Connection connection = connect.getConnection(); PreparedStatement preparedStatement = connection.prepareStatement(insertSQL)) {

            preparedStatement.setString(1, SupplierName);

            int rowsAffected = preparedStatement.executeUpdate();
            if (rowsAffected > 0) {
                System.out.println(" add category successfully!");

                // Thành công: Hiển thị thông báo thành công
                showSuccessAlert("Supplier added successfully!");

                // Xóa nội dung trên trường nhập liệu sau khi thêm thành công
                SupplierNameField.clear();
                getFromfromAddSupplier();

            } else {
                showAlert("Failed to add category.");
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

    }

    //in dữ liệu ra bảng 
    public class Supplier {

        private int supplierId;
        private String supplierName;

        public Supplier(int supplierId, String supplierName) {
            this.supplierId = supplierId;
            this.supplierName = supplierName;
        }

        public int getSupplierId() {
            return supplierId;
        }

        public String getSupplierName() {
            return supplierName;
        }

        public void setSupplierName(String supplierName) {
            this.supplierName = supplierName;
        }

       
    }

    // in ra bảng 
    @FXML
    private TableView<Supplier> supplierTable;//id bảng

    @FXML
    private TableColumn<Supplier, String> supplierNameColumn;//id cột trong bảng 

    public void initialize() {
        supplierNameColumn.setCellValueFactory(new PropertyValueFactory<>("supplierName"));// tên cột trong db

        ObservableList<Supplier> suppliers = FXCollections.observableArrayList(fetchDataFromDatabase());
        supplierTable.setItems(suppliers);
    }

    private List<Supplier> fetchDataFromDatabase() {
        List<Supplier> suppliers = new ArrayList<>();

        try {
            Connection connection = connect.getConnection();
            Statement statement = connection.createStatement();
            String query = "SELECT * FROM supplier";
            ResultSet resultSet = statement.executeQuery(query);

            while (resultSet.next()) {
                int supplierId = resultSet.getInt("supplierId");
                String supplierName = resultSet.getString("supplierName");
                Supplier supplier = new Supplier(supplierId, supplierName);
                suppliers.add(supplier);
            }

            resultSet.close();
            statement.close();
            connection.close();
        } catch (Exception e) {
            e.printStackTrace();
        }

        return suppliers;
    }
    
    //edit Supplier

    @FXML
    private void showEditSupplier(ActionEvent event) throws IOException {
        // Lấy hàng đã chọn từ TableView
        Supplier selectedSupplier = supplierTable.getSelectionModel().getSelectedItem();

        if (selectedSupplier != null) {
            // Tải cảnh "Sửa nhà cung cấp" và chuyển dữ liệu nhà cung cấp đã chọn
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/admin/editSupplier.fxml"));
            Parent root = loader.load();

            // để trỏ đến controller của editSupplierController giống như liên kết 
            editSupplierController editController = loader.getController();

            // Truyền dữ liệu nhà cung cấp đã chọn cho controller của editSupplierController, initData là hàm trong editSupplierController
            //selectedSupplier là giá trị đã được chọn ở trong table
            editController.initData(selectedSupplier);
            
            Stage stage = new Stage();
            Scene scene = new Scene(root);
            stage.setScene(scene);
            stage.show();
            
        } else {
            showAlert("Please select the information you want to edit!");
        }
    }
    
      @FXML
private void DeleteSupplier(ActionEvent event) throws IOException {
    // Get the selected category from the TableView
      Supplier selectedSupplier = supplierTable.getSelectionModel().getSelectedItem();


    if (selectedSupplier != null) {
        // Show a confirmation dialog to confirm the deletion
        Alert confirmation = new Alert(AlertType.CONFIRMATION);
        confirmation.setTitle("Confirm Delete");
        confirmation.setHeaderText("Are you sure you want to delete this category?");
        confirmation.getButtonTypes().setAll(ButtonType.YES, ButtonType.NO);

        ButtonType result = confirmation.showAndWait().orElse(ButtonType.NO);

        if (result == ButtonType.YES) {
            if (deleteSupplerFromDatabase(selectedSupplier.getSupplierId())) {
                // Remove the deleted category from the TableView
                supplierTable.getItems().remove(selectedSupplier);
                showSuccessAlert("Category deleted successfully!");
            } else {
                showAlert("Failed to delete category.");
            }
        }
    } else {
        showAlert("Please select the category you want to delete!");
    }
}

private boolean deleteSupplerFromDatabase(int supplierId) {
    // Implement the logic to delete the category from the database
    String deleteSQL = "DELETE FROM supplier WHERE supplierId = ?";
    
    try (Connection connection = connect.getConnection(); 
         PreparedStatement preparedStatement = connection.prepareStatement(deleteSQL)) {
        
        preparedStatement.setInt(1, supplierId);
        int rowsAffected = preparedStatement.executeUpdate();

        return rowsAffected > 0;
    } catch (SQLException e) {
        e.printStackTrace();
        return false;
    }
   

}

    // insert thành công sẽ hiện
    private void showSuccessAlert(String message) {
        Alert alert = new Alert(AlertType.INFORMATION);
        alert.setTitle("Success");
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    private void showAlert(String message) {
        Alert alert = new Alert(AlertType.ERROR);
        alert.setTitle("Validation Error");
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

    public void handleLogout(ActionEvent event) throws IOException {
        // Tạo một thể hiện của lớp logOut và thiết lập tham chiếu đến loginController
        loginController logoutHandler = new loginController();
        logoutHandler.handleLogout();
    }
}
