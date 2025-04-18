package fuzs.skeletonaifix;

import fuzs.puzzleslib.api.core.v1.ModConstructor;
import fuzs.puzzleslib.api.core.v1.utility.ResourceLocationHelper;
import fuzs.puzzleslib.api.event.v1.entity.EntityTickEvents;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.Difficulty;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.goal.RangedBowAttackGoal;
import net.minecraft.world.entity.monster.AbstractSkeleton;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class SkeletonAIFix implements ModConstructor {
    public static final String MOD_ID = "skeletonaifix";
    public static final String MOD_NAME = "Skeleton AI Fix";
    public static final Logger LOGGER = LoggerFactory.getLogger(MOD_NAME);

    @Override
    public void onConstructMod() {
        registerEventHandlers();
    }

    private static void registerEventHandlers() {
        EntityTickEvents.END.register(entity -> {
            if (entity instanceof AbstractSkeleton abstractSkeleton) {
                RangedBowAttackGoal<AbstractSkeleton> bowGoal = abstractSkeleton.bowGoal;
                // disable strafing behavior
                bowGoal.strafingTime = Integer.MIN_VALUE;
                // make skeleton shoot faster the closer the target is, this was removed when strafing was introduced
                LivingEntity livingEntity = abstractSkeleton.getTarget();
                if (livingEntity != null) {
                    int attackInterval;
                    if (abstractSkeleton.level().getDifficulty() == Difficulty.HARD) {
                        attackInterval = abstractSkeleton.getHardAttackInterval();
                    } else {
                        attackInterval = abstractSkeleton.getAttackInterval();
                    }
                    double distanceToTargetSqr = abstractSkeleton.distanceToSqr(livingEntity);
                    attackInterval -= (int) ((1.0 - Math.min(distanceToTargetSqr / bowGoal.attackRadiusSqr, 1.0)) * 20.0);
                    bowGoal.setMinAttackInterval(attackInterval);
                }
            }
        });
    }

    public static ResourceLocation id(String path) {
        return ResourceLocationHelper.fromNamespaceAndPath(MOD_ID, path);
    }
}
