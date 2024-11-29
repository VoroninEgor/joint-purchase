package uoykaii.ru.jointpurchase.dto.purchase

import java.time.LocalDateTime

data class PurchaseExtendRequest(
    val stopDate: LocalDateTime
)