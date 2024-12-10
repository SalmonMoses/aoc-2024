package me.salmonmoses.utils

private const val escape = '\u001B'
private const val reset = "$escape[0m"

enum class OutputColor(private val code: Int) {
    BLACK(30),
    RED(31),
    GREEN(32),
    YELLOW(33),
    BLUE(34),
    PURPLE(35),
    CYAN(36),
    WHITE(37),

    GRAY(90),
    HIGH_RED(91),
    HIGH_GREEN(92),
    HIGH_YELLOW(93),
    HIGH_BLUE(94),
    HIGH_PURPLE(95),
    HIGH_CYAN(96),
    HIGH_WHITE(97);

    val foreground
        get() = "$escape[${code}m"
    val background
        get() = "$escape[${code + 10}m"
}

object Colored {
    fun foreground(message: Any?, color: OutputColor): String {
        return "${color.foreground}$message$reset"
    }

    fun background(message: Any?, color: OutputColor): String {
        return "${color.background}$message$reset"
    }

    fun backgroundForeground(message: Any?, background: OutputColor, foreground: OutputColor): String {
        return "${background.background}${foreground.foreground}$message$reset"
    }
}