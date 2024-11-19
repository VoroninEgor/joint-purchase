package uoykaii.ru.jointpurchase.service

import jakarta.transaction.Transactional
import org.springframework.stereotype.Service
import org.springframework.web.multipart.MultipartFile
import uoykaii.ru.jointpurchase.entity.Image
import uoykaii.ru.jointpurchase.repository.ImageRepository
import uoykaii.ru.jointpurchase.repository.ItemRepository
import uoykaii.ru.jointpurchase.repository.PurchaseRepository
import uoykaii.ru.jointpurchase.util.ImageOwnerType
import java.time.LocalDateTime
import java.util.*


@Service
class ImageService(
    private val imageRepository: ImageRepository,
    private val minioService: MinioService,
    private val itemRepository: ItemRepository,
    private val purchaseRepository: PurchaseRepository
) {

    @Transactional
    fun upload(multipartImage: MultipartFile, imageOwnerId: UUID, imageOwnerType: ImageOwnerType) {
        val image = minioService.upload(multipartImage)
        image.createdDate = LocalDateTime.now()

        when (imageOwnerType) {
            ImageOwnerType.ITEM ->
                itemRepository.findById(imageOwnerId).ifPresent {
                    imageRepository.save(
                        image.apply { item = it }
                    )
                }

            ImageOwnerType.PURCHASE -> {
                purchaseRepository.findById(imageOwnerId).ifPresent {
                    imageRepository.save(
                        image.apply { purchase = it }
                    )
                }
            }
        }
    }

    fun getDownloadId(image: Image): String {
        return "${image.id}.${image.suffix}"
    }
}
