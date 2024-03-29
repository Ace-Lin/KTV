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
     * 添加页面添加页面
     */
    public class FragmentType
    {
        public static final int ROOMDETAIL = 0;
        public static final int PRODUCTDETAIL = 1;
        public static final int ADDROOM = 2;
        public static final int ADDPRODUCT = 3;
        public static final int SETTING = 4;
    }

    /**
     * 侧拉框选项类型
     */
    public class MineType
    {
        public static final int SET_PHOTO = 0;
        public static final int LOGIN = 1;
        public static final int PERSONAL_INFO = 2;
        public static final int SET_PWD= 3;
        public static final int LOG_OUT = 4;

    }
    /**
     * 消息类型
     */
    public class MsgType{
        public static final int UPDATE_IMG=0;


    }

    /**
     * 对话框类型
     */
    public class DlgType{
        public static final int DLG_NAME=0;
        public static final int DLG_PHONE=1;
        public static final int DLG_IDCARD=2;
        public static final int DLG_EMAIL=3;


    }

}
