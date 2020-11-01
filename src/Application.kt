package cob.ktor2

import io.ktor.application.*
import io.ktor.response.*
import io.ktor.request.*
import io.ktor.routing.*
import io.ktor.http.*
import io.ktor.html.*
import kotlinx.html.*
import kotlinx.css.*
import io.ktor.content.*
import io.ktor.http.content.*
import io.ktor.server.engine.embeddedServer
import io.ktor.server.netty.Netty
import org.jetbrains.exposed.sql.Database
import org.jetbrains.exposed.sql.Table
import org.jetbrains.exposed.sql.select
import org.jetbrains.exposed.sql.selectAll
import java.io.File

fun main(args: Array<String>): Unit = io.ktor.server.netty.EngineMain.main(args)
/*
{
    embeddedServer(Netty, 8080){
        routing {
            get("/db"){
                call.respondText (getTopuserData(), ContentType.Text.Plain)
            }
        }
    }
}
 */

@Suppress("unused") // Referenced in application.conf
@kotlin.jvm.JvmOverloads
fun Application.module1(/*testing: Boolean = false*/) {
    initDB()

    routing {

        get("/db"){
            call.respondText(getTopuserData(), contentType = ContentType.Text.Plain)
        }
        get("/") {
            call.respondText("HELLO WORLD!", contentType = ContentType.Text.Plain)
        }

        get("/add-db"){
            call.respondHtml {
                body{
                    userForm()
                }
            }
        }
        //http://127.0.0.1:8080/submit-db?id=10&name=halo&email=aa
        get("/submit-db{id}{name}{email}"){
            val msg= try{
                val id= call.parameters["id"]!!.toInt()
                val name= call.parameters["name"]
                val email= call.parameters["email"]

                if(id != null && name != null && email != null){
                    submitUser(id, name, email)
                    "Selamat, $name anda terdaftar!"
                } else "Mohon maaf, bbrp data tidak lengkap..."
            }catch (e: Throwable){
                println("/submit-db/{id?}{name?}{email?} ERROR")
                e.printStackTrace()
                "Terjadi error internal"
            }

            call.respondText(msg)
        }
        get("/sapa/{nama}/{email}"){
            val nama= call.parameters["nama"]
            val email= call.parameters["email"]
            call.respondText("Halo bro $nama email $email")
        }
        get("/config-db"){
            call.respondHtml {
                body { dbConfig() }
            }
        }
        get("/config-db-set/{set}"){
            val setKind= call.parameters["set"]
            val msg= when(setKind){
                "create" -> {createTable(); "Berhasil men-create"}
                "drop" -> {dropTable(); "Berhasil nge-drop"}
                else -> "Gagal ):"
            }
            call.respondText(msg)
        }

        get("/html-dsl") {
            call.respondHtml {
                head {
                    link {
                        rel= "stylesheet"
                        href= "/styles2.css"
                    }
                    ruleSet {
                        div{
                            hover {
                                backgroundColor= Color.red
                            }
                        }
                    }
                }
                body{
                    preset()
                    card()
                    btn()
                    div {
                        img {
                            src= "/sitatic/Obito.jpg"
                        }
                    }
                }
            }
        }


        get("/styles.css") {
            call.respondCss {
                body {
                    backgroundColor = Color.red
                }
                p {
                    fontSize = 2.em
                }
                rule("p.myclass") {
                    color = Color.blue
                }
            }
        }

//        file("/halo_bro", "/resources/satatic/Obito.jpg")

        println(File("${System.getProperty("user.dir")}/resources").absolutePath)

        // Static feature. Try to access `/static/ktor_logo.svg`
        static("/static") {
//            staticRootFolder= File("static")
            resources("static")
        }
///*
        static("/sitatic") {
            resources("satatic")
//            staticRootFolder= File("satatic")
//            file("ktor_logo.svg")
        }
// */
    }
}

fun FlowOrMetaDataContent.styleCss(builder: CSSBuilder.() -> Unit) {
    style(type = ContentType.Text.CSS.toString()) {
        +CSSBuilder().apply(builder).toString()
    }
}

fun CommonAttributeGroupFacade.style(builder: CSSBuilder.() -> Unit) {
    this.style = CSSBuilder().apply(builder).toString().trim()
}

suspend inline fun ApplicationCall.respondCss(builder: CSSBuilder.() -> Unit) {
    this.respondText(CSSBuilder().apply(builder).toString(), ContentType.Text.CSS)
}

/*
fun HTML.preset(){
        body {
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
}

// */


