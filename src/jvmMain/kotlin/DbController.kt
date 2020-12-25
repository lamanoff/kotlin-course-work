import java.time.LocalDateTime
import com.zaxxer.hikari.HikariConfig
import com.zaxxer.hikari.HikariDataSource
import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.transactions.transaction


class DbController {
    object Chats: Table() {
        override val tableName: String
            get() = "chat_storage"
        val chat_tag = varchar("tag", 15)
        val from_name = varchar("from_name", 15)
        val message = varchar("message", 50)
        val received_time = varchar("received_time", 50)
        override val primaryKey = PrimaryKey(chat_tag, name="chat_storage_pkey")
    }

    fun start() {
        val dbUrl = "jdbc:postgresql://0.0.0.0:5432/chat"
        val dbUser = "chat"
        val dbPass = "chat"
        val conf = hikari(dbUrl, dbUser, dbPass)
        Database.connect(conf)
        println(LocalDateTime.now().toString().count())
        transaction {
            SchemaUtils.create(Chats)
//            Chats.insert {
//                it[chat_tag] = "c_cpp"
//                it[from_name] = "Andrey"
//                it[message] = "test_message"
//                it[received_time] = LocalDateTime.now().toString()
//            }
        }
    }

    fun return_messages(tag_id: String): List<Pair<String, String>> {
        val filtered_messages = mutableListOf<Pair<String, String>>()
        transaction {
            val messages_by_tag = Chats.select { Chats.chat_tag eq tag_id }.toList()
            for (message in messages_by_tag) {
                filtered_messages.add(Pair(message[Chats.from_name].toString(), message[Chats.message].toString()))
            }
        }
        return filtered_messages
    }

//    fun add_message_by_tag(tag_id: String, from_name: String, message: String) {
//        transaction {
//            Chats.insert {
//                it[chat_tag] = tag_id
//                it[from_name] = from_name
//                it[message] = message
//                it[received_time] = LocalDateTime.now().toString()
//            }
//        }
//    }

    private fun hikari(dbUrl: String, dbUser: String, dbPass: String): HikariDataSource {
        val config = HikariConfig()

        config.driverClassName = "org.postgresql.Driver"
        config.jdbcUrl = dbUrl
        config.username = dbUser
        config.password = dbPass
        config.maximumPoolSize = 3
        config.isAutoCommit = true
        config.transactionIsolation = "TRANSACTION_REPEATABLE_READ"
        config.validate()
        return HikariDataSource(config)
    }
}