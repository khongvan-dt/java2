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
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.TextField;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.ButtonType;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import main.Main;

public class addCategoryController {

    @FXML
    private TextField categoryNameField;

    public void moreCategory() throws IOException {
        String CategoryName = categoryNameField.getText();

        if (CategoryName.isEmpty()) {
            showAlert("Please fill in all fields.");
            return;
        }
        String insertSQL = "INSERT INTO category (categoryName) VALUES (?)";
        try (Connection connection = connect.getConnection(); PreparedStatement preparedStatement = connection.prepareStatement(insertSQL)) {

            preparedStatement.setString(1, CategoryName);

            int rowsAffected = preparedStatement.executeUpdate();
            if (rowsAffected > 0) {
                System.out.println(" add category successfully!");

                // Thành công: Hiển thị thông báo thành công
                showSuccessAlert("Category added successfully!");

                // Xóa nội dung trên trường nhập liệu sau khi thêm thành công
                categoryNameField.clear();
                getFromAddcategory();
            } else {
                showAlert("Failed to add category.");
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

    }

    //in dữ liệu ra bảng 
    public class Category {

        private int categoryId;
        private String categoryName;

        public Category(int categoryId, String categoryName) {
            this.categoryId = categoryId;
            this.categoryName = categoryName;
        }

        public int getCategoryId() {
            return categoryId;
        }

        public String getCategoryName() {
            return categoryName;
        }
        public void setCategoryName(String categoryName) {
            this.categoryName = categoryName;
        }
    }

    @FXML
    private TableView<Category> categoryTable;//id bảng

    @FXML
    private TableColumn<Category, String> categoryNameColumn;//id cột trong bảng 

    public void initialize() {
        categoryNameColumn.setCellValueFactory(new PropertyValueFactory<>("categoryName"));// tên cột trong db

        ObservableList<Category> Categorys = FXCollections.observableArrayList(fetchDataFromDatabase());
        categoryTable.setItems(Categorys);
    }

    private List<Category> fetchDataFromDatabase() {
        List<Category> Categorys = new ArrayList<>();

        try {
            Connection connection = connect.getConnection();
            Statement statement = connection.createStatement();
            String query = "SELECT * FROM category";
            ResultSet resultSet = statement.executeQuery(query);

            while (resultSet.next()) {
                int categoryId = resultSet.getInt("categoryId");
                String categoryName = resultSet.getString("categoryName");
                Category category = new Category(categoryId, categoryName);
                Categorys.add(category);
            }

            resultSet.close();
            statement.close();
            connection.close();
        } catch (Exception e) {
            e.printStackTrace();
        }

        return Categorys;
    }

    //edit Supplier
   @FXML
    private void showEditCategory(ActionEvent event) throws IOException {
        // Lấy hàng đã chọn từ TableView
        Category selectedCategory = categoryTable.getSelectionModel().getSelectedItem();

        if (selectedCategory != null) {
            // Tải cảnh "Sửa nhà cung cấp" và chuyển dữ liệu nhà cung cấp đã chọn
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/admin/editCategory.fxml"));
            Parent root = loader.load();

            // để trỏ đến controller của editCategoryController giống như liên kết 
            editCategoryController editCategory = loader.getController();

            // Truyền dữ liệu nhà cung cấp đã chọn cho controller của editCategoryController, initData là hàm trong editCategoryController
            //selectedCategory là giá trị đã được chọn ở trong table
            editCategory.initData(selectedCategory);
            
            Stage stage = new Stage();
            Scene scene = new Scene(root);
            stage.setScene(scene);
            stage.show();
            
        } else {
            showAlert("Please select the information you want to edit!");
        }
    }
//insert thành công sẽ hiện 
@FXML
    private void ShowDeleteBTN(ActionEvent event) throws IOException {
        // Lấy hàng đã chọn từ TableView
        Category selectedCategory = categoryTable.getSelectionModel().getSelectedItem();

        if (selectedCategory != null) {
            // Tải cảnh "Sửa nhà cung cấp" và chuyển dữ liệu nhà cung cấp đã chọn
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/admin/editCategory.fxml"));
            Parent root = loader.load();

            // để trỏ đến controller của editCategoryController giống như liên kết 
            editCategoryController editCategory = loader.getController();

            // Truyền dữ liệu nhà cung cấp đã chọn cho controller của editCategoryController, initData là hàm trong editCategoryController
            //selectedCategory là giá trị đã được chọn ở trong table
            editCategory.initData(selectedCategory);
            
            Stage stage = new Stage();
            Scene scene = new Scene(root);
            stage.setScene(scene);
            stage.show();
            
        } else {
            showAlert("Please select the information you want to edit!");
        }
    }
    
    
    @FXML
private void deleteCategory(ActionEvent event) throws IOException {
    // Get the selected category from the TableView
    Category selectedCategory = categoryTable.getSelectionModel().getSelectedItem();

    if (selectedCategory != null) {
        // Show a confirmation dialog to confirm the deletion
        Alert confirmation = new Alert(AlertType.CONFIRMATION);
        confirmation.setTitle("Confirm Delete");
        confirmation.setHeaderText("Are you sure you want to delete this category?");
        confirmation.getButtonTypes().setAll(ButtonType.YES, ButtonType.NO);

        ButtonType result = confirmation.showAndWait().orElse(ButtonType.NO);

        if (result == ButtonType.YES) {
            if (deleteCategoryFromDatabase(selectedCategory.getCategoryId())) {
                // Remove the deleted category from the TableView
                categoryTable.getItems().remove(selectedCategory);
                showSuccessAlert("Category deleted successfully!");
            } else {
                showAlert("Failed to delete category.");
            }
        }
    } else {
        showAlert("Please select the category you want to delete!");
    }
}

private boolean deleteCategoryFromDatabase(int categoryId) {
    // Implement the logic to delete the category from the database
    String deleteSQL = "DELETE FROM category WHERE categoryId = ?";
    
    try (Connection connection = connect.getConnection(); 
         PreparedStatement preparedStatement = connection.prepareStatement(deleteSQL)) {
        
        preparedStatement.setInt(1, categoryId);
        int rowsAffected = preparedStatement.executeUpdate();

        return rowsAffected > 0;
    } catch (SQLException e) {
        e.printStackTrace();
        return false;
    }
}
    
    
    
    
    private void showSuccessAlert(String message) {
        Alert alert = new Alert(AlertType.INFORMATION);
        alert.setTitle("Success");
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
// insert không thành công sẽ hiện  

    private void showAlert(String message) {
        Alert alert = new Alert(AlertType.ERROR);
        alert.setTitle("Error");
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    public void getFromAddcategory() throws IOException {
//        linka.openScene("/admin/addCategory.fxml");
        Main.setRoot("/admin/addCategory.fxml");

    }

    public void getFromAddProduct() throws IOException {
//        linka.openScene("/admin/addProduct.fxml");
        Main.setRoot("/admin/addProduct.fxml");

    }

    public void getFromfromAddSupplier() throws IOException {
//        linka.openScene("/admin/addSupplier.fxml");
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
