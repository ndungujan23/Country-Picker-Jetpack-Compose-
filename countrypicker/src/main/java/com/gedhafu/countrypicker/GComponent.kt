package com.gedhafu.countrypicker

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.PressInteraction
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material.icons.rounded.ArrowBack
import androidx.compose.material.icons.rounded.Clear
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import com.gedhafu.countrypicker.model.*


@ExperimentalComposeUiApi
@Composable
fun GCountryDialCodeField(
    modifier: Modifier = Modifier,
    showDialCode: Boolean = true,
    enabled: Boolean = true,
    dialogTopBarElevation: Dp = 0.dp,
    dialogTopBarColor: Color = MaterialTheme.colors.surface,
    dialogTopBarContentColor: Color = MaterialTheme.colors.onSurface,
    selectedCountryISO: String = "",
    onCountryValueChange: (Map<String, String?>) -> Unit,
) {
    val context = LocalContext.current

    var showDialog by remember { mutableStateOf(false) }
    var selected by remember { mutableStateOf<GCountry?>(null) }
    val interactionSource = remember { MutableInteractionSource() }

    LaunchedEffect(1) {
        selected = GCountry().getRawCountries(context).find {
            it.iso_2.contentEquals(selectedCountryISO, true)
                .or(it.iso_3.contentEquals(selectedCountryISO, true))
        }
    }

    val onSelect: (GCountry?) -> Unit = {
        showDialog = false
        selected = it
        onCountryValueChange.invoke(it?.toMap() ?: GCountry().toMap())
    }

    Column(
        modifier = modifier
            .clickable(
                interactionSource = interactionSource,
                indication = null
            ) { if (enabled) showDialog = true },
    ) {
        Row(
            horizontalArrangement = Arrangement.spacedBy(4.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Image(
                painterResource(selected?.getFlag() ?: R.drawable.ic_globe),
                contentDescription = null,
                modifier = Modifier.width(35.dp),
            )
            if (showDialCode && !selected?.getDialCode().isNullOrEmpty()) {
                Text(
                    text = selected?.getDialCode().orEmpty(),
                    fontWeight = FontWeight.Bold,
                )
            }
            Icon(Icons.Default.ArrowDropDown, null)
        }
    }

    CountryPickerDialog(
        showDialog,
        showDialCode,
        selected,
        dialogTopBarElevation,
        dialogTopBarColor,
        dialogTopBarContentColor,
        onSelect,
    )
}


@ExperimentalComposeUiApi
@Composable
fun GCountryField(
    modifier: Modifier = Modifier,
    showDialCode: Boolean = true,
    enabled: Boolean = true,
    label: @Composable (() -> Unit)? = null,
    placeholder: @Composable (() -> Unit)? = null,
    trailingIcon: @Composable (() -> Unit)? = null,
    dialogTopBarElevation: Dp = 0.dp,
    dialogTopBarColor: Color = MaterialTheme.colors.surface,
    dialogTopBarContentColor: Color = MaterialTheme.colors.onSurface,
    textStyle: TextStyle = LocalTextStyle.current,
    shape: Shape = MaterialTheme.shapes.small,
    colors: TextFieldColors = TextFieldDefaults.outlinedTextFieldColors(),
    isError: Boolean = false,
    selectedCountryISO: String = "",
    onCountryValueChange: (Map<String, String?>) -> Unit,
) {
    val context = LocalContext.current

    var showDialog by remember { mutableStateOf(false) }
    var selected by remember { mutableStateOf<GCountry?>(null) }
    var value by remember { mutableStateOf(TextFieldValue()) }

    val onSelect: (GCountry?) -> Unit = {
        showDialog = false
        selected = it
        value = TextFieldValue(it?.name.orEmpty())
        onCountryValueChange.invoke(it?.toMap() ?: GCountry().toMap())
    }

    LaunchedEffect(1) {
        selected = GCountry().getRawCountries(context).find {
            it.iso_2.contentEquals(selectedCountryISO, true)
                .or(it.iso_3.contentEquals(selectedCountryISO, true))
        }
        value = TextFieldValue(selected?.name.orEmpty())
    }

    OutlinedTextField(
        modifier = modifier,
        textStyle = textStyle,
        label = label,
        placeholder = placeholder,
        value = value,
        onValueChange = {},
        singleLine = true,
        maxLines = 1,
        leadingIcon = {
            Image(
                painterResource(selected?.getFlag() ?: R.drawable.ic_globe),
                null,
                Modifier
                    .size(46.dp)
                    .padding(8.dp),
            )
        },
        trailingIcon = trailingIcon,
        isError = isError,
        enabled = enabled,
        readOnly = true,
        interactionSource = remember { MutableInteractionSource() }
            .also { interactionSource ->
                LaunchedEffect(interactionSource) {
                    interactionSource.interactions.collect {
                        if (it is PressInteraction.Release) {
                            if (enabled)
                                showDialog = true
                        }
                    }
                }
            },
        shape = shape,
        colors = colors
    )

    CountryPickerDialog(
        showDialog,
        showDialCode,
        selected,
        dialogTopBarElevation,
        dialogTopBarColor,
        dialogTopBarContentColor,
        onSelect,
    )
}


@ExperimentalComposeUiApi
@Composable
private fun CountryPickerDialog(
    open: Boolean = false,
    showDialCode: Boolean = true,
    selectedCountry: GCountry? = null,
    dialogTopBarElevation: Dp = 0.dp,
    dialogTopBarColor: Color = MaterialTheme.colors.surface,
    dialogTopBarContentColor: Color = MaterialTheme.colors.onSurface,
    onClose: (GCountry?) -> Unit,
) {
    val ctx = LocalContext.current

    val countriesList = GCountry().getRawCountries(ctx)
    var list by remember { mutableStateOf(countriesList) }

    var selected by remember { mutableStateOf(selectedCountry) }
    var query by remember { mutableStateOf(TextFieldValue()) }

    val onSelectCountry: (GCountry) -> Unit = {
        selected = it
        onClose.invoke(it)
    }

    val onSearchChanged: (TextFieldValue) -> Unit = {
        query = it
        list = countriesList.searchCountry(ctx, it.text)
    }

    val onSearchClear: () -> Unit = {
        query = TextFieldValue()
        list = countriesList
    }

    val onDismissDialog: () -> Unit = {
        onSearchClear.invoke()
        onClose.invoke(selected)
    }

    if (open) Dialog(
        onDismissRequest = onDismissDialog,
        properties = DialogProperties(
            dismissOnBackPress = true,
            dismissOnClickOutside = true,
            usePlatformDefaultWidth = false,
        ),
    ) {
        Scaffold(
            topBar = {
                TopAppBar(
                    title = {
                        BasicTextField(
                            modifier = Modifier
                                .background(
                                    MaterialTheme.colors.surface,
                                    MaterialTheme.shapes.small,
                                )
                                .fillMaxWidth(),
                            value = query,
                            onValueChange = onSearchChanged,
                            singleLine = true,
                            cursorBrush = SolidColor(MaterialTheme.colors.primary),
                            textStyle = LocalTextStyle.current,
                            decorationBox = { innerTextField ->
                                Box(Modifier.fillMaxWidth()) {
                                    if (query.text.isEmpty())
                                        Text(
                                            stringResource(R.string.search),
                                            style = LocalTextStyle.current
                                        )
                                    else innerTextField()
                                }
                            }
                        )
                    },
                    navigationIcon = {
                        IconButton(onClick = onDismissDialog) {
                            Icon(
                                imageVector = Icons.Rounded.ArrowBack,
                                contentDescription = "Back"
                            )
                        }
                    },
                    actions = {
                        if (query.text.isNotEmpty()) IconButton(onClick = onSearchClear) {
                            Icon(
                                imageVector = Icons.Rounded.Clear,
                                contentDescription = "Clear"
                            )
                        }
                    },
                    backgroundColor = dialogTopBarColor,
                    contentColor = dialogTopBarContentColor,
                    elevation = dialogTopBarElevation,
                )
            }
        ) {
            it.calculateTopPadding()
            LazyColumn(Modifier.fillMaxSize()) {
                items(list.size) { index ->
                    val country = list.elementAt(index)
                    Card(
                        Modifier
                            .fillMaxWidth()
                            .clickable { onSelectCountry.invoke(country) }
                    ) {
                        Row(
                            Modifier
                                .fillMaxWidth()
                                .padding(18.dp),
                            Arrangement.Start,
                            Alignment.CenterVertically
                        ) {
                            Row(
                                Modifier.weight(1f),
                                Arrangement.Start,
                                Alignment.CenterVertically
                            ) {
                                Image(
                                    painterResource(country.getFlag()),
                                    null,
                                    Modifier.size(25.dp),
                                )
                                Text(
                                    stringResource(country.getCountryName()),
                                    Modifier.padding(horizontal = 18.dp)
                                )
                            }
                            if (showDialCode) Text(country.getDialCode())
                        }
                    }
                }
            }
        }
    }
}
