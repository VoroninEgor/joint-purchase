package uoykaii.ru.jointpurchase.controller

import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import uoykaii.ru.jointpurchase.service.MinioService

@RestController
@RequestMapping
class ImageController(val minioService: MinioService) {

    @GetMapping("/image/{id}", produces = ["image/png"])
    fun getImage(@PathVariable id: String): ByteArray {
        return minioService.download(id)
    }
}
