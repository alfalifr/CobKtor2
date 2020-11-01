package cob.ktor2

import com.google.gson.Gson
import io.ktor.http.ContentType
import kotlinx.css.LinearDimension
import kotlinx.html.*
import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.SqlExpressionBuilder.eq
import org.jetbrains.exposed.sql.SqlExpressionBuilder.less
import org.jetbrains.exposed.sql.SqlExpressionBuilder.like
import org.jetbrains.exposed.sql.transactions.transaction


object User : Table("user") {
    val id = integer("id").autoIncrement()
    val name = varchar("name", length = 50).primaryKey()
    val email = varchar("email",length = 50)
}
data class Users(val id: Int, val name: String, val email: String)

object DbConnection{

}

fun initDB() {
    val url = "jdbc:mysql://localhost/cob?useUnicode=true&serverTimezone=UTC" //"jdbc:mysql://root:web@localhost:3306/cob?useUnicode=true&serverTimezone=UTC&password=admin"
    val driver = "com.mysql.cj.jdbc.Driver" //"com.mysql.jdbc.Driver" //
    Database.connect(url, driver, user = "root", password = "admin")
}

fun dropTable() = transaction { SchemaUtils.drop(User) }
fun createTable() = transaction { SchemaUtils.create(User) }
fun dummyData(){
    transaction {
        User.join(User, JoinType.FULL, User.email)
        User.insert {
            it[name]= "Pak joko"
            it[email]= "joko@mail.o"
        }
        User.insert {
            it[name]= "Pak eko"
            it[email]= "eko@mail.o"
        }
    }
}

fun getTopuserData(): String {
    var json: String = ""
    transaction {
        val res = User.selectAll().adjustWhere { User.id less 2 }.limit(2) //.orderBy(User.id, false).limit(5)
        val c = ArrayList<Users>()
        for (f in res) {
            c.add(Users(id = f[User.id], name = f[User.name], email = f[User.email]))
        }
        json = Gson().toJson(c)
    }
    return json
}


fun submitUser(id: Int, name: String, email: String): Boolean{
    return transaction {
        User.insert {
            it[this.id]= id
            it[this.name]= name
            it[this.email]= email
        }
        true
    }
}


/*
===================
HTML Section
===================
 */

fun BODY.userForm(){
    val titles= arrayOf("id", "name", "email")
    form{
        action= "http://localhost:8080/submit-db" //?nama=kepencet"
        for(txt in titles){
            div{
                style{ margin= "10px" }
                label{
                    htmlFor= txt
                    +txt
                }
                input {
                    style { marginLeft= LinearDimension("5px") }

                    id= txt +"a"
                    name= txt
                    type= if(txt == "id") InputType.number else InputType.text
                    ContentType.Text.Any
                }
            }
        }
        input{
            type= InputType.submit
            value= "Kirim bro"
        }
    }
}

fun BODY.dbConfig(){
    form{
        action= Config.ENDPOINT +"/config-db-set/drop"
        input {
            type= InputType.submit
            value= "Drop table"
        }
    }
    form{
        action= Config.ENDPOINT +"/config-db-set/create"
        input {
            type= InputType.submit
            value= "Create table"
        }
    }
}