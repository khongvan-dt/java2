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
import javafx.beans.property.SimpleIntegerProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
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
        private int inventoryId;

        public InventoryItem(int productNameId, int supplierId, String productName, String supplierName, Date date, int quantity, int inventoryId) {
            this.productNameId = productNameId;
            this.supplierId = supplierId;
            this.productName = productName;
            this.supplierName = supplierName;
            this.date = date;
            this.quantity = quantity;
            this.inventoryId = inventoryId;

        }

        public int getProductNameId() {
            return productNameId;
        }

        public int getInventoryid() {
            return inventoryId;
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
    private int i = 0;
    private int y = 0;
    private int c = 0;
    @FXML
    private TableColumn<InventoryItem, Integer> idColumn1;
    @FXML
    private TableColumn<InventoryItem, Integer> idColumn2;
    @FXML
    private TableColumn<InventoryItem, Integer> idColumn3;

    @FXML
    public void initialize() {
        idColumn1.setCellValueFactory(cellData -> new SimpleIntegerProperty(i++).asObject());
        idColumn2.setCellValueFactory(cellData -> new SimpleIntegerProperty(y++).asObject());
        idColumn3.setCellValueFactory(cellData -> new SimpleIntegerProperty(c++).asObject());

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

            String query = "SELECT productdelivery.importProductNameId,importgoods.import_id, importgoods.productName, supplier.supplierId, supplier.supplierName, productdelivery.dayShipping, productdelivery.shipmentQuantity, productdelivery.productDeliveryID "
                    + "FROM productdelivery "
                    + "INNER JOIN importgoods ON importgoods.import_id  = productdelivery.importProductNameId  "
                    + "INNER JOIN supplier ON productdelivery.supplier_id = supplier.supplierId "
                    + "ORDER BY productdelivery.productDeliveryID DESC";

            PreparedStatement preparedStatement = connection.prepareStatement(query);
            ResultSet resultSet = preparedStatement.executeQuery();

            while (resultSet.next()) {
                int productNameId = resultSet.getInt("import_id");
                int supplierId = resultSet.getInt("supplierId");
                String productName = resultSet.getString("importgoods.productName");
                String supplierName = resultSet.getString("supplierName");
                Date dayShipping = resultSet.getDate("dayShipping");
                int shipmentQuantity = resultSet.getInt("shipmentQuantity");

                productList.add(new InventoryItem(productNameId, supplierId, productName, supplierName, dayShipping, shipmentQuantity,0));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void sqlImportgoods() {
        try (Connection connection = connect.getConnection()) {

            String query = "SELECT importgoods.import_id, importGoods.productName, supplier.supplierId, supplier.supplierName, importGoods.import_date, importGoods.total_quantity_received "
                    + "FROM importGoods "
                    + "INNER JOIN supplier ON importGoods.supplier_id = supplier.supplierId "
                    + "ORDER BY importgoods.import_id DESC"; // Sắp xếp theo import_id giảm dần
            PreparedStatement preparedStatement = connection.prepareStatement(query);
            ResultSet resultSet = preparedStatement.executeQuery();

            while (resultSet.next()) {
                int productNameId = resultSet.getInt("import_id");
                int supplierId = resultSet.getInt("supplierId");
                String productName = resultSet.getString("ProductName");
                String supplierName = resultSet.getString("supplierName");
                Date importDate = resultSet.getDate("import_date");
                int totalQuantityReceived = resultSet.getInt("total_quantity_received");

                importDataList.add(new InventoryItem(productNameId, supplierId, productName, supplierName, importDate, totalQuantityReceived, 0));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void sqlInventory() {
        try (Connection connection = connect.getConnection()) {
            String query = "SELECT importgoods.import_id, importGoods.productName, supplier.supplierId, supplier.supplierName,"
                    + " inventory.date, inventory.inventory_id , inventory.inventoryNumber "
                    + "FROM inventory "
                    + "INNER JOIN importgoods ON importgoods.import_id  = inventory.importProductNameId "
                    + "INNER JOIN supplier ON inventory.supplierId = supplier.supplierId "
                    + "ORDER BY inventory.inventory_id DESC";
            PreparedStatement preparedStatement = connection.prepareStatement(query);
            ResultSet resultSet = preparedStatement.executeQuery();

            while (resultSet.next()) {
                int productNameId = resultSet.getInt("import_id");
                int supplierId = resultSet.getInt("supplierId");
                String productName = resultSet.getString("ProductName");
                String supplierName = resultSet.getString("supplierName");
                Date importDate = resultSet.getDate("date"); // Update this to match the actual column name
                int inventoryNumber = resultSet.getInt("inventoryNumber"); // Update this to match the actual column name
                int inventoryId = resultSet.getInt("inventory_id"); // Update this to match the actual column name

                InventoryList.add(new InventoryItem(productNameId, supplierId, productName, supplierName, importDate, inventoryNumber,inventoryId));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

//insert 
    // th1
    @FXML
    public void insertInventory() throws IOException {
        InventoryItem selectedDeliveryItem = ProductDeliveryTable.getSelectionModel().getSelectedItem();
        InventoryItem selectedImportItem = Importgoods.getSelectionModel().getSelectedItem();

        if (selectedDeliveryItem != null && selectedImportItem != null) {
            int productNameIdFromDelivery = selectedDeliveryItem.getProductNameId();
            int supplierIdFromDelivery = selectedDeliveryItem.getSupplierId();
            int productNameIdFromImport = selectedImportItem.getProductNameId();
            int supplierIdFromImport = selectedImportItem.getSupplierId();

            if (supplierIdFromDelivery == supplierIdFromImport && productNameIdFromDelivery == productNameIdFromImport) {
                int quantityFromImport = selectedImportItem.getQuantity();
                int quantityFromDelivery = selectedDeliveryItem.getQuantity();

                if (quantityFromImport >= quantityFromDelivery) {
                    int inventoryNumber = quantityFromImport - quantityFromDelivery;
                    if (insertInventoryRecord(productNameIdFromImport, supplierIdFromImport, inventoryNumber)) {
                        System.out.println("Added successfully!");
                        showSuccessAlert("Added successfully!");
                        getFromInventory();
                    } else {
                        showAlert("Failed to add.");
                    }
                } else {
                    showAlert("Quantity from Importgoods must be greater than or equal to Quantity from ProductDeliveryTable.");
                }
            } else {
                showAlert("SupplierId or ProductNameId do not match.");
            }
        } else {
            showAlert("Please select items from the tables.");
        }
    }

    @FXML
    public void insertInventoryTH2() throws IOException {
        InventoryItem selectedImportItem = Importgoods.getSelectionModel().getSelectedItem();
        InventoryItem selectedInventoryItem = Inventory.getSelectionModel().getSelectedItem();

        if (selectedImportItem != null && selectedInventoryItem != null) {
            int productNameIdFromImport = selectedImportItem.getProductNameId();
            int supplierIdFromImport = selectedImportItem.getSupplierId();
            int productNameIdFromInventory = selectedInventoryItem.getProductNameId();
            int supplierIdFromInventory = selectedInventoryItem.getSupplierId();

            if (supplierIdFromImport == supplierIdFromInventory && productNameIdFromImport == productNameIdFromInventory) {
                int quantityFromImport = selectedImportItem.getQuantity();
                int quantityFromInventory = selectedInventoryItem.getQuantity();

                int inventoryNumber = quantityFromImport + quantityFromInventory;

                if (insertInventoryRecord(productNameIdFromImport, supplierIdFromImport, inventoryNumber)) {
                    System.out.println("Added successfully!");
                    showSuccessAlert("Added successfully!");
                    getFromInventory();
                } else {
                    showAlert("Failed to add.");
                }
            } else {
                showAlert("SupplierId or ProductNameId do not match.");
            }
        } else {
            showAlert("Please select items from the tables.");
        }
    }

    @FXML
    public void insertInventoryTH3() throws IOException {
        InventoryItem selectedDeliveryItem = ProductDeliveryTable.getSelectionModel().getSelectedItem();
        InventoryItem selectedInventoryItem = Inventory.getSelectionModel().getSelectedItem();

        if (selectedDeliveryItem != null && selectedInventoryItem != null) {
            int productNameIdFromDelivery = selectedDeliveryItem.getProductNameId();
            int supplierIdFromDelivery = selectedDeliveryItem.getSupplierId();
            int productNameIdFromInventory = selectedInventoryItem.getProductNameId();
            int supplierIdFromInventory = selectedInventoryItem.getSupplierId();

            if (supplierIdFromDelivery == supplierIdFromInventory && productNameIdFromDelivery == productNameIdFromInventory) {
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
                    showAlert("Quantity from Inventory must be greater than or equal to Quantity from ProductDeliveryTable.");
                }
            } else {
                showAlert("SupplierId or ProductNameId do not match.");
            }
        } else {
            showAlert("Please select items from the tables.");
        }
    }

    // Phương thức để thực hiện insert vào bảng inventory
    private boolean insertInventoryRecord(int productNameId, int supplierId, int inventoryNumber) {
        try (Connection connection = connect.getConnection()) {
            String insertSQL = "INSERT INTO inventory (importProductNameId, supplierId, date, inventoryNumber) VALUES (?, ?, ?, ?)";
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

    @FXML
    public void insertInventoryTH4() throws IOException {
        InventoryItem selectedImportItem = Importgoods.getSelectionModel().getSelectedItem();

        if (selectedImportItem != null) {
            int productNameIdFromImport = selectedImportItem.getProductNameId();
            int supplierIdFromImport = selectedImportItem.getSupplierId();
            int quantityFromImport = selectedImportItem.getQuantity();

            // Insert the selected item into the inventory table
            if (insertInventoryRecord(productNameIdFromImport, supplierIdFromImport, quantityFromImport)) {
                System.out.println("Added successfully!");
                showSuccessAlert("Added successfully!");
                getFromInventory();
            } else {
                showAlert("Failed to add.");
            }
        } else {
            showAlert("Please select an item from the Importgoods table.");
        }
    }

    // delete 
//    public void deleteInventoryRecord() {
//        // Get the selected item from the TableView
//        InventoryItem selectedInventoryItem = Inventory.getSelectionModel().getSelectedItem();
//
//        if (selectedInventoryItem != null) {
//            // Create a confirmation dialog
//            Alert confirmDialog = new Alert(AlertType.CONFIRMATION);
//            confirmDialog.setTitle("Confirmation");
//            confirmDialog.setHeaderText("Delete Inventory Record");
//            confirmDialog.setContentText("Are you sure you want to delete this inventory record?");
//
//            // Show the confirmation dialog and wait for user input
//            ButtonType result = confirmDialog.showAndWait().orElse(ButtonType.CANCEL);
//
//            if (result == ButtonType.OK) {
//                int inventory_id = selectedInventoryItem.getInventoryid();
////                int supplierId = selectedInventoryItem.getSupplierId();
//
//                try (Connection connection = connect.getConnection()) {
//                    String deleteSQL = "DELETE FROM inventory WHERE inventory_id=?";
//                    PreparedStatement preparedStatement = connection.prepareStatement(deleteSQL);
//                    preparedStatement.setInt(1, inventory_id);
////                    preparedStatement.setInt(2, supplierId);
//
//                    int rowsAffected = preparedStatement.executeUpdate();
//                    if (rowsAffected > 0) {
//                        System.out.println("Inventory record deleted successfully.");
//                        showSuccessAlert("Inventory record deleted successfully.");
//                        // Remove the item from the ObservableList as well
//                        InventoryList.remove(selectedInventoryItem);
//                    } else {
//                        showAlert("Failed to delete inventory record.");
//                    }
//                } catch (SQLException e) {
//                    e.printStackTrace();
//                    showAlert("An error occurred while deleting inventory record.");
//                }
//            }
//        } else {
//            showAlert("No item selected in Inventory.");
//        }
//    }
    @FXML
    public void deleteInventoryRecord() {
        // Lấy dòng được chọn từ TableView
        InventoryItem selectedInventoryItem = Inventory.getSelectionModel().getSelectedItem();

        if (selectedInventoryItem != null) {
            // Tạo một hộp thoại xác nhận
            Alert confirmDialog = new Alert(AlertType.CONFIRMATION);
            confirmDialog.setTitle("Xác nhận");
            confirmDialog.setHeaderText("Xóa bản ghi kho hàng");
            confirmDialog.setContentText("Bạn có chắc chắn muốn xóa bản ghi kho hàng này?");

            // Hiển thị hộp thoại xác nhận và chờ người dùng nhập liệu
            ButtonType result = confirmDialog.showAndWait().orElse(ButtonType.CANCEL);

            if (result == ButtonType.OK) {
                int inventoryId = selectedInventoryItem.getInventoryid();

                try (Connection connection = connect.getConnection()) {
                    String deleteSQL = "DELETE FROM inventory WHERE inventory_id=?";
                    PreparedStatement preparedStatement = connection.prepareStatement(deleteSQL);
                    preparedStatement.setInt(1, inventoryId);

                    int rowsAffected = preparedStatement.executeUpdate();
                    if (rowsAffected > 0) {
                        System.out.println("Bản ghi kho hàng đã được xóa thành công.");
                        showSuccessAlert("Bản ghi kho hàng đã được xóa thành công.");
                        // Loại bỏ mục khỏi ObservableList cũng
                        InventoryList.remove(selectedInventoryItem);
                    } else {
                        showAlert("Không thể xóa bản ghi kho hàng.");
                    }
                } catch (SQLException e) {
                    e.printStackTrace();
                    showAlert("Có lỗi xảy ra khi xóa bản ghi kho hàng.");
                }
            }
        } else {
            showAlert("Không có mục nào được chọn trong kho hàng.");
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
