package adminController;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import db.connect;
import java.io.IOException;
import java.sql.Date;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javafx.beans.property.SimpleIntegerProperty;
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
import javafx.scene.control.ComboBox;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import main.Main;
import static org.apache.commons.lang3.StringUtils.isNumeric;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;

public class importGoodsController {

    public class Import {

        private int importId;
        private String productId;
        private String supplierId;
        private Date importDate;
        private int quantity;
        private int exchanged;
        private int totalReceived;
        private float price;
        private float productImportPrice;
        private float totalImportPrice;

        public Import(int importId, String productName, String supplierName, Date importDate, int quantity, int exchanged,
                int totalReceived, float price, float productImportPrice, float totalImportPrice) {
            this.importId = importId;
            this.productId = productName;
            this.supplierId = supplierName;
            this.importDate = importDate;
            this.quantity = quantity;
            this.exchanged = exchanged;
            this.totalReceived = totalReceived;
            this.price = price;
            this.productImportPrice = productImportPrice;
            this.totalImportPrice = totalImportPrice;
        }

        public int getImportId() {
            return importId;
        }

        public String getProductId() {
            return productId;
        }

        public String getSupplierId() {
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

        public Float getPrice() {
            return price;
        }

        public float getProductImportPrice() {
            return productImportPrice;
        }

        public float getTotalImportPrice() {
            return totalImportPrice;
        }

        public String getProductName() {

            return productId;
        }
    }

    @FXML
    private ComboBox<String> SupplierId;
    @FXML
    private TextField importQuantity;

    @FXML
    private TextField ProductNameText;

    @FXML
    private TextField exchangeNumber;

    @FXML
    private TextField total_quantity_received;

    @FXML
    private TextField fieldViewProductPrice;

    @FXML
    private TextField ImportPrice;

    private Map<String, Integer> supplierIdMap = new HashMap<>();

    @FXML
    private TableView<Import> importTable;

    @FXML
    private TableColumn<Import, String> productNameColumn;

    @FXML
    private TableColumn<Import, String> supplierNameColumn;

    @FXML
    private TableColumn<Import, Date> importDateColumn;

    @FXML
    private TableColumn<Import, Integer> quantityColumn;

    @FXML
    private TableColumn<Import, Integer> exchangedColumn;

    @FXML
    private TableColumn<Import, Integer> totalReceivedColumn;

    @FXML
    private TableColumn<Import, Float> price;

    @FXML
    private TableColumn<Import, Float> ImportPriceColumn;

    @FXML
    private TableColumn<Import, Float> totalImportFeecolum;
    private int i = 0;
    @FXML
    private TableColumn<Import, Integer> idColumn;

    @FXML
    private void initialize() {
        idColumn.setCellValueFactory(cellData -> new SimpleIntegerProperty(i++).asObject());

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

        } catch (SQLException e) {
            e.printStackTrace();
        }

