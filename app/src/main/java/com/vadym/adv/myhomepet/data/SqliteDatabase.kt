package com.vadym.adv.myhomepet.data

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import com.vadym.adv.myhomepet.ui.pet.PetModel

class SqliteDatabase private constructor(context: Context) : SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {

    override fun onCreate(db: SQLiteDatabase?) {
        val CREATE_PETS_TABLE = ("CREATE TABLE $TABLE_PETS($KEY_ID INTEGER PRIMARY KEY,$KEY_NAME TEXT,$KEY_CATEGORY TEXT,$KEY_ACTION TEXT,$KEY_PERIOD TEXT,$KEY_COUNTRY TEXT)")
        db?.execSQL(CREATE_PETS_TABLE)
    }

    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
        db?.execSQL("DROP TABLE IF EXISTS $TABLE_PETS")
        onCreate(db)
    }

    fun listPets(): MutableList<PetModel> {
        val sql = "select * from $TABLE_PETS"
        val db = this.readableDatabase
        val storePets = ArrayList<PetModel>()
        val cursor = db.rawQuery(sql, null)
        if (cursor.moveToFirst()) {
          do {
              val id = Integer.parseInt(cursor.getString(0))
              val name = cursor.getString(1)
//              val photo = Integer.parseInt(cursor.getString(3))
              val category = cursor.getString(2)
              val action = cursor.getString(3)
              val period = cursor.getString(4)
              val country = cursor.getString(5)
              storePets.add(PetModel(id, name, category, action, period, country))
          } while (cursor.moveToNext())
        }
        cursor.close()
        return storePets
    }

    fun addPet(petModel: PetModel) {
        val values = ContentValues()
//        values.put(KEY_PHOTO, petModel.petPhoto)
        values.put(KEY_CATEGORY, petModel.category)
        values.put(KEY_ACTION, petModel.action)
        values.put(KEY_PERIOD, petModel.period)
        values.put(KEY_COUNTRY, petModel.country)

        val db = this.writableDatabase
        db.insert(TABLE_PETS, null, values)
        db.close()
    }

    fun updatePet(petModel: PetModel) {
        val values = ContentValues()
//        values.put(KEY_PHOTO, petModel.petPhoto)
        values.put(KEY_CATEGORY, petModel.category)
        values.put(KEY_ACTION, petModel.action)
        values.put(KEY_COUNTRY, petModel.country)
        values.put(KEY_PERIOD, petModel.period)

        val db = this.readableDatabase
        db.update(TABLE_PETS, values, "$KEY_ID=?", arrayOf(petModel.id.toString()))
    }

    fun deletePet(id: Int) {
        val db = this.writableDatabase
        db.delete(TABLE_PETS, "$KEY_ID=?", arrayOf(id.toString()))
    }

    companion object {
        private val DATABASE_VERSION = 1
        private val DATABASE_NAME = "pet"
        val TABLE_PETS = "pets"

        val KEY_ID = "id"
        val KEY_NAME = "petName"
        val KEY_PHOTO = "petPhoto"
        val KEY_CATEGORY = "petCategory"
        val KEY_COUNTRY = "petCountry"
        val KEY_ACTION = "petAction"
        val KEY_PERIOD = "petPeriod"

        private var instance: SqliteDatabase? = null
        fun getInstance(context: Context): SqliteDatabase {
            if (instance == null) {
                instance = SqliteDatabase(context)
            }
            return instance!!
        }
    }
}