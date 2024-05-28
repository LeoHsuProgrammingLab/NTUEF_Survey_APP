package com.example.ntufapp.viewModel

import androidx.lifecycle.ViewModel
import com.example.ntufapp.model.PlotData
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update


class SurveyViewModel: ViewModel() { // <oldPlot, newPlot>
    private val _resultState = MutableStateFlow(Pair(PlotData(), PlotData()))
    val resultState: StateFlow<Pair<PlotData, PlotData>> = _resultState.asStateFlow()
    var fileName: String = ""
        private set
    fun setOutputFileName(name: String) {
        fileName = name
    }

    fun setOldData(oldPlot: PlotData){
        _resultState.update { currentState ->
            currentState.copy(
                first = oldPlot.clone()
            )
        }
    }

    fun setNewData(newPlot: PlotData) {
        val temp = newPlot.clone().apply { resetAllTrees() }
        _resultState.update { currentState ->
            currentState.copy(
                second = temp
            )
        }
    }

    fun reset() {
        _resultState.value = Pair(PlotData(), PlotData())
    }
}