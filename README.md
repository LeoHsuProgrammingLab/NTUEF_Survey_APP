# NTUEF_Survey_APP
## The Problem
Traditional forestry surveys often rely on manual, paper-based data collection, which is time-consuming, inefficient for data processing, and prone to human error.

## The Solution
This project is an Android tablet application created to solve these challenges by providing a robust digital tool for field data entry. Developed in partnership with forestry experts at the National Taiwan University Experimental Forest (NTUEF), the app is designed to:
* **Increase Efficiency:** Streamline data collection and automate processing.
* **Improve Accuracy:** Reduce errors associated with manual data entry and transcription with accuracy check.

## Key Features
* **Go Paperless:** Eliminates the tedious and error-prone process of manually keying in field notes into spreadsheets after a survey.
* **Real-Time Data Validation:** When re-surveying a plot, the app automatically validates new tree measurements against historical data, flagging significant deviations to ensure longitudinal data quality.
* **Seamless Data Export & Sync:** Automatically generates a structured JSON file and syncs all data to a remote database, ensuring it is centralized, secure, and ready for analysis.

## Getting Started
* **Android Studio Version**: `Android Studio Flamingo | 2022.2.1 Patch 2`
* **Kotlin Version**: `1.9.10`
* **Other Version**: For a complete and up-to-date list of all dependencies and their specific versions, please refer to the `app/build.gradle` file in the project.
* Step by Step:
  * Unzip the project file
  * Open the project in Android Studio
  * Create your own `local.properties` with the following content:
    ```Kotlin
    BASE_URL = "<BASE_URL>"
    API_PARAMS_TOKEN = "<API_PARAMS_TOKEN>"
    CATALOGUE_API_KEY = "<CATALOGUE_API_KEY>"
    PLOT_API_KEY = "<PLOT_API_KEY>"
    CODE_API_KEY = "<CODE_API_KEY>"
    CREATE_API_KEY = "<CREATE_API_KEY>"
    ```
  * "Gradle Sync" in Android Studio
  * Run the app on an Android device or emulator

## APP Usage Pipeline
* "下載樣區資料" -> "選擇樣區" -> "下載樣區內樹木資料" -> "開始調查" -> "輸入樹木資料" -> "完成調查並上傳資料"

## Author
* **Chih-Chuan (Leo) Hsu** - A Master's student in Computer Science and Engineering at Texas A&M University with a background in Forestry and Environmental Science.
* This application was originally conceived and built during my Master's studies at National Taiwan University.
