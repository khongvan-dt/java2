package db;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

public class CreateTable {

    public static void main(String[] args) {

        Connection connection = null;
        Statement statement = null;

        try {

            connection = connect.getConnection(); // Lấy kết nối tới cơ sở dữ liệu từ lớp Connect

            // Tạo đối tượng Statement để thực thi câu lệnh SQL
            statement = connection.createStatement();

            // Create "thể loại" table  
            String createCategoryTableSQL = "CREATE TABLE IF NOT EXISTS category ("
                    + "categoryId INT PRIMARY KEY AUTO_INCREMENT,"
                    + "categoryName VARCHAR(100) NOT NULL)";
            statement.executeUpdate(createCategoryTableSQL);

            // Create "nhà cung cấp" table
            String createSupplierTableSQL = "CREATE TABLE IF NOT EXISTS supplier ("
                    + "supplierId INT PRIMARY KEY AUTO_INCREMENT,"
                    + "supplierName VARCHAR(100) NOT NULL)";
            statement.executeUpdate(createSupplierTableSQL);

            // Create "tên Sản phẩm" table
            String createProductNameTableSQL = "CREATE TABLE IF NOT EXISTS ProductsName ("
                    + "ProductNameId INT PRIMARY KEY AUTO_INCREMENT,"
                    + "ProductName VARCHAR(800) NOT NULL)";
            statement.executeUpdate(createProductNameTableSQL);

            // Create "users" table
            String createTableSQL = "CREATE TABLE IF NOT EXISTS users ("
                    + "userId INT NOT NULL AUTO_INCREMENT PRIMARY KEY," + "username VARCHAR(50) NOT NULL,"
                    + "password VARCHAR(50) NOT NULL," + "email VARCHAR(100) NOT NULL," + "role VARCHAR(20) NOT NULL)";
            statement.executeUpdate(createTableSQL);

            // Create "Nhập hàng" table
            String createImportTableSQL = "CREATE TABLE IF NOT EXISTS importGoods ("
                    + "import_id INT PRIMARY KEY AUTO_INCREMENT,"
                    + "ProductNameId INT NOT NULL,"
                    + "supplier_id INT NOT NULL,"
                    + "import_date DATE NOT NULL,"
                    + "quantity_imported INT NOT NULL,"
                    + "quantity_returned INT NOT NULL,"
                    + "total_quantity_received INT NOT NULL,"
                    + "FOREIGN KEY (ProductNameId) REFERENCES ProductsName(ProductNameId),"
                    + "FOREIGN KEY (supplier_id) REFERENCES supplier(supplierId))";
            statement.executeUpdate(createImportTableSQL);

            // Create "Xuất hàng" table
            String createExportTableSQL = "CREATE TABLE IF NOT EXISTS productDelivery ("
                    + "productDeliveryID INT PRIMARY KEY AUTO_INCREMENT," + "ProductNameId INT NOT NULL,"
                    + "supplier_id INT NOT NULL,"
                    + "dayShipping DATE NOT NULL," + "shipmentQuantity INT NOT NULL,"
                    + "FOREIGN KEY (ProductNameId) REFERENCES importgoods(ProductNameId))"
                    + "FOREIGN KEY (supplier_id) REFERENCES importgoods(supplier_id))";

            statement.executeUpdate(createExportTableSQL);

            // Create "Tồn kho" table
            String createInventoryTableSQL = "CREATE TABLE IF NOT EXISTS inventory ("
                    + "inventory_id INT PRIMARY KEY AUTO_INCREMENT," + "ProductNameId INT NOT NULL,"
                    + "InventoryNumber INT NOT NULL,"
                    + "FOREIGN KEY (ProductNameId) REFERENCES ProductsName(ProductNameId))";
            statement.executeUpdate(createInventoryTableSQL);

            // Create "Sản phẩm" table
            String createProductTableSQL = "CREATE TABLE IF NOT EXISTS product ("
                    + "productId INT PRIMARY KEY AUTO_INCREMENT,"
                    + "categoryId INT NOT NULL,"
                    + "ProductNameId INT NOT NULL,"
                    + "productImportPrice float NOT NULL,"
                    + "Description VARCHAR(1000) NOT NULL,"
                    + "price float NOT NULL,"
                    + "img  VARCHAR(200) NOT NULL,"
                    + "FOREIGN KEY (categoryId) REFERENCES category(categoryId),"
                    + "FOREIGN KEY (ProductNameId) REFERENCES ProductsName(ProductNameId))";
            statement.executeUpdate(createProductTableSQL);

            // Create "Đơn hàng" table
            String createOrderTableSQL = "CREATE TABLE IF NOT EXISTS orders ("
                    + "order_id INT PRIMARY KEY AUTO_INCREMENT," + "userId INT NOT NULL,"
                    + "customerName VARCHAR(100) NOT NULL," + "phoneNumber VARCHAR(20) NOT NULL,"
                    + "Address VARCHAR(200) NOT NULL," + "orderDate DATE NOT NULL," + "orderNotes TEXT,"
                    + "FOREIGN KEY (userId) REFERENCES users(userId))";
            statement.executeUpdate(createOrderTableSQL);

            // Create "Chi tiết đơn hàng" table
            String createOrderDetailTableSQL = "CREATE TABLE IF NOT EXISTS orderDetails ("
                    + "order_detail_id INT PRIMARY KEY AUTO_INCREMENT,"
                    + "orderId INT NOT NULL,"
                    + "product_id INT NOT NULL,"
                    + "quantity INT NOT NULL,"
                    + "price DECIMAL(10, 2) NOT NULL,"
                    + "totalPrice DECIMAL(10, 2) NOT NULL,"
                    + "FOREIGN KEY (orderId) REFERENCES orders(order_id),"
                    + // Thay đổi "order" thành "orders"
                    "FOREIGN KEY (product_id) REFERENCES product(productId))";
            statement.executeUpdate(createOrderDetailTableSQL);

            // Create "feaback" table
            String createFeedbackTableSQL = "CREATE TABLE IF NOT EXISTS feedback ("
                    + "feedbackId INT PRIMARY KEY AUTO_INCREMENT,"
                    + "userId INT NOT NULL,"
                    + "order_id INT NOT NULL,"
                    + "nd_feedback TEXT NOT NULL," + "FOREIGN KEY (userId) REFERENCES users(userId),"
                    + "FOREIGN KEY (order_id) REFERENCES orders(order_id))";
            statement.executeUpdate(createFeedbackTableSQL);

            System.out.println("Table 'users' created successfully!");

        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            if (statement != null) {
                try {
                    statement.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }

            if (connection != null) {
                try {
                    connection.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
