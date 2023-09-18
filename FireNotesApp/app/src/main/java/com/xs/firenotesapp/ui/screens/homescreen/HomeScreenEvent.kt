package com.xs.firenotesapp.ui.screens.homescreen

import com.xs.firenotesapp.data.news.NewsData

sealed class HomeScreenEvent {
    data class OnChangeHeadline(val value:String): HomeScreenEvent()
    data class OnChangeDescription(val value:String): HomeScreenEvent()
    data class OnDelete(val newsData: NewsData): HomeScreenEvent()
    object PostNews: HomeScreenEvent()
    object Logout: HomeScreenEvent()

}