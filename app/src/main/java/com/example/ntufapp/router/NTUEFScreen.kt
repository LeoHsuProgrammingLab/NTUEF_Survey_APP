package com.example.ntufapp.router

import android.util.Log
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
import com.example.ntufapp.ui.widget.NTUEFTopBar
import com.example.ntufapp.viewModel.SurveyViewModel
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import com.example.ntufapp.data.ntufappInfo.Companion.dTag
import com.example.ntufapp.layout.NewSurveyScreen
import com.example.ntufapp.layout.ResultDisplayScreen
import com.example.ntufapp.layout.SaveScreen

enum class Screens {
    Start,
    ReSurvey,
    NewSurvey,
    ResultDisplay,
    SaveJson
}

@Composable
fun NTUEFApp(
    navController: NavHostController = rememberNavController(),
    viewModel: SurveyViewModel = viewModel()
) {
    Scaffold(
        topBar = { NTUEFTopBar() }
    ) { paddingValues ->

        val resultState by viewModel.resultState.collectAsState()

        NavHost(
            navController = navController,
            startDestination = Screens.Start.name,
            modifier = Modifier.padding(paddingValues)
        ) {
            composable(route = Screens.Start.name) {
                PlotOptionsScreen(
                    onNextButtonClick = {plotData, surveyType, outputFilename ->
                        viewModel.setNewData(plotData) // set the NewPlotData as the input json file and reset all trees
                        if (surveyType == "ReSurvey") {
                            viewModel.setOldData(plotData) // set the OldPlotData as the input json file
                            navController.navigate(Screens.ReSurvey.name)
                        } else {
                            navController.navigate(Screens.NewSurvey.name)
                        }
                        viewModel.fileName = outputFilename
                    }
                )
            }

            composable(route = Screens.ReSurvey.name) {
                ReSurveyScreen(
                    onNextButtonClick = {
                        navController.navigate("${Screens.ResultDisplay}/source=${Screens.ReSurvey.name}")
                    },
                    oldPlotData = resultState.first, // old plot data
                    newPlotData = resultState.second //new plot data
                )
            }

            composable(route = Screens.NewSurvey.name) {
                NewSurveyScreen(
                    onNextButtonClick = {
                        navController.navigate("${Screens.ResultDisplay}/source=${Screens.NewSurvey.name}")
                    },
                    newPlotData = resultState.second
                )
            }

            composable(route = "${Screens.ResultDisplay}/source={source}") {backStackEntry ->
                val source = backStackEntry.arguments?.getString("source")
                Log.i(dTag, "source: $source")

                ResultDisplayScreen(
                    oldPlotData = resultState.first,
                    newPlotData = resultState.second,
                    from = source?: "",
                    onBackButtonClick = {
                        when (source) {
                            "ReSurvey" -> navController.navigate(Screens.ReSurvey.name)
                            "NewSurvey" -> navController.navigate(Screens.NewSurvey.name)
                        }
                        Log.d(dTag, resultState.second.PlotTrees.size.toString())
                        Log.d(dTag, resultState.second.PlotTrees[0].Species)
                    },
                    onNextButtonClick = {
                        navController.navigate(Screens.SaveJson.name)
                    }
                )
            }

            composable(route = Screens.SaveJson.name) {
                SaveScreen(
                    newPlotData = resultState.second,
                    outputFilename = viewModel.fileName,
                    onBackButtonClick = {
                        resultState.first.resetAllTrees()
                        navController.navigate(Screens.Start.name)
                    }
                )
            }
        }
    }
}