package gg.tame.keila.protocol;

import net.minecraft.network.protocol.common.custom.CustomPacketPayload;

public interface KeilaCustomPayload extends CustomPacketPayload {

    @Override
    Type<? extends KeilaCustomPayload> type();
}
