package com.kerempurcek.appointmenttrackingsystem.TrackingSystem.presentation.receiver

import android.app.NotificationManager
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import androidx.core.app.NotificationCompat
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.kerempurcek.appointmenttrackingsystem.R

//Bu sınıf, belirlenen saatte Firestore’daki randevuyu taşıyacak.

class AppointmentReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context?, intent: Intent?) {
        val auth = FirebaseAuth.getInstance()
        val userId = auth.currentUser?.uid ?: return // Kullanıcı giriş yapmamışsa çık

        val db = FirebaseFirestore.getInstance()

        // Kullanıcının randevusunu al
        db.collection("CustomerAppointments").document(userId).get()
            .addOnSuccessListener { document ->
                if (document.exists()) {
                    val data = document.data

                    // Yeni koleksiyona ekle
                    db.collection("Appointments").document(userId).set(data!!)
                        .addOnSuccessListener {
                            // Eski koleksiyondan sil
                            db.collection("CustomerAppointments").document(userId).delete()
                            // Randevu hatırlatması için bildirim gönder
                            sendAppointmentReminderNotification(context)
                        }
                }
            }
    }
    // Bildirim gönderen fonksiyon
    private fun sendAppointmentReminderNotification(context: Context?) {
        if (context == null) return

        // Bildirim kanalı oluşturulmuş olmalı
        val notificationManager =
            context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        // Bildirim içeriği
        val notificationId = 1 // Bildirimin benzersiz ID'si
        val notificationTitle = "Randevunuz Yaklaşıyor"
        val notificationMessage = "Randevunuzun saati yaklaşıyor, unutmayın!"

        // Bildirim oluşturma
        val notificationBuilder = NotificationCompat.Builder(context, "appointment_reminder_channel")
            .setSmallIcon(R.drawable.barberlogonew) // Küçük ikon
            .setContentTitle(notificationTitle)
            .setContentText(notificationMessage)
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .setAutoCancel(true)

        // Bildirim gönder
        notificationManager.notify(notificationId, notificationBuilder.build())
    }
}
