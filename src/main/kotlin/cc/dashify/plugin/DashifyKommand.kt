package cc.dashify.plugin

import cc.dashify.plugin.DashifyPluginMain.Companion.plugin
import cc.dashify.plugin.util.StringUtil
import io.github.monun.kommand.node.RootNode
import net.kyori.adventure.text.Component.text
import net.kyori.adventure.text.event.ClickEvent
import net.kyori.adventure.text.format.TextColor
import net.kyori.adventure.text.format.TextDecoration
import org.mindrot.jbcrypt.BCrypt

/**
 * DashifyKommand
 */
object DashifyKommand {
    fun registerKommand(builder: RootNode) {
        builder.apply { 
            then("key") {
                executes {
                    val key = BCrypt.hashpw(StringUtil.generateRandomString(64), BCrypt.gensalt())

                    plugin.config.set("key", key)
                    plugin.saveConfig()

                    player.sendMessage(text("Dashify key (re)generated."))
                    player.sendMessage(
                        text("Click ").append(
                            text("here", TextColor.color(0xFFCF2C)).decorate(TextDecoration.BOLD, TextDecoration.UNDERLINED)
                                .clickEvent(ClickEvent.copyToClipboard(key))
                        ).append(text(" to copy"))
                    )
                }
            }
            then("enable") {
                executes {
                    if (plugin.config.getBoolean("enabled")) {
                        player.sendMessage(text("Dashify plugin is already enabled.", TextColor.color(0xFFA500)))
                    } else {
                        plugin.config.set("enabled", true)
                        plugin.saveConfig()

                        if (!DashifyServer.isServerRunning) DashifyServer.startKtor()

                        player.sendMessage(text("Dashify plugin enabled."))
                    }
                }
            }
            then("disable") {
                executes {
                    if (!plugin.config.getBoolean("enabled")) {
                        player.sendMessage(text("Dashify plugin is already disabled.", TextColor.color(0xFFA500)))
                    } else {
                        plugin.config.set("enabled", false)
                        plugin.saveConfig()

                        if (DashifyServer.isServerRunning) DashifyServer.stopKtor()

                        player.sendMessage(text("Dashify plugin disabled."))
                    }
                }
            }
        }
    }
}