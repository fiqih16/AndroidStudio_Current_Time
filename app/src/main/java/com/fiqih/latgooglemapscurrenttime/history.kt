package com.fiqih.latgooglemapscurrenttime

import android.content.DialogInterface
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.ActionBar
import androidx.appcompat.app.AlertDialog
import androidx.recyclerview.widget.LinearLayoutManager
import kotlinx.android.synthetic.main.activity_history.*

class history : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_history)
        setupListOfDataIntoRecyclerView()

        // Tombol Back
        val actionBar:ActionBar?= supportActionBar
        actionBar!!.setDisplayHomeAsUpEnabled(true)

    }

    // Tombol back
    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }

    // method untuk mendapatkan jumlah record
    private fun getItemList(): ArrayList<MpModel>{
        val databaseHandler: DatabaseHandler = DatabaseHandler(this)
        val empList: ArrayList<MpModel> = databaseHandler.viewActivities()
        rv_history.layoutManager = LinearLayoutManager(this)
        rv_history.adapter = itemAdapter(this,empList)
        return empList
    }


    // method untuk menampilkan emplist ke recycler view
    private fun  setupListOfDataIntoRecyclerView(){
        if (getItemList().size > 0) {
            rv_history.visibility = View.VISIBLE
        }else{
            rv_history.visibility = View.GONE
        }
    }

    // method untuk menampilkan dialog konfirmasi delete
    fun deleteRecordAlertDialog(mpModel: MpModel) {
        val builder = AlertDialog.Builder(this)

        builder.setTitle("Hapus?")
        builder.setMessage("Hapus Data Terpilih?")
        builder.setIcon(android.R.drawable.ic_delete)

        // menampilkan tombol yes
        builder.setPositiveButton("Yes") { dialog: DialogInterface, which ->
            val databaseHandler : DatabaseHandler = DatabaseHandler(this)
            val status = databaseHandler.deleteEmployee(MpModel(mpModel.id,"","",""))

            if (status > -1){
                Toast.makeText(this, "Berhasil menghapus", Toast.LENGTH_SHORT).show()
                setupListOfDataIntoRecyclerView()
            }

            dialog.dismiss()
        }
        // menampilkan tombol no
        builder.setNegativeButton("No") { dialog: DialogInterface, which ->
            //Toast.makeText(this, "No", Toast.LENGTH_SHORT).show()
            dialog.dismiss()
        }
        val alertDialog: AlertDialog = builder.create()
        // menampilkan user menekan tombol yes or no
        alertDialog.setCancelable(false)
        // menampilkan kotak dialog
        alertDialog.show()
    }

}