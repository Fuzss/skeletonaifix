package fuzs.skeletonaifix.fabric.client;

import fuzs.skeletonaifix.SkeletonAIFix;
import fuzs.skeletonaifix.client.SkeletonAIFixClient;
import fuzs.puzzleslib.api.client.core.v1.ClientModConstructor;
import net.fabricmc.api.ClientModInitializer;

public class SkeletonAIFixFabricClient implements ClientModInitializer {

    @Override
    public void onInitializeClient() {
        ClientModConstructor.construct(SkeletonAIFix.MOD_ID, SkeletonAIFixClient::new);
    }
}
