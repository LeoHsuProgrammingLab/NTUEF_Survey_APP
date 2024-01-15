package com.example.ntufapp.data

object DataSource {
    val columnName = listOf(
        "樣樹編號",
        "樹種",
        "胸徑",
        "生長狀態",
        "樹高",
        "目視樹高",
        "分岔樹高",
        "調查日期",
        "營林區",
        "林班",
        "樣區編號",
        "樣區名稱",
        "樣區面積(m2)",
        "樣區型態",
        "TWD97_X",
        "TWD97_Y",
        "海拔(m)",
        "坡度",
        "坡向",
        "調查人員",
        "樹高調查人員"
    )

    val SurveyorList = listOf(
        "John",
        "Leo",
        "Alex",
        "Ethan",
        "Elaine",
        "Anthony",
        "Mason",
        "Bryant",
        "Joseph"
    )

    val HeightSurveyorList = listOf(
        "小許",
        "小黃",
        "小華",
        "小張",
        "小明"
    )

    val SpeciesList = listOf(
        "柳杉",
        "台灣杉",
        "香杉",
        "紅豆杉",
        "杉木",
        "紅檜",
        "扁柏",
        "二葉松",
        "櫸木",
        "烏心石",
        "樟樹",
        "光臘樹",
        "楓香",
        "榔榆",
        "墨點櫻桃"
    )

    val TreeMultiConditionList = listOf(
        "風折",
        "中空",
        "斷尾",
        "分岔",
        "傾斜",
        "頂枯",
        "葉部病害",
        "附生植物",
        "蟲害",
    )

    val TreeSingleConditionList = listOf(
        "正常", // single
        "風倒(死亡)", // single
        "枯立(死亡)", // single
        "死亡", // single
    )

    val SquirrelConditionList = listOf(
        "鼠害早期",
        "鼠害中期",
        "鼠害晚期"
    )
}