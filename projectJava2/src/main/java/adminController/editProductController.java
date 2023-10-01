package adminController;

import db.connect;
import java.io.File;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextArea;
import adminController.addProductController.Product;

public class editProductController {

    @FXML
    private ComboBox<String> fieldViewProductCategoryId;

    @FXML
    private ComboBox<String> fieldViewProductName;

    @FXML
    private ComboBox<String> supplierId;

    @FXML
    private TextArea fieldViewProductDescriptions;

    private ObservableList<Product> productList = FXCollections.observableArrayList();

    private File selectedImageFile;

    private Map<String, Integer> categoryNameidMap = new HashMap<>();
    private Map<String, Integer> productNameIdMap = new HashMap<>();
    private Map<String, Integer> supplierNamesMap = new HashMap<>();
    private Product selectedProduct;

    public void setSelectedProduct(Product product) {
        this.selectedProduct = product;
    }

    @FXML
    public void initialize() {

        try (Connection connection = connect.getConnection()) {
            // Populate the category ComboBox with data from the database
            String selectCategory = "SELECT categoryId, categoryName FROM category";
            PreparedStatement preparedStatement = connection.prepareStatement(selectCategory);
            ResultSet resultSet = preparedStatement.executeQuery();
            ObservableList<String> categoryList = FXCollections.observableArrayList();
            while (resultSet.next()) {
                String categoryName = resultSet.getString("categoryName");
                categoryList.add(categoryName);
                categoryNameidMap.put(categoryName, resultSet.getInt("categoryId"));
            }
            fieldViewProductCategoryId.setItems(categoryList);

            // Populate the product ComboBox with data from the database
            String selectProduct = "SELECT importGoods.ProductNameId, ProductsName.ProductName FROM importGoods "
                    + "INNER JOIN ProductsName ON importGoods.ProductNameId = ProductsName.ProductNameId "
                    + "GROUP BY importGoods.supplier_id, importGoods.ProductNameId";
            PreparedStatement preparedStatement2 = connection.prepareStatement(selectProduct);
            ResultSet resultSet2 = preparedStatement2.executeQuery();
            ObservableList<String> productNamesList = FXCollections.observableArrayList();
            while (resultSet2.next()) {
                String productsName = resultSet2.getString("ProductsName.ProductName");
                productNamesList.add(productsName);
                productNameIdMap.put(productsName, resultSet2.getInt("importGoods.ProductNameId"));
            }
            fieldViewProductName.setItems(productNamesList);

            // Populate the supplier ComboBox with data from the database
            String selectSupplier = "SELECT importGoods.supplier_id, supplier.supplierName FROM importGoods "
                    + "INNER JOIN supplier ON importGoods.supplier_id = supplier.supplierId "
                    + "GROUP BY importGoods.supplier_id";
            PreparedStatement preparedStatement3 = connection.prepareStatement(selectSupplier);
            ResultSet resultSet3 = preparedStatement3.executeQuery();
            ObservableList<String> supplierNamesList = FXCollections.observableArrayList();
            while (resultSet3.next()) {
                String supplierName = resultSet3.getString("supplier.supplierName");
                supplierNamesList.add(supplierName);
                supplierNamesMap.put(supplierName, resultSet3.getInt("importGoods.supplier_id"));
            }
            supplierId.setItems(supplierNamesList);

        } catch (SQLException e) {
            e.printStackTrace();
        }

        // Set the initial values for the ComboBoxes based on the selected product
        fieldViewProductCategoryId.setValue(selectedProduct.getCategory());
        fieldViewProductName.setValue(selectedProduct.getProductName());
        supplierId.setValue(selectedProduct.getSupplierName());
        fieldViewProductDescriptions.setText(selectedProduct.getDescription());
    }
}
