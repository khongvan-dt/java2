package adminController;

import java.io.IOException;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class linkA {

    // Phương thức để đóng cửa sổ hiện tại
    private void closeCurrentStage(Stage currentStage) {
        // Lấy cửa sổ hiện tại
        Stage stage = (Stage) currentStage.getScene().getWindow();

        // Đóng cửa sổ hiện tại
        stage.close();
    }

    public void openLoginForm() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("login.fxml"));
            Parent root = loader.load();

            Stage loginStage = new Stage();
            loginStage.setScene(new Scene(root));
            loginStage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void redirectToLoginForm() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/admin/createAccount.fxml"));
            Parent root = loader.load();

            Stage createAccountStage = new Stage();
            createAccountStage.setScene(new Scene(root));
            createAccountStage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void addCategory(Stage fromAddCategory) {
        try {
            // Tạo một FXMLLoader để tải tệp FXML của form đăng nhập
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/admin/addCategory.fxml"));

            // Sử dụng FXMLLoader để tải nội dung từ FXML và tạo một giao diện đồ họa (Parent)
            Parent root = loader.load();

            // Tạo một Stage mới cho form "addCategory"
            Stage addCategoryStage = new Stage();

            // Đặt Scene cho Stage với giao diện đồ họa mà bạn đã tải
            addCategoryStage.setScene(new Scene(root));

            // Hiển thị cửa sổ Stage với giao diện đồ họa
            addCategoryStage.show();

            // Đóng cửa sổ "fromAddCategory" (nếu bạn muốn đóng nó)
            closeCurrentStage(fromAddCategory);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void addProduct(Stage fromAddProduct) {
        try {
            // Tạo một FXMLLoader để tải tệp FXML của form "addProduct"
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/admin/addProduct.fxml"));

            // Sử dụng FXMLLoader để tải nội dung từ FXML và tạo một giao diện đồ họa (Parent)
            Parent root = loader.load();

            // Tạo một Stage mới cho form "addProduct"
            Stage addProductStage = new Stage();
            // Đặt Scene cho Stage với giao diện đồ họa mà bạn đã tải
            addProductStage.setScene(new Scene(root));

            // Hiển thị cửa sổ Stage với giao diện đồ họa
            addProductStage.show();

            // Đóng cửa sổ "fromAddProduct" (nếu bạn muốn đóng nó)
            closeCurrentStage(fromAddProduct);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void fromSupplier(Stage supplier) {
        try {
            // Tạo một FXMLLoader để tải tệp FXML của form đăng nhập
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/admin/addSupplier.fxml"));

            // Sử dụng FXMLLoader để tải nội dung từ FXML và tạo một giao diện đồ họa (Parent)
            Parent root = loader.load();

            // Đặt Scene cho Stage với giao diện đồ họa mà bạn đã tải
            supplier.setScene(new Scene(root));

            // Hiển thị cửa sổ Stage với giao diện đồ họa
            supplier.show();

            // Đóng cửa sổ hiện tại
            closeCurrentStage(supplier);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void openHomePage(Stage home) {
        try {
            // Tạo một FXMLLoader để tải tệp FXML của form đăng nhập
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/web/home.fxml"));

            // Sử dụng FXMLLoader để tải nội dung từ FXML và tạo một giao diện đồ họa (Parent)
            Parent root = loader.load();

            // Đặt Scene cho Stage với giao diện đồ họa mà bạn đã tải
            home.setScene(new Scene(root));

            // Hiển thị cửa sổ Stage với giao diện đồ họa
            home.show();

            // Đóng cửa sổ hiện tại
            closeCurrentStage(home);
        } catch (IOException e) {      
            e.printStackTrace();
        }
    }

    public void getMoreProductName(Stage ProductName) {
        try {
            // Tạo một FXMLLoader để tải tệp FXML của form đăng nhập
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/admin/addProductName.fxml"));

            // Sử dụng FXMLLoader để tải nội dung từ FXML và tạo một giao diện đồ họa (Parent)
            Parent root = loader.load();

            // Đặt Scene cho Stage với giao diện đồ họa mà bạn đã tải
            ProductName.setScene(new Scene(root));

            // Đóng cửa sổ hiện tại
            closeCurrentStage(ProductName);

            // Hiển thị cửa sổ Stage với giao diện đồ họa
            ProductName.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
