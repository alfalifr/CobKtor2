package cob.ktor2

import kotlinx.html.*
import kotlinx.css.*
//import kotlinx.css.Float //as CFloat

typealias CssFloat= kotlinx.css.Float

//import kotlinx.html.HTML

fun BODY.card(){
    for(i in 0 until 2){
        div{
            style{
                backgroundColor= if(i == 0) Color.blue else Color.red
                width= LinearDimension("50%")
                float= CssFloat.left
                hover {
                    color= Color.green
                    backgroundColor= Color.blue
                }
            }
            span{ +"Halo bro" }
        }
    }
}

fun BODY.btn(){
    button {
        style{
            color= Color.red
            padding= "10px"
            hover {
                color= Color.green
                backgroundColor= Color.blue
            }
        }
        onClick= Window.alert("hai Brooo") //"window.alert('hai')"
        +"Click me"
    }
    div{
        style { color= Color.blue }
        onClick= Console.log("Ok Bro") //window{ print("Ok Bro") }//"window.print()"
        +"Hai click"
    }
}


fun BODY.preset(){
        h1 {
            style {
                color = Color.blue
                backgroundColor= Color.red
//                            fontSize= LinearDimension("500")
            }
            +"HTML" //+"hoho"
//                        span {  }
//                        styleCss {  }
//                        style { color = Color.blue }
        }
        div{
            style{
                backgroundColor= Color.green
                padding= "30px"
                fontSize= LinearDimension("30px")
            }
            span {
                style { color= Color.white }
                +"Halo bro"
            }
        }
        ul {
            for (n in 1..10) {
                li { +"$n" }
            }
        }
}
/*
val HTML.window
    get()= Window
 */

fun HTMLTag.window(f: Window.() -> String): String{
    return f(Window)
}
fun HTMLTag.console(f: Console.() -> String): String{
    return f(Console)
}

object Window{
    const val PREFIX= "window"

    fun alert(msg: String): String = "$PREFIX.alert('$msg')"
    fun print(): String = "$PREFIX.print()"
}
object Console{
    const val PREFIX= "console"

    fun log(msg: String): String = "$PREFIX.log('$msg')"
}