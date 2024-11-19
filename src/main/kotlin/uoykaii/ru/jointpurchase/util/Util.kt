package uoykaii.ru.jointpurchase.util

import java.time.LocalDateTime


fun getFileSuffix(fileName: String): String? {
    return fileName.substringAfterLast('.', "").takeIf { it.isNotEmpty() }
}

fun purchaseCanBeStopped(stopDate: LocalDateTime?): Boolean =
    stopDate?.isBefore(LocalDateTime.now()) ?: false
