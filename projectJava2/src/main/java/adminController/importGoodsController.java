package adminController;

import db.connect;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Date;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.event.ActionEvent;
import main.Main;

public class importGoodsController {
    public class Import {
    private int importId;
    private int productId;
    private int supplierId;
    private Date importDate;
    private int quantity;
    private int exchanged;
    private int totalReceived;

    public Import(int importId, int productId, int supplierId, Date importDate, int quantity, int exchanged, int totalReceived) {
        this.importId = importId;
        this.productId = productId;
        this.supplierId = supplierId;
        this.importDate = importDate;
        this.quantity = quantity;
        this.exchanged = exchanged;
        this.totalReceived = totalReceived;
    }
  

    public int getImportId() {
        return importId;
    }

    public int getProductId() {
        return productId;
    }

    public int getSupplierId() {
        return supplierId;
    }

    public Date getImportDate() {
        return importDate;
    }

    public int getQuantity() {
        return quantity;
    }

    public int getExchanged() {
        return exchanged;
    }

    public int getTotalReceived() {
        return totalReceived;
    }
}

    @FXML
    private ComboBox<String> SupplierId;

    @FXML
    private ComboBox<String> fieldViewProductName;

    @FXML
    private TextField importQuantity;

    @FXML
    private TextField exchangeNumber;

    @FXML
    private TextField total_quantity_received;

    @FXML
    private TableView<Import> importTable;

    @FXML
    private TableColumn<Import, Integer> importIdColumn;

    @FXML
    private TableColumn<Import, Integer> productIdColumn;

    @FXML
    private TableColumn<Import, Integer> supplierIdColumn;

    @FXML
    private TableColumn<Import, Date> importDateColumn;

    @FXML
    private TableColumn<Import, Integer> quantityColumn;

    @FXML
    private TableColumn<Import, Integer> exchangedColumn;

    @FXML
    private TableColumn<Import, Integer> totalReceivedColumn;

    private Map<String, Integer> supplierIdMap = new HashMap<>();
    private Map<String, Integer> productNameIdMap = new HashMap<>();

    @FXML
    private void initialize() {
        try (Connection connection = connect.getConnection()) {
            String selectSupplier = "SELECT supplierId, supplierName FROM supplier";
            PreparedStatement preparedStatement = connection.prepareStatement(selectSupplier);
            ResultSet resultSet = preparedStatement.executeQuery();
            ObservableList<String> suppliers = FXCollections.observableArrayList();

            while (resultSet.next()) {
                int supplierID = resultSet.getInt("supplierId");
                String supplierName = resultSet.getString("supplierName");
                suppliers.add(supplierName);
                supplierIdMap.put(supplierName, supplierID);
            }

            SupplierId.setItems(suppliers);

            String selectProduct = "SELECT ProductNameId, ProductName FROM ProductsName";
            PreparedStatement preparedStatement2 = connection.prepareStatement(selectProduct);
            ResultSet resultSet2 = preparedStatement2.executeQuery();
            ObservableList<String> productNames = FXCollections.observableArrayList();

            while (resultSet2.next()) {
                int productNameId = resultSet2.getInt("ProductNameId");
                String productsName = resultSet2.getString("ProductName");
                productNames.add(productsName);
                productNameIdMap.put(productsName, productNameId);
            }

            fieldViewProductName.setItems(productNames);

            // Load danh sách Nhập hàng vào TableView
            ObservableList<Import> imports = FXCollections.observableArrayList(fetchDataFromDatabase());
            importIdColumn.setCellValueFactory(new PropertyValueFactory<>("importId"));
            productIdColumn.setCellValueFactory(new PropertyValueFactory<>("productId"));
            supplierIdColumn.setCellValueFactory(new PropertyValueFactory<>("supplierId"));
            importDateColumn.setCellValueFactory(new PropertyValueFactory<>("importDate"));
            quantityColumn.setCellValueFactory(new PropertyValueFactory<>("quantity"));
            exchangedColumn.setCellValueFactory(new PropertyValueFactory<>("exchanged"));
            totalReceivedColumn.setCellValueFactory(new PropertyValueFactory<>("totalReceived"));

            importTable.setItems(imports);

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void insertImportgoods() {
        String Quantity = importQuantity.getText();
        String exchange = exchangeNumber.getText();
        String totalQuantity = total_quantity_received.getText();

        String selectedSupplierName = SupplierId.getValue();
        String selectedProductName = fieldViewProductName.getValue();

        if (Quantity.isEmpty() || exchange.isEmpty() || totalQuantity.isEmpty() || selectedSupplierName == null || selectedProductName == null) {
            showAlert("Please fill in all fields and select a supplier and product.");
            return;
        }

        int supplierId = supplierIdMap.get(selectedSupplierName);
        int productNameId = productNameIdMap.get(selectedProductName);

        java.util.Date currentDate = new java.util.Date();
        Date dateNew = new Date(currentDate.getTime());

        String insertSQL = "INSERT INTO importgoods (ProductNameId, supplier_id, import_date, quantity_imported, quantity_returned, total_quantity_received) "
                + "VALUES (?, ?, ?, ?, ?, ?)";

        try (Connection connection = connect.getConnection(); PreparedStatement preparedStatement = connection.prepareStatement(insertSQL)) {

            preparedStatement.setInt(1, productNameId);
            preparedStatement.setInt(2, supplierId);
            preparedStatement.setDate(3, dateNew);
            preparedStatement.setString(4, Quantity);
            preparedStatement.setString(5, exchange);
            preparedStatement.setString(6, totalQuantity);

            int rowsAffected = preparedStatement.executeUpdate();
            if (rowsAffected > 0) {
                System.out.println("Add successfully!");
                showSuccessAlert("Added successfully!");
                importQuantity.clear();
                exchangeNumber.clear();
                total_quantity_received.clear();
                SupplierId.getSelectionModel().clearSelection();
                fieldViewProductName.getSelectionModel().clearSelection();
                // Sau khi thêm dữ liệu mới, cập nhật lại TableView
                importTable.getItems().clear();
                importTable.getItems().addAll(fetchDataFromDatabase());
            } else {
                showAlert("Failed to add.");
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private List<Import> fetchDataFromDatabase() {
        List<Import> imports = new ArrayList<>();

        try (Connection connection = connect.getConnection()) {
            String query = "SELECT * FROM import";
            PreparedStatement preparedStatement = connection.prepareStatement(query);
            ResultSet resultSet = preparedStatement.executeQuery();

            while (resultSet.next()) {
                int importId = resultSet.getInt("importId");
                int productId = resultSet.getInt("productNameId");
                int supplierId = resultSet.getInt("supplierId");
                Date importDate = resultSet.getDate("importDate");
                int quantity = resultSet.getInt("importQuantity");
                int exchanged = resultSet.getInt("damagedQuantity");
                int totalReceived = resultSet.getInt("receivedQuantity");
                Import importData = new Import(importId, productId, supplierId, importDate, quantity, exchanged, totalReceived);
                imports.add(importData);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return imports;
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
