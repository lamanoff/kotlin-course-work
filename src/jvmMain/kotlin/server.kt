
import io.ktor.application.*
import io.ktor.features.*
import io.ktor.html.*
import io.ktor.http.*
import io.ktor.http.content.*
import io.ktor.request.*
import io.ktor.response.*
import io.ktor.routing.*
import io.ktor.serialization.*
import io.ktor.server.engine.*
import io.ktor.server.netty.*
import kotlinx.html.*
import java.util.concurrent.ConcurrentHashMap
import kotlin.collections.set


fun HTML.index() {
    head {
        title("Stackoverflow chat")
    }
    body {
        div {
            id = "root"
        }
        script(src = "/static/output.js") {}
        script(src = "https://code.getmdl.io/1.3.0/material.min.js") {}
        script(src = "https://kit.fontawesome.com/864f4e1060.js") {}
        link(href = "https://fonts.googleapis.com/icon?family=Material+Icons", rel = "stylesheet") {}
        link(href = "/static/style.css", rel = "stylesheet") {}
        link(href = "https://code.getmdl.io/1.3.0/material.indigo-pink.min.css", rel = "stylesheet") {}
        link(href = "https://fonts.googleapis.com/css2?family=Roboto:wght@300&display=swap", rel = "stylesheet") {}
    }
}

fun main() {
    embeddedServer(Netty, port = 8080, module = Application::main).start(wait = true)
}

fun ask_bot(question: String): Pair<String, String> {
    val payload = mapOf("question" to question)
    val resp = khttp.get("http://127.0.0.1:8000/getAnswer", params = payload)
    val resp_json = resp.jsonObject
    if (resp_json.get("status") == "ok") {
        if (resp_json.has("tag") and resp_json.has("answer")) {
            return Pair(resp_json["tag"] as String, resp_json["answer"] as String)
        }
    }
    return Pair("error", "bot dont available")
}

fun Application.main() {
    val messages = ConcurrentHashMap<String, MutableList<MessageItem>>()

    install(ContentNegotiation) {
        json()
    }
    install(CORS) {
        method(HttpMethod.Get)
        method(HttpMethod.Post)
        method(HttpMethod.Delete)
        anyHost()
    }
    install(Compression) {
        gzip()
    }
    install(DefaultHeaders)
    install(CallLogging)

    routing {
        get("/") {
            call.respondHtml(HttpStatusCode.OK, HTML::index)
        }
        route("api") {
            get("messages") {
                val tag = call.parameters["tag"]
                call.respond(messages[tag] ?: emptyList<MessageItem>())
            }
            post("message") {
                val message = call.receive<MessageItem>()
                val bot_answer: Pair<String, String> = ask_bot(message.content as String)
                if (!messages.containsKey(message.tag))
                    messages[message.tag] = mutableListOf()
                messages[message.tag]?.add(message)
                messages[message.tag]?.add(MessageItem("Bot Assistant", bot_answer.second, bot_answer.first))
                call.respond(messages[message.tag] ?: emptyList<MessageItem>())
            }
        }
        static("/static") {
            resources()
        }
    }
}