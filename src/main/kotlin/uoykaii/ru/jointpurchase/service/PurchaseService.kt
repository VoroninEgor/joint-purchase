package uoykaii.ru.jointpurchase.service

import jakarta.transaction.Transactional
import org.springframework.stereotype.Service
import uoykaii.ru.jointpurchase.dto.purchase.*
import uoykaii.ru.jointpurchase.entity.Purchase
import uoykaii.ru.jointpurchase.repository.PurchaseRepository
import uoykaii.ru.jointpurchase.util.ImageOwnerType
import uoykaii.ru.jointpurchase.util.PurchaseStatus
import uoykaii.ru.jointpurchase.util.purchaseCanBeStopped
import java.time.LocalDateTime
import java.util.*

@Service
class PurchaseService(
    private val purchaseRepository: PurchaseRepository,
    private val imageService: ImageService,
    private val itemService: ItemService
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

        imageService.upload(purchaseCreateRequest.image, purchase.id!!, ImageOwnerType.PURCHASE)

        return PurchaseCreateResponse(purchase.id!!)
    }

    @Transactional
    fun publish(id: UUID) {
        val purchase = purchaseRepository.findById(id)
            .orElseThrow { throw IllegalArgumentException("Ошибка публикации закупки $id, не найдено") }

        purchase.apply {
            status = PurchaseStatus.PUBLISHED
            publishedDate = LocalDateTime.now()
        }
    }

    fun getPreviewsByStatus(status: PurchaseStatus? = null): PurchasePreviewsListResponse {
        val previews: MutableList<PurchasePreviewResponse> = mutableListOf()

        val purchases = if (status == null) purchaseRepository.findAll() else purchaseRepository.findAllByStatus(status)

        purchases.forEach {
            previews.add(
                PurchasePreviewResponse(
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
                    canBeStopped = purchaseCanBeStopped(it.stopDate, it.status),
                    imageId = imageService.getDownloadId(it.image!!)
                )
            )
        }
        return PurchasePreviewsListResponse(previews)
    }

    fun getById(id: UUID): PurchaseResponse {
        val purchase =
            purchaseRepository.findById(id).orElseThrow { throw IllegalArgumentException("нет такой закупки: $id") }
        print("возврат purchaseResponse для: $purchase")
        return purchase.let {
            PurchaseResponse(
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
                canBeStopped = purchaseCanBeStopped(it.stopDate, it.status),
                imageId = imageService.getDownloadId(it.image!!),
                itemsPreviews = itemService.getPreviewsByPurchase(it)
            )
        }
    }

    @Transactional
    fun stop(id: UUID) {
        val purchase = purchaseRepository.findById(id)
            .orElseThrow { throw IllegalArgumentException("ошибка стопа, закупка: $id не найден") }

        if (!purchaseCanBeStopped(purchase.stopDate, purchase.status)) {
                throw IllegalArgumentException("нельзя стопнуть закупку: $id")
        }
        purchase.status = PurchaseStatus.AWAITING_INVOICE
    }

    @Transactional
    fun extend(id: UUID, request: PurchaseExtendRequest) {
        val purchase = purchaseRepository.findById(id)
            .orElseThrow { throw IllegalArgumentException("ошибка продления, закупка: $id не найден") }

        if (purchase.stopDate == null || purchase.stopDate!!.isBefore(request.stopDate)) {
            purchase.stopDate = request.stopDate
        } else {
            throw IllegalArgumentException("нельзя продлить закупку: $id")
        }
    }
}
