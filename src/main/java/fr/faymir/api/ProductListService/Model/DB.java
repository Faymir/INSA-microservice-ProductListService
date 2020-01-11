package fr.faymir.api.ProductListService.Model;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class DB {
    private static DB instance = null;
    private ProductIDList products;

    private DB(){
        products = new ProductIDList();
        for (int i = 0; i < 100; i++)
            products.addProduct(i);
    }

    public static DB getInstance(){
        if(instance == null){
            instance = new DB();
        }
        return instance;
    }

    public ProductIDList productIDList(){
        return products;
    }
    public void addProductID(int id){
        products.addProduct(id);
    }
    public void deleteProductID(int id){
        products.removeProduct(id);
    }
}
