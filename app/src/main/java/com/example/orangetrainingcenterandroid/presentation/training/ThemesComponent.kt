package com.example.orangetrainingcenterandroid.presentation.training

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.example.orangetrainingcenterandroid.R
import com.example.orangetrainingcenterandroid.ui.theme.Grey

@Composable
fun ThemeSingleItem(
    text: String,
    onClick: () -> Unit,
    isSelected: Boolean,
    modifier: Modifier = Modifier,
) {
    val buttonShape = RoundedCornerShape(percent = 50)
    Button(
        onClick = onClick,
        shape = buttonShape,
        colors = ButtonDefaults.buttonColors(
            backgroundColor = if (isSelected) Color.Gray else Color.White,
            contentColor = if (isSelected) Color.White else Color.Black
        ),
        border = BorderStroke(2.dp, Grey),
        modifier = modifier
    ) {
        Text(
            text = text,
            style = TextStyle(
                fontWeight = FontWeight.Bold,
                fontFamily = FontFamily(Font(R.font.neue_helvetica)),
                textAlign = TextAlign.Start
            )
        )
    }
}

@Composable
fun ThemeList(
    themes: List<String>,
    selectedTheme: String,
    onThemeSelected: (String) -> Unit
) {
    LazyRow(modifier = Modifier.padding(horizontal = 16.dp)) {
        items(themes) { theme ->
            ThemeSingleItem(
                text = theme,
                onClick = {
                    val newSelectedTheme = if (theme == selectedTheme) "" else theme
                    onThemeSelected(newSelectedTheme)
                },
                isSelected = theme == selectedTheme,
                modifier = Modifier.padding(end = 8.dp)
            )
        }
    }
}








