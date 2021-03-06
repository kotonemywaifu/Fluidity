package me.liuli.fluidity.inject.mixins.net;

import me.liuli.fluidity.config.ConfigManager;
import net.minecraft.client.Minecraft;
import net.minecraft.network.EnumConnectionState;
import net.minecraft.network.PacketBuffer;
import net.minecraft.network.handshake.client.C00Handshake;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;

@Mixin(C00Handshake.class)
public class MixinC00Handshake {

    @Shadow
    private int protocolVersion;

    @Shadow
    public int port;

    @Shadow
    private EnumConnectionState requestedState;

    @Shadow
    public String ip;

    /**
     * @author CCBlueX
     */
    @Overwrite
    public void writePacketData(PacketBuffer buf) {
        buf.writeVarIntToBuffer(this.protocolVersion);
        buf.writeString(this.ip + (ConfigManager.Companion.getAntiForge() && !Minecraft.getMinecraft().isIntegratedServerRunning() ? "" : "\0FML\0"));
        buf.writeShort(this.port);
        buf.writeVarIntToBuffer(this.requestedState.getId());
    }
}
