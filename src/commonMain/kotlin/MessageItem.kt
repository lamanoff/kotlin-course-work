import kotlinx.serialization.Serializable

@Serializable
data class MessageItem(val author: String, val content: String, val tag: String = "")