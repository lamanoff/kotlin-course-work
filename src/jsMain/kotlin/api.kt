
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

suspend fun getMessages(tag: String) : List<MessageItem> {
    return jsonClient.get("$endpoint/api/messages"){
        contentType(ContentType.Application.Json)
        parameter("tag", tag)
    }
}

suspend fun sendMessage(author: String, content: String, tag: String) : List<MessageItem> {
    return jsonClient.post("$endpoint/api/message"){
        contentType(ContentType.Application.Json)
        body = MessageItem(author, content, tag)
    }
}