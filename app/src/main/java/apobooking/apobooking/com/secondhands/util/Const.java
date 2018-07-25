package apobooking.apobooking.com.secondhands.util;

/**
 * Created by procreationsmac on 09/07/2018.
 */

public abstract class Const {
    public class Firebase{
        public static final String ALL_CITIES = "All cities";
        public static final String ALL_SHOPS = "All shops";
        public static final String SHOP_ID = "id";
        public static final String CITY_ID = "cityId";
        public static final String NAME_ID = "nameId";
        public static final String IMAGE_PATH = "imagePath";
        public static final String UPDATE_DAY = "updateDay";
        public static final String ADDRESS = "address";
        public static final String BASE_IMAGE_REFERENCE = "gs://secondhand-208310.appspot.com/";
        public static final String SHOPS_NAME_NAME = "name";
        public static final String CITIES_NAME = "name";
        public static final String IMAGES_ARRAY = "images";

        public class  Tables{
            public static final String SHOPS = "shops";
            public static final String CITIES = "cities";
            public static final String SHOPS_NAME = "shopsName";
        }
    }

    public class RecyclerView{
        public static final int TOTAL_ITEM_EACH_LOAD = 10;
    }

    public class Bundle{
        public static final String SHOP_NAME = "shopName";
        public static final String SHOP_CITY = "shopCity";
        public static final String SHOP_UPDATE_DAY = "shopUpdateDay";
        public static final String SHOP_ID = "shopId";
        public static final String LOAD_ONE_SHOP = "loadOneShop";
    }


}
