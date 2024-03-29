package com.tomczyn.linkding

expect class Settings {
    fun getString(key: String): String?
    fun setString(key: String, value: String)
}

fun Settings.getString(key: LinkdingSettings): String? {
    return getString(key.toString())
}

fun Settings.setString(key: LinkdingSettings, value: String) {
    setString(key.toString(), value)
}

val Settings.isUserLoggedIn: Boolean
    get() = getString(LinkdingSettings.TOKEN)?.isNotEmpty() ?: false

enum class LinkdingSettings {
    HOST, TOKEN
}
