package uoykaii.ru.jointpurchase.configuration

import com.hubspot.jackson.datatype.protobuf.ProtobufModule
import org.springframework.boot.autoconfigure.jackson.Jackson2ObjectMapperBuilderCustomizer
import org.springframework.context.annotation.Configuration
import org.springframework.http.converter.json.Jackson2ObjectMapperBuilder

@Configuration
class JacksonConfig : Jackson2ObjectMapperBuilderCustomizer {
    override fun customize(jacksonObjectMapperBuilder: Jackson2ObjectMapperBuilder) {
        jacksonObjectMapperBuilder.modulesToInstall(ProtobufModule())
    }
}