        try (Connection connection = connect.getConnection()) {
            ObservableList<Import> imports = FXCollections.observableArrayList();

            // Your database query to retrieve data
            String query2 = "SELECT importGoods.import_id, importGoods.productName, "
                    + "supplier.supplierName, importGoods.import_date, importGoods.quantity_imported, importGoods.price, importGoods.productImportPrice,"
                    + " importGoods.totalImportFee,"
                    + "importGoods.quantity_returned, importGoods.total_quantity_received FROM importGoods "
                    + "INNER JOIN supplier ON importGoods.supplier_id = supplier.supplierId";
            PreparedStatement preparedStatement2 = connection.prepareStatement(query2);
            ResultSet resultSet2 = preparedStatement2.executeQuery();

            while (resultSet2.next()) {

                int import_id22 = resultSet2.getInt("importGoods.import_id");
                String ProductName22 = resultSet2.getString("importGoods.productName");
                String supplierName22 = resultSet2.getString("supplier.supplierName");
                Date import_date22 = resultSet2.getDate("importGoods.import_date");
                int quantity_imported22 = resultSet2.getInt("importGoods.quantity_imported");
                int quantity_returned22 = resultSet2.getInt("importGoods.quantity_returned");
                int total_quantity_received22 = resultSet2.getInt("importGoods.total_quantity_received");
                Float price22 = resultSet2.getFloat("importGoods.price");
                Float productImportPrice22 = resultSet2.getFloat("importGoods.productImportPrice");
                Float totalImportFee22 = resultSet2.getFloat("importGoods.totalImportFee");

                imports.add(new Import(import_id22, ProductName22, supplierName22,
                        import_date22, quantity_imported22, quantity_returned22,
                        total_quantity_received22, productImportPrice22, totalImportFee22, price22));
            }
            productNameColumn.setCellValueFactory(new PropertyValueFactory<>("productId"));
            supplierNameColumn.setCellValueFactory(new PropertyValueFactory<>("supplierId"));
            importDateColumn.setCellValueFactory(new PropertyValueFactory<>("importDate"));
            quantityColumn.setCellValueFactory(new PropertyValueFactory<>("quantity"));
            exchangedColumn.setCellValueFactory(new PropertyValueFactory<>("exchanged"));
            totalReceivedColumn.setCellValueFactory(new PropertyValueFactory<>("totalReceived"));
            ImportPriceColumn.setCellValueFactory(new PropertyValueFactory<>("totalImportPrice")); // Đổi tên cột này
            price.setCellValueFactory(new PropertyValueFactory<>("price"));
            totalImportFeecolum.setCellValueFactory(new PropertyValueFactory<>("productImportPrice")); // Đổi tên cột này
            // Đoạn mã dưới đây sẽ sắp xếp danh sách imports theo import_id giảm dần.
            imports.sort(Comparator.comparing(Import::getImportId).reversed());

            // Cập nhật importTable với danh sách đã sắp xếp.
            importTable.setItems(imports);

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void insertImportgoods() throws IOException {
        String quantity = importQuantity.getText().trim();
        String exchange = exchangeNumber.getText().trim();
        String selectedSupplierName = SupplierId.getValue();
        String productPrice = fieldViewProductPrice.getText().trim();
        String importPrice = ImportPrice.getText().trim();
        String ProductNameT = ProductNameText.getText().trim();
        // Kiểm tra xem mô tả có trống không hoặc quá dài không

        if (ProductNameT.length() > 500) {
            showAlert("Product Product Name cannot be longer than 5000 characters.");
            ProductNameText.clear();
            return;
        }
        if (quantity.isEmpty() || exchange.isEmpty() || selectedSupplierName == null || ProductNameT.isEmpty()
                || productPrice.isEmpty() || importPrice.isEmpty()) {
            showAlert("Please fill in all missing information fields.");
            return;
        }

        if (!isNumeric(quantity)) {
            showAlert("Quantity, exchange, product price, and import price must be numeric.");
            importQuantity.clear();
            return;
        }
        if (!isNumeric(exchange)) {
            showAlert("Quantity, exchange, product price, and import price must be numeric.");
            exchangeNumber.clear();
            return;
        }

        if (!isNumeric(productPrice)) {
            showAlert("Quantity, exchange, product price, and import price must be numeric.");
            fieldViewProductPrice.clear();
            return;
        }
        if (!isNumeric(importPrice)) {
            showAlert("Quantity, exchange, product price, and import price must be numeric.");
            ImportPrice.clear();
            return;
        }
        int supplierId = supplierIdMap.get(selectedSupplierName);

        Date currentDate = new Date(System.currentTimeMillis());

        int intQuantity = Integer.parseInt(quantity);
        int intExchange = Integer.parseInt(exchange);
        float floatImportPrice = Float.parseFloat(importPrice);
        float floatProductPrice = Float.parseFloat(productPrice);

        int totalQuantity = intQuantity - intExchange;
        float totalImportFee = (float) totalQuantity * floatImportPrice;

        String insertSQL = "INSERT INTO importgoods "
                + "(productName, supplier_id, import_date, quantity_imported, quantity_returned, total_quantity_received, price, productImportPrice, totalImportFee) "
                + "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";

        try (Connection connection = connect.getConnection(); PreparedStatement preparedStatement = connection.prepareStatement(insertSQL)) {
            preparedStatement.setString(1, ProductNameT);
            preparedStatement.setInt(2, supplierId);
            preparedStatement.setDate(3, currentDate);
            preparedStatement.setInt(4, intQuantity);
            preparedStatement.setInt(5, intExchange);
            preparedStatement.setInt(6, totalQuantity);
            preparedStatement.setFloat(7, floatProductPrice);
            preparedStatement.setFloat(8, floatImportPrice);
            preparedStatement.setFloat(9, totalImportFee);

            int rowsAffected = preparedStatement.executeUpdate();
            if (rowsAffected > 0) {
                System.out.println("Added successfully!");
                showSuccessAlert("Added successfully!");
                // Clear input fields and selections here
                clearInputFields();
                // Refresh the TableView
                getFromImportGoods();

            } else {
                showAlert("Failed to add.");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void clearInputFields() {
        importQuantity.clear();
        exchangeNumber.clear();
        SupplierId.getSelectionModel().clearSelection();
        ProductNameText.clear();
        fieldViewProductPrice.clear();
        ImportPrice.clear();
    }

    // Xóa một hóa đơn nhập hàng
    @FXML
    private void delete(ActionEvent event) throws IOException, SQLException {
        try (Connection connection = connect.getConnection()) {
            // Lấy hóa đơn nhập hàng được chọn từ TableView
            Import selectedImport = importTable.getSelectionModel().getSelectedItem();

            if (selectedImport != null) {
                // Hiển thị hộp thoại xác nhận xóa
                Alert confirmation = new Alert(AlertType.CONFIRMATION);
                confirmation.setTitle("Confirm Delete");
                confirmation.setHeaderText("Are you sure you want to delete this import?");
                confirmation.getButtonTypes().setAll(ButtonType.YES, ButtonType.NO);

                ButtonType result = confirmation.showAndWait().orElse(ButtonType.NO);

                if (result == ButtonType.YES) {
                    if (deleteImportFromDatabase(selectedImport.getImportId())) {
                        // Xóa hóa đơn nhập hàng khỏi TableView
                        importTable.getItems().remove(selectedImport);
                        showSuccessAlert("Import deleted successfully!");
                    } else {
                        // Hiển thị thông báo không cho phép xóa
                        showAlert("Deletion is not allowed due to related records.");
                    }
                }
            } else {
                showAlert("Please select the import you want to delete!");
            }
        } catch (SQLException e) {
            // Xử lý lỗi SQL
            showAlert("An error occurred while deleting the import: " + e.getMessage());
        } catch (Exception e) {
            // Xử lý các lỗi khác
            showAlert("An unexpected error occurred: " + e.getMessage());
        }
    }

    private boolean canDeleteImport(int import_id) {
        // Check if the import_id is used in the productDelivery table
        String checkUsageSQL = "SELECT COUNT(*) FROM productDelivery WHERE ProductNameId = ? OR supplier_id = ?";

        try (Connection connection = connect.getConnection(); PreparedStatement preparedStatement = connection.prepareStatement(checkUsageSQL)) {
            preparedStatement.setInt(1, import_id);  // Set ProductNameId
            preparedStatement.setInt(2, import_id);  // Set supplier_id

            ResultSet resultSet = preparedStatement.executeQuery();

            if (resultSet.next()) {
                int usageCount = resultSet.getInt(1);
                if (usageCount == 0) {
                    // Không tìm thấy sử dụng trong bảng productDelivery, có thể xóa
                    return true;
                } else {
                    // Có các tham chiếu trong bảng productDelivery, không thể xóa
                    showAlert("Không thể xóa do có liên kết trong bảng productDelivery.");
                    return false;
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return false; // Mặc định không cho phép xóa trong trường hợp có lỗi
    }

    private boolean deleteImportFromDatabase(int import_id) {
        // Implement the logic to delete the import from the database
        String deleteSQL = "DELETE FROM importgoods WHERE import_id = ?";

        try (Connection connection = connect.getConnection(); PreparedStatement preparedStatement = connection.prepareStatement(deleteSQL)) {
            preparedStatement.setInt(1, import_id);
            int rowsAffected = preparedStatement.executeUpdate();

            if (rowsAffected > 0) {
                // Deletion was successful
                return true;
            } else {
                // Deletion failed
                showAlert("Failed to delete the import.");
                return false;
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return false; // Xử lý ngoại lệ và trả về false        }
        }
    }
    //edit Supplier

    @FXML
    private void showEdit(ActionEvent event) throws IOException {
        // Lấy hóa đơn nhập hàng được chọn từ TableView
        Import selectedImport = importTable.getSelectionModel().getSelectedItem();

        if (selectedImport != null) {
            // Tải giao diện "Edit Import Goods" và truyền thông tin của hóa đơn đã chọn
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/admin/editImportGoods.fxml"));
            Parent root = loader.load();

            // Để trỏ đến controller của EditImportgood
            editImportgood editController = loader.getController();

            // Truyền thông tin hóa đơn đã chọn cho controller của EditImportgood
            editController.initData(selectedImport);

            Stage stage = new Stage();
            Scene scene = new Scene(root);
            stage.setScene(scene);
            stage.show();
        } else {
            showAlert("Please select the import you want to edit!");
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

//    // insert thành công sẽ hiện
    private void showAlert(String message) {
        Alert alert = new Alert(AlertType.ERROR);
        alert.setTitle("Error");
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    private void showSuccessAlert(String message) {
        Alert alert = new Alert(AlertType.INFORMATION);
        alert.setTitle("Success");
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
