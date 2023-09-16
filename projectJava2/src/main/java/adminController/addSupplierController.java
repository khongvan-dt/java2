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
import javafx.scene.control.Alert;
import javafx.scene.control.TextField;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
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
