package com.kerempurcek.appointmenttrackingsystem.TrackingSystem.presentation.ui

import android.graphics.Color
import android.os.Bundle
import androidx.activity.SystemBarStyle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.view.ViewCompat
import androidx.core.view.WindowCompat
import androidx.core.view.WindowInsetsCompat
import com.kerempurcek.appointmenttrackingsystem.R

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {

        //StatusBar and navigationBar fullScreen Mode
        enableEdgeToEdge(
                   statusBarStyle = SystemBarStyle.light(Color.TRANSPARENT,Color.TRANSPARENT),
                    navigationBarStyle = SystemBarStyle.light(Color.TRANSPARENT,Color.TRANSPARENT)
        )

        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_main)




        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            // enableedgetoedge aktif etmek için kaldırılması gerekli  v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
    }
}