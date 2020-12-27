import io.ktor.client.*
import io.ktor.client.features.json.*
import io.ktor.client.features.json.serializer.*
import io.ktor.client.features.websocket.*
import io.ktor.client.request.*
import io.ktor.http.*
import kotlinx.browser.window

val jsonClient = HttpClient {
    install(JsonFeature) { serializer = KotlinxSerializer() }
}

val endpoint = window.location.origin

suspend fun getMessages(chat_id: Int) : MutableList<MessageItem> {
    return jsonClient.get("$endpoint/api/chat"){
        contentType(ContentType.Application.Json)
        parameter("id", chat_id)
    }
}

suspend fun getTag(question: String) : String {
    return jsonClient.get("$endpoint/api/tag"){
        contentType(ContentType.Application.Json)
        parameter("question", question)
    }
}

suspend fun sendMessage(chat_id: Int, author: String, content: String) : List<MessageItem> {
    return jsonClient.post("$endpoint/api/message"){
        contentType(ContentType.Application.Json)
        body = MessageItem(author, content)
    }
}

suspend fun sendNickname(nickname: String) {
    return jsonClient.post("$endpoint/api/nickname"){
        contentType(ContentType.Application.Json)
        body = nickname
    }
}

suspend fun createWebsocket() {
    return jsonClient.ws("$endpoint/ws"){
    }
}