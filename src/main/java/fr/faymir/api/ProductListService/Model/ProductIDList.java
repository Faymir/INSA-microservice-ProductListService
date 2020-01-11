package fr.faymir.api.ProductListService.Model;

import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

@RestController
public class ProductIDList {
    List<Integer> productList;

    public ProductIDList(){
        productList = new ArrayList<>();
    }

    public List<Integer> getProductList() {
        return productList;
    }

    public void addProduct(Integer productID) {
        this.productList.add(productID);
    }

    public void removeProduct(Integer productID){
        productList.remove(productID);
    }

    public int totalProduct(){
        return  productList.size();
    }
}
