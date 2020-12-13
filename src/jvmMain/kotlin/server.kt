
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

fun HTML.index() {
    head {
        title("Stackoverflow search")
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
    }.start(wait = true)
}