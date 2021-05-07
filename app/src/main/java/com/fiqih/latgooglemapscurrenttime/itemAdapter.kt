package com.fiqih.latgooglemapscurrenttime

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.activity_history.view.*
import kotlinx.android.synthetic.main.activity_itemhistory.view.*

class itemAdapter (val context: Context, val items: ArrayList<MpModel>) : RecyclerView.Adapter<itemAdapter.ViewHolder>() {
    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val linlay = view.linlay
        val Keg = view.Tv_kegia
        val waktu = view.Tv_Waktu
        val lokasi = view.Tv_Lok
        val no = view.Tv_id
    }

    // method untuk membuat view holder
    // inflate = memunculkan data
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            LayoutInflater.from(context).inflate(
                R.layout.activity_itemhistory, parent, false)
        )
    }

    // memasukkan data ke view holder
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = items.get(position)
        holder.Keg.text = item.namaKeg

        holder.waktu.text = item.waktu

        holder.lokasi.text = item.lokasi
        holder.no.text = position.toString()
//        holder.tvalamat.text = item.alamat

        if (position % 2 == 0) {
            holder.linlay.setBackgroundColor(
                ContextCompat.getColor(
                    context,
                    R.color.design_default_color_secondary
                )
            )
        } else {
            holder.linlay.setBackgroundColor(
                ContextCompat.getColor(
                    context,
                    R.color.design_default_color_surface
                )
            )
        }
    }

    override fun getItemCount(): Int {
        return items.size
    }


}