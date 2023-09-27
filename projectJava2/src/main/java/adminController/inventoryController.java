/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package adminController;

import db.connect;
import java.io.IOException;
import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import main.Main;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.ButtonType;

/**
 *
 * @author Administrator
 */
public class inventoryController {

    // in ra bảng 
    @FXML
    private TableView<InventoryItem> ProductDeliveryTable;

    @FXML
    private TableColumn<InventoryItem, String> productName;

    @FXML
    private TableColumn<InventoryItem, String> supplierName;

    @FXML
    private TableColumn<InventoryItem, Date> importDate;

    @FXML
    private TableColumn<InventoryItem, Integer> quantityColumn;

    @FXML
    private TableView<InventoryItem> Importgoods;

    @FXML
    private TableColumn<InventoryItem, String> productName1;

    @FXML
    private TableColumn<InventoryItem, String> supplierName1;

    @FXML
    private TableColumn<InventoryItem, Date> importDate1;

    @FXML
    private TableColumn<InventoryItem, Integer> quantityColumn1;

    @FXML
    private TableView<InventoryItem> Inventory;

    @FXML
    private TableColumn<InventoryItem, String> productName11;

    @FXML
    private TableColumn<InventoryItem, String> supplierName11;

    @FXML
    private TableColumn<InventoryItem, Date> importDate11;

    @FXML
    private TableColumn<InventoryItem, Integer> quantityColumn11;

    private ObservableList<InventoryItem> productList = FXCollections.observableArrayList();
    private ObservableList<InventoryItem> importDataList = FXCollections.observableArrayList();
    private ObservableList<InventoryItem> InventoryList = FXCollections.observableArrayList();

    public class InventoryItem {

        private int productNameId;
        private int supplierId;
        private String productName;
        private String supplierName;
        private Date date;
        private int quantity;

        public InventoryItem(int productNameId, int supplierId, String productName, String supplierName, Date date, int quantity) {
            this.productNameId = productNameId;
            this.supplierId = supplierId;
            this.productName = productName;
            this.supplierName = supplierName;
            this.date = date;
            this.quantity = quantity;
        }

        public int getProductNameId() {
            return productNameId;
        }

        public void setProductNameId(int productNameId) {
            this.productNameId = productNameId;
        }

        public int getSupplierId() {
            return supplierId;
        }

        public void setSupplierId(int supplierId) {
            this.supplierId = supplierId;
        }

        public String getProductName() {
            return productName;
        }

        public void setProductName(String productName) {
            this.productName = productName;
        }

        public String getSupplierName() {
            return supplierName;
        }

        public void setSupplierName(String supplierName) {
            this.supplierName = supplierName;
        }

        public Date getDate() {
            return date;
        }

        public void setDate(Date date) {
            this.date = date;
        }

        public int getQuantity() {
            return quantity;
        }

        public void setQuantity(int quantity) {
            this.quantity = quantity;
        }
    }

