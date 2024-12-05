package com.adira.signmaster.ui.quiz

import android.Manifest
import android.content.pm.PackageManager
import android.os.Bundle
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.GridLayoutManager
import com.adira.signmaster.R
import com.adira.signmaster.databinding.ActivityQuizBinding
import com.adira.signmaster.ui.quiz.quiz_menu_fragment.QuizMenuFragment

class QuizActivity : AppCompatActivity() {

    private lateinit var binding: ActivityQuizBinding
    private val viewModel: QuizViewModel by viewModels()

    companion object {
        private const val LOCATION_PERMISSION_REQUEST_CODE = 1001
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityQuizBinding.inflate(layoutInflater)
        setContentView(binding.root)

        supportActionBar?.hide()

        // Check for location permissions
        if (hasLocationPermission()) {
            setupRecyclerView()
            fetchChapters()
        } else {
            requestLocationPermission()
        }
    }

    private fun hasLocationPermission(): Boolean {
        return ContextCompat.checkSelfPermission(
            this,
            Manifest.permission.ACCESS_FINE_LOCATION
        ) == PackageManager.PERMISSION_GRANTED
    }

    private fun requestLocationPermission() {
        ActivityCompat.requestPermissions(
            this,
            arrayOf(Manifest.permission.ACCESS_FINE_LOCATION),
            LOCATION_PERMISSION_REQUEST_CODE
        )
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == LOCATION_PERMISSION_REQUEST_CODE) {
            if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // Permission granted
                setupRecyclerView()
                fetchChapters()
            } else {
                // Permission denied
                Toast.makeText(
                    this,
                    "Location permission is required to access this feature.",
                    Toast.LENGTH_SHORT
                ).show()
            }
        }
    }

    private fun setupRecyclerView() {
        val adapter = ChapterAdapter { chapterId, chapterTitle ->
            navigateToQuizMenu(chapterId, chapterTitle) // Pass both id and title
        }
        binding.recyclerViewQuiz.layoutManager = GridLayoutManager(this, 3)
        binding.recyclerViewQuiz.adapter = adapter

        viewModel.chapters.observe(this) { chapters ->
            adapter.submitList(chapters)
        }

        viewModel.error.observe(this) { errorMessage ->
            Toast.makeText(this, errorMessage, Toast.LENGTH_SHORT).show()
        }
    }


    private fun fetchChapters() {
        viewModel.fetchChapters()
    }

    private fun navigateToQuizMenu(chapterId: Int, chapterTitle: String) {
        val fragment = QuizMenuFragment.newInstance(chapterId, chapterTitle) // Pass both id and title
        supportFragmentManager.beginTransaction()
            .replace(R.id.main, fragment) // R.id.main harus merujuk ke container layout Anda
            .addToBackStack(null) // Tambahkan fragment ke back stack
            .commit()
    }

}
