package com.iftikar.customerintern.presentation.destination_details_screen

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.background
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
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.ClickableText
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.outlined.LocationOn
import androidx.compose.material3.BottomSheetScaffold
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SheetValue
import androidx.compose.material3.Text
import androidx.compose.material3.rememberBottomSheetScaffoldState
import androidx.compose.material3.rememberStandardBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.TextLayoutResult
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import coil.compose.AsyncImage
import com.iftikar.customerintern.R
import com.iftikar.customerintern.domain.model.BookingStatus
import com.iftikar.customerintern.domain.model.Destination
import com.iftikar.customerintern.presentation.components.ButtonComponent
import com.iftikar.customerintern.presentation.components.TopAppBarComponent
import com.iftikar.customerintern.ui.theme.LightOrange
import com.iftikar.customerintern.ui.theme.StarColor

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DestinationDetailsScreen(
    viewModel: DestinationDetailScreenViewModel = hiltViewModel(),
    destinationId: String,
    onNavigateBack: () -> Unit
) {
    val state by viewModel.state.collectAsStateWithLifecycle()
    val sheetState = rememberBottomSheetScaffoldState(
        bottomSheetState = rememberStandardBottomSheetState(
            initialValue = SheetValue.Expanded,
            skipHiddenState = true
        )
    )

    val onAction = viewModel::onAction

    val buttonText = if (state.booking == null) {
        "Book Now"
    } else {
        when (state.booking!!.status) {
            BookingStatus.PENDING -> "Requesting..."
            BookingStatus.ACCEPTED -> "Accepted"
            else -> "Book Now"
        }
    }

    val buttonEnabled by remember {
        derivedStateOf {
            state.booking == null
        }
    }

    BackHandler {
        onNavigateBack()
    }

    LaunchedEffect(Unit) {
        viewModel.getDestination(destinationId)
        viewModel.getBooking(destinationId)
    }

    BottomSheetScaffold(
        scaffoldState = sheetState,
        sheetPeekHeight = 200.dp,
        sheetShape = RoundedCornerShape(
            topStart = 32.dp,
            topEnd = 32.dp
        ),
        sheetDragHandle = null,
        sheetContainerColor = Color.White,
        sheetContent = {
            DestinationBottomSheetContent(
                buttonText = buttonText,
                destination = state.destination,
                buttonEnabled = buttonEnabled,
                onBookClick = {
                    onAction(DestinationDetailScreenAction.BookDestination(destinationId))
                }
            )
        }
    ) {
        Box(modifier = Modifier.fillMaxSize()) {
            AsyncImage(
                model = state.destination.imageUrl,
                contentDescription = null,
                modifier = Modifier.fillMaxSize(),
                contentScale = ContentScale.Crop
            )
            TopAppBarComponent(
                title = "Details",
                onNavigateBack = onNavigateBack
            )
        }
    }
}


