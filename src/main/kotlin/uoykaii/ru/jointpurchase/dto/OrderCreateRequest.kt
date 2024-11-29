package uoykaii.ru.jointpurchase.dto

import jakarta.validation.constraints.Min
import java.util.*

data class OrderCreateRequest(
    val itemId: UUID,

    @field:Min(value = 1, message = "Должно быть не менее 1")
    val count: Int
)
