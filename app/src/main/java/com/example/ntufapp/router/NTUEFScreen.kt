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
import androidx.compose.runtime.MutableState
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
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.example.ntufapp.R
import com.example.ntufapp.data.ntufappInfo.Companion.dTag
import com.example.ntufapp.layout.DownloadUploadJsonScreen
import com.example.ntufapp.layout.NewSurveyScreen
import com.example.ntufapp.layout.ResultDisplayScreen
import com.example.ntufapp.layout.SaveScreen
import com.example.ntufapp.ui.widget.NavigationItem
import com.example.ntufapp.ui.widget.dialog.GeneralConfirmDialog
import kotlinx.coroutines.CoroutineScope
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
            title = "首頁",
            selectedIcon = Icons.Filled.Home,
            unselectedIcon = Icons.Outlined.Home,
            onClick = {
                scope.launch {
                    drawerState.close()
                    navController.navigate(Screens.Start.name)
                }
            },
        ),
        NavigationItem(
            title = "上傳 & 下載JSON頁面",
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
                    NavigationDrawerItemWithDialog(
                        item = item,
                        id = id,
                        selectedItemIndex = selectedItemIndex,
                        navController = navController,
                        drawerState = drawerState,
                        scope = scope
                    )
                }
            }
        },
        drawerState = drawerState,
    ) {
        Scaffold(
            topBar = { NTUEFTopBar { scope.launch { drawerState.open() } } }
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
                            viewModel.setOutputFileName(outputFilename)
                        }
                    )
                }
                // Currently Only ReSurvey
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
                    Log.d("location mid", "first: ${resultState.first.location_mid}")
                    Log.d("location mid", "second: ${resultState.second.location_mid}")

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
                    Log.d("location mid", "location mid_: ${resultState.second.location_mid}")

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

@Composable
fun NavigationDrawerItemWithDialog(
    item: NavigationItem,
    id: Int,
    selectedItemIndex: MutableState<Int>,
    navController: NavHostController,
    drawerState: DrawerState,
    scope: CoroutineScope
) {
    val isShowConfirmDialog = remember { mutableStateOf(false) }

    NavigationDrawerItem(
        label = { Text(text = item.title) },
        selected = (selectedItemIndex.value == id),
        icon = {
            Icon(
                if (selectedItemIndex.value == id) item.selectedIcon else item.unselectedIcon,
                contentDescription = item.title
            )
        },
        onClick = {
            val isNoAffectPage = when (navController.currentDestination?.route) {
                Screens.Start.name, Screens.LoadJson.name -> true
                else -> false
            }

            if (isNoAffectPage) {
                selectedItemIndex.value = id
                scope.launch { drawerState.close() }
                item.onClick()
            } else {
                isShowConfirmDialog.value = true
            }
        },
        modifier = Modifier.padding(16.dp)
    )

    if (isShowConfirmDialog.value) {
        GeneralConfirmDialog(
            reminder = "確定要離開嗎？將會遺失所有未儲存資料！",
            confirmText = stringResource(R.string.leave),
            onDismiss = {
                isShowConfirmDialog.value = false
                scope.launch { drawerState.close() }
            },
            onCancelClick = {
                isShowConfirmDialog.value = false
                scope.launch { drawerState.close() }
            },
            onConfirmClick = {
                selectedItemIndex.value = id
                item.onClick()
                isShowConfirmDialog.value = false
                scope.launch {
                    if (selectedItemIndex.value == 0) {
                        navController.navigate(Screens.Start.name)
                    } else {
                        navController.navigate(Screens.LoadJson.name)
                    }
                    drawerState.close()
                }
            }
        )
    }
}