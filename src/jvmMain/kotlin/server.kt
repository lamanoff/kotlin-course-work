import io.ktor.application.*
import io.ktor.features.*
import io.ktor.html.*
import io.ktor.http.*
import io.ktor.http.content.*
import io.ktor.response.*
import io.ktor.routing.*
import io.ktor.serialization.*
import io.ktor.server.engine.*
import io.ktor.server.netty.*
import kotlinx.html.*
import java.net.URI
import java.net.http.HttpClient
import java.net.http.HttpRequest
import java.net.http.HttpResponse

fun HTML.index() {
    head {
        title("Hello from Ktor!")
    }
    body {
        div {
            +"Hello from Ktor"
        }
        div {
            id = "root"
        }
        script(src = "/static/output.js") {}
    }
}

fun main() {
    embeddedServer(Netty, port = 8080, host = "127.0.0.1") {
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
        routing {
            get("/") {
                call.respondHtml(HttpStatusCode.OK, HTML::index)
            }
            route("api") {
                post("search") {
                    val stub = listOf(
                        "https://www.google.com/",
                        "https://yandex.ru/",
                        "https://www.youtube.com/"
                    )
                    call.respond(stub)
                }
            }
            static("/static") {
                resources()
            }

            get("/answer") {
                val question: String = call.parameters["question"].toString()
                val payload = mapOf("question" to question)
                val r = khttp.get("http://0.0.0.0:8888/getAnswer", params = payload)
                call.respond(r.text)
            }
        }
    }.start(wait = true)
}