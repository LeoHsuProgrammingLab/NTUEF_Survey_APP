package com.example.ntufapp.viewModel

import androidx.lifecycle.ViewModel
import com.example.ntufapp.model.PlotData
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update


class TreeViewModel: ViewModel() {
    private val _resultState = MutableStateFlow(Pair(PlotData(), PlotData()))
    val resultState: StateFlow<Pair<PlotData, PlotData>> = _resultState.asStateFlow()

    fun setOldData(oldPlot: PlotData){
        _resultState.update { currentState ->
            currentState.copy(
                first = oldPlot
            )
        }
    }

    fun setNewData(newPlot: PlotData) {
        _resultState.update {currentState ->
            currentState.copy(
                second = newPlot
            )
        }
    }

    fun reset() {
        _resultState.value = Pair(PlotData(), PlotData())
    }

    fun compare() {
        /*TODO: compare the visHeight, measHeight, forkHeight, DBH, and send the alarm*/
    }

}