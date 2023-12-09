package bloomand.myapplication

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
class DbHelper(context: Context) : SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {

    companion object {
        private const val DATABASE_VERSION = 1
        private const val DATABASE_NAME = "DISH"

        private const val TABLE_USERS = "users"
        private const val TABLE_DISHES = "dishes"
        private const val TABLE_USER_DISHES = "user_dishes"
    }

    fun addDish(dish: Dish){
        val cv = ContentValues()
        cv.put("name", dish.name)
        cv.put("calorie", dish.calorie)
        cv.put("time", dish.time)
        cv.put("ingredient", dish.ingredient)
        cv.put("difficulty", dish.difficulty)
        cv.put("imageID", dish.imageID)

        val db = this.writableDatabase
        db.insert(TABLE_DISHES, null, cv)
        db.close()
    }

    fun addUser(user: User){
        val cv = ContentValues()
        cv.put("email",user.email)
        cv.put("password",user.password)

        val db = this.writableDatabase
        db.insert(TABLE_USERS,null, cv)

        db.close()
    }

    fun checkUser(username: String): Boolean {
        val db = this.readableDatabase
        val cursor = db.query(TABLE_USERS, arrayOf("id"), "username=?", arrayOf(username), null, null, null)
        val exists = cursor.moveToFirst()
        cursor.close()

        return exists
    }

    fun getUser(username: String, password: String): Boolean {
        val db = this.readableDatabase
        val cursor = db.query(TABLE_USERS, arrayOf("id"), "username=? AND password=?", arrayOf(username, password), null, null, null)
        val exists = cursor.moveToFirst()
        cursor.close()

        return exists
    }
    fun getUserID(username: String): Int {
        val db = this.readableDatabase
        val cursor = db.query(TABLE_USERS, arrayOf("id"), "username=?", arrayOf(username), null, null, null)
        var userId = -1
        if (cursor.moveToFirst()) {
            userId = cursor.getInt(cursor.getColumnIndex("id"))
        }
        return userId
    }

    fun getDish(name: String): Int {
        val db = this.readableDatabase

        val dishQuery = "SELECT id FROM $TABLE_DISHES WHERE name=?"
        val cursor = db.rawQuery(dishQuery, arrayOf(name))

        var dishId = -1
        if (cursor.moveToFirst()) {
            dishId = cursor.getInt(cursor.getColumnIndex("id"))
        }
        cursor.close()
        return dishId
    }

    fun saveFavDish(userId: Int, dishId: Int) {
        val db = this.writableDatabase
        val cv = ContentValues()
        cv.put("userid", userId)
        cv.put("dishid", dishId)
        db.insert(TABLE_USER_DISHES, "id", cv)
    }

    fun getFavDish(userId: Int): List<Dish> {
        val listOfDish = mutableListOf<Dish>()
        val db = this.readableDatabase
        val favCitiesQuery = """
        SELECT $TABLE_DISHES.*
        FROM $TABLE_DISHES INNER JOIN $TABLE_USER_DISHES
        ON $TABLE_DISHES.id = $TABLE_USER_DISHES.dishid 
        WHERE $TABLE_USER_DISHES.userid = ?
    """.trimIndent()

        val dishCursor = db.rawQuery(favCitiesQuery, arrayOf(userId.toString()))
        if (dishCursor.moveToFirst()) {
            do {
                listOfDish.add(Dish(dishCursor.getInt(0), dishCursor.getString(1), dishCursor.getInt(3), dishCursor.getInt(2), dishCursor.getString(5),dishCursor.getInt(4),dishCursor.getInt(6)))
            } while (dishCursor.moveToNext())
        }
        dishCursor.close()
        println(listOfDish)
        return listOfDish
    }


    override fun onCreate(db: SQLiteDatabase) {
        val CREATE_USERS_TABLE = "CREATE TABLE $TABLE_USERS (id INTEGER PRIMARY KEY AUTOINCREMENT, username TEXT, password TEXT)"
        val CREATE_DISHES_TABLE = "CREATE TABLE $TABLE_DISHES (id INTEGER PRIMARY KEY AUTOINCREMENT, name TEXT, time INTEGER, calorie INTEGER, difficulty INTEGER, ingredient TEXT, imageID INTEGER)"
        val CREATE_USER_DISHES_TABLE = "CREATE TABLE $TABLE_USER_DISHES (id INTEGER PRIMARY KEY AUTOINCREMENT, userid INTEGER, dishid INTEGER)"

        db.execSQL(CREATE_USERS_TABLE)
        db.execSQL(CREATE_DISHES_TABLE)
        db.execSQL(CREATE_USER_DISHES_TABLE)

        db.execSQL("INSERT INTO $TABLE_USERS (username, password) VALUES ('1', '1'), ('user2', 'password2'), ('user3', 'password3'), ('user4', 'password4'), ('user5', 'password5')")
    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        db.execSQL("DROP TABLE IF EXISTS $TABLE_USERS")
        db.execSQL("DROP TABLE IF EXISTS $TABLE_DISHES")
        db.execSQL("DROP TABLE IF EXISTS $TABLE_USER_DISHES")
        onCreate(db)
    }

}