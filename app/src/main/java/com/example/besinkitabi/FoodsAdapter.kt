package com.example.besinkitabi

import android.annotation.SuppressLint
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.recyclerviewlist.databinding.FoodsRowBinding
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class FoodsAdapter(
    private var besinListesi: MutableList<BesinModel>,
    private val foodsDAO: FoodsDAO,
    private val foodView: FoodView
) : RecyclerView.Adapter<FoodsAdapter.ViewHolder>() {

    class ViewHolder(val binding: FoodsRowBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = FoodsRowBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun getItemCount(): Int = besinListesi.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val besin = besinListesi[position]
        holder.binding.apply {
            besinisim1.text = besin.Isim
            besinkarbonhidrat1.text = besin.karbonhidrat
            besinprotein1.text = besin.protein
            besinyag1.text = besin.yag
            besinkalori1.text = besin.kalori
            besinGramMiktari.text = besin.gramMiktari.toString()
        }

        holder.binding.sil.setOnClickListener { deleteItem(position, besin) }
    }



    @SuppressLint("NotifyDataSetChanged")
    fun updateData(newList: List<BesinModel>) {
        besinListesi.clear()
        besinListesi.addAll(newList)
        notifyDataSetChanged()
    }

    private fun deleteItem(position: Int, besin: BesinModel) {
        // Veritabanından silme işlemi
        CoroutineScope(Dispatchers.IO).launch {
            foodsDAO.deleteFood(besin)

            // UI güncellemesi için main thread'e dönüyoruz
            withContext(Dispatchers.Main) {
                // RecyclerView'dan öğeyi sil
                besinListesi.removeAt(position)
                notifyItemRemoved(position)
                notifyItemRangeChanged(position, itemCount)
            }
            foodView.updateTotals()
        }
    }
}


