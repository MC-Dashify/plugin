package cc.dashify.plugin.router

import cc.dashify.plugin.DashifyPluginMain.Companion.plugin
import cc.dashify.plugin.manager.PlayerManager
import cc.dashify.plugin.manager.RuntimeManager
import cc.dashify.plugin.manager.SystemManager
import cc.dashify.plugin.manager.WorldManager
import cc.dashify.plugin.util.DashifyUtil
import io.ktor.http.*
import io.ktor.serialization.jackson.*
import io.ktor.server.application.*
import io.ktor.server.auth.*
import io.ktor.server.plugins.contentnegotiation.*
import io.ktor.server.plugins.cors.routing.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*

/**
 * dashify()
 * dashify router
 */
fun Application.dashify() {
    install(Authentication) {
        bearer("auth-bearer") {
            authenticate { bearerTokenCredential ->
                if (bearerTokenCredential.token == DashifyUtil.key) UserIdPrincipal("dashify") else {
                    respond(
                        HttpStatusCode.Unauthorized,
                        hashMapOf("status" to "Unauthorized", "detail" to "Invalid key.")
                    )
                    null
                }
            }
        }
    }

    install(ContentNegotiation) {
        // TODO: Change to kotlinx.serialization
        jackson {}
    }

    routing {
        authenticate("auth-bearer") {
            // TODO: REMOVE All !!

            get("/") {
                call.respond(HttpStatusCode.OK, hashMapOf("status" to "ok"))
            }

            get("/worlds") {
                call.respond(HttpStatusCode.OK, WorldManager.getWorldsList())
            }
            get("/world/{uuid}") {
                val result = WorldManager.getWorldInfo(call.parameters["uuid"]!!)
                if (result["error"] == null) {
                    call.respond(HttpStatusCode.OK, result)
                } else {
                    call.respond(result["statusCode"] as HttpStatusCode, result)
                }
            }

            get("/players") {
                call.respond(HttpStatusCode.OK, PlayerManager.getPlayerList())
            }

            get("/player/{uuid}") {
                val result = PlayerManager.getPlayerInfo(call.parameters["uuid"]!!)
                if (result["error"] == null) {
                    call.respond(HttpStatusCode.OK, result)
                } else {
                    call.respond(result["statusCode"] as HttpStatusCode, result)
                }
            }
            post("/player/{uuid}/kick") {
                val result = PlayerManager.managePlayer("kick", call.parameters["uuid"]!!, call.receiveText())
                if (result["error"] == null) {
                    call.response.status(HttpStatusCode.OK)
                } else {
                    call.respond(result["statusCode"] as HttpStatusCode, result)
                }
            }
            post("/player/{uuid}/ban") {
                val result = PlayerManager.managePlayer("ban", call.parameters["uuid"]!!, call.receiveText())
                if (result["error"] == null) {
                    call.response.status(HttpStatusCode.OK)
                } else {
                    call.respond(result["statusCode"] as HttpStatusCode, result)
                }
            }

            get("/stats") {
                val stats = SystemManager.getSysInfo()
                stats["jvm"] = RuntimeManager.getMemory()
                stats["tps"] = plugin.server.tps
                call.respond(HttpStatusCode.OK, stats)
            }
        }
    }
}