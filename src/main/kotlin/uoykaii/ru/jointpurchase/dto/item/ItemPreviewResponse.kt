package uoykaii.ru.jointpurchase.dto.item

import java.time.LocalDateTime
import java.util.*

data class ItemPreviewResponse(
    val id: UUID,
    val name: String,
    val description: String,
    val price: Double,
    val type: String,
    val createdDate: LocalDateTime,
    val imageIds: List<String>
)
