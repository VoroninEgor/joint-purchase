package uoykaii.ru.jointpurchase.security

import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import org.springframework.http.HttpStatus
import org.springframework.stereotype.Component
import org.springframework.web.servlet.HandlerInterceptor
import uoykaii.ru.jointpurchase.client.AuthenticationClient
import uoykaii.ru.jointpurchase.security.SecurityContextHolder.createContext

@Component
class AuthenticationEnrichmentInterceptor(
    private val authClient: AuthenticationClient
) : HandlerInterceptor {
    override fun preHandle(request: HttpServletRequest, response: HttpServletResponse, handler: Any): Boolean =
        runCatching {
            if (request.getHeader("Authorization") != null) {
                val token = request.getHeader("Authorization").replace("Bearer ", "")
                createContext(authClient.getUserByToken(token))
            }
            return super.preHandle(request, response, handler)
        }.getOrElse {
            true
        }
}