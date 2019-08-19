package com.newland.karaoke.database;

import org.litepal.LitePal;
import org.litepal.crud.LitePalSupport;

import java.util.List;

public class KTVOrderProduct extends LitePalSupport {

   private int id;
   private KTVProductList productList;
   private int product_quantity;
    private  KTVOrderInfo ktvOrderInfo;

    public KTVOrderInfo getKtvOrderInfo() {
        return ktvOrderInfo;
    }

    public void setKtvOrderInfo(KTVOrderInfo ktvOrderInfo) {
        this.ktvOrderInfo = ktvOrderInfo;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public KTVProductList getProductList() {
        String linkId=this.getClass().getSimpleName().toLowerCase();
        List<KTVProductList> list= LitePal.where(linkId+"_id =?",String.valueOf(id)).find(KTVProductList.class);

        if(list.isEmpty())
        {
            return null;
        }
        else
        {
            return list.get(0);
        }

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


    @Override
    public String toString()
    {
        return "KTVOrderProduct{" +
                "id=" + id +
                " 商品详细=" + getProductList().toString() +
                ", 下单数量=" + product_quantity + '\'' +
                '}';
    }

}
