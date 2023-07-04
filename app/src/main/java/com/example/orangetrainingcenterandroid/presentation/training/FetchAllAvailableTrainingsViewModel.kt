package com.example.orangetrainingcenterandroid.presentation.training



import android.content.ContentValues
import androidx.compose.runtime.mutableStateListOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.orangetrainingcenterandroid.common.ApiException
import com.example.orangetrainingcenterandroid.domain.training.model.ThemeDomainModel
import com.example.orangetrainingcenterandroid.domain.training.model.TrainingDomainModel
import com.example.orangetrainingcenterandroid.domain.training.usecase.FetchAllAvailableTrainingsUseCase
import com.example.orangetrainingcenterandroid.domain.training.usecase.GetAllThemesUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import timber.log.Timber
import java.text.SimpleDateFormat
import java.time.LocalDate
import java.time.ZoneId
import java.time.temporal.ChronoUnit
import java.util.*
import javax.inject.Inject


@HiltViewModel
class FetchAllAvailableTrainingsViewModel @Inject constructor(
    private val fetchAllAvailableTrainingsUseCase: FetchAllAvailableTrainingsUseCase,
    private val getAllThemesUseCase: GetAllThemesUseCase
) : ViewModel()
{

    private val _trainingsState = MutableStateFlow<List<TrainingDomainModel>>(emptyList())
    val trainingsState: StateFlow<List<TrainingDomainModel>> = _trainingsState

    private val _currentCursor = MutableStateFlow<String?>(null)
    val currentCursor: StateFlow<String?> = _currentCursor

    private var _loading = MutableStateFlow(false)
    val loading: StateFlow<Boolean> = _loading

    var isFetching = false
    var hasMoreData = true
   // var currentCursor: String? = null


    private val themesState = mutableStateListOf<ThemeDomainModel>()
    val themeNamesState = mutableStateListOf<String>()

    init {
        loadThemes()
    }


    fun clearCurrentCursor() {
        _currentCursor.value = null
    }
    fun clearTrainingsState() {
        _trainingsState.value = emptyList()
    }

    fun fetchData(
        searchQuery: String,
        themeQuery: String,
        startDateQuery: LocalDate?,
        endDateQuery: LocalDate?,
        cursor: String? = null
    ) {
        if (isFetching) {
            return
        }

        isFetching = true
        _loading.value = true

        viewModelScope.launch(Dispatchers.IO) {
            try {
                val trainingResult = fetchAllAvailableTrainingsUseCase.fetchAllAvailableTrainings(
                    searchQuery,
                    themeQuery,
                    startDateQuery,
                    endDateQuery,
                    cursor
                )

                val fetchedTrainings = trainingResult.trainings
                val nextCursor = trainingResult.nextCursor

                if (cursor == null || cursor == "") {
                    _trainingsState.emit(emptyList())
                }

                if (fetchedTrainings.isNotEmpty()) {
                    _trainingsState.emit(_trainingsState.value + fetchedTrainings)
                }

                hasMoreData = nextCursor != null

                _currentCursor.value = nextCursor
            } catch (e: ApiException) {
                Timber.tag(ContentValues.TAG).e(e, "Exception occurred")
            } finally {
                _loading.value = false
                isFetching = false
            }
        }
    }


    fun changeDateFormat(date: Date, newFormat: String): String {
        val desiredFormat = SimpleDateFormat(newFormat, Locale.ENGLISH)
        desiredFormat.timeZone = TimeZone.getTimeZone("UTC")
        return desiredFormat.format(date)
    }

    fun loadThemes() {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                val themes = getAllThemesUseCase.getAllThemes()
                themesState.clear()
                themesState.addAll(themes)
                themeNamesState.clear()
                themeNamesState.addAll(themes.map { it.name })
                println("el themes ${themes}")
            } catch (e: Exception) {
                // Handle the exception appropriately
                Timber.tag(ContentValues.TAG).e(e, "Exception occurred")
            }
        }
    }

}
