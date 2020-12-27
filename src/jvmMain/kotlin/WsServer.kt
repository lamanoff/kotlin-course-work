package io.ktor.samples.chat

import io.ktor.http.cio.websocket.*
import org.json.JSONObject
import java.util.*
import java.util.concurrent.ConcurrentHashMap
import java.util.concurrent.CopyOnWriteArrayList
import java.util.concurrent.atomic.AtomicInteger


class WsServer {
    val usersCounter = AtomicInteger()
    val tagRoom = ConcurrentHashMap<String, ConcurrentHashMap<String, String>>()
    val memberNames = ConcurrentHashMap<String, String>()
    val lastMessages = LinkedList<String>()
    val members = ConcurrentHashMap<String, MutableList<WebSocketSession>>()
    val tag_sockets = ConcurrentHashMap<String, MutableList<WebSocketSession>>()

    suspend fun memberJoin(member: String, tag_chat: String?, socket: WebSocketSession) {
        if (tag_chat == null) {
         // юзер законнектился, но не по тегу. Ждем пока он задаст вопрос


        } else {
            val name = memberNames.computeIfAbsent(member) { "user${usersCounter.incrementAndGet()}" }
            val list = members.computeIfAbsent(member) { CopyOnWriteArrayList<WebSocketSession>() }
            list.add(socket)
//            if (list.size == 1) {
//                broadcast("server", "Member joined: $name.")
//            }

            val messages = synchronized(lastMessages) { lastMessages.toList() }
            for (message in messages) {
                socket.send(Frame.Text(message))
            }
        }
    }

    suspend fun notify_msg_by_tag(tag: String, from_name: String, message: String) {
        val sockets_by_tag = this.tag_sockets.get(tag) ?: return
        println("notify: ${sockets_by_tag}")
        for (sock in sockets_by_tag){
            val json = JSONObject(hashMapOf("tag" to tag, "author" to from_name, "content" to message) as Map<String, Any>?)
            sock.send(Frame.Text(json.toString()))
        }
    }

    fun create_room(tag_room: String, socket: WebSocketSession) {
        if (this.tag_sockets.containsKey(tag_room)) {
            this.tag_sockets[tag_room]?.add(socket)
        } else {
            this.tag_sockets[tag_room] = mutableListOf(socket)
        }
    }

}