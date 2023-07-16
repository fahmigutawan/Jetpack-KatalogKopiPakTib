package com.mmdub.qofee.component

import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.unit.dp
import com.google.accompanist.placeholder.PlaceholderHighlight
import com.google.accompanist.placeholder.material.placeholder
import com.google.accompanist.placeholder.material.shimmer
import com.google.accompanist.placeholder.shimmer

fun Modifier.textLoadingModifier() = placeholder(
    visible = true,
    color = Color.LightGray,
    shape = RoundedCornerShape(4.dp),
    highlight = PlaceholderHighlight.shimmer(highlightColor = Color.White)
)

fun Modifier.rectLoadingModifier(shape: Shape = RoundedCornerShape(0.dp)) = placeholder(
    visible = true,
    color = Color.LightGray,
    highlight = PlaceholderHighlight.shimmer(highlightColor = Color.White),
    shape = shape
)

fun Modifier.circleLoadingModifier() = placeholder(
    visible = true,
    color = Color.LightGray,
    shape = CircleShape,
    highlight = PlaceholderHighlight.shimmer(highlightColor = Color.White)
)