import io.ktor.client.*
import io.ktor.client.features.json.*
import io.ktor.client.features.json.serializer.*
import io.ktor.client.request.*
import io.ktor.http.*
import kotlinx.browser.window

val jsonClient = HttpClient {
    install(JsonFeature) { serializer = KotlinxSerializer() }
}

val endpoint = window.location.origin

suspend fun getMessages(chat_id: Int) : List<MessageItem> {
    return jsonClient.get("$endpoint/api/chat"){
        contentType(ContentType.Application.Json)
        parameter("id", chat_id)
    }
}

suspend fun sendMessage(chat_id: Int, author: String, content: String) : List<MessageItem> {
    return jsonClient.post("$endpoint/api/message"){
        contentType(ContentType.Application.Json)
        body = MessageItem(author, content)
    }
}