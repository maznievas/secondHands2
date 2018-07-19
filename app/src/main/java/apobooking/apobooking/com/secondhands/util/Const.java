package apobooking.apobooking.com.secondhands.util;

/**
 * Created by procreationsmac on 09/07/2018.
 */

public abstract class Const {
    public class Firebase{
        public static final String ALL_CITIES = "All cities";
        public static final String ALL_SHOPS = "All shops";
        public static final String CITY_ID = "cityId";
        public static final String NAME_ID = "nameId";
        public static final String UPDATE_DAY = "updateDay";
        public static final String ADDRESS = "address";

        public class  Tables{
            public static final String SHOPS = "shops";
            public static final String CITIES = "cities";
            public static final String SHOPS_NAME = "shopsName";
        }
    }

    public class RecyclerView{
        public static final int TOTAL_ITEM_EACH_LOAD = 3;
    }

    public class Bundle{
        public static final String SHOP_NAME = "shopName";
        public static final String SHOP_CITY = "shopCity";
        public static final String SHOP_UPDATE_DAY = "shopUpdateDay";
    }
}
