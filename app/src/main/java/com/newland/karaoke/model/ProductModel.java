package com.newland.karaoke.model;


import com.newland.karaoke.database.KTVProduct;


public class ProductModel{
    private String product_name;
    private int product_count;
    private double product_price;
    private String product_picture;
    private int product_num;
    private int product_id;

    public int getProduct_id() {
        return product_id;
    }

    public ProductModel(KTVProduct ktvProduct) {
        product_num=0;
        product_count= ktvProduct.getProduct_count();
        product_price= ktvProduct.getProduct_price();
        product_picture= ktvProduct.getProduct_picture();
        product_name= ktvProduct.getProduct_name();
        product_id= ktvProduct.getId();
    }

    public String getProduct_name() {
        return product_name;
    }

    public void setProduct_name(String product_name) {
        this.product_name = product_name;
    }

    public int getProduct_count() {
        return product_count;
    }

    public void setProduct_count(int product_count) {
        this.product_count = product_count;
    }

    public double getProduct_price() {
        return product_price;
    }

    public void setProduct_price(double product_price) {
        this.product_price = product_price;
    }

    public String getProduct_picture() {
        return product_picture;
    }

    public void setProduct_picture(String product_picture) {
        this.product_picture = product_picture;
    }

    public int getProduct_num() {
        return product_num;
    }

    public void setProduct_num(int product_num) {
        this.product_num = product_num;
    }
}
