package com.newland.karaoke.database;

import org.litepal.LitePal;
import org.litepal.crud.LitePalSupport;

public class KTVOrderProduct extends LitePalSupport {

   private int id;
   private int product_quantity;
   private  KTVOrderInfo ktvOrderInfo;
   private  KTVProduct product;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getProduct_quantity() {
        return product_quantity;
    }

    public void setProduct_quantity(int product_quantity) {
        this.product_quantity = product_quantity;
    }

    public KTVProduct product(){
        return product;
    }

    public KTVProduct getProduct() {
       // KTVOrderProduct product = LitePal.find(KTVOrderProduct.class,getId());
        //LitePal.find(KTVOrderProduct.class,getId(),true).productList;
        return LitePal.find(KTVOrderProduct.class,getId(),true).product();
    }

    public void setProduct(KTVProduct product) {
        this.product = product;
    }


    public KTVOrderInfo getKtvOrderInfo() {
        return ktvOrderInfo;
    }

    public void setKtvOrderInfo(KTVOrderInfo ktvOrderInfo) {
        this.ktvOrderInfo = ktvOrderInfo;
    }

    @Override
    public String toString()
    {
        return "KTVOrderProduct{" +
                "id=" + id +
                " 商品详细=" + getProduct() +
                ", 下单数量=" + product_quantity + '\'' +
                '}';
    }

}
