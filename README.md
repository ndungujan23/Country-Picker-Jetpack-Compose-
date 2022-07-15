# GCountryPicker

A Jetpack compose library that provides a TextField / Dropdown component that allows you to pick a
country or country data.

[![Kotlin Version](https://img.shields.io/badge/Kotlin-v1.5.31-blue.svg)](https://kotlinlang.org)
[![Platform](https://img.shields.io/badge/Platform-Android-green.svg?style=flat)](https://www.android.com/)
[![API](https://img.shields.io/badge/API-21%2B-brightgreen.svg?style=flat)](https://android-arsenal.com/api?level=21)

# Features

- Use component as an icon inside a TextField component
- Our custom InputField that allows you to choose from a list of countries

# 🎬 Preview

| -- | -- | -- | | <img src="/screenshots/1.png" height="500px"/>
| <img src="/screenshots/2.png" height="500px"/>| <img src="/screenshots/3.png" height="500px"/>
| <img src="/screenshots/4.png" height="500px"/>| <img src="/screenshots/5.png" height="500px"/>

## Installation

* Add it in your root build.gradle at the end of repositories:

  ```groovy
  allprojects {
      repositories {
          maven { url 'https://jitpack.io' }
      }
  }
  ```

* Add the dependency in your app's build.gradle file

  ```groovy
  dependencies {
         implementation 'com.gedhafu.countrypicker:countrypicker:1.0.1'
  }
  ```

## Code Sample

1. Wherever you want to implement a countrypicker

```groovy
    GCountryField(
        placeholder = {
            Text(stringResource(R.string.select_country))
        },
        onCountryValueChange = { i -> println(i) }
)
```

1. Wherever you want to implement a call code selector

- Add this component inside the leadingIcon of a TextField Component

```groovy
    GCountryDialCodeDropdown(onCountryValueChange = { i -> println(i) })
```

### All Attributes
------------------------

| Attribute | Description | Default |
| --- | --- | --- |
| `h` |  |  |

## How to Contribute🤝

Whether you're helping us fix bugs, improve the docs, or a feature request, we'd love to have you!
💪 Check out our __[Contributing Guide]__ for ideas on contributing.

## Bugs and Feedback

For bugs, feature requests, and discussion please use __[GitHub Issues]__.

### LICENSE

This project is licensed under the MIT License - see the [LICENSE](LICENSE) file for details

