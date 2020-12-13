
import kotlinext.js.jsObject
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.launch
import react.*
import react.dom.*

private val scope = MainScope()

val app = functionalComponent<RProps> {
    val (answers, setAnswers) = useState(emptyList<String>())

    useEffect(dependencies = listOf()) {
        scope.launch {
            setAnswers(searchAnswers(""))
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

        main (classes = "main mdl-layout__content"){
            div (classes = "mdl-grid"){
                div (classes = "mdl-cell mdl-cell--2-col mdl-cell--hide-tablet mdl-cell--hide-phone"){}
                div(classes = "mdl-color--white mdl-shadow--4dp content mdl-color-text--grey-800 mdl-cell mdl-cell--8-col") {

                    div (classes = "mdl-card__title light-title") {
                        h2(classes = "mdl-card__title-text") {
                            +"Chat"
                        }
                    }

                    div (classes = "mdl-card mdl-cell mdl-cell--12-col") {
                        div (classes = "mdl-card__supporting-text mdl-grid mdl-grid--no-spacing message-line") {
                            div (classes = "section__circle-container mdl-cell mdl-cell--2-col mdl-cell--1-col-phone"){
                                i (classes = "fas fa-user-circle user-icon"){}
                            }
                            div (classes = "section__text mdl-cell mdl-cell--10-col-desktop mdl-cell--6-col-tablet mdl-cell--3-col-phone"){
                                h5 {
                                    +"Lorem ipsum dolor sit amet"
                                }
                                +"Dolore ex deserunt aute fugiat aute nulla ea sunt aliqua nisi cupidatat eu. Duis nulla tempor do aute et eiusmod velit exercitation nostrud quis"
                            }
                        }
                    }

                    div (classes = "mdl-card mdl-cell mdl-cell--12-col") {
                        div (classes = "mdl-card__supporting-text mdl-grid mdl-grid--no-spacing message-line") {
                            div (classes = "section__circle-container mdl-cell mdl-cell--2-col mdl-cell--1-col-phone"){
                                i (classes = "fas fa-user-circle user-icon"){}
                            }
                            div (classes = "section__text mdl-cell mdl-cell--10-col-desktop mdl-cell--6-col-tablet mdl-cell--3-col-phone"){
                                h5 {
                                    +"Lorem ipsum dolor sit amet"
                                }
                                +"Dolore ex deserunt aute fugiat aute nulla ea sunt aliqua nisi cupidatat eu. Duis nulla tempor do aute et eiusmod velit exercitation nostrud quis"
                            }
                        }
                    }

                    div (classes = "mdl-card mdl-cell mdl-cell--12-col") {
                        div (classes = "mdl-card__supporting-text mdl-grid mdl-grid--no-spacing message-line") {
                            div (classes = "section__circle-container mdl-cell mdl-cell--2-col mdl-cell--1-col-phone"){
                                i (classes = "fas fa-user-circle user-icon"){}
                            }
                            div (classes = "section__text mdl-cell mdl-cell--10-col-desktop mdl-cell--6-col-tablet mdl-cell--3-col-phone"){
                                h5 {
                                    +"Lorem ipsum dolor sit amet"
                                }
                                +"Dolore ex deserunt aute fugiat aute nulla ea sunt aliqua nisi cupidatat eu. Duis nulla tempor do aute et eiusmod velit exercitation nostrud quis"
                            }
                        }
                    }


                    div (classes = "center light-title") {
                        child(
                            InputComponent,
                            props = jsObject {
                                onSubmit = { input ->
                                    scope.launch {
                                        setAnswers(searchAnswers(input))
                                    }
                                }
                            }
                        )
                    }

//                    div (classes = "mdl-card--border") {
//                        ul (classes = "mdl-list") {
//                            answers.forEach { item ->
//                                li (classes = "mdl-list__item") {
//                                    span (classes = "mdl-list__item-primary-content") {
//                                        +item
//                                    }
//                                }
//                            }
//                        }
//                    }
                }
            }
        }
    }
}