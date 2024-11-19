package uoykaii.ru.jointpurchase.dto.purchase

import uoykaii.ru.jointpurchase.util.PurchaseStatus
import java.time.LocalDateTime
import java.util.*

data class PurchasePreviewResponse(
    val id: UUID,
    val name: String,
    val moneyGoal: Double,
    val collectedMoney: Double,
    val organizationalFee: Short,
    val stopDate: LocalDateTime?,
    val deliveryMethod: String,
    val paymentMethod: String,
    val status: PurchaseStatus?,
    val createdDate: LocalDateTime?,
    val publishedDate: LocalDateTime?,
    val imageId: String
)