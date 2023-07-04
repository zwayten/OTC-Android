package com.example.orangetrainingcenterandroid.presentation.menu

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp
import com.example.orangetrainingcenterandroid.ui.theme.Orange500

@Composable
fun BottomNavigationBar(
    items: List<BottomNavItem>,
    selectedIndex: Int,
    onItemSelected: (Int) -> Unit
) {
    val itemShape = RoundedCornerShape(50.dp)
    BoxWithConstraints(
        modifier = Modifier
            .fillMaxWidth()
            .wrapContentHeight()
            .background(Color.Transparent)
            .padding(16.dp)
            .shadow(20.dp, RoundedCornerShape(50.dp))
            .background(Color.Transparent)
    ) {
        BottomNavigation(
            modifier = Modifier.background(Color.Transparent),
            backgroundColor = Color.White,
            elevation = 20.dp
        ) {
            items.forEachIndexed { index, item ->
                val isSelected = index == selectedIndex

                BottomNavigationItem(
                    selected = isSelected,
                    onClick = { onItemSelected(index) },
                    icon = {
                        Box(
                            modifier = Modifier.fillMaxWidth(0.6f)
                                .padding(8.dp)
                        ) {
                            Box(
                                modifier = Modifier
                                    .fillMaxSize()
                                    .background(
                                        color = if (isSelected) {
                                            Orange500
                                        } else {
                                            Color.Transparent
                                        },
                                        shape = itemShape
                                    )
                            )
                            Box(
                                modifier = Modifier.fillMaxSize(),
                                contentAlignment = Alignment.Center
                            ) {
                                Icon(
                                    item.icon,
                                    contentDescription = item.title,
                                    tint = if (isSelected) {
                                        Color.White
                                    } else {
                                        Color.Black
                                    }
                                )
                            }
                        }
                    },
                    selectedContentColor = item.selectedContentColor,
                    unselectedContentColor = item.unselectedContentColor
                )
            }
        }
    }
}



data class BottomNavItem(
    val title: String,
    val icon: ImageVector,
    val selectedContentColor: Color,
    val unselectedContentColor: Color
)