@Composable
fun DestinationBottomSheetContent(
    modifier: Modifier = Modifier,
    buttonText: String,
    buttonEnabled: Boolean,
    destination: Destination,
    onBookClick: () -> Unit
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(24.dp)
            .verticalScroll(rememberScrollState()),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        Box(
            modifier = Modifier
                .width(40.dp)
                .height(4.dp)
                .align(Alignment.CenterHorizontally)
                .background(
                    color = Color.LightGray,
                    shape = RoundedCornerShape(2.dp)
                )
        )

        Spacer(Modifier.height(16.dp))

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(
                horizontalAlignment = Alignment.Start,
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Text(
                    text = destination.name,
                    style = MaterialTheme.typography.titleLarge,
                    fontSize = 28.sp,
                    fontWeight = FontWeight.SemiBold
                )
                Text(
                    text = destination.location,
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Normal
                )
            }
            Box(
                modifier = Modifier.size(52.dp)
            ) {
                AsyncImage(
                    model = R.drawable.person1,
                    contentDescription = null,
                    modifier = Modifier.fillMaxSize(),
                    contentScale = ContentScale.Crop
                )
            }
        }

        Spacer(Modifier.height(6.dp))

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(45.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Row(
                horizontalArrangement = Arrangement.spacedBy(3.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    imageVector = Icons.Outlined.LocationOn,
                    contentDescription = null,
                    modifier = Modifier.size(20.dp)
                )
                Text(
                    text = destination.location.substringBefore(",").trim(),
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Normal
                )
            }

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
                    text = buildAnnotatedString {
                        append("${destination.rating} ")

                        withStyle(
                            style = SpanStyle(
                                fontWeight = FontWeight.Normal
                            )
                        ) {
                            append("(${destination.ratingCount})")
                        }
                    },
                    style = MaterialTheme.typography.titleMedium
                )
            }
            Text(
                text = buildAnnotatedString {
                    withStyle(
                        style = SpanStyle(
                            color = Color.Blue,
                            fontWeight = FontWeight.Normal
                        )
                    ) {
                        append("$${destination.cost}/")
                    }
                    withStyle(
                        style = SpanStyle(
                            fontWeight = FontWeight.Normal
                        )
                    ) {
                        append("Person")
                    }
                },
                style = MaterialTheme.typography.titleMedium
            )
        }
        Spacer(Modifier.height(6.dp))
        AsyncImage(
            model = R.drawable.places_overview,
            contentDescription = null,
            modifier = Modifier.fillMaxWidth(),
            contentScale = ContentScale.FillWidth
        )
        Spacer(Modifier.height(6.dp))
        Column(
            modifier = Modifier.fillMaxWidth(),
            horizontalAlignment = Alignment.Start,
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Text(
                text = "About Destination",
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.SemiBold
            )
            DescriptionText(
                text = destination.description
            )
        }
        Spacer(Modifier.height(4.dp))
        ButtonComponent(
            onClick = onBookClick,
            enabled = buttonEnabled
        ) {
            Text(
                text = buttonText,
                style = MaterialTheme.typography.titleLarge,
                textAlign = TextAlign.Center,
                modifier = Modifier.padding(8.dp)
            )
        }
    }
}

@Composable
fun DescriptionText(
    text: String,
    modifier: Modifier = Modifier,
    maxLines: Int = 4
) {
    var expanded by remember { mutableStateOf(false) }
    var textLayoutResult by remember { mutableStateOf<TextLayoutResult?>(null) }

    // Expanded → simple Text
    if (expanded) {
        Text(
            text = text,
            style = MaterialTheme.typography.bodyMedium,
            modifier = modifier
        )
        return
    }

    val annotatedText = remember(text, textLayoutResult) {
        if (textLayoutResult == null || !textLayoutResult!!.hasVisualOverflow) {
            AnnotatedString(text)
        } else {
            val lastLine = maxLines - 1
            val endIndex = textLayoutResult!!
                .getLineEnd(lastLine, visibleEnd = true)

            val trimmed = text
                .substring(0, endIndex)
                .trimEnd('.', ' ', '\n')

            buildAnnotatedString {
                append(trimmed)
                append("… ")

                pushStringAnnotation(
                    tag = "READ_MORE",
                    annotation = "read_more"
                )
                withStyle(
                    SpanStyle(
                        color = LightOrange,
                        fontWeight = FontWeight.Medium
                    )
                ) {
                    append("Read More")
                }
                pop()
            }
        }
    }

    ClickableText(
        text = annotatedText,
        style = MaterialTheme.typography.bodyMedium,
        maxLines = maxLines,
        overflow = TextOverflow.Clip,
        modifier = modifier,
        onTextLayout = { result ->
            // 🔒 Prevent infinite update loop
            if (textLayoutResult == null) {
                textLayoutResult = result
            }
        },
        onClick = { offset ->
            annotatedText
                .getStringAnnotations(
                    tag = "READ_MORE",
                    start = offset,
                    end = offset
                )
                .firstOrNull()
                ?.let {
                    expanded = true
                }
        }
    )
}























