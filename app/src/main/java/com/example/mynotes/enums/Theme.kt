package com.example.mynotes.enums

enum class Theme(val value: Int) {
    DARK(
        1
    ),
    LIGHT(
        0
    );
    companion object {
        const val DARK = "DARK_THEME"
    }
}