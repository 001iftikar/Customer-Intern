package com.iftikar.customerintern.presentation.landing_screen

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.iftikar.customerintern.R
import com.iftikar.customerintern.presentation.components.ButtonComponent
import com.iftikar.customerintern.ui.theme.LightOrange
import com.iftikar.customerintern.ui.theme.LighterBlue
import com.iftikar.customerintern.ui.theme.UbuntuHeadingLarge

@Composable
fun LandingScreen(
    onNavigate: () -> Unit
) {
    Surface(
        modifier = Modifier.fillMaxSize()
    ) {
        Column(
            modifier = Modifier.fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(44.dp)
        ) {
            Box {
                AsyncImage(
                    model = R.drawable.landing_screen_picture,
                    contentDescription = null,
                    modifier = Modifier.fillMaxWidth(),
                    contentScale = ContentScale.Fit
                )
                SkipTextButton(
                    modifier = Modifier.align(Alignment.TopEnd)
                        .padding(vertical = 63.dp, horizontal = 24.dp),
                    onClick = onNavigate
                )
            }

            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(3.dp)
            ) {
                Text(
                    text = "Life is short and the",
                    style = UbuntuHeadingLarge
                )
                Row {
                    Text(
                        text = "world is ",
                        style = UbuntuHeadingLarge
                    )
                    Column(
                        verticalArrangement = Arrangement.spacedBy(5.dp)
                    ) {
                        Text(
                            text = "wide",
                            style = UbuntuHeadingLarge,
                            color = LightOrange
                        )
                        AsyncImage(
                            model = R.drawable.landing_screen_curve_logo,
                            contentDescription = null,
                            modifier = Modifier.width(85.dp)
                        )
                    }
                }
                Spacer(Modifier.height(8.dp))
                DescriptionText()
            }
            Spacer(Modifier.height(1.dp))
            ButtonComponent(
                modifier = Modifier.padding(horizontal = 24.dp),
                text = "Get Started",
                onClick = onNavigate
            )
        }
    }
}

@Composable
private fun DescriptionText() {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(4.dp)
    ) {
        Text(
            text = "At Friends tours and travel, we customize",
            style = MaterialTheme.typography.bodyLarge
        )
        Text(
            text = "reliable and trustworthy educational tours to",
            style = MaterialTheme.typography.bodyLarge
        )
        Text(
            text = "destinations all over the world",
            style = MaterialTheme.typography.bodyLarge
        )
    }
}

@Composable
private fun SkipTextButton(
    modifier: Modifier = Modifier,
    onClick: () -> Unit
) {
    Text(
        text = "Skip",
        style = MaterialTheme.typography.titleMedium,
        color = LighterBlue,
        modifier = modifier.clickable(
            onClick = onClick
        )
    )
}































