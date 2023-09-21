package adminController;

import com.mysql.cj.conf.StringProperty;
import db.connect;
import java.io.IOException;
import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.text.Font;
import main.Main;

public class productDeliveryController {

    @FXML
    private TableView<ProductDelivery> exportTable;

    @FXML
    private TableColumn<ProductDelivery, String> supplierNameColumn;

    @FXML
    private TableColumn<ProductDelivery, String> productNameColumn;

    public void initialize() {
        String query = "SELECT ProductsName.ProductNameId, supplier.supplierId, \n"
                + "       MAX(supplier.supplierName) as supplierName,\n"
                + "       MAX(ProductsName.ProductName) as ProductName,\n"
                + "       SUM(quantity_imported) as total_quantity_imported,\n"
                + "       SUM(quantity_returned) as total_quantity_returned,\n"
                + "       SUM(total_quantity_received) as total_total_quantity_received\n"
                + "FROM importGoods \n"
                + "INNER JOIN supplier ON importGoods.supplier_id = supplier.supplierId \n"
                + "INNER JOIN ProductsName ON importGoods.ProductNameId = ProductsName.ProductNameId \n"
                + "GROUP BY ProductsName.ProductNameId, supplier.supplierId;"; // Thêm GROUP BY để lấy mỗi giá trị duy nhất của ProductNameId

        try (Connection connection = connect.getConnection(); PreparedStatement preparedStatement = connection.prepareStatement(query); ResultSet resultSet = preparedStatement.executeQuery()) {

            ObservableList<ProductDelivery> productDeliveries = FXCollections.observableArrayList();

            while (resultSet.next()) {
                int productNameId = resultSet.getInt("ProductNameId");
                int supplierId = resultSet.getInt("supplierId");
                String supplierName = resultSet.getString("supplierName");
                String productName = resultSet.getString("ProductName");

                // Tạo một đối tượng ProductDelivery và thêm vào danh sách
                productDeliveries.add(new ProductDelivery(supplierName, productName, productNameId, supplierId));
            }

            // Đặt cột dữ liệu cho exportTable
            supplierNameColumn.setCellValueFactory(new PropertyValueFactory<>("supplierName"));
            productNameColumn.setCellValueFactory(new PropertyValueFactory<>("productName"));

            // Đưa danh sách vào exportTable
            exportTable.setItems(productDeliveries);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public class ProductDelivery {

        private SimpleStringProperty supplierName;
        private SimpleStringProperty productName;
        private int supplierId;
        private int productNameId;

        public ProductDelivery(String supplierName, String productName, int supplierId, int productNameId) {
            this.supplierName = new SimpleStringProperty(supplierName);
            this.productName = new SimpleStringProperty(productName);
            this.supplierId = supplierId;
            this.productNameId = productNameId;
        }

        public String getSupplierName() {
            return supplierName.get();
        }

        public String getProductName() {
            return productName.get();
        }

        public int getSupplierId() {
            return supplierId;
        }

        public int getProductNameId() {
            return productNameId;
        }
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

    // insert thành công sẽ hiện
    private void showSuccessAlert(String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Success");
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    private void showAlert(String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Validation Error");
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
