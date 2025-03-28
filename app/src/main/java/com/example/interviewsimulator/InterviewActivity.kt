package com.example.interviewsimulator

import android.Manifest
import android.content.pm.PackageManager
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.camera.core.CameraSelector
import androidx.camera.core.CameraX
import androidx.camera.core.Preview
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat

class InterviewActivity : AppCompatActivity() {

    private lateinit var questionText: TextView
    private lateinit var continueButton: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_interview)

        questionText = findViewById(R.id.questionText)
        continueButton = findViewById(R.id.continueButton)

        val responseType = intent.getStringExtra("responseType")

        if (responseType == "camera") {
            requestCameraPermission()
        } else {
            setupTextResponseUI()
        }
    }

    private fun requestCameraPermission() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.CAMERA), 100)
        } else {
            startCamera()
        }
    }

    private fun startCamera() {
        val cameraProviderFuture = ProcessCameraProvider.getInstance(this)
        cameraProviderFuture.addListener({
            val cameraProvider = cameraProviderFuture.get()
            val preview = Preview.Builder().build()
            val cameraSelector = CameraSelector.DEFAULT_BACK_CAMERA

            preview.setSurfaceProvider(findViewById(R.id.cameraPreview).surfaceProvider)
            cameraProvider.bindToLifecycle(this, cameraSelector, preview)
        }, ContextCompat.getMainExecutor(this))
    }

    private fun setupTextResponseUI() {
        questionText.text = "¿Por qué quieres trabajar aquí?"
        continueButton.setOnClickListener {
            // Handle continue action
            Toast.makeText(this, "Continuar a la siguiente pregunta", Toast.LENGTH_SHORT).show()
        }
    }
}