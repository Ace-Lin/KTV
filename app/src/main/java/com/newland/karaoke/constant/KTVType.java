package com.newland.karaoke.constant;

public class KTVType {

    /**
     * 订单状态
     */
    public class OrderStatus
    {
        public static final int UNPAID = 0;
        public static final int CANCEL = 1;
        public static final int PAID = 2;
    }

    /**
     * 房间状态
     */
    public   class  RoomStatus
    {
        public static final int FREE = 0;
        public static final int NO_FREE = 1;
    }

    /**
     * 房间类型
     */
    public class  RoomType
    {
        public static final int BIG = 0;
        public static final int MIDDLE = 1;
        public static final int SMAlL = 2;
    }

    /**
     * 支付类型
     */
    public class  PayType
    {
        public static final int CASH = 0;
        public static final int CARD = 1;
        public static final int QRCODE = 2;
    }

    /**
     * 卡类型
     */
    public class CardType
    {
        public static final int ICCARD = 0;
        public static final int RFCARD = 1;
        public static final int SWIPER = 2;
    }


    /**
     * 添加页面添加页面
     */
    public class FragmentType
    {
        public static final int ROOMDETAIL = 0;
        public static final int PRODUCTDETAIL = 1;
        public static final int ADDROOM = 2;
        public static final int ADDPRODUCT = 3;
        public static final int EDITROOM = 4;
        public static final int EDITRODUCT = 5;
    }

}
