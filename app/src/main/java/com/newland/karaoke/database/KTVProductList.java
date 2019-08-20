package com.newland.karaoke.database;

import org.litepal.LitePal;
import org.litepal.crud.LitePalSupport;

import java.util.ArrayList;
import java.util.List;

public class KTVProductList extends LitePalSupport {
    private int id;
    private String product_name;
    private int product_count;
    private double product_price;
    private int product_picture;
    private List<KTVOrderProduct> product = new ArrayList<KTVOrderProduct>();

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
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

    public int getProduct_picture() {
        return product_picture;
    }

    public void setProduct_picture(int product_picture) {
        this.product_picture = product_picture;
    }

    public List<KTVOrderProduct> getProduct() {

        return product;
    }

    public void setProduct(List<KTVOrderProduct> product) {
        this.product = product;
    }

    @Override
    public String toString()
    {
       return "KTVProductListInfo{" +
               "id=" + id +
               " 商品名字=" + product_name +
               " 商品数量=" + product_count +
               ", 商品价格=" + product_price + '\'' +
               '}';
    }


}
