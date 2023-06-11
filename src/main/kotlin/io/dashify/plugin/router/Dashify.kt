package io.dashify.plugin.router

import io.dashify.plugin.util.ConfigHandler
import io.dashify.plugin.manager.PlayerManager
import io.dashify.plugin.manager.WorldManager
import io.dashify.plugin.DashifyPluginMain.Companion.plugin
import io.dashify.plugin.manager.RuntimeManager
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
            if (!checkIsEnabled()) return@get call.respond(HttpStatusCode.fromValue(418), "I'm a tea pot :3")
            call.response.status(HttpStatusCode.OK)
        }

        get("/worlds") {
            if (!checkIsEnabled()) return@get call.respond(HttpStatusCode.fromValue(418), "I'm a tea pot :3")
            call.respond(HttpStatusCode.OK, WorldManager.getWorldsList())
        }
        get("/worlds/{uuid}") {
            if (!checkIsEnabled()) return@get call.respond(HttpStatusCode.fromValue(418), "I'm a tea pot :3")

            val result = WorldManager.getWorldInfo(call.parameters["uuid"]!!)
            if (result["error"] == null) { call.respond(HttpStatusCode.OK, result) }
            else { call.respond(result["statusCode"] as HttpStatusCode, result) }
        }

        get("/players") {
            if (!checkIsEnabled()) return@get call.respond(HttpStatusCode.fromValue(418), "I'm a tea pot :3")
            call.respond(HttpStatusCode.OK, PlayerManager.getPlayerList())
        }
        get("/players/{uuid}") {
            if (!checkIsEnabled()) return@get call.respond(HttpStatusCode.fromValue(418), "I'm a tea pot :3")

            val result = PlayerManager.getPlayerInfo(call.parameters["uuid"]!!)
            if (result["error"] == null) { call.respond(HttpStatusCode.OK, result) }
            else { call.respond(result["statusCode"] as HttpStatusCode, result) }
        }
        post("/players/{uuid}/kick") {
            if (!checkIsEnabled()) return@post call.respond(HttpStatusCode.fromValue(418), "I'm a tea pot :3")

            val result = PlayerManager.managePlayer("kick", call.parameters["uuid"]!!, call.receiveText())
            if (result["error"] == null) { call.response.status(HttpStatusCode.OK) }
            else { call.respond(result["statusCode"] as HttpStatusCode, result) }
        }
        post("/players/{uuid}/ban") {
            if (!checkIsEnabled()) return@post call.respond(HttpStatusCode.fromValue(418), "I'm a tea pot :3")

            val result = PlayerManager.managePlayer("ban", call.parameters["uuid"]!!, call.receiveText())
            if (result["error"] == null) { call.response.status(HttpStatusCode.OK) }
            else { call.respond(result["statusCode"] as HttpStatusCode, result) }
        }

        get("/tps") {
            if (!checkIsEnabled()) return@get call.respond(HttpStatusCode.fromValue(418), "I'm a tea pot :3")
            call.respond(HttpStatusCode.OK, plugin.server.tps)
        }
        get("/jvm") {
            if (!checkIsEnabled()) return@get call.respond(HttpStatusCode.fromValue(418), "I'm a tea pot :3")
            call.respond(HttpStatusCode.OK, RuntimeManager.getMemory())
        }
    }
}
