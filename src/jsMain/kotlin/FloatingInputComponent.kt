import kotlinx.html.InputType
import kotlinx.html.js.onChangeFunction
import kotlinx.html.js.onSubmitFunction
import org.w3c.dom.HTMLInputElement
import org.w3c.dom.events.Event
import react.RProps
import react.dom.div
import react.dom.form
import react.dom.input
import react.dom.label
import react.functionalComponent
import react.useState

external interface FloatingInputProps : RProps {
    var onSubmit: (String) -> Unit
}

val FloatingInputComponent = functionalComponent<InputProps> { props ->
    val (text, setText) = useState("")

    val submitHandler: (Event) -> Unit = {
        it.preventDefault()
        setText("")
        props.onSubmit(text)
    }

    val changeHandler: (Event) -> Unit = {
        val value = (it.target as HTMLInputElement).value
        setText(value)
    }

    form {
        attrs.onSubmitFunction = submitHandler
        div (classes = "mdl-textfield mdl-js-textfield mdl-textfield--floating-label") {
            input(InputType.text, classes = "mdl-textfield__input") {
                attrs.onChangeFunction = changeHandler
                attrs.value = text
            }
            label(classes = "mdl-textfield__label") {
                +"Nickname"
            }
        }
    }
}