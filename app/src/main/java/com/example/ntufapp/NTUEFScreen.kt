package com.example.ntufapp

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.ntufapp.layout.PlotOptionsScreen
import com.example.ntufapp.layout.ReSurveyScreen
import com.example.ntufapp.ui.NTUEFTopBar
import com.example.ntufapp.viewModel.TreeViewModel
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.platform.LocalContext
import com.example.ntufapp.layout.NewSurveyScreen
import com.example.ntufapp.layout.ResultDisplayScreen
import com.example.ntufapp.layout.showMessage

enum class Screens {
    Start,
    ReSurvey,
    NewSurvey,
    ResultDisplay,
    Visualization
}

@Composable
fun NTUEFApp(
    navController: NavHostController = rememberNavController(),
    viewModel: TreeViewModel = viewModel()
) {
    Scaffold(
        topBar = {
            NTUEFTopBar()
        }
    ) { paddingValues ->

        val resultState by viewModel.resultState.collectAsState()

        NavHost(
            navController = navController,
            startDestination = Screens.Start.name,
            modifier = Modifier.padding(paddingValues)
        ) {
            composable(route = Screens.Start.name) {
                PlotOptionsScreen(
                    onNextButtonClick = {plotData, surveyType ->
                        viewModel.setOldData(plotData)
                        viewModel.setNewData(plotData)
                        if (surveyType == "ReSurvey") {
                            navController.navigate(Screens.ReSurvey.name)
                        } else {
                            navController.navigate(Screens.NewSurvey.name)
                        }
                    }
                )
            }

            composable(route = Screens.ReSurvey.name) {
                ReSurveyScreen(
                    onNextButtonClick = {
                        navController.navigate(Screens.ResultDisplay.name)
                    },
                    oldPlotData = resultState.first, // old plot data
                    newPlotData = resultState.second //new plot data
                )
            }

            composable(route = Screens.ResultDisplay.name) {
                ResultDisplayScreen(
                    oldPlotData = resultState.first,
                    newPlotData = resultState.second,
                    onBackButtonClick = {
                        navController.navigate(Screens.ReSurvey.name)
                    },
                    onNextButtonClick = {
                        navController.navigate(Screens.Visualization.name)
                    }
                )
            }

            composable(route = Screens.Visualization.name) {

            }

            composable(route = Screens.NewSurvey.name) {
                NewSurveyScreen(
                    onNextButtonClick = {
                        navController.navigate(Screens.ResultDisplay.name)
                    },
                    newPlotData = resultState.second
                )
            }
        }
    }
}