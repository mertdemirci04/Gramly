package com.example.besinkitabi

import android.graphics.Color
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.room.Room
import com.example.recyclerviewlist.R
import com.github.mikephil.charting.charts.LineChart
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.data.LineData
import com.github.mikephil.charting.data.LineDataSet
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class WeightActivity : AppCompatActivity() {

    private lateinit var chart: LineChart
    private lateinit var weightInput: EditText
    private lateinit var saveButton: Button
    private lateinit var db: WeightDatabase
    private lateinit var dao: WeightDAO
    private lateinit var clearButton: Button


    private val entries = ArrayList<com.github.mikephil.charting.data.Entry>()
    private var index = 0f

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_weight2)
        db = Room.databaseBuilder(applicationContext, WeightDatabase::class.java, "weight_db").build()
        dao = db.weightDao()
        chart = findViewById(R.id.lineChart)
        weightInput = findViewById(R.id.editTextWeight)
        saveButton = findViewById(R.id.buttonSave)
        clearButton = findViewById(R.id.buttonClear)


        CoroutineScope(Dispatchers.IO).launch {
            val savedWeights = dao.getAll()
            withContext(Dispatchers.Main) {
                for (w in savedWeights) {
                    entries.add(Entry(index++, w.value))
                }
                updateChart()
            }
        }
        saveButton.setOnClickListener {
            val weight = weightInput.text.toString().toFloatOrNull()
            if (weight != null) {
                entries.add(Entry(index++, weight))
                updateChart()
                weightInput.text.clear()
                CoroutineScope(Dispatchers.IO).launch {
                    dao.insert(Weight(value = weight))
                }
                Toast.makeText(this, "Kilo başarıyla kaydedildi 🎉", Toast.LENGTH_SHORT).show()
            } else {
                Toast.makeText(this, "Geçerli bir kilo girin", Toast.LENGTH_SHORT).show()
            }
        }
        clearButton.setOnClickListener {
            CoroutineScope(Dispatchers.IO).launch {
                dao.deleteAll()
                withContext(Dispatchers.Main) {
                    entries.clear()
                    chart.clear()
                    chart.invalidate()
                    Toast.makeText(this@WeightActivity, "Tüm veriler silindi ❌", Toast.LENGTH_SHORT).show()
                }
            }
        }


    }

    private fun setupChart() {
        chart.setTouchEnabled(true)
        chart.setPinchZoom(true)
        chart.description.isEnabled = false
        chart.axisRight.isEnabled = false

        chart.axisLeft.apply {
            textColor = Color.LTGRAY
            gridColor = Color.DKGRAY
            axisLineColor = Color.GRAY
        }

        chart.xAxis.isEnabled = false
        chart.legend.isEnabled = false

        chart.setBackgroundColor(Color.parseColor("#1E293B"))

        // 🚨 Buraya eklendi:
        chart.setNoDataText("Henüz veri yok")
        chart.setNoDataTextColor(Color.LTGRAY)
    }



    private fun updateChart() {
        if (entries.isEmpty()) {
            chart.clear()
            return
        }

        val dataSet = LineDataSet(entries, "Kilo").apply {
            color = Color.parseColor("#F59E0B")
            valueTextColor = Color.WHITE
            lineWidth = 2f
            setDrawCircles(true)
            circleRadius = 6f
            circleHoleColor = Color.WHITE
            setCircleColor(Color.parseColor("#F59E0B"))
            valueTextSize = 12f
            mode = LineDataSet.Mode.CUBIC_BEZIER
        }

        chart.data = LineData(dataSet)
        chart.animateX(500)
        chart.invalidate()
    }


}
