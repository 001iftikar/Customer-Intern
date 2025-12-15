package com.iftikar.customerintern

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import com.descope.Descope
import com.descope.types.DeliveryMethod
import com.iftikar.customerintern.ui.theme.CustomerInternTheme
import com.iftikar.customerintern.utils.DESCOPE_PROJECT_ID
import kotlinx.coroutines.launch

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        installSplashScreen()
        super.onCreate(savedInstanceState)
        Descope.setup(this, projectId = DESCOPE_PROJECT_ID)
        enableEdgeToEdge()
        setContent {
            CustomerInternTheme {
                val scope = rememberCoroutineScope()
                Scaffold() { inn->
                    var email by remember { mutableStateOf("") }
                    var otp by remember { mutableStateOf("") }
                    Column(
                        modifier = Modifier.padding(inn)
                    ) {
                        TextField(
                            value = email,
                            onValueChange = {email = it}
                        )

                        Button(
                            onClick = {
                                scope.launch {
                                    Descope.otp.signUp(method = DeliveryMethod.Email, loginId = email)
                                }
                            }
                        ) {
                            Text("Email")
                        }

                        TextField(
                            value = otp,
                            onValueChange = {otp = it}
                        )

                        Button(
                            onClick = {
                                scope.launch {
                                    Descope.otp.verify(method = DeliveryMethod.Email, loginId = email, code = otp)
                                }
                            }
                        ) {
                            Text("Otp")
                        }
                    }
                }
            }
        }
    }
}

























