package me.liuli.fluidity.util.client

import com.google.gson.JsonObject
import me.liuli.fluidity.Fluidity
import me.liuli.fluidity.util.mc
import net.minecraft.util.IChatComponent
import org.apache.logging.log4j.LogManager
import org.lwjgl.opengl.Display

private val logger = LogManager.getLogger(Fluidity.NAME)

fun logInfo(msg: String) {
    logger.info(msg)
}

fun logWarn(msg: String) {
    logger.warn(msg)
}

fun logError(msg: String) {
    logger.error(msg)
}

fun logError(msg: String, t: Throwable) {
    logger.error(msg, t)
}

fun displayAlert(message: String) {
    displayChatMessage("§7[${Fluidity.COLORED_NAME}§7] §f$message")
}

fun displayChatMessage(message: String) {
    if (mc.thePlayer == null) {
        logInfo("[CHAT] $message")
        return
    }
    val jsonObject = JsonObject()
    jsonObject.addProperty("text", message)
    mc.thePlayer.addChatMessage(IChatComponent.Serializer.jsonToComponent(jsonObject.toString()))
}

fun setTitle(status: String? = null) {
    Display.setTitle("${Fluidity.NAME}->${Fluidity.VERSION}" +
            if (!status.isNullOrEmpty()) { "::$status" } else { "" })
}