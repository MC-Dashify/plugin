package io.dashify.plugin.router

import io.dashify.plugin.ConfigHandler
import io.dashify.plugin.DashifyPluginMain
import io.dashify.plugin.PlayerInfoProvider
import io.dashify.plugin.WorldInfoProvider
import io.ktor.http.*
import io.ktor.serialization.jackson.*
import io.ktor.server.application.*
import io.ktor.server.plugins.contentnegotiation.*
import io.ktor.server.plugins.cors.routing.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import java.util.*

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

            call.respond(WorldInfoProvider.getWorldsList())
        }
        get("/worlds/{uuid}") {
            if (!checkIsEnabled()) return@get call.respond(HttpStatusCode.fromValue(418), "I'm a tea pot :3")
            try {
                UUID.fromString(call.parameters["uuid"])
            } catch (e: IllegalArgumentException) {
                call.respond(HttpStatusCode.BadRequest, hashMapOf("error" to "invalid UUID"))
            }

            val worldUuids = DashifyPluginMain.plugin.server.worlds.map { it.uid }.toList()
            if (worldUuids.contains(UUID.fromString(call.parameters["uuid"]))) {
                val result = WorldInfoProvider.getWorldInfo(call.parameters["uuid"]!!)
                if (result["error"] == null) {
                    call.respond(HttpStatusCode.OK, result)
                } else {
                    call.respond(HttpStatusCode.InternalServerError, result)
                }
            } else {
                call.respond(HttpStatusCode.BadRequest, hashMapOf("error" to "invalid UUID"))
            }
        }
        get("/players") {
            if (!checkIsEnabled()) return@get call.respond(HttpStatusCode.fromValue(418), "I'm a tea pot :3")

            call.respond(PlayerInfoProvider.getPlayerList())
        }
        get("/players/{uuid}") {
            if (!checkIsEnabled()) return@get call.respond(HttpStatusCode.fromValue(418), "I'm a tea pot :3")
            DashifyPluginMain.plugin.server.onlinePlayers.forEach {
                if (call.parameters["uuid"] == it.uniqueId.toString()) {
                    call.respond(PlayerInfoProvider.getPlayerInfo(it.uniqueId.toString()))
                } else {
                    call.response.status(HttpStatusCode.BadRequest)
                }
            }
        }
    }
}
