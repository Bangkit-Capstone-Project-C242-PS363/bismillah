package com.adira.signmaster.ui.study

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.LinearLayoutManager
import com.adira.signmaster.R
import com.adira.signmaster.databinding.ActivityStudyBinding

class StudyActivity : AppCompatActivity() {

    private lateinit var binding: ActivityStudyBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityStudyBinding.inflate(layoutInflater)
        setContentView(binding.root)

        supportActionBar?.hide()

        // Data untuk LearnMaterialItems
        val learnMaterialItems = listOf(
            LearnMaterialItem("Study Material 1", R.drawable.introduction_icon),
            LearnMaterialItem("Study Material 2", R.drawable.introduction_icon),
            LearnMaterialItem("Study Material 3", R.drawable.introduction_icon),
            LearnMaterialItem("Study Material 4", R.drawable.introduction_icon),
            LearnMaterialItem("Study Material 5", R.drawable.introduction_icon),
            LearnMaterialItem("Study Material 6", R.drawable.introduction_icon),
            LearnMaterialItem("Study Material 7", R.drawable.introduction_icon),
            LearnMaterialItem("Study Material 8", R.drawable.introduction_icon),
            // Tambahkan lebih banyak item sesuai kebutuhan
        )

        // Adapter untuk RecyclerView
        val learnMaterialAdapter = LearnMaterialAdapter(learnMaterialItems) { learnMaterialItem ->
            // Implementasi aksi ketika item diklik
        }

        // Mengatur RecyclerView
        binding.rvLearnMaterial.apply {
            layoutManager = LinearLayoutManager(this@StudyActivity)
            adapter = learnMaterialAdapter
        }
    }
}
