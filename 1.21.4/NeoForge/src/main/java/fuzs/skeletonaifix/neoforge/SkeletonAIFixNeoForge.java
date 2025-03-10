package fuzs.skeletonaifix.neoforge;

import fuzs.skeletonaifix.SkeletonAIFix;
import fuzs.puzzleslib.api.core.v1.ModConstructor;
import net.neoforged.fml.common.Mod;

@Mod(SkeletonAIFix.MOD_ID)
public class SkeletonAIFixNeoForge {

    public SkeletonAIFixNeoForge() {
        ModConstructor.construct(SkeletonAIFix.MOD_ID, SkeletonAIFix::new);
    }
}
