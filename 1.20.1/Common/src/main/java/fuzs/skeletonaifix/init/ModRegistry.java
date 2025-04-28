package fuzs.skeletonaifix.init;

import fuzs.puzzleslib.api.init.v3.tags.BoundTagFactory;
import fuzs.skeletonaifix.SkeletonAIFix;
import net.minecraft.tags.TagKey;
import net.minecraft.world.entity.EntityType;

public class ModRegistry {
    static final BoundTagFactory TAGS = BoundTagFactory.make(SkeletonAIFix.MOD_ID);
    public static final TagKey<EntityType<?>> WELL_BEHAVED_SKELETONS_ENTITY_TYPE_TAG = TAGS.registerEntityTypeTag(
            "well_behaved_skeletons");

    public static void bootstrap() {
        // NO-OP
    }
}
