package humaneer.org.wearablerunning;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.sql.Date;

/**https://github.com/hmkcode/Android/blob/master/android-sqlite/src/com/hmkcode/android/sqlite/MySQLiteHelper.java
 * Created by Minki on 2017-03-07.
 */

public class DBHelper extends SQLiteOpenHelper {

    public static int ID;
    public static int DISTANCE;
    public static int TIME;
    public static double SPEED;
    public static Date DATE;

    private static final String DB_NAME = "FinaletDB";
    private static final int DB_VERSION = 1;

    // DBHelper 생성자로 관리할 DB 이름과 버전 정보를 받음
    public DBHelper(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
    }

    // DB를 새로 생성할 때 호출되는 함수
    @Override
    public void onCreate(SQLiteDatabase db) {
        // 새로운 테이블 생성
        /* 이름은 health_data이고, 자동으로 값이 증가하는 _id 정수형 기본키 컬럼과
        item 문자열 컬럼, price 정수형 컬럼, create_at 문자열 컬럼으로 구성된 테이블을 생성. */
        db.execSQL("CREATE TABLE health_data (_id INTEGER PRIMARY KEY AUTOINCREMENT, distance INTEGER, time INTEGER, speed REAL, date DATETIME);");
    }

    // DB 업그레이드를 위해 버전이 변경될 때 호출되는 함수
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }

    public void insert(int distance, int timeSeconds, double speed, Date date) {
        // 읽고 쓰기가 가능하게 DB 열기
        SQLiteDatabase db = getWritableDatabase();
        // DB에 입력한 값으로 행 추가
        db.execSQL("INSERT INTO health_data VALUES(null, " + distance + ", " + timeSeconds + ", " + speed + ", " + date + ");");
        db.close();
    }

    // 아직 안쓰임
    public void update(String item, int price) {
        SQLiteDatabase db = getWritableDatabase();
        // 입력한 항목과 일치하는 행의 가격 정보 수정
        db.execSQL("UPDATE health_data SET price=" + price + " WHERE item='" + item + "';");
        db.close();
    }

    // 아직 안쓰임
    public void delete(String item) {
        SQLiteDatabase db = getWritableDatabase();
        // 입력한 항목과 일치하는 행 삭제
        db.execSQL("DELETE FROM health_data WHERE item='" + item + "';");
        db.close();
    }

    public Object[] getResult() {
        // 읽기가 가능하게 DB 열기
        SQLiteDatabase db = getReadableDatabase();

        // DB에 있는 데이터를 쉽게 처리하기 위해 Cursor를 사용하여 테이블에 있는 모든 데이터 출력
        Cursor cursor = db.rawQuery("SELECT * FROM health_data", null);
        Object[] result = new Object[cursor.getCount()];

        while (cursor.moveToNext()) {
            result[0] = cursor.getString(0);    // id
            result[1] = cursor.getInt(1);       // distance
            result[2] = cursor.getInt(2);       // time seconds
            result[3] = cursor.getDouble(3);    // speed
            result[4] = cursor.getString(4);    // date
        }

        return result;
    }
}
