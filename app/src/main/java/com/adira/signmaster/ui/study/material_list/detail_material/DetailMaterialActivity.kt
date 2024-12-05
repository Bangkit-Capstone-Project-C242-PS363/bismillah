package com.adira.signmaster.ui.study.material_list.detail_material

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.adira.signmaster.R
import com.adira.signmaster.data.response.MaterialDetail
import com.adira.signmaster.databinding.ActivityDetailMaterialBinding
import com.adira.signmaster.ui.study.material_list.MaterialListActivity
import com.bumptech.glide.Glide

class DetailMaterialActivity : AppCompatActivity() {

    private lateinit var binding: ActivityDetailMaterialBinding
    private var currentMaterialIndex = 0
    private lateinit var materials: List<MaterialDetail>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailMaterialBinding.inflate(layoutInflater)
        setContentView(binding.root)
        supportActionBar?.hide()

        val materialTitle = intent.getStringExtra("MATERIAL_TITLE")
        val visualContentUrl = intent.getStringExtra("VISUAL_CONTENT_URL")
        materials = intent.getParcelableArrayListExtra("MATERIALS_LIST") ?: emptyList()
        currentMaterialIndex = intent.getIntExtra("MATERIAL_INDEX", 0)

        binding.tvMaterialTitle.text = materialTitle

        if (!visualContentUrl.isNullOrEmpty()) {
            Glide.with(this)
                .load(visualContentUrl)
                .into(binding.ivMaterial)
        } else {
            Toast.makeText(this, "Visual content not available", Toast.LENGTH_SHORT).show()
        }

        binding.fabMenu.setOnClickListener {
            val intent = Intent(this, MaterialListActivity::class.java)
            intent.putParcelableArrayListExtra("MATERIALS_LIST", ArrayList(materials))
            intent.putExtra("MATERIAL_TITLE", binding.tvMaterialTitle.text.toString())
            val chapterId = intent.getStringExtra("CHAPTER_ID")
            if (!chapterId.isNullOrEmpty()) {
                intent.putExtra("CHAPTER_ID", chapterId)
            } else {
                finish()
                return@setOnClickListener
            }

            startActivity(intent)
        }

        binding.fabNext.setOnClickListener {
            if (currentMaterialIndex < materials.size - 1) {
                currentMaterialIndex++
                val nextMaterial = materials[currentMaterialIndex]
                binding.tvMaterialTitle.text = nextMaterial.title
                Glide.with(this)
                    .load(nextMaterial.visual_content_url)
                    .into(binding.ivMaterial)
            } else {
                Toast.makeText(this, "No more materials", Toast.LENGTH_SHORT).show()
            }
        }

        binding.fabBefore.setOnClickListener {
            if (currentMaterialIndex > 0) {
                currentMaterialIndex--
                val previousMaterial = materials[currentMaterialIndex]
                binding.tvMaterialTitle.text = previousMaterial.title
                Glide.with(this)
                    .load(previousMaterial.visual_content_url)
                    .into(binding.ivMaterial)
            } else {
                Toast.makeText(this, "No more previous materials", Toast.LENGTH_SHORT).show()
            }
        }
    }
}






