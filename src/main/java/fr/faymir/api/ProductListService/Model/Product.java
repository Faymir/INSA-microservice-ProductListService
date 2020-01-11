package fr.faymir.api.ProductListService.Model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.ArrayList;
import java.util.List;

public class Product {
    private int id;
    private String name;
    private String category;
    private String description;
    private float price;
    private String image;
    private List<String> comments;
    private List<Integer> evaluation;
    private float starts;

    public Product(){}

    public Product(int id, String name, String category, String description, float price, String image, List<String> comments, List<Integer> evaluation) {
        this.id = id;
        this.name = name;
        this.category = category;
        this.description = description;
        this.price = price;
        this.image = image;
        this.comments = comments;
        this.evaluation = evaluation;
        calculateStars();
    }

    public Product(int id, ProductInfo productInfo, List<CustomerReview> reviews){
        this.id = id;
        this.name = productInfo.getName();
        this.category = productInfo.getCategory();
        this.description = productInfo.getDescription();
        this.price = productInfo.getPrice();
        this.image = productInfo.getImage();
        this.comments = new ArrayList<>();
        this.evaluation = new ArrayList<>();
        reviews.forEach( r -> {
            this.comments.add(r.getComment());
            this.evaluation.add(r.getEvaluation());
        });
        calculateStars();

    }

    public void calculateStars(){
        if(this.evaluation == null)
            return;
        double sum = this.evaluation.stream().mapToDouble(e -> e).sum();
        this.starts = (float) (Math.round(sum / (double)this.evaluation.size() * 100.0) / 100.0);
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public float getPrice() {
        return price;
    }

    public void setPrice(float price) {
        this.price = price;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public List<String> getComments() {
        return comments;
    }

    public void setComments(List<String> comments) {
        this.comments = comments;
    }

    public List<Integer> getEvaluation() {
        return evaluation;
    }

    public void setEvaluation(List<Integer> evaluation) {
        this.evaluation = evaluation;
    }


    @JsonIgnore
    public void setStarts(float starts) {
        this.starts = starts;
    }

    @JsonProperty
    public float getStarts() {
        return starts;
    }
}
