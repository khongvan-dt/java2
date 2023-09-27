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
import java.util.Optional;
import java.util.ResourceBundle;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.ButtonType;
import javafx.stage.Stage;

public class productDeliveryController {

    public class DeliveryData {

        private String supplierName;
        private int supplierId;
        private String productName;
        private int productNameId;
        private Date inventoryDate;
        private int inventoryNumber;
        private int productDeliveryID;

        public DeliveryData(String productName, String supplierName, Date importDate, int quantity, int productNameId, int supplierId, int productDeliveryID) {
            this.productName = productName;
            this.supplierName = supplierName;
            this.inventoryDate = importDate;
            this.inventoryNumber = quantity;
            this.productNameId = productNameId;
            this.supplierId = supplierId;
            this.productDeliveryID = productDeliveryID;
        }

        public int getProductDeliveryID() {
            return productDeliveryID;
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
    private TableView<DeliveryData> ProductDeliveryTable;

    @FXML
    private TableColumn<DeliveryData, String> productName;

    @FXML
    private TableColumn<DeliveryData, String> supplierName;

    @FXML
    private TableColumn<DeliveryData, Date> importDate;

    @FXML
    private TableColumn<DeliveryData, Integer> quantityColumn;

    private int i = 0;
    private int y = 0;
    @FXML
    private TableColumn<DeliveryData, Integer> idColumn;
    
    @FXML
    private TableColumn<DeliveryData, Integer> id1;

    @FXML
    private TextField quantity;

    @FXML
    public void initialize() {
         id1.setCellValueFactory(cellData -> new SimpleIntegerProperty(y++).asObject());
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

                inventoryList.add(new DeliveryData(productName, supplierName, importDate, inventoryNumber, productNameId, supplierId, 0));

            }
            // Bind columns to properties in the DeliveryData class
            productNameColumn.setCellValueFactory(new PropertyValueFactory<>("productName"));
            supplierNameColumn.setCellValueFactory(new PropertyValueFactory<>("supplierName"));
            importDateColumn.setCellValueFactory(new PropertyValueFactory<>("importDate"));
            quantity2.setCellValueFactory(new PropertyValueFactory<>("quantity"));
            exportTable.setItems(inventoryList);

        } catch (SQLException e) {
            e.printStackTrace();
        }
        idColumn.setCellValueFactory(cellData -> new SimpleIntegerProperty(i++).asObject());

        try (Connection connection = connect.getConnection()) {

            String query = "SELECT ProductsName.ProductName, supplier.supplierName, ProductsName.ProductNameId, supplier.supplierId,"
                    + "productdelivery.dayShipping, productdelivery.shipmentQuantity, productdelivery.productDeliveryID "
                    + "FROM productdelivery "
                    + "INNER JOIN ProductsName ON productdelivery.ProductNameId = ProductsName.ProductNameId "
                    + "INNER JOIN supplier ON productdelivery.supplier_id = supplier.supplierId";

            PreparedStatement preparedStatement = connection.prepareStatement(query);
            ResultSet resultSet = preparedStatement.executeQuery();

            ObservableList<DeliveryData> deliveryDataList = FXCollections.observableArrayList();

            while (resultSet.next()) {

                int productDeliveryID = resultSet.getInt("productDeliveryID");
                int productNameId2 = resultSet.getInt("ProductNameId");
                int supplierId2 = resultSet.getInt("supplierId");
                String productName2 = resultSet.getString("ProductName");
                String supplierName2 = resultSet.getString("supplierName");
                Date importDate2 = resultSet.getDate("dayShipping");
                int shipmentQuantity2 = resultSet.getInt("shipmentQuantity");
                // Create a new DeliveryData object and add it to the list
                deliveryDataList.add(new DeliveryData(productName2, supplierName2, importDate2,
                        shipmentQuantity2, productNameId2, supplierId2, productDeliveryID));
            }
            productName.setCellValueFactory(new PropertyValueFactory<>("productName"));
            supplierName.setCellValueFactory(new PropertyValueFactory<>("supplierName"));
            importDate.setCellValueFactory(new PropertyValueFactory<>("importDate"));
            quantityColumn.setCellValueFactory(new PropertyValueFactory<>("quantity"));

            ProductDeliveryTable.setItems(deliveryDataList);
            for (DeliveryData data : deliveryDataList) {
                System.out.println(data);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // Insert
    public void insertProductDelivery() throws IOException {
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
                getFromProductDelivery();
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
    private void deleteProductDelivery(ActionEvent event) throws IOException {
        // Get the selected item from the ProductDeliveryTable
        DeliveryData selectedProductDelivery = ProductDeliveryTable.getSelectionModel().getSelectedItem();

        if (selectedProductDelivery != null) {
            // Show a confirmation dialog to confirm the deletion
            Alert confirmation = new Alert(AlertType.CONFIRMATION);
            confirmation.setTitle("Confirm Delete");
            confirmation.setHeaderText("Are you sure you want to delete this product delivery?");
            confirmation.getButtonTypes().setAll(ButtonType.YES, ButtonType.NO);

            ButtonType result = confirmation.showAndWait().orElse(ButtonType.NO);

            if (result == ButtonType.YES) {
                if (deleteProductDeliveryFromDatabase(selectedProductDelivery.getProductDeliveryID())) {
                    // Remove the deleted item from the ProductDeliveryTable
                    ProductDeliveryTable.getItems().remove(selectedProductDelivery);
                    showSuccessAlert("Product delivery deleted successfully!");
                } else {
                    showAlert("Failed to delete product delivery.");
                }
            }
        } else {
            showAlert("Please select the product delivery you want to delete!");
        }
    }

    private boolean deleteProductDeliveryFromDatabase(int productDeliveryID) {
        // Implement the logic to delete the product delivery from the database
        String deleteSQL = "DELETE FROM productdelivery WHERE productDeliveryID = ?";

        try (Connection connection = connect.getConnection(); PreparedStatement preparedStatement = connection.prepareStatement(deleteSQL)) {
            preparedStatement.setInt(1, productDeliveryID);
            int rowsAffected = preparedStatement.executeUpdate();

            return rowsAffected > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
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
