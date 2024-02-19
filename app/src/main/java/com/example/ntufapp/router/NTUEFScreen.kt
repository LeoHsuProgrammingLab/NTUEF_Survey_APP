package com.example.ntufapp.router

import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Dataset
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.outlined.Dataset
import androidx.compose.material.icons.outlined.Home
import androidx.compose.material.rememberScaffoldState
import androidx.compose.material3.DrawerState
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.Icon
import androidx.compose.material3.ModalDrawerSheet
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.NavigationDrawerItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.rememberDrawerState
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
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import com.example.ntufapp.data.ntufappInfo.Companion.dTag
import com.example.ntufapp.layout.DownloadUploadJsonScreen
import com.example.ntufapp.layout.NewSurveyScreen
import com.example.ntufapp.layout.ResultDisplayScreen
import com.example.ntufapp.layout.SaveScreen
import com.example.ntufapp.ui.widget.Drawer
import com.example.ntufapp.ui.widget.NavigationItem
import kotlinx.coroutines.launch

enum class Screens {
    Start,
    ReSurvey,
    NewSurvey,
    ResultDisplay,
    SaveJson,
    LoadJson
}

@RequiresApi(Build.VERSION_CODES.R)
@Composable
fun NTUEFApp(
    navController: NavHostController = rememberNavController(),
    viewModel: SurveyViewModel = viewModel()
) {
    val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
    val scope = rememberCoroutineScope()

    val items = listOf(
        NavigationItem(
            title = "調查頁面",
            selectedIcon = Icons.Filled.Home,
            unselectedIcon = Icons.Outlined.Home,
            onClick = {
                scope.launch {
                    drawerState.close()
                    navController.navigate(Screens.Start.name)
                }
            }
        ),
        NavigationItem(
            title = "上下載Json頁面",
            selectedIcon = Icons.Filled.Dataset,
            unselectedIcon = Icons.Outlined.Dataset,
            onClick = {
                scope.launch {
                    drawerState.close()
                    navController.navigate(Screens.LoadJson.name)
                }
            }
        )
    )

    val selectedItemIndex = rememberSaveable {
        mutableStateOf(0)
    }
    ModalNavigationDrawer(
        drawerContent = {
            ModalDrawerSheet {
                items.forEachIndexed { id, item ->
                    NavigationDrawerItem(
                        label = {
                            Text(text = item.title,)
                        },
                        selected = (id == selectedItemIndex.value),
                        icon = {
                            Icon(
                                if (id == selectedItemIndex.value) item.selectedIcon else item.unselectedIcon,
                                contentDescription = item.title
                            )
                        },
                        onClick = {
                            selectedItemIndex.value = id
                            scope.launch {
                                drawerState.close()
                            }
                            item.onClick()
                        }
                    )
                }
            }
        },
        drawerState = drawerState,
    ) {
        Scaffold(
            topBar = {
                NTUEFTopBar(
                    onMenuClick = {
                        scope.launch {
                            drawerState.open()
                        }
                    }
                )
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
                        onNextButtonClick = { navController.navigate("${Screens.ResultDisplay}/source=${Screens.ReSurvey.name}") },
                        newPlotData = resultState.second //new plot data
                    )
                }

                composable(route = Screens.NewSurvey.name) {
                    NewSurveyScreen(
                        onNextButtonClick = { navController.navigate("${Screens.ResultDisplay}/source=${Screens.NewSurvey.name}") },
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
                            viewModel.reset()
                            navController.navigate(Screens.Start.name)
                        }
                    )
                }

                composable(route = Screens.LoadJson.name) {
                    DownloadUploadJsonScreen()
                }
            }
        }
    }
}