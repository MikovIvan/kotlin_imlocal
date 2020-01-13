package ru.imlocal.data

import android.text.Html
import org.threeten.bp.format.DateTimeFormatter
import ru.imlocal.data.Constants.Companion.FORMATTER2
import ru.imlocal.data.Constants.Companion.FORMATTER3
import ru.imlocal.data.Constants.Companion.FORMATTER4

class Constants {
    companion object {
        val KEY_RUB = Html.fromHtml("&#8381;").toString()
        val FORMATTER: DateTimeFormatter = DateTimeFormatter.ofPattern("dd.MM.yy")
        val FORMATTER2: DateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")
        val FORMATTER3: DateTimeFormatter = DateTimeFormatter.ofPattern("HH:mm dd.MM.yyyy")
        val FORMATTER4: DateTimeFormatter = DateTimeFormatter.ofPattern("dd.MM.yyyy")
        val FORMATTER5: DateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd")
    }
}

fun String.newDateFormat(): String? {
    return FORMATTER3.format(FORMATTER2.parse(this))
}

fun String.newDateFormat2(): String? {
    return FORMATTER4.format(FORMATTER2.parse(this))
}