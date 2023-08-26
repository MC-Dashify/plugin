package cc.dashify.plugin.router

import cc.dashify.plugin.DashifyPluginMain.Companion.plugin
import cc.dashify.plugin.manager.DataManager
import cc.dashify.plugin.manager.PlayerManager
import cc.dashify.plugin.util.ConfigHandler
import io.ktor.http.*
import io.ktor.serialization.jackson.*
import io.ktor.server.application.*
import io.ktor.server.plugins.contentnegotiation.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*

/**
 * dashify()
 * dashify router
 */
fun Application.dashify() {
    fun checkIsEnabled() = ConfigHandler["enabled"].toString().toBoolean()

    install(ContentNegotiation) {
        jackson {}
    }

    suspend fun isAuthorized(call: ApplicationCall): Boolean {
        if (!checkIsEnabled()) {
            call.respond(
                HttpStatusCode.fromValue(418),
                hashMapOf("status" to "I'm a tea pot :3", "detail" to "server disabled plugin.")
            )
            return true
        }
        val authHeader = call.request.headers["Authorization"]
        if (authHeader == null) {
            call.respond(
                HttpStatusCode.Unauthorized,
                hashMapOf("status" to "Unauthorized", "detail" to "Authorization header not found.")
            )
            return true
        }
        if (authHeader != "Bearer ${ConfigHandler["key"].toString()}") {
             call.respond(
                HttpStatusCode.Unauthorized,
                hashMapOf("status" to "Unauthorized", "detail" to "Invalid key.")
            )
            return true
        }
        return false
    }

    routing {
        get("/") {
            if(isAuthorized(call)) { return@get }
            call.respond(HttpStatusCode.OK, hashMapOf("status" to "ok"))
        }

        get("/worlds") {
            if(isAuthorized(call)) { return@get }
            call.respond(HttpStatusCode.OK, DataManager.getWorldsList())
        }
        get("/worlds/{uuid}") {
            if(isAuthorized(call)) { return@get }

            val (status, result) = DataManager.getWorldInfo(call.parameters["uuid"]!!)
            call.respond(status as HttpStatusCode, result)
        }

        get("/players") {
            if(isAuthorized(call)) { return@get }
            call.respond(HttpStatusCode.OK, DataManager.getPlayerList())
        }
        get("/players/{uuid}") {
            if(isAuthorized(call)) { return@get }

            val (status, result) = DataManager.getPlayerInfo(call.parameters["uuid"]!!)
            call.respond(status as HttpStatusCode, result)
        }
        get("/players/bans") {
            if(isAuthorized(call)) { return@get }
            call.respond(HttpStatusCode.OK, DataManager.getBannedPlayerList())
        }
        post("/players/{uuid}/kick") {
            if(isAuthorized(call)) { return@post }

            val (status, result) = PlayerManager.managePlayer("kick", call.parameters["uuid"]!!, call.receiveText())
            call.respond(status as HttpStatusCode, result)
        }
        post("/players/{uuid}/ban") {
            if(isAuthorized(call)) { return@post }

            val (status, result) = PlayerManager.managePlayer("ban", call.parameters["uuid"]!!, call.receiveText())
            call.respond(status as HttpStatusCode, result)
        }
        post("/players/{uuid}/pardon"){
            if(isAuthorized(call)) { return@post }

            val (status, result) = PlayerManager.managePlayer("pardon", call.parameters["uuid"]!!, call.receiveText())
            call.respond(status as HttpStatusCode, result)
        }

        get("/stats") {
            if(isAuthorized(call)) { return@get }

            val stats = DataManager.getSysInfo()
            stats["jvm"] = DataManager.getMemory()
            stats["tps"] = plugin.server.tps
            call.respond(HttpStatusCode.OK, stats)
        }
    }
}
