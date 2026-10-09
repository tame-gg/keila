package gg.tame.palladium.protocol;

import net.minecraft.network.protocol.common.custom.CustomPacketPayload;

public interface PalladiumCustomPayload extends CustomPacketPayload {

    @Override
    Type<? extends PalladiumCustomPayload> type();
}
