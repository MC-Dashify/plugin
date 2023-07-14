/*
 * Copyright (C) 2023 Dashify
 *
 *  This program is free software: you can redistribute it and/or modify
 *  it under the terms of the GNU General Public License as published by
 *  the Free Software Foundation, either version 3 of the License, or
 *  any later version.
 *
 *  This program is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU General Public License for more details.
 *
 *  You should have received a copy of the GNU General Public License
 *  along with this program.  If not, see <https://www.gnu.org/licenses/>.
 */

package cc.dashify.plugin.router

import cc.dashify.plugin.DashifyPluginMain.Companion.plugin
import cc.dashify.plugin.manager.PlayerManager
import cc.dashify.plugin.manager.RuntimeManager
import cc.dashify.plugin.manager.SystemManager
import cc.dashify.plugin.manager.WorldManager
import cc.dashify.plugin.util.DashifyUtil
import cc.dashify.plugin.util.DashifyUtil.enabled
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
    val statusOK = hashMapOf("status" to "OK")
    val statusDisabled = hashMapOf("status" to "I'm a teapot :3", "detail" to "Dashify is disabled.")
    val statusAuthError = hashMapOf("error" to "Invalid API key / No Authorization Header.")
    val disabledCode = HttpStatusCode.fromValue(418)
    
    install(ContentNegotiation) {
        jackson {
            enable(SerializationFeature.INDENT_OUTPUT)
            setDefaultPrettyPrinter(DashifyUtil.CustomPrettyPrinter)
        }
    }

    routing {
        get("/") {
            if (enabled) {
                call.respond(HttpStatusCode.OK, statusOK)
            }
            else {
                call.respond(disabledCode, statusDisabled)
            }
        }
        get("/worlds") {
            val auth = validateKey(call.authKey)

            if (enabled) {
                if (!auth) {
                    call.respond(HttpStatusCode.Unauthorized, statusAuthError)
                } else {
                    call.respond(HttpStatusCode.OK, WorldManager.getWorldList())
                }
            }
            else {
                call.respond(disabledCode, statusDisabled)
            }
        }
        get("/worlds/{uuid}") {
            val auth = validateKey(call.authKey)

            if (enabled) {
                if (!auth) {
                    call.respond(HttpStatusCode.Unauthorized, statusAuthError)
                } else {
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
            else {
                call.respond(disabledCode, statusDisabled)
            }
        }

        get("/players") {
            val auth = validateKey(call.authKey)

            if (enabled) {
                if (!auth) {
                    call.respond(HttpStatusCode.Unauthorized, statusAuthError)
                } else {
                    call.respond(HttpStatusCode.OK, PlayerManager.getPlayerList())
                }
            }
            else {
                call.respond(disabledCode, statusDisabled)
            }
        }

        get("/players/{uuid}") {
            val auth = validateKey(call.authKey)

            if (enabled) {
                if (!auth) {
                    call.respond(HttpStatusCode.Unauthorized, statusAuthError)
                } else {
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
            else {
                call.respond(disabledCode, statusDisabled)
            }
        }
        post("/players/{uuid}/kick") {
            val auth = validateKey(call.authKey)

            if (enabled) {
                if (!auth) {
                    call.respond(HttpStatusCode.Unauthorized, statusAuthError)
                } else {
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
            else {
                call.respond(disabledCode, statusDisabled)
            }
        }
        post("/players/{uuid}/ban") {
            val auth = validateKey(call.authKey)

            if (enabled) {
                if (!auth) {
                    call.respond(HttpStatusCode.Unauthorized, statusAuthError)
                } else {
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
            else {
                call.respond(disabledCode, statusDisabled)
            }
        }

        get("/stats") {
            val auth = validateKey(call.authKey)

            if (enabled) {
                if (!auth) {
                    call.respond(HttpStatusCode.Unauthorized, statusAuthError)
                } else {
                    val stats = SystemManager.getSysInfo()
                    stats["jvm"] = RuntimeManager.getMemory()
                    stats["tps"] = plugin.server.tps
                    call.respond(HttpStatusCode.OK, stats)
                }
            }
            else {
                call.respond(disabledCode, statusDisabled)
            }
        }
    }
}

private val ApplicationCall.authKey
    get() = this.request.headers["Authorization"]