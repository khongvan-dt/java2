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

        private int productId;
        private int productDeliveryID;

        // Constructor and other methods
        public int getProductId() {
            return productId;
        }

        public void setProductId(int productId) {
            this.productId = productId;
        }

        private SimpleStringProperty supplierName;
        private int supplierId;
        private SimpleStringProperty productName;
        private int productNameId;
        private Date importDate;
        private int quantity;

//        public DeliveryData(String supplierName, int supplierId, String productName, int productNameId, int totalQuantityReceived) {
//            this.supplierName = new SimpleStringProperty(supplierName);
//            this.supplierId = supplierId;
//            this.productName = new SimpleStringProperty(productName);
//            this.productNameId = productNameId;
//            this.quantity = totalQuantityReceived;
//        }
        public DeliveryData(int productDeliveryID, String productName, String supplierName, Date importDate, int quantity) {
            this.productDeliveryID = productDeliveryID;
            this.productName = new SimpleStringProperty(productName);
            this.supplierName = new SimpleStringProperty(supplierName);
            this.importDate = importDate;
            this.quantity = quantity;
        }

        // Getters for common properties
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

        // Getters for additional properties needed for ProductDeliveryTable
        public Date getImportDate() {
            return importDate;
        }

        public int getQuantity() {
            return quantity;
        }
    }

    @FXML
    private TableColumn<DeliveryData, Integer> quantity2;

    @FXML
    private TableView<DeliveryData> ProductDeliveryTable;

    @FXML
    private TableColumn<DeliveryData, String> productName;
    @FXML
    private TableColumn<DeliveryData, String> supplierName;

    @FXML
    private TableColumn<DeliveryData, Date> importDate;

    @FXML
    private TableView<DeliveryData> exportTable;

    @FXML
    private TableColumn<DeliveryData, String> productNameColumn;

    @FXML
    private TableColumn<DeliveryData, String> supplierNameColumn;

    @FXML
    private TableColumn<DeliveryData, Date> importDateColumn;

    @FXML
    private TableColumn<DeliveryData, Integer> quantityColumn;
    @FXML
    private TableColumn<DeliveryData, Integer> productIdColumn;

    @FXML
    private TextField quantity;
    private ObservableList<DeliveryData> inventoryList = FXCollections.observableArrayList();
    private ObservableList<DeliveryData> DeliveryDataList = FXCollections.observableArrayList();

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
                Date importDate = resultSet.getDate("date"); // Match the actual column name
                int inventoryNumber = resultSet.getInt("InventoryNumber"); // Match the actual column name

                // Create a new DeliveryData object and add it to the ObservableList
                inventoryList.add(new DeliveryData(productNameId, productName, supplierName, importDate, inventoryNumber));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @FXML
    public void initialize() {
        // Call the method to populate the inventory table
        sqlInventory();
        // Bind columns to properties in the DeliveryData class
        productNameColumn.setCellValueFactory(new PropertyValueFactory<>("productName"));
        supplierNameColumn.setCellValueFactory(new PropertyValueFactory<>("supplierName"));
        importDateColumn.setCellValueFactory(new PropertyValueFactory<>("importDate"));
        quantity2.setCellValueFactory(new PropertyValueFactory<>("quantity"));

        // Set the data in the TableView
        exportTable.setItems(inventoryList);
    }

    private void fetchProductDeliveryData() {
        try (Connection connection = connect.getConnection()) {
            String query = "SELECT productDelivery.productDeliveryID, ProductsName.ProductNameId, supplier.supplierId, ProductsName.ProductName, supplier.supplierName,"
                    + "productDelivery.dayShipping, productDelivery.shipmentQuantity "
                    + "FROM productDelivery"
                    + "INNER JOIN ProductsName ON productDelivery.ProductNameId = ProductsName.ProductNameId "
                    + "INNER JOIN supplier ON productDelivery.supplier_id = supplier.supplierId";

            PreparedStatement preparedStatement = connection.prepareStatement(query);
            ResultSet resultSet = preparedStatement.executeQuery();

            while (resultSet.next()) {
                int productDeliveryID = resultSet.getInt("productDeliveryID");
                int productNameId = resultSet.getInt("ProductNameId");
                int supplierId = resultSet.getInt("supplierId");
                String productName = resultSet.getString("ProductName");
                String supplierName = resultSet.getString("supplierName");
                Date dayShipping = resultSet.getDate("dayShipping");
                int shipmentQuantity = resultSet.getInt("shipmentQuantity");

                // Create a new DeliveryData object and add it to the ObservableList
                DeliveryDataList.add(new DeliveryData(productDeliveryID, productName, supplierName, dayShipping, shipmentQuantity));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @FXML
    public void DeliveryData() {
        fetchProductDeliveryData();

        // Bind columns to properties in the DeliveryData class
        productIdColumn.setCellValueFactory(new PropertyValueFactory<>("productDeliveryID"));
        productName.setCellValueFactory(new PropertyValueFactory<>("productName"));
        supplierName.setCellValueFactory(new PropertyValueFactory<>("supplierName"));
        importDate.setCellValueFactory(new PropertyValueFactory<>("importDate"));
        quantityColumn.setCellValueFactory(new PropertyValueFactory<>("quantity"));

        // Set the data in the TableView
        ProductDeliveryTable.setItems(DeliveryDataList);
    }

    // Insert
    public void insertProductdelivery() {
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
    // xóa 

    @FXML
    private void deleteProductDelivery(ActionEvent event) {
        // Get the selected item from the TableView
        DeliveryData selectedProduct = ProductDeliveryTable.getSelectionModel().getSelectedItem();

        if (selectedProduct != null) {
            // Get the ID or any unique identifier of the selected item
            int productId = selectedProduct.getProductId(); // Assuming you have a method to get the product ID

            // Execute the SQL DELETE statement to remove the item from the database
            deleteProductFromDatabase(productId);

            // Refresh the TableView to reflect the updated data
            fetchProductDeliveryData();
        } else {
            showAlert("Please select a product to delete.");
        }
    }

    private void deleteProductFromDatabase(int productId) {
        try (Connection connection = connect.getConnection()) {
            String deleteSQL = "DELETE FROM productDelivery WHERE ProductId = ?";
            PreparedStatement preparedStatement = connection.prepareStatement(deleteSQL);
            preparedStatement.setInt(1, productId);

            int rowsAffected = preparedStatement.executeUpdate();

            if (rowsAffected > 0) {
                showSuccessAlert("Product delivery deleted successfully.");
            } else {
                showAlert("Failed to delete product delivery.");
            }
        } catch (SQLException e) {
            e.printStackTrace();
            showAlert("Error occurred while deleting product delivery.");
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
