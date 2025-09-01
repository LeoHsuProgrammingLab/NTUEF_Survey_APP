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
        "鼠害",
    )

    val TreeSingleConditionList = listOf(
        "正常", // single
        "風倒", // single
        "枯立", // single
    )

    val GrowthCodeList = listOf(
        GrowthCode(code = "0", code_name = "正常", sort = 1, enabled = true),
        GrowthCode(code = "1", code_name = "風倒", sort = 2, enabled = true),
        GrowthCode(code = "2", code_name = "風折", sort = 3, enabled = true),
        GrowthCode(code = "3", code_name = "鼠害", sort = 4, enabled = true),
        GrowthCode(code = "4", code_name = "枯立", sort = 5, enabled = true),
        GrowthCode(code = "5", code_name = "中空", sort = 6, enabled = true),
        GrowthCode(code = "6", code_name = "斷尾", sort = 7, enabled = true),
        GrowthCode(code = "7", code_name = "分岔", sort = 8, enabled = true),
        GrowthCode(code = "8", code_name = "傾斜", sort = 9, enabled = true),
        GrowthCode(code = "9", code_name = "葉部病害", sort = 10, enabled = true),
        GrowthCode(code = "10", code_name = "蟲害", sort = 11, enabled = true),
        GrowthCode(code = "11", code_name = "頂枯", sort = 12, enabled = true),
        GrowthCode(code = "12", code_name = "附生植物", sort = 13, enabled = true)
    )
}

data class GrowthCode(
    val code: String,
    val code_name: String,
    val sort: Int,
    val enabled: Boolean
)