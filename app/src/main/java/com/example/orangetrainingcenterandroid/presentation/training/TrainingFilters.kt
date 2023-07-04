package com.example.orangetrainingcenterandroid.presentation.training

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.modifier.modifierLocalConsumer
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import com.example.orangetrainingcenterandroid.ui.theme.Orange500
import com.maxkeppeker.sheets.core.models.base.rememberSheetState
import com.maxkeppeler.sheets.calendar.CalendarDialog
import com.maxkeppeler.sheets.calendar.models.CalendarConfig
import com.maxkeppeler.sheets.calendar.models.CalendarSelection
import java.time.LocalDate

@Composable
fun TrainingFilterSection(
    themes: List<String>,
    selectedTheme: String,
    onThemeSelected: (String) -> Unit,
    onStartDateSelected: (LocalDate) -> Unit,
    onEndDateSelected: (LocalDate) -> Unit,
    initialStartDate: LocalDate?,
    initialEndDate: LocalDate?
) {
    var isExpanded by remember { mutableStateOf(false) }

    val surfaceColor: Color = MaterialTheme.colors.surface
    val textColor: Color = MaterialTheme.colors.onBackground
    val progressColor: Color = MaterialTheme.colors.primary

    Column (modifier = Modifier
        .fillMaxWidth(1f)
        .padding(top = 10.dp)

        .background(surfaceColor))
    {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .background(surfaceColor)
                .padding(10.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "Show More Filters",
                style = MaterialTheme.typography.h6,
                color= textColor,
                modifier = Modifier.weight(1f)
            )
            IconButton(
                onClick = { isExpanded = !isExpanded },
                modifier = Modifier.padding(end = 16.dp),

            ) {
                Icon(
                    imageVector = if (isExpanded) Icons.Filled.KeyboardArrowUp else Icons.Filled.KeyboardArrowDown,
                    contentDescription = "Expand/Collapse",
                    tint = textColor,
                )
            }
        }
        if (isExpanded) {
            Column(modifier = Modifier.background(surfaceColor) ){
                ThemeList(
                    themes = themes,
                    selectedTheme = selectedTheme,
                    onThemeSelected = onThemeSelected
                )
                Row(modifier = Modifier.fillMaxWidth()) {
                    StartDatePickerComp(
                        initialStartDate = initialStartDate,
                        onStartDateSelected = onStartDateSelected,
                        modifier = Modifier.weight(1f).padding(end = 5.dp)
                    )

                    EndDatePickerComp(
                        initialEndDate = initialEndDate,
                        onEndDateSelected = onEndDateSelected,
                        modifier = Modifier.weight(1f).padding(end = 5.dp)
                    )
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun StartDatePickerComp(
    initialStartDate: LocalDate?,
    onStartDateSelected: (LocalDate) -> Unit,
    modifier: Modifier
) {
    val calendarState = rememberSheetState()
    val textFieldState = remember { mutableStateOf(initialStartDate?.toString() ?: "") }

    CalendarDialog(
        state = calendarState,
        config = CalendarConfig(monthSelection = true, yearSelection = true),
        selection = CalendarSelection.Date { date ->
            onStartDateSelected(date)
            textFieldState.value = date.toString()
        }
    )

    TextField(
        value = textFieldState.value,
        onValueChange = { textFieldState.value = it },
        readOnly = true,
        trailingIcon = {
            IconButton(onClick = { calendarState.show() }) {
                Icon(
                    imageVector = Icons.Filled.CalendarToday,
                    contentDescription = "Select date",
                )
            }
        },
        label = { Text("Start Date") },
        colors = TextFieldDefaults.textFieldColors(
            backgroundColor = Color.Transparent,
            textColor = Color.Black,
            focusedIndicatorColor = Orange500,
            unfocusedIndicatorColor = Color.Black
        ),
        modifier = modifier
    )
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EndDatePickerComp(
    initialEndDate: LocalDate?,
    onEndDateSelected: (LocalDate) -> Unit,
    modifier: Modifier
) {
    val calendarState = rememberSheetState()
    val textFieldState = remember { mutableStateOf(initialEndDate?.toString() ?: "") }

    CalendarDialog(
        state = calendarState,
        config = CalendarConfig(monthSelection = true, yearSelection = true),
        selection = CalendarSelection.Date { date ->
            onEndDateSelected(date)
            textFieldState.value = date.toString()
        }
    )

    TextField(
        value = textFieldState.value,
        onValueChange = { textFieldState.value = it },
        readOnly = true,
        trailingIcon = {
            IconButton(onClick = { calendarState.show() }) {
                Icon(
                    imageVector = Icons.Filled.CalendarToday,
                    contentDescription = "Select date",
                )
            }
        },
        colors = TextFieldDefaults.textFieldColors(
            backgroundColor = Color.Transparent,
            textColor = Color.Black,
            focusedIndicatorColor = Orange500,
            unfocusedIndicatorColor = Color.Black
        ),
        label = { Text("End Date") },
        modifier = modifier
    )
}

@Composable
fun SearchBar(
    searchQuery: String,
    onSearchQueryChange: (String) -> Unit
) {
    OutlinedTextField(
        value = searchQuery,
        onValueChange = onSearchQueryChange,
        label = { Text("Search") },
        modifier = Modifier
            .fillMaxWidth()
            .height(60.dp)
            .background(MaterialTheme.colors.surface),
        singleLine = true,
        shape = RoundedCornerShape(50.dp),
        leadingIcon = {
            Icon(
                imageVector = Icons.Default.Search,
                contentDescription = "Search Icon",
                tint = Color.Gray
            )
        },
        trailingIcon = {
            if (searchQuery.isNotEmpty()) {
                IconButton(onClick = { onSearchQueryChange("") }) {
                    Icon(
                        imageVector = Icons.Default.Clear,
                        contentDescription = "Clear Icon",
                        tint = Color.Gray
                    )
                }
            }
        },
        colors = TextFieldDefaults.outlinedTextFieldColors(
            focusedBorderColor = Color.Black,
            unfocusedBorderColor = Color.Black,
            cursorColor = Color.Black
        )
    )
}

