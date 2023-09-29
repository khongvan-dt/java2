/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package webController;

/**
 *
 * @author Administrator
 */
public class SharedData {

    private static int categoryId;

    public static int getCategoryId() {
        return categoryId;
    }

    public static void setCategoryId(int categoryId) {
        SharedData.categoryId = categoryId;
    }
}
