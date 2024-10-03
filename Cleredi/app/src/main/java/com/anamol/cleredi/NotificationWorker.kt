package com.anamol.cleredi

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Build
import androidx.core.app.NotificationCompat
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.Call
import java.util.logging.FileHandler
import java.util.logging.Logger
import java.util.logging.SimpleFormatter

data class NotificationRequest(
    val title: String,
    val message: String
)

data class NotificationResponse(
    val title: String,
    val message: String
)

interface NotificationApi {
    @POST("/send-notification")
    fun sendNotification(@Body request: NotificationRequest): Call<Void>

    @GET("/get-notification")
    fun getNotification(): Call<NotificationResponse>
}

class NotificationWorker(appContext: Context, workerParams: WorkerParameters) :
    CoroutineWorker(appContext, workerParams) {

    private val logger: Logger = Logger.getLogger("NotificationWorkerLogger")

    init {
        setupLogger()
    }

    private fun setupLogger() {
        try {
            val fileHandler = FileHandler("${applicationContext.filesDir}/error_log.txt", true)
            fileHandler.formatter = SimpleFormatter()
            logger.addHandler(fileHandler)
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    override suspend fun doWork(): Result {
        val retrofit = Retrofit.Builder()
            .baseUrl("http://192.168.1.165:5000")
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        val api = retrofit.create(NotificationApi::class.java)
        return try {
            val response = api.getNotification().execute()
            if (response.isSuccessful && response.body() != null) {
                val notification = response.body()!!
                showNotification(notification.title, notification.message)
                Result.success()
            } else {
                Result.retry()
            }
        } catch (e: Exception) {
            logger.severe("Error fetching notification: ${e.message}")
            Result.retry()
        }
    }

    private fun showNotification(title: String, message: String) {
        val intent = Intent(applicationContext, MainActivity::class.java).apply {
            addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
        }
        val pendingIntent = PendingIntent.getActivity(
            applicationContext,
            0,
            intent,
            PendingIntent.FLAG_ONE_SHOT or PendingIntent.FLAG_IMMUTABLE
        )

        val channelId = "Default_Channel"
        val notificationBuilder = NotificationCompat.Builder(applicationContext, channelId)
            .setContentTitle(title)
            .setContentText(message)
            .setAutoCancel(true)
            .setSmallIcon(R.drawable.ic_launcher_foreground)
            .setSound(android.provider.Settings.System.DEFAULT_NOTIFICATION_URI)
            .setContentIntent(pendingIntent)

        val notificationManager = applicationContext.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(channelId, "Default Channel", NotificationManager.IMPORTANCE_DEFAULT)
            notificationManager.createNotificationChannel(channel)
        }
        notificationManager.notify(0, notificationBuilder.build())
    }
}
