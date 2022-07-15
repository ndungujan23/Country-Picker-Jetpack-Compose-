package com.gedhafu.countrypicker.ui.component

import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.platform.LocalContext
import com.gedhafu.countrypicker.model.GCountry
import com.gedhafu.countrypicker.model.getRawCountries
import java.util.*

@Composable
fun Greeting(name: String) {
    val a = GCountry().getRawCountries(LocalContext.current)

    LaunchedEffect(1) {
        val locales: Set<String> = Locale.getISOCountries().toSet()
        val aa: List<Locale> = locales.map { Locale("", it) }
        println(aa.size)

        println(a)
    }

    Text(text = "Hello $name!")
}
