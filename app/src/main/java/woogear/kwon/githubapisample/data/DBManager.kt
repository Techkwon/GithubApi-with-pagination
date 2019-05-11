package woogear.kwon.githubapisample.data

import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import android.util.Log
import woogear.kwon.githubapisample.model.GithubUser

/**
 * DBManager class manages data with SQLite database
 * */

class DBManager(context: Context?, name: String?, factory: SQLiteDatabase.CursorFactory?, version: Int) :

    SQLiteOpenHelper(
        context,
        name,
        factory,
        version
    ) {

    private val TAG = "[DBManager]"
    private lateinit var db: SQLiteDatabase
    private val tableName = "MyTable"

    init {
        createTable()
    }

    override fun onCreate(db: SQLiteDatabase?) {

    }

    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    private fun createTable(){
        db = writableDatabase
        val query = "CREATE TABLE IF NOT EXISTS $tableName (" +
                "id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "url TEXT, " +
                "login TEXT, " +
                "score TEXT, " +
                "UNIQUE(login)" +
                ");"

        db.execSQL(query)
        Log.d(TAG, "Table Created..!")
        db.close()
    }

    fun insertUser(user: GithubUser){
        db = writableDatabase
        val query = "INSERT OR IGNORE INTO " + tableName + " VALUES (null, '" +
                user.avatar_url + "', '" +
                user.login + "', '" +
                user.score + "')"

        db.execSQL(query)
        db.close()
        Log.d(TAG, "User Inserted Successfully..!")
    }


    fun isSaved(login: String) : Boolean{
        db = readableDatabase
        val query = "SELECT * FROM $tableName WHERE login = '$login'"
        val cursor: Cursor = db.rawQuery(query, null)

        if(cursor.count <= 0){
            cursor.close()
            db.close()
            return false
        }

        cursor.close()
        db.close()
        return true
    }

    fun selectUsers() : List<GithubUser>{

        val users = ArrayList<GithubUser>()
        db = readableDatabase
        val query = "SELECT * FROM $tableName"
        val cursor: Cursor = db.rawQuery(query, null)

        while (cursor.moveToNext()){
            val url = cursor.getString(1)
            val login = cursor.getString(2)
            val score = cursor.getString(3)
            users.add(GithubUser(url, login, score.toFloat()))
        }

        cursor.close()
        db.close()
        Log.d(TAG, "Selected Users..!")
        return users
    }

    fun deleteUser(user: String) {
        db = writableDatabase
        val query = "DELETE FROM $tableName WHERE login = '$user'"
        db.execSQL(query)
        db.close()
    }


}