package uoykaii.ru.jointpurchase.service

import io.minio.GetObjectArgs
import io.minio.GetObjectResponse
import io.minio.MinioClient
import io.minio.PutObjectArgs
import org.springframework.stereotype.Service
import org.springframework.web.multipart.MultipartFile
import uoykaii.ru.jointpurchase.entity.Image
import uoykaii.ru.jointpurchase.util.getFileSuffix
import java.util.*

@Service
class MinioService(val minioClient: MinioClient) {

    fun upload(multipartImage: MultipartFile): Image {
        val originFileName = multipartImage.originalFilename ?: throw IllegalArgumentException("Файл без имени")
        val imageSuffix = getFileSuffix(originFileName)

        if (imageSuffix == null || imageSuffix !in listOf("png", "jpg")) {
            throw IllegalArgumentException("Неверный формат файла: $imageSuffix. Допустимые форматы: jpg, png.")
        }

        multipartImage.inputStream.use { inputStream ->
            val size = multipartImage.size
            val uuid = UUID.randomUUID()
            val newName = "$uuid.$imageSuffix"

            minioClient.putObject(
                PutObjectArgs.builder()
                    .bucket(BUCKET_NAME)
                    .`object`(newName)
                    .stream(inputStream, size, -1)
                    .build()
            )

            return Image().apply {
                id = uuid
                suffix = imageSuffix
            }
        }
    }

    fun download(id: String): ByteArray {
        println("Поход в minio за: $id")
        return minioClient.getObject(
            GetObjectArgs.builder()
                .bucket(BUCKET_NAME)
                .`object`(id)
                .build()
        ).readBytes()
    }


    companion object {
        const val BUCKET_NAME = "images"
    }
}