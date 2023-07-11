package cc.dashify.plugin.router

import cc.dashify.plugin.DashifyPluginMain.Companion.plugin
import cc.dashify.plugin.manager.PlayerManager
import cc.dashify.plugin.manager.RuntimeManager
import cc.dashify.plugin.manager.SystemManager
import cc.dashify.plugin.manager.WorldManager
import cc.dashify.plugin.util.DashifyUtil
import cc.dashify.plugin.util.DashifyUtil.validateKey
import com.fasterxml.jackson.databind.SerializationFeature
import io.ktor.http.*
import io.ktor.serialization.jackson.*
import io.ktor.server.application.*
import io.ktor.server.plugins.contentnegotiation.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*

/**
 * @author aroxu, pybsh
 */

/**
 * Dashify router
 *
 * This is where all the HTTP routes are defined.
 */

fun Application.dashify() {
    install(ContentNegotiation) {
        jackson {
            enable(SerializationFeature.INDENT_OUTPUT)
            setDefaultPrettyPrinter(DashifyUtil.CustomPrettyPrinter)
        }
    }

    routing {
        get("/") {
            call.respond(HttpStatusCode.OK, hashMapOf("status" to "OK"))
        }
        get("/worlds") {
            val auth = validateKey(call.request.headers["Authorization"] ?: "", call)
            if (!auth) return@get else call.respond(HttpStatusCode.OK, WorldManager.getWorldsList())
        }
        get("/worlds/{uuid}") {
            val auth = validateKey(call.request.headers["Authorization"] ?: "", call)
            if (!auth) return@get else {
                call.parameters["uuid"]?.let { uuid ->
                    val result = WorldManager.getWorldInfo(uuid)
                    if (result["error"] == null) {
                        call.respond(HttpStatusCode.OK, result)
                    } else {
                        call.respond(result["statusCode"] as HttpStatusCode, result)
                    }
                } ?: call.respond(HttpStatusCode.BadRequest, hashMapOf("error" to "No UUID provoded."))
            }
        }

        get("/players") {
            val auth = validateKey(call.request.headers["Authorization"] ?: "", call)
            if (!auth) return@get else call.respond(HttpStatusCode.OK, PlayerManager.getPlayerList())
        }

        get("/players/{uuid}") {
            val auth = validateKey(call.request.headers["Authorization"] ?: "", call)
            if (!auth) return@get else {
                call.parameters["uuid"]?.let { uuid ->
                    val result = PlayerManager.getPlayerInfo(uuid)
                    if (result["error"] == null) {
                        call.respond(HttpStatusCode.OK, result)
                    } else {
                        call.respond(result["statusCode"] as HttpStatusCode, result)
                    }
                }
            }
        }
        post("/players/{uuid}/kick") {
            val auth = validateKey(call.request.headers["Authorization"] ?: "", call)
            if (!auth) return@post else {
                call.parameters["uuid"]?.let { uuid ->
                    val result = PlayerManager.managePlayer("kick", uuid, call.receiveText())
                    if (result["error"] == null) {
                        call.response.status(HttpStatusCode.OK)
                    } else {
                        call.respond(result["statusCode"] as HttpStatusCode, result)
                    }
                }
            }
        }
        post("/players/{uuid}/ban") {
            val auth = validateKey(call.request.headers["Authorization"] ?: "", call)
            if (!auth) return@post else {
                call.parameters["uuid"]?.let { uuid ->
                    val result = PlayerManager.managePlayer("ban", uuid, call.receiveText())
                    if (result["error"] == null) {
                        call.response.status(HttpStatusCode.OK)
                    } else {
                        call.respond(result["statusCode"] as HttpStatusCode, result)
                    }
                }
            }
        }

        get("/stats") {
            val auth = validateKey(call.request.headers["Authorization"] ?: "", call)
            if (!auth) return@get else {
                val stats = SystemManager.getSysInfo()
                stats["jvm"] = RuntimeManager.getMemory()
                stats["tps"] = plugin.server.tps
                call.respond(HttpStatusCode.OK, stats)
            }
        }
    }
}