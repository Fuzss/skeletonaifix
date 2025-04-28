package fuzs.skeletonaifix.neoforge;

import fuzs.puzzleslib.api.core.v1.ModConstructor;
import fuzs.puzzleslib.api.data.v2.core.DataProviderHelper;
import fuzs.skeletonaifix.SkeletonAIFix;
import fuzs.skeletonaifix.data.ModEntityTypeTagProvider;
import net.minecraftforge.fml.common.Mod;

@Mod(SkeletonAIFix.MOD_ID)
public class SkeletonAIFixNeoForge {

    public SkeletonAIFixNeoForge() {
        ModConstructor.construct(SkeletonAIFix.MOD_ID, SkeletonAIFix::new);
        DataProviderHelper.registerDataProviders(SkeletonAIFix.MOD_ID, ModEntityTypeTagProvider::new);
    }
}
