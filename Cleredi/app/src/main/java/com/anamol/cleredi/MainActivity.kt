package com.anamol.cleredi

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.app.NotificationCompat
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import com.anamol.cleredi.ui.theme.ClerediTheme

class MainActivity : ComponentActivity() {

    private val handler = Handler(Looper.getMainLooper())
    private val runnable = object : Runnable {
        override fun run() {
            fetchNotification()
            handler.postDelayed(this, 1000) // Check every second
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            ClerediTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    TaskQueueInfoScreen(
                        modifier = Modifier
                            .padding(innerPadding)
                            .fillMaxSize()
                            .padding(16.dp)
                    )
                }
            }
        }
        handler.post(runnable) // Start the runnable
    }

    private fun fetchNotification() {
        val retrofit = Retrofit.Builder()
            .baseUrl("http://192.168.1.165:5000") // Ensure this IP is correct
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        val api = retrofit.create(NotificationApi::class.java)

        api.getNotification().enqueue(object : Callback<NotificationResponse> {
            override fun onResponse(call: Call<NotificationResponse>, response: Response<NotificationResponse>) {
                if (response.isSuccessful && response.body() != null) {
                    val notification = response.body()!!
                    showLocalNotification(notification.title, notification.message)
                }
            }

            override fun onFailure(call: Call<NotificationResponse>, t: Throwable) {
//                Toast.makeText(this@MainActivity, "Error: ${t.message}", Toast.LENGTH_SHORT).show()
            }
        })
    }

    private fun showLocalNotification(title: String, message: String) {
        val intent = Intent(this, MainActivity::class.java).apply {
            addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
        }
        val pendingIntent = PendingIntent.getActivity(
            this,
            0,
            intent,
            PendingIntent.FLAG_ONE_SHOT or PendingIntent.FLAG_IMMUTABLE
        )

        val channelId = "Default_Channel"
        val notificationBuilder = NotificationCompat.Builder(this, channelId)
            .setContentTitle(title)
            .setContentText(message)
            .setAutoCancel(true)
            .setSmallIcon(R.drawable.ic_launcher_foreground)
            .setContentIntent(pendingIntent)

        val notificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(channelId, "Default Channel", NotificationManager.IMPORTANCE_DEFAULT)
            notificationManager.createNotificationChannel(channel)
        }
        notificationManager.notify(0, notificationBuilder.build())
    }

    override fun onDestroy() {
        super.onDestroy()
        handler.removeCallbacks(runnable) // Stop the handler when the activity is destroyed
    }
}

@Composable
fun TaskQueueInfoScreen(modifier: Modifier = Modifier) {
    Column(
        modifier = modifier
//            .background(Color(0xFFEFEFEF))
            .fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            shape = RoundedCornerShape(16.dp),
            elevation = CardDefaults.cardElevation(8.dp),
            colors = CardDefaults.cardColors(containerColor = Color.White)
        ) {
            Column(
                modifier = Modifier.padding(24.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = "Celery with Redis: Task Queue",
                    fontSize = 28.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFF2C3E50)
                )

                Spacer(modifier = Modifier.height(12.dp))

                Text(
                    text = "Celery is an asynchronous task queue/job queue system. Redis is often used as a message broker to handle and distribute tasks to workers efficiently.",
                    fontSize = 16.sp,
                    color = Color(0xFF7F8C8D),
                    lineHeight = 22.sp
                )

                Spacer(modifier = Modifier.height(16.dp))

                Column(
                    horizontalAlignment = Alignment.Start,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text(
                        text = "Key Features of Celery and Redis:",
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Medium,
                        color = Color(0xFF34495E)
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    val features = listOf(
                        "• Asynchronous task processing: Celery allows tasks to be executed in the background, improving application responsiveness.",
                        "• Distributed architecture: Tasks can be distributed across multiple workers, enhancing scalability.",
                        "• Integration with Redis: Redis serves as a message broker, ensuring efficient task management and queuing.",
                        "• Reliability: Celery provides task retry mechanisms in case of failure, ensuring high availability."
                    )

                    features.forEach { feature ->
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            text = feature,
                            fontSize = 14.sp,
                            color = Color(0xFF2C3E50),
                            lineHeight = 20.sp
                        )
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                Text(
                    text = "Learn more about it.",
                    fontSize = 14.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = MaterialTheme.colorScheme.secondary
                )
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun TaskQueueInfoScreenPreview() {
    ClerediTheme {
        TaskQueueInfoScreen()
    }
}
