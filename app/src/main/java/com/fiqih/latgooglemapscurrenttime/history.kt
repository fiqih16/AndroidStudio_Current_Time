package com.fiqih.latgooglemapscurrenttime

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import kotlinx.android.synthetic.main.activity_history.*

class history : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_history)
        setupListOfDataIntoRecyclerView()
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

}