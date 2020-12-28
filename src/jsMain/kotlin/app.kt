
import kotlinext.js.jsObject
import kotlinx.browser.window
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.launch
import react.*
import react.dom.*


private val scope = MainScope()

val app = functionalComponent<RProps> {
    val (messages, setMessages) = useState(listOf<MessageItem>())
    val (tag, setTag) = useState("")
    val (loggedIn, setLoggedIn) = useState(false)
    val (nickname, setNickname) = useState("")

    useEffect {
        GlobalScope.launch {
            window.setInterval(setMessages(getMessages(tag)), 1000)
        }
    }

    div (classes = "mdl-layout mdl-js-layout mdl-layout--fixed-header"){
        header (classes = "mdl-layout__header header") {
            div (classes = "mdl-layout__header-row mdl-color--primary-dark"){
                span (classes = "mdl-layout-title"){
                    +"Kotlin course work"
                }
            }
            div (classes = "mdl-layout__header-row"){}
            div (classes = "mdl-layout__header-row"){}
            div (classes = "mdl-layout__header-row"){}
            div (classes = "mdl-layout__header-row"){}
            div (classes = "mdl-layout__header-row"){}
        }

        main (classes = "main mdl-layout__content no-scroll"){
            div (classes = "full-height mdl-grid") {
                if (loggedIn) {
                    div(classes = "mdl-cell mdl-cell--2-col mdl-cell--hide-tablet mdl-cell--hide-phone") {}
                    div(classes = "grid-d full-height mdl-color--white mdl-shadow--4dp mdl-color-text--grey-800 mdl-cell mdl-cell--8-col") {
                        div(classes = "mdl-card__title light-title") {
                            h2(classes = "mdl-card__title-text") {
                                +"Chat"
                            }
                            span(classes = "chat-tag mdl-chip") {
                                span(classes = "mdl-chip__text") {
                                    +tag
                                }
                            }
                        }
                        div(classes = "content") {
                            messages.forEach { item ->
                                div(classes = "message-container mdl-card mdl-cell mdl-cell--12-col") {
                                    div(classes = "mdl-card__supporting-text mdl-grid mdl-grid--no-spacing message-line") {
                                        div(classes = "section__circle-container mdl-cell mdl-cell--2-col mdl-cell--1-col-phone") {
                                            i(classes = "fas fa-user-circle user-icon") {}
                                        }
                                        div(classes = "section__text mdl-cell mdl-cell--10-col-desktop mdl-cell--6-col-tablet mdl-cell--3-col-phone") {
                                            h5 {
                                                +item.author
                                            }
                                            +item.content
                                        }
                                    }
                                }
                            }
                        }
                        div(classes = "light-title") {
                            child(
                                InputComponent,
                                props = jsObject {
                                    onSubmit = { input ->
                                        scope.launch {
                                            val gotMessages = sendMessage(nickname, input, tag)
                                            setMessages(gotMessages)
                                        }
                                    }
                                }
                            )
                        }
                    }
                } else {
                    div(classes = "mdl-color--white mdl-shadow--4dp mdl-color-text--grey-800 center card") {
                        div(classes = "mdl-card__title light-title") {
                            h2(classes = "mdl-card__title-text") {
                                +"Log in"
                            }
                        }
                        div(classes = "center-full") {
                            child(
                                NicknameInputComponent,
                                props = jsObject {
                                    onSubmit = { input ->
                                        scope.launch {
                                            setNickname(input)
                                            setLoggedIn(true)
                                        }
                                    }
                                }
                            )
                        }
                    }
                }
            }
        }
    }
}