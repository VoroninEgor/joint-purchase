package uoykaii.ru.jointpurchase.dto.order

import java.util.*

data class OrderCreateRequest(
    val itemId: UUID,
    val count: UUID
)
