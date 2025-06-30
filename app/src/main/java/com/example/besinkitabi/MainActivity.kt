package com.example.besinkitabi

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.View
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.GridLayoutManager
import androidx.room.Room
import com.example.recyclerviewlist.R
import com.example.recyclerviewlist.databinding.ActivityMainBinding
import com.google.firebase.messaging.FirebaseMessaging
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private var besinList: MutableList<BesinModel> = mutableListOf()
    private lateinit var adapter: BesinAdapter
    private lateinit var besinDAO: besinDAO
    private lateinit var besinDB: BesinDatabase
    private val notificationPermissionCode = 101
    private var tumBesinler: MutableList<BesinModel> = mutableListOf()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityMainBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        checkNotificationPermission()

        FirebaseMessaging.getInstance().token.addOnCompleteListener { task ->
            if (!task.isSuccessful) {
                Log.w("FCM", "Fetching FCM token failed", task.exception)
                return@addOnCompleteListener
            }

            val token = task.result
            Log.d("FCM", "FCM Token: $token")
        }

        besinDB = Room.databaseBuilder(applicationContext, BesinDatabase::class.java, "besin").build()
        besinDAO = besinDB.besinDAO()

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        adapter = BesinAdapter(besinList)
        binding.recyclerViewList.layoutManager = GridLayoutManager(this, 2)
        binding.recyclerViewList.adapter = adapter

        val retrofit = Retrofit.Builder()
            .baseUrl("https://raw.githubusercontent.com/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(besinAPI::class.java)

        CoroutineScope(Dispatchers.IO).launch {
            try {
                val existingData = besinDAO.getAllData()

                if (existingData.isEmpty()) {
                    val besinler = retrofit.getBesin()
                    Log.d("Retrofit", "Gelen veri: $besinler")
                    withContext(Dispatchers.Main) {
                        tumBesinler.clear()
                        tumBesinler.addAll(besinler)
                        adapter.setList(tumBesinler)
                        besinDAO.insert(besinler)
                    }
                } else {
                    withContext(Dispatchers.Main) {
                        tumBesinler.clear()
                        tumBesinler.addAll(existingData)
                        adapter.setList(existingData)
                    }
                }

            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
        binding.kilobilgi.setOnClickListener {
            kilobilgigit(view = view)
        }
        binding.bmiGit.setOnClickListener {
            bmiGit(view = view)
        }
        binding.floatingActionButton.setOnClickListener {
            profilegit(view = view)
        }
        binding.arama.addTextChangedListener(object : TextWatcher {
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                val query = s.toString()
                if (query.isEmpty()) {
                    adapter.setList(tumBesinler)
                } else {
                    adapter.filter(query)
                }
            }
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun afterTextChanged(s: Editable?) {}
        })
    }

    private fun checkNotificationPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            if (ContextCompat.checkSelfPermission(
                    this,
                    Manifest.permission.POST_NOTIFICATIONS
                ) != PackageManager.PERMISSION_GRANTED
            ) {
                ActivityCompat.requestPermissions(
                    this,
                    arrayOf(Manifest.permission.POST_NOTIFICATIONS),
                    notificationPermissionCode
                )
            }
        }
    }

    // İzin sonucu kontrol
    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == notificationPermissionCode) {
            if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Log.d("Permission", "Bildirim izni verildi")
            } else {
                Log.d("Permission", "Bildirim izni reddedildi")
            }
        }
    }

    fun profilegit(view: View) {
        val intent = Intent(this, FoodView::class.java)
        startActivity(intent)
    }

    fun bmiGit(view: View) {
        val intent = Intent(this, BMIActivity::class.java)
        startActivity(intent)
    }

    fun kilobilgigit(view: View) {
        val intent = Intent(this, WeightActivity::class.java)
        startActivity(intent)
    }
}
