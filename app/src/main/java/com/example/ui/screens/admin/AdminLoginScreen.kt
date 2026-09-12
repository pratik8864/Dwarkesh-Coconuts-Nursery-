package com.example.ui.screens.admin

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.AppText
import com.example.ui.components.DwarkeshLogoView
import com.example.ui.theme.CreamBackground
import com.example.ui.theme.ForestGreen
import com.example.ui.theme.ForestGreenDark
import com.example.ui.theme.GoldAccent
import com.example.ui.theme.GoldAccentDark
import com.example.ui.theme.GoldAccentLight
import com.example.viewmodel.MainViewModel

@Composable
fun AdminLoginScreen(
    viewModel: MainViewModel,
    onLoginSuccess: () -> Unit
) {
    var pin by remember { mutableStateOf("") }
    val context = LocalContext.current

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(CreamBackground)
            .padding(24.dp),
        contentAlignment = Alignment.Center
    ) {
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .testTag("admin_login_card"),
            shape = RoundedCornerShape(20.dp),
            colors = CardDefaults.cardColors(containerColor = Color.White),
            elevation = CardDefaults.cardElevation(defaultElevation = 6.dp)
        ) {
            Column(
                modifier = Modifier.padding(24.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                // Logo on Admin Login Screen
                DwarkeshLogoView(
                    size = 110.dp,
                    transparentBackground = true,
                    showGlow = true
                )

                Spacer(modifier = Modifier.height(14.dp))

                Text(
                    text = "DWARKESH NURSERY",
                    fontWeight = FontWeight.Black,
                    fontFamily = FontFamily.Serif,
                    fontSize = 18.sp,
                    color = ForestGreenDark,
                    letterSpacing = 1.sp
                )

                Text(
                    text = AppText("admin_login"),
                    color = GoldAccentDark,
                    fontSize = 13.sp,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.padding(top = 2.dp)
                )

                Spacer(modifier = Modifier.height(24.dp))

                OutlinedTextField(
                    value = pin,
                    onValueChange = { if (it.length <= 6) pin = it },
                    label = { Text(AppText("enter_pin")) },
                    leadingIcon = {
                        Icon(Icons.Default.Lock, contentDescription = "Security PIN", tint = ForestGreen)
                    },
                    visualTransformation = PasswordVisualTransformation(),
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.NumberPassword),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = ForestGreen,
                        unfocusedBorderColor = Color.LightGray
                    ),
                    shape = RoundedCornerShape(12.dp),
                    modifier = Modifier
                        .fillMaxWidth()
                        .testTag("admin_pin_input"),
                    singleLine = true
                )

                Spacer(modifier = Modifier.height(10.dp))

                Text(
                    text = "Default PIN for nursery owner: 1234",
                    color = Color.Gray,
                    fontSize = 11.sp,
                    textAlign = TextAlign.Center
                )

                Spacer(modifier = Modifier.height(20.dp))

                Button(
                    onClick = {
                        val success = viewModel.adminLogin(pin)
                        if (success) {
                            Toast.makeText(context, "Admin access granted!", Toast.LENGTH_SHORT).show()
                            onLoginSuccess()
                        } else {
                            Toast.makeText(context, "Incorrect PIN. Default is 1234", Toast.LENGTH_LONG).show()
                        }
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(48.dp)
                        .testTag("admin_unlock_button"),
                    colors = ButtonDefaults.buttonColors(containerColor = ForestGreen),
                    shape = RoundedCornerShape(12.dp)
                ) {
                    Text(
                        text = "🔓 " + AppText("unlock"),
                        fontWeight = FontWeight.Bold,
                        fontSize = 14.sp
                    )
                }
            }
        }
    }
}
