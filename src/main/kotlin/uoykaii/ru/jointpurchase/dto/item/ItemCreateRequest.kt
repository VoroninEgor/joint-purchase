package uoykaii.ru.jointpurchase.dto.item

import org.springframework.web.multipart.MultipartFile
import java.util.UUID

data class ItemCreateRequest(
    val purchaseId: UUID,
    val name: String,
    val description: String,
    val price: Double,
    val type: String,
    val images: List<MultipartFile>
)
