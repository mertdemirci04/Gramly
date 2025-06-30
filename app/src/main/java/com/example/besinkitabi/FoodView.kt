package com.example.besinkitabi

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.room.Room
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.WorkManager
import com.example.recyclerviewlist.R
import com.example.recyclerviewlist.databinding.ActivityFoodViewBinding
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.util.Calendar
import java.util.concurrent.TimeUnit

class FoodView : AppCompatActivity() {
    private lateinit var adapter: FoodsAdapter
    private lateinit var foodsDB: FoodsDatabase
    private lateinit var foodsDAO: FoodsDAO
    private lateinit var binding: ActivityFoodViewBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityFoodViewBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        scheduleDatabaseClear(this)

        // Room veritabanı başlatma
        foodsDB = Room.databaseBuilder(applicationContext, FoodsDatabase::class.java, "foods").build()
        foodsDAO = foodsDB.foodDAO()

        // Adapter ve RecyclerView yapılandırması
        binding.foodsRecyclerViewList.layoutManager = LinearLayoutManager(this)
        adapter = FoodsAdapter(mutableListOf(), foodsDAO, this)
        binding.foodsRecyclerViewList.adapter = adapter

        // Verileri yükle
        updateTotals()
        loadFoods()

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
    }

    fun updateTotals() {
        CoroutineScope(Dispatchers.IO).launch {
            val besinlistesi = foodsDAO.getAllData()

            // Kalori hesaplama
            val toplamKalori = besinlistesi.sumOf { besin ->
                val kalori = besin.kalori.split(" ")[0].toDoubleOrNull() ?: 0.0
                kalori * (besin.gramMiktari / 100.0)
            }

            // Karbonhidrat hesaplama
            val toplamkarb = besinlistesi.sumOf { besin ->
                val karbString = besin.karbonhidrat.filter { it.isDigit() || it == '.' }
                val karb = karbString.toDoubleOrNull() ?: 0.0
                karb * (besin.gramMiktari / 100.0)
            }

            // Protein hesaplama
            val toplamprotein = besinlistesi.sumOf { besin ->
                val proteinString = besin.protein.filter { it.isDigit() || it == '.' }
                val protein = proteinString.toDoubleOrNull() ?: 0.0
                protein * (besin.gramMiktari / 100.0)
            }

            // Yağ hesaplama
            val toplamyag = besinlistesi.sumOf { besin ->
                val yagString = besin.yag.filter { it.isDigit() || it == '.' }
                val yag = yagString.toDoubleOrNull() ?: 0.0
                yag * (besin.gramMiktari / 100.0)
            }

            withContext(Dispatchers.Main) {
                binding.toplamkalori.text = "Toplam Kalori: %.1f".format(toplamKalori)
                binding.toplampro.text = "Toplam Protein: %.1f".format(toplamprotein)
                binding.toplamyag.text = "Toplam Yağ: %.1f".format(toplamyag)
                binding.toplamkarb.text = "Toplam Karbonhidrat: %.1f".format(toplamkarb)
            }
        }
    }


    fun loadFoods() {
        CoroutineScope(Dispatchers.IO).launch {
            val foods = foodsDAO.getAllData()
            withContext(Dispatchers.Main) {
                adapter.updateData(foods)
            }
        }
    }


    fun scheduleDatabaseClear(context: Context) {
        // Şu anki zaman
        val currentTime = Calendar.getInstance()

        // Gece 23:59'u hedefle
        val targetTime = Calendar.getInstance().apply {
            set(Calendar.HOUR_OF_DAY, 23)
            set(Calendar.MINUTE, 59)
            set(Calendar.SECOND, 59)
        }

        // Geçmişse ertesi güne ayarla
        if (targetTime.before(currentTime)) {
            targetTime.add(Calendar.DAY_OF_YEAR, 1)
        }

        val initialDelay = targetTime.timeInMillis - currentTime.timeInMillis

        val workRequest = OneTimeWorkRequestBuilder<ClearDataBaseWorker>()
            .setInitialDelay(initialDelay, TimeUnit.MILLISECONDS)
            .build()

        WorkManager.getInstance(context).enqueue(workRequest)
    }

}
