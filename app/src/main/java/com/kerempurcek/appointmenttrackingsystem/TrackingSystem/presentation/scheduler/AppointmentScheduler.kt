package com.kerempurcek.appointmenttrackingsystem.TrackingSystem.presentation.scheduler


import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent

import android.os.Build
import android.provider.Settings
import androidx.core.content.ContextCompat
import com.google.firebase.auth.FirebaseAuth
import com.kerempurcek.appointmenttrackingsystem.TrackingSystem.presentation.receiver.AppointmentReceiver
import java.text.SimpleDateFormat
import java.util.Locale
// kullanıcı kimliğine göre alarmı planlama
class AppointmentScheduler(private val context: Context) {
    fun scheduleAppointment(appointmentTime: String) {
        // Android 12 ve sonrası için izin kontrolü
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            val alarmManager = ContextCompat.getSystemService(context, AlarmManager::class.java)
            if (alarmManager?.canScheduleExactAlarms() == false) {
                Intent().also { intent ->
                    intent.action = Settings.ACTION_REQUEST_SCHEDULE_EXACT_ALARM
                    context.startActivity(intent)
                }
            }
        }
        val auth = FirebaseAuth.getInstance()
        val userId = auth.currentUser?.uid ?: return // Kullanıcı giriş yapmamışsa çık

        val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager
        val intent = Intent(context, AppointmentReceiver::class.java)

        val pendingIntent = PendingIntent.getBroadcast(
            context,
            userId.hashCode(), // Kullanıcıya özel ID
            intent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )

        // Tarih formatını Date'e çevir
        val dateFormat = SimpleDateFormat("dd/MM/yyyy HH:mm", Locale.getDefault())
        val date = dateFormat.parse(appointmentTime) ?: return

        alarmManager.setExactAndAllowWhileIdle(
            AlarmManager.RTC_WAKEUP,
            date.time,
            pendingIntent
        )
    }
}
