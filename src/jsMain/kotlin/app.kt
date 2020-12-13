
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
    div(classes = "mdl-card mdl-card-wide mdl-shadow--2dp") {

        div (classes = "mdl-card__title") {
            h2(classes = "mdl-card__title-text") {
                +"Search on Stackoverflow"
            }
        }

        div (classes = "center") {
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

        div (classes = "mdl-card--border") {
            ul (classes = "mdl-list") {
                answers.forEach { item ->
                    li (classes = "mdl-list__item") {
                        span (classes = "mdl-list__item-primary-content") {
                            +item
                        }
                    }
                }
            }
        }
    }
}