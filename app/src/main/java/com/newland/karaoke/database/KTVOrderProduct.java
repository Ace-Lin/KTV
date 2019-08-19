package com.newland.karaoke.database;

import org.litepal.crud.LitePalSupport;

public class KTVOrderProduct extends LitePalSupport {

   private int id;
   private KTVProductList productList;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public KTVProductList getProductList() {
        return productList;
    }

    public void setProductList(KTVProductList productList) {
        this.productList = productList;
    }

    public int getProduct_quantity() {
        return product_quantity;
    }

    public void setProduct_quantity(int product_quantity) {
        this.product_quantity = product_quantity;
    }

    private int product_quantity;

}
