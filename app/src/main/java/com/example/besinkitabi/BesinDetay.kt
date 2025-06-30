    package com.example.besinkitabi

    import android.content.Intent
    import android.os.Bundle
    import android.util.Log
    import android.view.View
    import android.widget.Toast
    import androidx.activity.enableEdgeToEdge
    import androidx.appcompat.app.AppCompatActivity
    import androidx.core.view.ViewCompat
    import androidx.core.view.WindowInsetsCompat
    import androidx.room.Room
    import com.bumptech.glide.Glide
    import com.example.recyclerviewlist.R
    import com.example.recyclerviewlist.databinding.ActivityBesinDetayBinding
    import com.squareup.picasso.Callback
    import com.squareup.picasso.NetworkPolicy
    import com.squareup.picasso.Picasso
    import kotlinx.coroutines.CoroutineScope
    import kotlinx.coroutines.Dispatchers
    import kotlinx.coroutines.launch
    import kotlinx.coroutines.withContext

    private lateinit var binding: ActivityBesinDetayBinding
    private var besinList: ArrayList<BesinModel> = ArrayList()
    private lateinit var BesinDAO : besinDAO
    private lateinit var besinDB: BesinDatabase
    private lateinit var foodDB: FoodsDatabase
    private lateinit var foodDAO: FoodsDAO

    class BesinDetay : AppCompatActivity() {
        override fun onCreate(savedInstanceState: Bundle?) {
            super.onCreate(savedInstanceState)
            enableEdgeToEdge()

            binding = ActivityBesinDetayBinding.inflate(layoutInflater)
            setContentView(binding.root)

            // Veritabanlarını initialize ediyoruz
            besinDB = Room.databaseBuilder(applicationContext, BesinDatabase::class.java, "besin").build()
            foodDB = Room.databaseBuilder(applicationContext, FoodsDatabase::class.java, "foods").build()

            // DAO'ları initialize ediyoruz
            BesinDAO = besinDB.besinDAO()
            foodDAO = foodDB.foodDAO()

            // Window insets ayarlaması
            ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
                val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
                v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
                insets
            }

            val id = intent.getIntExtra("besinId", -1)
            if (id != -1) {
                CoroutineScope(Dispatchers.IO).launch {
                    val besin = BesinDAO.findById(id)
                    withContext(Dispatchers.Main) {
                        if (besin != null) {
                            binding.besinIsim.text = besin.Isim
                            binding.besinkarbonhidrat.text = besin.karbonhidrat
                            binding.besinProtein.text = besin.protein
                            binding.besinYag.text = besin.yag
                            binding.besinKalori.text = besin.kalori

                            val gorsel = besin.gorsel // besin objesinden gelen görsel kullanılıyor
                            if (!gorsel.isNullOrEmpty()) {
                                Glide.with(this@BesinDetay)
                                    .load(gorsel)
                                    .override(700, 700) // Glide'da resize için override kullanılır
                                    .centerCrop()
                                    .placeholder(R.drawable.diet) // Yükleme sırasında gösterilecek resim
                                    .error(R.drawable.diet) // Yüklenemezse gösterilecek resim
                                    .into(binding.imageView)
                            } else {
                                binding.imageView.setImageResource(R.drawable.ic_launcher_background)
                            }
                        } else {
                            Log.e("MainActivity", "Besin bulunamadı!")
                            Log.d("BesinDetay", "Alınan besin ID: $id")
                        }
                    }
                }
            }
        }

        fun yedimButton(view : View) {

            try {
                if (binding.gramtext.text.isNotEmpty()) {
                    val id = intent.getIntExtra("besinId", -1)
                    val besinGram = binding.gramtext.text.toString().toInt()

                    CoroutineScope(Dispatchers.IO).launch {
                        val besin = BesinDAO.findById(id)
                        if (besin != null) {
                            if (foodDAO.findById(id) != null) {
                                besin.gramMiktari += besinGram
                                BesinDAO.update(besin)
                                foodDAO.update(besin)
                            } else {
                                // Eğer besin bulunamazsa, ilk defa ekleniyor demektir
                                besin.gramMiktari = besinGram // İlk defa ekleniyor
                                BesinDAO.insert(listOf(besin)) // Yeni besin ekle
                                foodDAO.insert(besin)
                            }
                        }
                        withContext(Dispatchers.Main) {
                            val intent = Intent(this@BesinDetay, MainActivity::class.java)
                            startActivity(intent)
                        }
                    }
                    Toast.makeText(this@BesinDetay, "Besin Kaydedildi! 🎉", Toast.LENGTH_SHORT).show()
                } else {
                    Toast.makeText(this, "Gram bilgisini giriniz.", Toast.LENGTH_LONG).show()
                }
            }
            catch (e : Exception){
                Toast.makeText(this,"Geçerli gram değeri girin!",Toast.LENGTH_SHORT).show()
            }


        }


    }
