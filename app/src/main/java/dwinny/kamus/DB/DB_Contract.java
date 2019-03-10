package dwinny.kamus.DB;

import android.provider.BaseColumns;

public class DB_Contract {
    static String TABLE_ID = "indo";
    static String TABLE_EN = "english";

    static final class KolomKata implements BaseColumns {
        static String KATA = "kata";
        static String ARTI = "arti";
    }
}
