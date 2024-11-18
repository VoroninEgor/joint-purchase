package uoykaii.ru.jointpurchase.service

import jakarta.transaction.Transactional
import org.springframework.stereotype.Service
import org.springframework.web.multipart.MultipartFile
import uoykaii.ru.jointpurchase.repository.ImageRepository
import uoykaii.ru.jointpurchase.repository.ItemRepository
import java.util.*


@Service
class ImageService(
    private val imageRepository: ImageRepository,
    private var itemRepository: ItemRepository,
    private val minioService: MinioService
) {

    @Transactional
    fun upload(multipartImage: MultipartFile, itemId: UUID) {
        val image = minioService.upload(multipartImage)

        itemRepository.findById(itemId).ifPresent {
            imageRepository.save(
                image.apply { item = it }
            )
        }
    }
}