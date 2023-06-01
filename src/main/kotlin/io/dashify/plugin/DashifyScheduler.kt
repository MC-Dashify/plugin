package io.dashify.plugin

import io.dashify.plugin.DashifyPluginMain.Companion.plugin
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.engine.*
import io.ktor.server.jetty.*
import io.ktor.server.response.*
import io.ktor.server.routing.*

object DashifyScheduler {
    var Ktor = Runnable {
        embeddedServer(Jetty, port = 8080, module = Application::ktor).start(wait=true)
    }
}

fun Application.ktor() {
    routing {
        get("/") {
            if( ConfigHandler["toggle"] == "disable" ) { call.response.status(HttpStatusCode(418, "I'm a tea pot")) }
            else call.response.status(HttpStatusCode.OK)
        }
        get("/worlds"){
            if( ConfigHandler["toggle"] == "disable" ) { call.response.status(HttpStatusCode(418, "I'm a tea pot")) }
            call.respond(WorldInfoProvider.getWorldsList().toString())
        }
        get("/worlds/{uuid}"){
            if( ConfigHandler["toggle"] == "disable" ) { call.response.status(HttpStatusCode(418, "I'm a tea pot")) }
            plugin.server.worlds.forEach {
                if (call.parameters["uuid"] == it.uid.toString()){
                    call.respond(WorldInfoProvider.getWorldInfo(it.uid.toString()).toString())
                }
                else {
                    call.response.status(HttpStatusCode.BadRequest)
                }
            }
        }
        get("/players"){
            if( ConfigHandler["toggle"] == "disable" ) { call.response.status(HttpStatusCode(418, "I'm a tea pot")) }
            call.respond(PlayerInfoProvider.getPlayerList().toString())
        }
    }
}

