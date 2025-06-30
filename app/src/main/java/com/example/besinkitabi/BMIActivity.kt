package com.example.besinkitabi

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.recyclerviewlist.R
import com.example.recyclerviewlist.databinding.ActivityBesinDetayBinding
import com.example.recyclerviewlist.databinding.ActivityBmiactivityBinding


private lateinit var binding: ActivityBmiactivityBinding

class BMIActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        binding = ActivityBmiactivityBinding.inflate(layoutInflater)
        setContentView(binding.root)

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
    }
    fun bmiHesapla(view: View) {
        var boyStr = binding.boytext.text.toString()
        var kiloStr = binding.kilotext.text.toString()

        if (boyStr.isNotEmpty() && kiloStr.isNotEmpty()) {
            var boy = boyStr.toDouble() / 100
            var kilo = kiloStr.toDouble()

            var BMI = kilo / (boy * boy)

            binding.bmi.text = "BMI Değeriniz : %.2f".format(BMI)

            if(BMI < 18.5){
                binding.yorum.text = "Boyunuza göre uygun ağırlıkta olmadığınızı, zayıf olduğunuzu gösterir. Zayıflık, bazı hastalıklar için risk oluşturan ve istenmeyen bir durumdur. Boyunuza uygun ağırlığa erişmeniz için yeterli ve dengeli beslenmeli, beslenme alışkanlıklarınızı geliştirmeye özen göstermelisiniz."
            }
            else if(BMI < 24.9 && BMI > 18.5){
                binding.yorum.text = "Boyunuza göre uygun ağırlıkta olduğunuzu gösterir. Yeterli ve dengeli beslenerek ve düzenli fiziksel aktivite yaparak bu ağırlığınızı korumaya özen gösteriniz."
            }
            else if(BMI < 29.9 && BMI > 25){
                binding.yorum.text = "Boyunuza göre vücut ağırlığınızın fazla olduğunu gösterir. Fazla kilolu olma durumu gerekli önlemler alınmadığı takdirde pek çok hastalık için risk faktörü olan obeziteye (şişmanlık) yol açar."
            }
            else if(BMI < 34.9 && BMI > 30){
                binding.yorum.text = "Boyunuza göre vücut ağırlığınızın fazla olduğunu bir başka deyişle şişman olduğunuzun bir göstergesidir. Şişmanlık, kalp-damar hastalıkları, diyabet, hipertansiyon v.b. kronik hastalıklar için risk faktörüdür. Bir sağlık kuruluşuna başvurarak hekim / diyetisyen kontrolünde zayıflayarak normal ağırlığa inmeniz sağlığınız açısından çok önemlidir. Lütfen, sağlık kuruluşuna başvurunuz."
            }
            else if(BMI > 40){
                binding.yorum.text = "Boyunuza göre vücut ağırlığınızın fazla olduğunu bir başka deyişle şişman olduğunuzun bir göstergesidir. Şişmanlık, kalp-damar hastalıkları, diyabet, hipertansiyon v.b. kronik hastalıklar için risk faktörüdür. Bir sağlık kuruluşuna başvurarak hekim / diyetisyen kontrolünde zayıflayarak normal ağırlığa inmeniz sağlığınız açısından çok önemlidir. Lütfen, sağlık kuruluşuna başvurunuz."
            }
        } else {
            Toast.makeText(this,"Boy ve Kilo değerini girin!",Toast.LENGTH_LONG).show()
        }
    }
    }
