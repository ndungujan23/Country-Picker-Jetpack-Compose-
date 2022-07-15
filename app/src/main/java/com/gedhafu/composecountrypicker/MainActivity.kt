package com.gedhafu.composecountrypicker

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import com.gedhafu.composecountrypicker.ui.theme.ComposeCountryPickerTheme
import com.gedhafu.countrypicker.R
import com.gedhafu.countrypicker.ui.component.GCountryCodeField

class MainActivity : ComponentActivity() {
    @OptIn(ExperimentalComposeUiApi::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            ComposeCountryPickerTheme {
                Column(
                    modifier = Modifier.fillMaxSize(),
                    Arrangement.Center,
                    Alignment.CenterHorizontally,
                ) {
                    GCountryCodeField(
                        placeholder = {
                            Text(stringResource(R.string.select_country))
                        },
                        onCountrySelect = { i -> println(i) }
                    )
                }
            }
        }
    }
}

@OptIn(ExperimentalComposeUiApi::class)
@Preview(showBackground = true)
@Composable
fun DefaultPreview() {
    ComposeCountryPickerTheme {
        GCountryCodeField(
            onCountrySelect = { i -> println(i) }
        )
    }
}