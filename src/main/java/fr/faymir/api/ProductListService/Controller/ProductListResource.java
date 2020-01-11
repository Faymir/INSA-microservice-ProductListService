package fr.faymir.api.ProductListService.Controller;

import fr.faymir.api.ProductListService.Model.*;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/products")
public class ProductListResource {

    @GetMapping("/ids")
    public ProductIDList getProductsId(){
        return DB.getInstance().productIDList();
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Product addProduct(@RequestBody Product product){
//        product.calculateStars();
        RestTemplate restTemplate = new RestTemplate();
        HttpEntity<ProductInfo> productInfoHttpEntity = new HttpEntity<>(
                new ProductInfo(product.getId(),
                        product.getName(),
                        product.getCategory(),
                        product.getDescription(),
                        product.getPrice(),
                        product.getImage())
        );
        List<CustomerReview> reviews = new ArrayList<>();
        for (int i = 0; i < product.getComments().size(); i++) {
            reviews.add(new CustomerReview(product.getId(), product.getComments().get(i),
                    product.getEvaluation().get(i)));
        }

        HttpEntity< List<CustomerReview> > reviewsHttpEntity = new HttpEntity<>(reviews);

        ProductInfo createdProductInfo = restTemplate.postForObject(
                "http://localhost:8081/productDetail/", productInfoHttpEntity, ProductInfo.class);
        assert createdProductInfo != null;

        ResponseEntity<List<CustomerReview>> response =
                restTemplate.exchange("http://localhost:8082/reviews/",
                        HttpMethod.POST, reviewsHttpEntity, new ParameterizedTypeReference<List<CustomerReview>>() {
                        });
        assert response.getStatusCode() == HttpStatus.CREATED;

        List<CustomerReview> createdReviews = response.getBody();
        assert createdReviews != null;
        DB.getInstance().addProductID(product.getId());
        product.calculateStars();
        System.out.println(product.getCategory() + "\t" +
                createdProductInfo.getCategory() + "\treviews = " +
                createdReviews.size());
        return product;
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteProduct(@PathVariable("id") Integer id){
        RestTemplate restTemplate = new RestTemplate();
        restTemplate.delete("http://localhost:8081/productDetail/" + id);
        restTemplate.delete("http://localhost:8082/reviews/" + id);
        DB.getInstance().deleteProductID(id);
    }

    @GetMapping("/all/{category}")
    public List<Product> getByCategory(@PathVariable("category") String category){
        RestTemplate restTemplate = new RestTemplate();

//        List<ProductInfo> productInfoList = restTemplate.getForObject("http://localhost:8081/productDetail/all/" + category, List<>.class);
        ResponseEntity<List<ProductInfo>> rateResponse =
                restTemplate.exchange("http://localhost:8081/productDetail/all/" + category,
                        HttpMethod.GET, null, new ParameterizedTypeReference<List<ProductInfo>>() {
                        });
        List<ProductInfo> productInfoList = rateResponse.getBody();

        if(productInfoList == null || productInfoList.isEmpty())
            return new ArrayList<>();

        List<Product> products = new ArrayList<>();
        for (int i = 0; i < productInfoList.size(); i++) {
            int id = productInfoList.get(i).getId();
            List<CustomerReview> customerReviews = getReviews(id);
            assert customerReviews != null;
            products.add(new Product(id, productInfoList.get(i),customerReviews));
        }
        return products;
    }


    @GetMapping
    public List<Product> getAllProducts(){

        RestTemplate restTemplate = new RestTemplate();

        List<Product> products = new ArrayList<>();

        getProductsId().getProductList().forEach( i-> {
            ProductInfo productInfo = restTemplate.getForObject("http://localhost:8081/productDetail/" + i, ProductInfo.class);
            List<CustomerReview> customerReviews = getReviews(i);
//            List<CustomerReview> customerReview = restTemplate.getForObject("http://localhost:8082/reviews/" + i, CustomerReview.class);

            assert productInfo != null;
            products.add(new Product(i, productInfo,customerReviews));
        });
        return  products;
    }

    @GetMapping("/one/{id}")
    public Product getProductById(@PathVariable("id") Integer id){
        RestTemplate restTemplate = new RestTemplate();
        ProductInfo productInfo = restTemplate.getForObject("http://localhost:8081/productDetail/" + id, ProductInfo.class);
        List<CustomerReview> customerReviews = getReviews(id);

        if(productInfo == null)
            return  null;
        Product p = new Product(id, productInfo, customerReviews);
        return  p;
    }


    @GetMapping("/total")
    public Integer getProductsNumber(){
        return  getProductsId().totalProduct();
    }

    private List<CustomerReview> getReviews(int id){
        RestTemplate restTemplate = new RestTemplate();
        ResponseEntity<List<CustomerReview>> cRResponse =
                restTemplate.exchange("http://localhost:8082/reviews/" + id,
                        HttpMethod.GET, null, new ParameterizedTypeReference<List<CustomerReview>>() {
                        });
        return cRResponse.getBody();
    }
}
