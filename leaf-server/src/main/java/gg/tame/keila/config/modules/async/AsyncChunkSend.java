package gg.tame.keila.config.modules.async;

import gg.tame.keila.config.ConfigModules;
import gg.tame.keila.config.EnumConfigCategory;
import gg.tame.keila.config.annotations.HotReloadUnsupported;

@HotReloadUnsupported
public class AsyncChunkSend extends ConfigModules {

    public String getBasePath() {
        return EnumConfigCategory.ASYNC.getBaseKeyName() + ".async-chunk-send";
    }

    public static boolean enabled = false;
    private static boolean asyncChunkSendInitialized;

    @Override
    public void onLoaded() {
        config.addCommentRegionBased(getBasePath(), """
                Prepare and send chunk packets off the main thread to reduce MSPT during chunk loads.
                High-risk: enable only after queue and packet-order tests; see docs/keila/runtime-safety.md.""",
            """
                使区块数据包准备和发送异步化以提高服务器性能.
                当许多玩家同时加载区块时, 这可以显著减少主线程负载.""");

        if (asyncChunkSendInitialized) {
            config.getConfigSection(getBasePath());
            return;
        }
        asyncChunkSendInitialized = true;

        enabled = config.getBoolean(getBasePath() + ".enabled", enabled);
    }
}