    @FXML
    public void initialize() {
        // Gọi phương thức populateImportTable() để đổ dữ liệu vào productList
        populateImportTable();
        sqlImportgoods();
        sqlInventory();
        productName.setCellValueFactory(new PropertyValueFactory<>("productName"));
        supplierName.setCellValueFactory(new PropertyValueFactory<>("supplierName"));
        importDate.setCellValueFactory(new PropertyValueFactory<>("date"));
        quantityColumn.setCellValueFactory(new PropertyValueFactory<>("quantity"));

        productName1.setCellValueFactory(new PropertyValueFactory<>("productName"));
        supplierName1.setCellValueFactory(new PropertyValueFactory<>("supplierName"));
        importDate1.setCellValueFactory(new PropertyValueFactory<>("date"));//lấy biến ở dòng 80
        quantityColumn1.setCellValueFactory(new PropertyValueFactory<>("quantity"));

        productName11.setCellValueFactory(new PropertyValueFactory<>("productName"));
        supplierName11.setCellValueFactory(new PropertyValueFactory<>("supplierName"));
        importDate11.setCellValueFactory(new PropertyValueFactory<>("date"));
        quantityColumn11.setCellValueFactory(new PropertyValueFactory<>("quantity"));
//         Đặt dữ liệu vào TableView
        ProductDeliveryTable.setItems(productList);
        Importgoods.setItems(importDataList);
        Inventory.setItems(InventoryList);

    }

// Phương thức này sẽ điền dữ liệu vào productList từ cơ sở dữ liệu
    private void populateImportTable() {
        try (Connection connection = connect.getConnection()) {
            String query = "SELECT ProductsName.ProductNameId, supplier.supplierId, ProductsName.ProductName, supplier.supplierName, productdelivery.dayShipping, productdelivery.shipmentQuantity "
                    + "FROM productdelivery "
                    + "INNER JOIN ProductsName ON productdelivery.ProductNameId = ProductsName.ProductNameId "
                    + "INNER JOIN supplier ON productdelivery.supplier_id = supplier.supplierId";

            PreparedStatement preparedStatement = connection.prepareStatement(query);
            ResultSet resultSet = preparedStatement.executeQuery();

            while (resultSet.next()) {
                int productNameId = resultSet.getInt("ProductNameId");
                int supplierId = resultSet.getInt("supplierId");
                String productName = resultSet.getString("ProductName");
                String supplierName = resultSet.getString("supplierName");
                Date dayShipping = resultSet.getDate("dayShipping");
                int shipmentQuantity = resultSet.getInt("shipmentQuantity");

                productList.add(new InventoryItem(productNameId, supplierId, productName, supplierName, dayShipping, shipmentQuantity));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void sqlImportgoods() {
        try (Connection connection = connect.getConnection()) {
            String query = "SELECT ProductsName.ProductNameId, supplier.supplierId, ProductsName.ProductName, supplier.supplierName, importGoods.import_date, importGoods.total_quantity_received "
                    + "FROM importGoods "
                    + "INNER JOIN ProductsName ON importGoods.ProductNameId = ProductsName.ProductNameId "
                    + "INNER JOIN supplier ON importGoods.supplier_id = supplier.supplierId";

            PreparedStatement preparedStatement = connection.prepareStatement(query);
            ResultSet resultSet = preparedStatement.executeQuery();

            while (resultSet.next()) {
                int productNameId = resultSet.getInt("ProductNameId");
                int supplierId = resultSet.getInt("supplierId");
                String productName = resultSet.getString("ProductName");
                String supplierName = resultSet.getString("supplierName");
                Date importDate = resultSet.getDate("import_date");
                int totalQuantityReceived = resultSet.getInt("total_quantity_received");

                importDataList.add(new InventoryItem(productNameId, supplierId, productName, supplierName, importDate, totalQuantityReceived));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void sqlInventory() {
        try (Connection connection = connect.getConnection()) {
            String query = "SELECT ProductsName.ProductNameId, supplier.supplierId, ProductsName.ProductName, supplier.supplierName,"
                    + " inventory.date, inventory.InventoryNumber "
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
                Date importDate = resultSet.getDate("date"); // Update this to match the actual column name
                int inventoryNumber = resultSet.getInt("InventoryNumber"); // Update this to match the actual column name

                InventoryList.add(new InventoryItem(productNameId, supplierId, productName, supplierName, importDate, inventoryNumber));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

//insert 
    @FXML
    public void insertInventory() throws IOException {
        // Lấy dữ liệu từ bảng ProductDeliveryTable
        InventoryItem selectedDeliveryItem = ProductDeliveryTable.getSelectionModel().getSelectedItem();
        InventoryItem selectedImportItem = Importgoods.getSelectionModel().getSelectedItem();
        InventoryItem selectedInventoryItem = Inventory.getSelectionModel().getSelectedItem();

        // Kiểm tra xem các item đã được chọn
        if (selectedDeliveryItem != null && selectedImportItem != null && selectedInventoryItem != null) {
            int productNameIdFromDelivery = selectedDeliveryItem.getProductNameId();
            int supplierIdFromDelivery = selectedDeliveryItem.getSupplierId();
            int productNameIdFromImport = selectedImportItem.getProductNameId();
            int supplierIdFromImport = selectedImportItem.getSupplierId();
            int productNameIdFromInventory = selectedInventoryItem.getProductNameId();
            int supplierIdFromInventory = selectedInventoryItem.getSupplierId();

            // Check if any of the IDs are empty (zero)
            if (productNameIdFromDelivery == 0 || supplierIdFromDelivery == 0
                    || productNameIdFromImport == 0 || supplierIdFromImport == 0
                    || productNameIdFromInventory == 0 || supplierIdFromInventory == 0) {
                showAlert("SupplierId or ProductNameId cannot be empty (zero).");
                return; // Exit the method to prevent further processing
            }

            // Trường hợp 1: Check SupplierId và ProductNameId từ cả ProductDeliveryTable và Importgoods
            if (productNameIdFromDelivery == productNameIdFromImport && supplierIdFromDelivery == supplierIdFromImport) {
                int totalQuantityReceived = getTotalQuantityReceived(productNameIdFromImport, supplierIdFromImport);
                int shipmentQuantity = selectedDeliveryItem.getQuantity();

                if (totalQuantityReceived >= shipmentQuantity) {
                    int inventoryNumber = totalQuantityReceived - shipmentQuantity;
                    if (insertInventoryRecord(productNameIdFromImport, supplierIdFromImport, inventoryNumber)) {
                        System.out.println("Added successfully!");
                        showSuccessAlert("Added successfully!");
                        getFromInventory();
                    } else {
                        showAlert("Failed to add.");
                    }
                } else {
                    showAlert("total_quantity_received must be greater than or equal to shipmentQuantity.");
                }
            } else {
                System.out.println("Mismatched SupplierId or ProductNameId: ");
                System.out.println("SupplierId from Importgoods: " + supplierIdFromImport);
                System.out.println("ProductNameId from Importgoods: " + productNameIdFromImport);
                System.out.println("SupplierId from ProductDeliveryTable: " + supplierIdFromDelivery);
                System.out.println("ProductNameId from ProductDeliveryTable: " + productNameIdFromDelivery);
                showAlert("SupplierId or ProductNameId do not match.");
            }

            // Trường hợp 2: Check SupplierId và ProductNameId từ cả Importgoods và Inventory
            if (productNameIdFromImport == productNameIdFromInventory && supplierIdFromImport == supplierIdFromInventory) {
                int quantityFromImport = selectedImportItem.getQuantity();
                int quantityFromInventory = selectedInventoryItem.getQuantity();

                int inventoryNumber = quantityFromImport + quantityFromInventory; // Tổng hợp số lượng từ cả hai bảng

                if (insertInventoryRecord(productNameIdFromImport, supplierIdFromImport, inventoryNumber)) {
                    System.out.println("Added successfully!");
                    showSuccessAlert("Added successfully!");
                    getFromInventory();
                } else {
                    showAlert("Failed to add.");
                }
            } else {
                System.out.println("Mismatched SupplierId or ProductNameId: ");
                System.out.println("SupplierId from Importgoods: " + supplierIdFromImport);
                System.out.println("ProductNameId from Importgoods: " + productNameIdFromImport);
                System.out.println("SupplierId from Inventory: " + supplierIdFromInventory);
                System.out.println("ProductNameId from Inventory: " + productNameIdFromInventory);
                showAlert("SupplierId or ProductNameId do not match.");
            }

            // Trường hợp 3: Check SupplierId và ProductNameId từ cả Inventory và ProductDeliveryTable
            if (productNameIdFromInventory == productNameIdFromDelivery && supplierIdFromInventory == supplierIdFromDelivery) {
                int quantityFromInventory = selectedInventoryItem.getQuantity();
                int quantityFromDelivery = selectedDeliveryItem.getQuantity();

                if (quantityFromInventory >= quantityFromDelivery) {
                    int inventoryNumber = quantityFromInventory - quantityFromDelivery;
                    if (insertInventoryRecord(productNameIdFromInventory, supplierIdFromInventory, inventoryNumber)) {
                        System.out.println("Added successfully!");
                        showSuccessAlert("Added successfully!");
                        getFromInventory();
                    } else {
                        showAlert("Failed to add.");
                    }
                } else {
                    System.out.println("Mismatched SupplierId or ProductNameId: ");
                    System.out.println("SupplierId from Inventory: " + supplierIdFromInventory);
                    System.out.println("ProductNameId from Inventory: " + productNameIdFromInventory);
                    System.out.println("SupplierId from ProductDeliveryTable: " + supplierIdFromDelivery);
                    System.out.println("ProductNameId from ProductDeliveryTable: " + productNameIdFromDelivery);
                    showAlert("SupplierId or ProductNameId do not match or quantity mismatch.");
                }
            }
        }
    }
    // Phương thức để thực hiện insert vào bảng inventory

    private boolean insertInventoryRecord(int productNameId, int supplierId, int inventoryNumber) {
        try (Connection connection = connect.getConnection()) {
            String insertSQL = "INSERT INTO inventory (ProductNameId, supplierId, date, InventoryNumber) VALUES (?, ?, ?, ?)";
            PreparedStatement preparedStatement = connection.prepareStatement(insertSQL);
            preparedStatement.setInt(1, productNameId);
            preparedStatement.setInt(2, supplierId);

            java.util.Date currentDate = new java.util.Date();
            Date dateNew = new Date(currentDate.getTime());
            preparedStatement.setDate(3, dateNew);

            preparedStatement.setInt(4, inventoryNumber);

            int rowsAffected = preparedStatement.executeUpdate();
            return rowsAffected > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

// Phương thức để lấy total_quantity_received từ bảng Importgoods
    private int getTotalQuantityReceived(int productNameId, int supplierId) {
        try (Connection connection = connect.getConnection()) {
            String query = "SELECT total_quantity_received FROM importGoods WHERE ProductNameId = ? AND supplier_id = ?";
            PreparedStatement preparedStatement = connection.prepareStatement(query);
            preparedStatement.setInt(1, productNameId);
            preparedStatement.setInt(2, supplierId);
            ResultSet resultSet = preparedStatement.executeQuery();

            if (resultSet.next()) {
                return resultSet.getInt("total_quantity_received");
            } else {
                return -1; // Trả về -1 nếu không tìm thấy dữ liệu
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return -1;
        }
    }
    // delete 

    public void deleteInventoryRecord() {
        // Get the selected item from the TableView
        InventoryItem selectedInventoryItem = Inventory.getSelectionModel().getSelectedItem();

        if (selectedInventoryItem != null) {
            // Create a confirmation dialog
            Alert confirmDialog = new Alert(AlertType.CONFIRMATION);
            confirmDialog.setTitle("Confirmation");
            confirmDialog.setHeaderText("Delete Inventory Record");
            confirmDialog.setContentText("Are you sure you want to delete this inventory record?");

            // Show the confirmation dialog and wait for user input
            ButtonType result = confirmDialog.showAndWait().orElse(ButtonType.CANCEL);

            if (result == ButtonType.OK) {
                int productNameId = selectedInventoryItem.getProductNameId();
                int supplierId = selectedInventoryItem.getSupplierId();

                try (Connection connection = connect.getConnection()) {
                    String deleteSQL = "DELETE FROM inventory WHERE ProductNameId = ? AND supplierId = ?";
                    PreparedStatement preparedStatement = connection.prepareStatement(deleteSQL);
                    preparedStatement.setInt(1, productNameId);
                    preparedStatement.setInt(2, supplierId);

                    int rowsAffected = preparedStatement.executeUpdate();
                    if (rowsAffected > 0) {
                        System.out.println("Inventory record deleted successfully.");
                        showSuccessAlert("Inventory record deleted successfully.");
                        // Remove the item from the ObservableList as well
                        InventoryList.remove(selectedInventoryItem);
                    } else {
                        showAlert("Failed to delete inventory record.");
                    }
                } catch (SQLException e) {
                    e.printStackTrace();
                    showAlert("An error occurred while deleting inventory record.");
                }
            }
        } else {
            showAlert("No item selected in Inventory.");
        }
    }
// insert thành công sẽ hiện

    private void showSuccessAlert(String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Success");
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    // insert không thành công sẽ hiện
    private void showAlert(String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
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
}
