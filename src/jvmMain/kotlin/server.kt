
import io.ktor.application.*
import io.ktor.features.*
import io.ktor.html.*
import io.ktor.http.*
import io.ktor.http.cio.websocket.*
import io.ktor.http.content.*
import io.ktor.request.*
import io.ktor.response.*
import io.ktor.routing.*
import io.ktor.samples.chat.*
import io.ktor.serialization.*
import io.ktor.server.engine.*
import io.ktor.server.netty.*
import io.ktor.sessions.*
import io.ktor.util.*
import io.ktor.websocket.*
import kotlinx.coroutines.channels.consumeEach
import kotlinx.html.*
import org.json.JSONObject
import java.time.Duration


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

data class ChatSession(val id: String)


fun Application.main() {
    val db = DbController()
    val ws_server = WsServer()
    db.start()

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
    install(WebSockets) {
        pingPeriod = Duration.ofMinutes(1)
    }

//    intercept(ApplicationCallPipeline.Features) {
//        if (call.sessions.get<ChatSession>() == null) {
//            call.sessions.set(ChatSession(generateNonce()))
//        }
//    }
//    val tag_by_room = ConcurrentHashMap<String, MutableList<WebSocketSession>>()
    routing {
        webSocket("/ws") {
            for(frame in incoming) {
                frame as? Frame.Text ?: continue
                send(frame.readText())
            }
            incoming.consumeEach { frame ->
                if (frame is Frame.Text) {
                    val json = JSONObject(frame.readText())
                    var tag = ""
                    var answer = ""
                    if (!json.has("tag")) {
                        // tag ещё не создан нужно ответить на вопрос и создать комнату
                        val params = JSONObject(frame.readText())
                        val body = params["question"] ?: return@webSocket
                        val payload = mapOf("question" to body)
                        val resp = khttp.get("http://127.0.0.1:8000/getAnswer", params = payload as Map<String, String>)
                        val resp_json = resp.jsonObject
                        if (resp_json.get("status") == "ok") {
                            if (resp_json.has("tag") and resp_json.has("answer")) {
                                tag = resp_json["tag"] as String
                                answer = resp_json["answer"] as String
                                ws_server.create_room(tag as String, this)
                            }
                        } else {
                            this.send(Frame.Text("error"))
                            return@webSocket
                        }
                    }
                    val from = json.get("from")
                    if ((tag != "") and (answer != "")) {
                        ws_server.notify_msg_by_tag(tag as String, from as String, answer as String)
                    } else {
                        val tag = json.get("tag")
                        val msg = json.get("question")
                        ws_server.create_room(tag as String, this)
                        ws_server.notify_msg_by_tag(tag as String, from as String, msg as String)
                    }
                }
            }
        }
        get("/") {
            call.respondHtml(HttpStatusCode.OK, HTML::index)
        }
        route("api") {
            post("nickname") {
                val nickname = call.receive<String>()
                call.respond("OK")
            }
            get("chat") {
                val tag = call.parameters["tag"]
                if (tag == null) {
                    call.respond("ERROR")
                    return@get
                }
                //val messages = db.return_messages(tag)
                val wrap_to_message_ites = mutableListOf<MessageItem>()
//                for (message in messages) {
//                    wrap_to_message_ites.add(MessageItem(message.first, message.second))
//                }

                call.respond(wrap_to_message_ites)
            }
            post("message") {
                val message = call.receive<MessageItem>()
                call.respond(listOf(message))
            }
            get("tag") {
                var id = call.parameters["question"]
                // todo: тут нужно по вопросу получить тэг
                call.respond("random chat tag")
            }
            post("search") {
                val body = call.receive<String>()
                val payload = mapOf("question" to body)
                val resp = khttp.get("http://0.0.0.0:8888/getAnswer", params = payload)
                call.respond(resp)
            }
        }
        static("/static") {
            resources()
        }
    }
}