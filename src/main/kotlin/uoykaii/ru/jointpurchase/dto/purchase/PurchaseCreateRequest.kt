package uoykaii.ru.jointpurchase.dto.purchase

import jakarta.validation.constraints.Max
import jakarta.validation.constraints.Min
import org.springframework.web.multipart.MultipartFile
import java.time.LocalDateTime

data class PurchaseCreateRequest(
    val name: String,
    val description: String,
    val moneyGoal: Double,
    @field:Min(0)
    @field:Max(20)
    val organizationalFee: Short,
    val stopDate: LocalDateTime?,
    val deliveryMethod: String,
    val paymentMethod: String,
    val image: MultipartFile
)
