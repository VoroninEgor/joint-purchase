package uoykaii.ru.jointpurchase.service

import jakarta.transaction.Transactional
import org.springframework.stereotype.Service
import uoykaii.ru.jointpurchase.dto.purchase.PurchaseCreateRequest
import uoykaii.ru.jointpurchase.dto.purchase.PurchaseCreateResponse
import uoykaii.ru.jointpurchase.dto.purchase.PurchasePreviewResponse
import uoykaii.ru.jointpurchase.dto.purchase.PurchasePreviewsListResponse
import uoykaii.ru.jointpurchase.entity.Image
import uoykaii.ru.jointpurchase.entity.Purchase
import uoykaii.ru.jointpurchase.repository.PurchaseRepository
import uoykaii.ru.jointpurchase.util.ImageOwnerType
import uoykaii.ru.jointpurchase.util.PurchaseStatus
import java.time.LocalDateTime
import java.util.*
import kotlin.jvm.optionals.getOrElse

@Service
class PurchaseService(
    private val purchaseRepository: PurchaseRepository,
    private val imageService: ImageService
) {

    @Transactional
    fun create(purchaseCreateRequest: PurchaseCreateRequest): PurchaseCreateResponse {
        val purchase: Purchase = Purchase().apply {
            id = UUID.randomUUID()
            name = purchaseCreateRequest.name
            description = purchaseCreateRequest.description
            moneyGoal = purchaseCreateRequest.moneyGoal
            organizationalFee = purchaseCreateRequest.organizationalFee
            stopDate = purchaseCreateRequest.stopDate
            deliveryMethod = purchaseCreateRequest.deliveryMethod
            paymentMethod = purchaseCreateRequest.paymentMethod
            status = PurchaseStatus.DRAFT
            createdDate = LocalDateTime.now()
        }.also { purchaseRepository.save(it) }

        purchaseCreateRequest.run {
            imageService.upload(image, purchase.id!!, ImageOwnerType.PURCHASE)
        }

        return PurchaseCreateResponse(purchase.id!!)
    }

    fun publish(id: UUID) {
        val purchase = purchaseRepository.findById(id).getOrElse { throw IllegalArgumentException("Ошибка публикации закупки $id, не найдено") }

        purchase.apply {
            status = PurchaseStatus.PUBLISHED
            publishedDate = LocalDateTime.now()
        }.also {
            purchaseRepository.save(purchase)
        }
    }

    fun getAllPreviews(): PurchasePreviewsListResponse {
        val previews: MutableList<PurchasePreviewResponse> = mutableListOf()
        purchaseRepository.findAll().forEach{
            previews.add(PurchasePreviewResponse(
                id = it.id!!,
                name = it.name,
                moneyGoal = it.moneyGoal,
                collectedMoney = it.collectedMoney,
                organizationalFee = it.organizationalFee,
                stopDate = it.stopDate,
                deliveryMethod = it.deliveryMethod,
                paymentMethod = it.paymentMethod,
                status = it.status,
                createdDate = it.createdDate,
                publishedDate = it.publishedDate,
                imageId = imageService.getDownloadId(it.image!!)
            ))
        }
        return PurchasePreviewsListResponse(previews)
    }

}
