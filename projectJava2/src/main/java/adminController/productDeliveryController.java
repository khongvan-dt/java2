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
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.fxml.Initializable;
import javafx.fxml.Initializable;
import javafx.scene.control.TableView;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import java.net.URL;
import java.util.ResourceBundle;

public class productDeliveryController {

    public class DeliveryData {

        private String supplierName;
        private int supplierId;
        private String productName;
        private int productNameId;
        private Date inventoryDate;
        private int inventoryNumber;

        public DeliveryData(String productName, String supplierName, Date importDate, int quantity, int productNameId, int supplierId) {
            this.productName = productName;
            this.supplierName = supplierName;
            this.inventoryDate = importDate;
            this.inventoryNumber = quantity;
            this.productNameId = productNameId;
            this.supplierId = supplierId;

        }

        // Getters for common properties
        public String getSupplierName() {
            return supplierName;
        }

        public String getProductName() {
            return productName;
        }

        public int getSupplierId() {
            return supplierId;
        }

        public int getProductNameId() {
            return productNameId;
        }

        // Getters for additional properties needed for ProductDeliveryTable
        public Date getImportDate() {
            return inventoryDate;
        }

        public int getQuantity() {
            return inventoryNumber;
        }

    }
    private ObservableList<DeliveryData> inventoryList = FXCollections.observableArrayList();

    @FXML
    private TableView<DeliveryData> exportTable;

    @FXML
    private TableColumn<DeliveryData, String> productNameColumn;

    @FXML
    private TableColumn<DeliveryData, String> supplierNameColumn;

    @FXML
    private TableColumn<DeliveryData, Date> importDateColumn;

    @FXML
    private TableColumn<DeliveryData, Integer> quantity2;

    @FXML
    private TextField quantity;

    @FXML
    public void initialize() {
        sqlInventory();

        // Bind columns to properties in the DeliveryData class
        productNameColumn.setCellValueFactory(new PropertyValueFactory<>("productName"));
        supplierNameColumn.setCellValueFactory(new PropertyValueFactory<>("supplierName"));
        importDateColumn.setCellValueFactory(new PropertyValueFactory<>("importDate"));
        quantity2.setCellValueFactory(new PropertyValueFactory<>("quantity")); // Make sure this matches the field name in DeliveryData.
    }

    private void sqlInventory() {
        try (Connection connection = connect.getConnection()) {
            String query = "SELECT ProductsName.ProductNameId, supplier.supplierId, ProductsName.ProductName, supplier.supplierName, "
                    + "inventory.date, inventory.InventoryNumber "
                    + "FROM inventory "
                    + "INNER JOIN ProductsName ON inventory.ProductNameId = ProductsName.ProductNameId "
                    + "INNER JOIN supplier ON inventory.supplierId = supplier.supplierId";

            PreparedStatement preparedStatement = connection.prepareStatement(query);
            ResultSet resultSet = preparedStatement.executeQuery();

            while (resultSet.next()) {
                int productNameId = resultSet.getInt("ProductNameId");
                int supplierId = resultSet.getInt("supplierId");
                String productName = resultSet.getString("ProductName");
                String supplierName = resultSet.getString("supplierName");
                Date importDate = resultSet.getDate("date");
                int inventoryNumber = resultSet.getInt("InventoryNumber");

                inventoryList.add(new DeliveryData(productName, supplierName, importDate, inventoryNumber, productNameId, supplierId));

            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        exportTable.setItems(inventoryList);
    }

    // Insert
    public void insertProductDelivery() {
        String shipQuantity = quantity.getText().trim();

        // Check if shipQuantity is empty
        if (shipQuantity.isEmpty()) {
            showAlert("Please enter a quantity.");
            return;
        }

        // Check if shipQuantity is a valid integer
        if (!isNumeric(shipQuantity)) {
            showAlert("Quantity must be a numeric value.");
            return;
        }

        // Assuming you have selected a row from exportTable, you can get the selected item.
        DeliveryData selectedProduct = exportTable.getSelectionModel().getSelectedItem();

        if (selectedProduct == null) {
            showAlert("Please select a product from the export table.");
            return;
        }

        int productNameId = selectedProduct.getProductNameId();
        int availableQuantity = selectedProduct.getQuantity();

        // Kiểm tra nếu số lượng nhập vào lớn hơn số lượng hiện có
        int shipmentQuantity = Integer.parseInt(shipQuantity);
        if (shipmentQuantity > availableQuantity) {
            showAlert("Shipment quantity cannot exceed available quantity.");
            return;
        }

        // Get the supplierId
        int supplierId = selectedProduct.getSupplierId();

        java.util.Date currentDate = new java.util.Date();
        java.sql.Date sqlDate = new java.sql.Date(currentDate.getTime());

        String insertSQL = "INSERT INTO productDelivery (ProductNameId, supplier_id, dayShipping, shipmentQuantity) VALUES (?, ?, ?, ?)";

        try (Connection connection = connect.getConnection(); PreparedStatement preparedStatement = connection.prepareStatement(insertSQL)) {
            preparedStatement.setInt(1, productNameId);
            preparedStatement.setInt(2, supplierId);
            preparedStatement.setDate(3, sqlDate);
            preparedStatement.setInt(4, shipmentQuantity);

            int rowsAffected = preparedStatement.executeUpdate();

            if (rowsAffected > 0) {
                showSuccessAlert("Product delivery added successfully.");
                quantity.clear();
                sqlInventory(); // Refresh the exportTable data
            } else {
                showAlert("Failed to insert product delivery.");
            }
        } catch (SQLException e) {
            e.printStackTrace();
            showAlert("Error occurred while inserting product delivery.");
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

    public void getOder() throws IOException {
        Main.setRoot("/admin/oder.fxml");
    }

    public void getAccount() throws IOException {
        Main.setRoot("/admin/account.fxml");
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
    // Helper method to check if a string is numeric

    private boolean isNumeric(String str) {
        try {
            Integer.parseInt(str);
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }
}
