package com.gedhafu.countrypicker.model

import android.content.Context
import android.util.Log
import com.gedhafu.countrypicker.R
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.google.gson.reflect.TypeToken
import java.io.BufferedReader
import java.io.InputStreamReader
import java.io.StringWriter
import java.lang.reflect.Type

data class GCountry(
    val name: String? = "Afghanistan",
    val dial_code: String = "93",
    val iso_2: String = "af",
    val iso_3: String? = "afg",
    val emoji: String? = null,
    val capital: String? = null,
    val timezone: List<String>? = null,
    val utc_offset: List<String>? = null,
)

fun GCountry.toMap(): Map<String, String?> {
    return mapOf(
        "name" to name,
        "dial_code" to dial_code,
        "iso_2" to iso_2,
        "iso_3" to iso_3,
        "capital" to capital,
    )
}

fun List<GCountry>.searchCountry(ctx: Context, query: String): MutableList<GCountry> {
    return this.filter {
        ctx.resources.getString(it.getCountryName()).trim().contains(query.trim(), true)
    }.toMutableList()
}

fun GCountry.getRawCountries(ctx: Context): List<GCountry> {
    val resourceReader = ctx.resources.openRawResource(R.raw.raw_countries)
    val writer = StringWriter()
    try {
        val reader = BufferedReader(InputStreamReader(resourceReader, "UTF-8"))
        var line: String? = reader.readLine()
        while (line != null) {
            writer.write(line)
            line = reader.readLine()
        }
    } catch (e: Exception) {
        Log.e(
            GCountry::class.java.simpleName,
            "Unhandled exception while using JSONResourceReader $e"
        )
    } finally {
        try {
            resourceReader.close()
        } catch (e: Exception) {
            Log.e(
                GCountry::class.java.simpleName,
                "Unhandled exception while using JSONResourceReader $e"
            )
        }
    }

    val jsonStringList: String = writer.toString()
    return if (jsonStringList.isEmpty()) {
        emptyList()
    } else {
        val rawListType: Type = object : TypeToken<ArrayList<GCountry?>?>() {}.type
        val gson: Gson = GsonBuilder().create()

        gson.fromJson<List<GCountry?>?>(jsonStringList, rawListType).filterNotNull()
    }
}
