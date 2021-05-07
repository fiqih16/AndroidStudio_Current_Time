package com.fiqih.latgooglemapscurrenttime

import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.database.SQLException
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper

class DatabaseHandler (context: Context) : SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {

    companion object {
        private val DATABASE_VERSION = 1
        private val DATABASE_NAME = "mapsDatabase"

        private val TABLE_CONTACTS = "mapsTable"

        private val KEY_ID = "_id"
        private val KEY_NAMAKEG = "namakeg"
        private val KEY_WAKTU = "waktu"

        private val KEY_LOKASI = "lokasi"
    }

    override fun onCreate(db: SQLiteDatabase?) {
        val CREATE_CONTACTS_TABLE = ("CREATE TABLE " + TABLE_CONTACTS + "("
                + KEY_ID + " INTEGER PRIMARY KEY,"
                + KEY_NAMAKEG + " TEXT,"
                + KEY_WAKTU + " TEXT,"
                + KEY_LOKASI + " TEXT)")
        db?.execSQL(CREATE_CONTACTS_TABLE)
    }

    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
        db!!.execSQL("DROP TABLE IF EXISTS $TABLE_CONTACTS")
        onCreate(db)
    }


    fun addActivities(gmp: MpModel): Long {
        val db = this.writableDatabase
        val contentValues = ContentValues()
        contentValues.put(KEY_NAMAKEG, gmp.namaKeg)
        contentValues.put(KEY_WAKTU, gmp.waktu)
        contentValues.put(KEY_LOKASI, gmp.lokasi)

        val success = db.insert(TABLE_CONTACTS, null, contentValues)
        db.close()
        return success
    }


    fun viewActivities(): ArrayList<MpModel> {
        val gmpList: ArrayList<MpModel> = ArrayList<MpModel>()
        val selectQuery = "SELECT * FROM $TABLE_CONTACTS"

        val db = this.readableDatabase

        var cursor: Cursor? = null

        try {
            cursor = db.rawQuery(selectQuery, null)
        } catch (e: SQLException) {
            db.execSQL(selectQuery)
            return ArrayList()
        }
        var id: Int
        var namakeg: String
        var waktu: String
        var lokasi: String

        if (cursor.moveToFirst()) {
            do {
                id = cursor.getInt(cursor.getColumnIndex(KEY_ID))
                namakeg = cursor.getString(cursor.getColumnIndex(KEY_NAMAKEG))
                waktu = cursor.getString(cursor.getColumnIndex(KEY_WAKTU))
                lokasi = cursor.getString(cursor.getColumnIndex(KEY_LOKASI))
                val gmp = MpModel(id = id, namaKeg = namakeg, waktu = waktu, lokasi = lokasi)
                gmpList.add(gmp)
            } while (cursor.moveToNext())
        }
        return gmpList
    }

    // method untuk menghapus data/record dalam database
    fun deleteEmployee(gmp: MpModel): Int {
        val db = this.writableDatabase
        val contentValues = ContentValues()
        contentValues.put(KEY_ID, gmp.id)

        val success = db.delete(TABLE_CONTACTS, KEY_ID + "=" + gmp.id, null)
        db.close()
        return success
    }

}