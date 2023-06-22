package cc.dashify.plugin.router

import cc.dashify.plugin.DashifyPluginMain.Companion.plugin
import cc.dashify.plugin.manager.PlayerManager
import cc.dashify.plugin.manager.RuntimeManager
import cc.dashify.plugin.manager.SystemManager
import cc.dashify.plugin.manager.WorldManager
import cc.dashify.plugin.util.ConfigHandler
import io.ktor.http.*
import io.ktor.serialization.jackson.*
import io.ktor.server.application.*
import io.ktor.server.plugins.contentnegotiation.*
import io.ktor.server.plugins.cors.routing.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*

fun Application.dashify() {
    fun checkIsEnabled() = ConfigHandler["enabled"].toString().toBoolean()

    install(CORS) {
        allowMethod(HttpMethod.Options)
        allowMethod(HttpMethod.Put)
        allowMethod(HttpMethod.Delete)
        allowMethod(HttpMethod.Patch)
        allowHeader(HttpHeaders.Authorization)
        allowHeader("Freddy-Fazbear-Street")
        anyHost()
    }

    install(ContentNegotiation) {
        jackson {}
    }

    routing {
        get("/") {
            if (!checkIsEnabled()) return@get call.respond(
                HttpStatusCode.fromValue(418),
                hashMapOf("status" to "I'm a tea pot :3", "detail" to "server disabled plugin.")
            )
            call.respond(HttpStatusCode.OK, hashMapOf("status" to "ok"))
        }

        get("/worlds") {
            if (!checkIsEnabled()) return@get call.respond(
                HttpStatusCode.fromValue(418),
                hashMapOf("status" to "I'm a tea pot :3", "detail" to "server disabled plugin.")
            )
            call.respond(HttpStatusCode.OK, WorldManager.getWorldsList())
        }
        get("/worlds/{uuid}") {
            if (!checkIsEnabled()) return@get call.respond(
                HttpStatusCode.fromValue(418),
                hashMapOf("status" to "I'm a tea pot :3", "detail" to "server disabled plugin.")
            )

            val result = WorldManager.getWorldInfo(call.parameters["uuid"]!!)
            if (result["error"] == null) {
                call.respond(HttpStatusCode.OK, result)
            } else {
                call.respond(result["statusCode"] as HttpStatusCode, result)
            }
        }

        get("/players") {
            if (!checkIsEnabled()) return@get call.respond(
                HttpStatusCode.fromValue(418),
                hashMapOf("status" to "I'm a tea pot :3", "detail" to "server disabled plugin.")
            )
            call.respond(HttpStatusCode.OK, PlayerManager.getPlayerList())
        }
        get("/players/{uuid}") {
            if (!checkIsEnabled()) return@get call.respond(
                HttpStatusCode.fromValue(418),
                hashMapOf("status" to "I'm a tea pot :3", "detail" to "server disabled plugin.")
            )

            val result = PlayerManager.getPlayerInfo(call.parameters["uuid"]!!)
            if (result["error"] == null) {
                call.respond(HttpStatusCode.OK, result)
            } else {
                call.respond(result["statusCode"] as HttpStatusCode, result)
            }
        }
        post("/players/{uuid}/kick") {
            if (!checkIsEnabled()) return@post call.respond(
                HttpStatusCode.fromValue(418),
                hashMapOf("status" to "I'm a tea pot :3", "detail" to "server disabled plugin.")
            )

            val result = PlayerManager.managePlayer("kick", call.parameters["uuid"]!!, call.receiveText())
            if (result["error"] == null) {
                call.response.status(HttpStatusCode.OK)
            } else {
                call.respond(result["statusCode"] as HttpStatusCode, result)
            }
        }
        post("/players/{uuid}/ban") {
            if (!checkIsEnabled()) return@post call.respond(
                HttpStatusCode.fromValue(418),
                hashMapOf("status" to "I'm a tea pot :3", "detail" to "server disabled plugin.")
            )

            val result = PlayerManager.managePlayer("ban", call.parameters["uuid"]!!, call.receiveText())
            if (result["error"] == null) {
                call.response.status(HttpStatusCode.OK)
            } else {
                call.respond(result["statusCode"] as HttpStatusCode, result)
            }
        }
        get("/stats") {
            if (!checkIsEnabled()) return@get call.respond(
                HttpStatusCode.fromValue(418),
                hashMapOf("status" to "I'm a tea pot :3", "detail" to "server disabled plugin.")
            )
            val stats = SystemManager.getSysInfo()
            stats["jvm"] = RuntimeManager.getMemory()
            stats["tps"] = plugin.server.tps
            call.respond(HttpStatusCode.OK, stats)
        }
    }
}
