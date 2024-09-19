package com.example.mobileapplicationassignment.frontEndUi.settings

import android.annotation.SuppressLint
import android.app.NotificationManager
import android.content.Context
import android.content.SharedPreferences
import android.util.Log
import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp



@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun NotificationSettings(context: Context) {
    val sharedPreferences = context.getSharedPreferences("settings", Context.MODE_PRIVATE)
    val editor: SharedPreferences.Editor = sharedPreferences.edit()

    val isNotificationEnabled = remember {
        mutableStateOf(sharedPreferences.getBoolean("notifications", false))
    }

    LaunchedEffect(isNotificationEnabled.value) {
        editor.putBoolean("notifications", isNotificationEnabled.value)
        editor.apply()
        Log.d("NotificationSettings", "Notifications are ${if (isNotificationEnabled.value) "enabled" else "disabled"}")
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(
                Brush.verticalGradient(
                    colors = listOf(
                        MaterialTheme.colorScheme.primary.copy(alpha = 0.1f),
                        MaterialTheme.colorScheme.background
                    )
                )
            )
            .padding(16.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 16.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(
                text = if (isNotificationEnabled.value) "Notifications Enabled" else "Notifications Disabled",
                style = MaterialTheme.typography.bodyLarge,
                fontSize = 18.sp
            )
            // 添加切换开关
            Switch(
                checked = isNotificationEnabled.value,
                onCheckedChange = { isChecked ->
                    isNotificationEnabled.value = isChecked
                    toggleNotifications(context, isChecked)
                },
                colors = SwitchDefaults.colors(
                    checkedThumbColor = Color.Black,
                    uncheckedThumbColor = MaterialTheme.colorScheme.onBackground,
                    checkedTrackColor = Color(0xFF81C784),
                    uncheckedTrackColor = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.5f)
                )
            )
        }
    }
}

@SuppressLint("ServiceCast")
fun toggleNotifications(context: Context, enabled: Boolean) {
    val notificationManager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

    if (enabled) {
        Toast.makeText(context, "Notifications Enabled", Toast.LENGTH_SHORT).show()
    } else {
        // 禁用通知
        Toast.makeText(context, "Notifications Disabled", Toast.LENGTH_SHORT).show()
        notificationManager.cancelAll()
    }
}


