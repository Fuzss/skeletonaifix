package fuzs.skeletonaifix.neoforge.client;

import fuzs.skeletonaifix.SkeletonAIFix;
import fuzs.skeletonaifix.client.SkeletonAIFixClient;
import fuzs.puzzleslib.api.client.core.v1.ClientModConstructor;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.fml.common.Mod;

@Mod(value = SkeletonAIFix.MOD_ID, dist = Dist.CLIENT)
public class SkeletonAIFixNeoForgeClient {

    public SkeletonAIFixNeoForgeClient() {
        ClientModConstructor.construct(SkeletonAIFix.MOD_ID, SkeletonAIFixClient::new);
    }
}
