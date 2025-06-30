package com.example.besinkitabi


import android.content.Intent
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.recyclerviewlist.databinding.RecyclerviewRowBinding


class BesinAdapter(val besinListesi: MutableList<BesinModel>) : RecyclerView.Adapter<BesinAdapter.ViewHolder>() {

    private var filteredList: MutableList<BesinModel> = besinListesi.toMutableList()

    class ViewHolder(val binding: RecyclerviewRowBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = RecyclerviewRowBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun getItemCount(): Int {
        return filteredList.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val besin = filteredList[position]
        if (besin != null) {
            holder.binding.besinisim1.text = besin.Isim
            holder.binding.besinkarbonhidrat1.text = besin.karbonhidrat
            holder.binding.besinprotein1.text = besin.protein
            holder.binding.besinyag1.text = besin.yag
            holder.binding.besinkalori1.text = besin.kalori
            holder.itemView.setOnClickListener {
                val intent = Intent(holder.itemView.context, BesinDetay::class.java)
                intent.putExtra("besinId", besin.id)
                intent.putExtra("gorsel", besin.gorsel)
                holder.itemView.context.startActivity(intent)
                Log.d("MainActivity", "Eklenen besinlerin ID'leri: ${besin.id}")

            }
        } else {
        }
    }
    fun filter(query: String) {
        filteredList = if (query.isEmpty()) {
            besinListesi.toMutableList()
        } else {
            besinListesi.filter { it.Isim.startsWith(query, ignoreCase = true) }.toMutableList()
        }
        notifyDataSetChanged()
    }
    fun setList(newList: List<BesinModel>) {
        besinListesi.clear()
        besinListesi.addAll(newList)
        filteredList.clear()
        filteredList.addAll(newList)
        notifyDataSetChanged()
    }

    fun updateList(newList: List<BesinModel>) {
        besinListesi.clear()
        besinListesi.addAll(newList)
        filteredList.clear()
        filteredList.addAll(newList)
        notifyDataSetChanged()
    }


}
