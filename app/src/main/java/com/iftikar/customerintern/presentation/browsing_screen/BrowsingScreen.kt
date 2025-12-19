package com.iftikar.customerintern.presentation.browsing_screen

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
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.BookmarkBorder
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.outlined.LocationOn
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import coil.compose.AsyncImage
import com.iftikar.customerintern.R
import com.iftikar.customerintern.domain.model.Destination
import com.iftikar.customerintern.ui.theme.CustomLightGray
import com.iftikar.customerintern.ui.theme.LightOrange
import com.iftikar.customerintern.ui.theme.StarColor
import com.iftikar.customerintern.ui.theme.UbuntuHeadingLarge

@Composable
fun BrowsingScreen(
    viewModel: BrowsingScreenViewModel = hiltViewModel(),
    onClick: (String) -> Unit
) {
    val state by viewModel.state.collectAsStateWithLifecycle()
    Scaffold(
        modifier = Modifier.fillMaxSize(),
        topBar = {
            UserProfileCard(
                modifier = Modifier
                    .systemBarsPadding()
                    .padding(start = 24.dp),
                userName = "Usr Name"
            )
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .padding(innerPadding)
                .fillMaxSize()
                .padding(horizontal = 24.dp, vertical = 44.dp)
        ) {
            HeadLineText()
            Spacer(Modifier.height(44.dp))
            Destinations(
                destinations = state.destinations,
                isLoading = state.isLoading,
                onClick = {onClick(it)}
            )
        }
    }
}

@Composable
private fun UserProfileCard(
    modifier: Modifier = Modifier,
    userName: String
) {
    ElevatedCard(
        modifier = modifier,
        colors = CardDefaults.elevatedCardColors(
            containerColor = CustomLightGray,
            contentColor = Color.Black
        ),
        shape = RoundedCornerShape(30.dp)
    ) {
        Row(
            modifier = Modifier.padding(6.dp),
            horizontalArrangement = Arrangement.spacedBy(3.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier.size(37.dp),
                contentAlignment = Alignment.Center
            ) {
                AsyncImage(
                    model = R.drawable.user_profile,
                    contentDescription = "User profile",
                    contentScale = ContentScale.Crop,
                    modifier = Modifier.fillMaxSize()
                )
            }

            Text(
                text = userName,
                style = MaterialTheme.typography.titleMedium
            )
        }
    }
}

@Composable
private fun HeadLineText(modifier: Modifier = Modifier) {
    Column(
        modifier = modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.Start,
        verticalArrangement = Arrangement.spacedBy(6.dp)
    ) {
        Text(
            text = "Explore the",
            style = UbuntuHeadingLarge,
            fontWeight = FontWeight.ExtraLight
        )

        Row(
            horizontalArrangement = Arrangement.spacedBy(3.dp),
        ) {
            Text(
                text = "Beautiful",
                style = UbuntuHeadingLarge
            )
            Column(
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = "world!",
                    style = UbuntuHeadingLarge,
                    color = LightOrange
                )
                Box(
                    modifier = Modifier
                        .width(98.dp)
                        .padding(start = 6.dp),
                    contentAlignment = Alignment.Center
                ) {
                    AsyncImage(
                        model = R.drawable.mainscreen_curve_logo,
                        contentDescription = null,
                        modifier = Modifier.fillMaxWidth()
                    )
                }
            }
        }
    }
}

@Composable
private fun Destinations(
    modifier: Modifier = Modifier,
    destinations: List<Destination>,
    isLoading: Boolean,
    onClick: (String) -> Unit
) {


    Column(
        modifier = modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(12.dp),
        horizontalAlignment = Alignment.Start
    ) {
        Text(
            text = "Best Destination",
            style = MaterialTheme.typography.titleLarge,
            fontWeight = FontWeight.SemiBold
        )

        if (isLoading) {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator(
                    modifier = Modifier.size(28.dp),
                    strokeWidth = 3.dp,
                    strokeCap = StrokeCap.Round,
                    color = LightOrange
                )
            }
        } else {
            LazyRow(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                items(destinations) { destination ->
                    DestinationItem(destination = destination,
                        onClick = {onClick(destination.id)})
                }
            }
        }
    }
}

@Composable
private fun DestinationItem(
    modifier: Modifier = Modifier,
    destination: Destination,
    onClick: () -> Unit
) {
    ElevatedCard(
        modifier = modifier
            .width(268.dp)
            .height(384.dp),
        colors = CardDefaults.elevatedCardColors(
            containerColor = Color.White,
            contentColor = Color.Black
        ),
        shape = RoundedCornerShape(32.dp),
        onClick = onClick
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp, Alignment.CenterVertically),
            horizontalAlignment = Alignment.Start
        ) {
            Box(
                modifier = Modifier
                    .height(286.dp)
                    .width(240.dp),
            ) {
                AsyncImage(
                    model = destination.imageUrl,
                    contentDescription = null,
                    modifier = Modifier.fillMaxSize(),
                    contentScale = ContentScale.Crop
                )
                IconButton(
                    onClick = {},
                    modifier = Modifier
                        .align(Alignment.TopEnd)
                        .padding(top = 12.dp, end = 12.dp),
                    shape = CircleShape,
                    colors = IconButtonDefaults.iconButtonColors(
                        containerColor = Color.Gray.copy(0.3f)
                    )
                ) {
                    Icon(
                        imageVector = Icons.Default.BookmarkBorder,
                        contentDescription = null,
                        tint = Color.White
                    )
                }
            }

            Row(
                modifier = Modifier.width(240.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = destination.name,
                    style = MaterialTheme.typography.titleMedium
                )
                Row(
                    horizontalArrangement = Arrangement.spacedBy(3.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        imageVector = Icons.Default.Star,
                        contentDescription = null,
                        tint = StarColor,
                        modifier = Modifier.size(16.dp)
                    )
                    Text(
                        text = destination.rating
                    )
                }
            }
            Row(
                modifier = Modifier.width(240.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(
                    horizontalArrangement = Arrangement.spacedBy(3.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        imageVector = Icons.Outlined.LocationOn,
                        contentDescription = "Location",
                        modifier = Modifier.size(20.dp)
                    )
                    Text(
                        text = destination.location,
                        style = MaterialTheme.typography.titleSmall
                    )
                }
                AsyncImage(
                    model = R.drawable.user_rating,
                    contentDescription = null,
                    modifier = Modifier
                        .height(32.dp)
                        .width(72.dp)
                )
            }
        }
    }
}











