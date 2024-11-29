package uoykaii.ru.jointpurchase.util

import java.time.LocalDateTime


fun getFileSuffix(fileName: String): String? {
    return fileName.substringAfterLast('.', "").takeIf { it.isNotEmpty() }
}

fun purchaseCanBeStopped(stopDate: LocalDateTime?, status: PurchaseStatus?): Boolean =
    status == PurchaseStatus.PUBLISHED && stopDate?.isBefore(LocalDateTime.now()) ?: false

fun canEditOrder(status: OrderStatus?): Boolean =
    status == OrderStatus.NEW