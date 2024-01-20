package com.example.monpokedex

import android.app.Application
import com.example.monpokedex.data.AppContainer
import com.example.monpokedex.data.DefaultAppContainer

class PokedexApplication : Application() {
    /** AppContainer instance used by the rest of classes to obtain dependencies */
    lateinit var container: AppContainer
    override fun onCreate() {
        super.onCreate()
        container = DefaultAppContainer()
    }
}