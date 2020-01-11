package fr.faymir.api.ProductListService.Model;

public class CustomerReview {
    private Integer id;
    private String comment;
    private int evaluation;

    public CustomerReview(){}

    public CustomerReview(Integer id, String comment, int evaluation) {
        this.id = id;
        this.comment = comment;
        this.evaluation = evaluation;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public int getEvaluation() {
        return evaluation;
    }

    public void setEvaluation(int evaluation) {
        this.evaluation = evaluation;
    }
}
