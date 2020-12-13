import io.ktor.client.*
import io.ktor.client.features.json.*
import io.ktor.client.features.json.serializer.*
import io.ktor.client.request.*
import io.ktor.http.*
import kotlinx.browser.window

val jsonClient = HttpClient {
    install(JsonFeature) { serializer = KotlinxSerializer() }
}

suspend fun searchAnswers(question: String) : List<String> {
    return jsonClient.post(window.location.origin + "/api/search"){
        contentType(ContentType.Application.Json)
        body = question
    }
}