package fuzs.skeletonaifix;

import fuzs.puzzleslib.api.config.v3.ConfigHolder;
import fuzs.puzzleslib.api.core.v1.ModConstructor;
import fuzs.puzzleslib.api.event.v1.entity.EntityTickEvents;
import fuzs.skeletonaifix.config.ServerConfig;
import fuzs.skeletonaifix.init.ModRegistry;
import net.minecraft.resources.Identifier;
import net.minecraft.world.Difficulty;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.goal.RangedBowAttackGoal;
import net.minecraft.world.entity.monster.skeleton.AbstractSkeleton;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class SkeletonAIFix implements ModConstructor {
    public static final String MOD_ID = "skeletonaifix";
    public static final String MOD_NAME = "Skeleton AI Fix";
    public static final Logger LOGGER = LoggerFactory.getLogger(MOD_NAME);

    public static final ConfigHolder CONFIG = ConfigHolder.builder(MOD_ID).server(ServerConfig.class);

    @Override
    public void onConstructMod() {
        ModRegistry.bootstrap();
        registerEventHandlers();
    }

    private static void registerEventHandlers() {
        EntityTickEvents.END.register(SkeletonAIFix::onEndEntityTick);
    }

    private static void onEndEntityTick(Entity entity) {
        if (entity instanceof AbstractSkeleton abstractSkeleton && abstractSkeleton.getType()
                .is(ModRegistry.WELL_BEHAVED_SKELETONS_ENTITY_TYPE_TAG)) {
            RangedBowAttackGoal<AbstractSkeleton> bowGoal = abstractSkeleton.bowGoal;
            // disable strafing behavior
            bowGoal.strafingTime = Integer.MIN_VALUE;
            // make skeleton shoot faster the closer the target is, this was removed when strafing was introduced
            LivingEntity livingEntity = abstractSkeleton.getTarget();
            if (livingEntity != null) {
                double attackInterval = getAttackInterval(abstractSkeleton);
                double distanceToTargetSqr = abstractSkeleton.distanceToSqr(livingEntity);
                int minAttackInterval = getMinAttackInterval(attackInterval,
                        distanceToTargetSqr,
                        bowGoal.attackRadiusSqr);
                bowGoal.setMinAttackInterval(minAttackInterval);
            }
        }
    }

    /**
     * @see AbstractSkeleton#reassessWeaponGoal()
     */
    private static double getAttackInterval(AbstractSkeleton abstractSkeleton) {
        double attackInterval;
        if (abstractSkeleton.level().getDifficulty() == Difficulty.HARD) {
            attackInterval = abstractSkeleton.getHardAttackInterval();
        } else {
            attackInterval = abstractSkeleton.getAttackInterval();
        }

        attackInterval *= CONFIG.get(ServerConfig.class).attackIntervalMultiplier;
        return attackInterval;
    }

    private static int getMinAttackInterval(double attackInterval, double distanceToTargetSqr, double attackRadiusSqr) {
        double distanceScale = CONFIG.get(ServerConfig.class).attackIntervalDistanceScale;
        double baseAttackInterval = attackInterval * (1.0 - distanceScale);
        double attackRadiusScale = easeOutQuad(Math.min(distanceToTargetSqr / attackRadiusSqr, 1.0));
        double scaledAttackInterval = attackInterval * distanceScale * attackRadiusScale;
        return (int) (baseAttackInterval + scaledAttackInterval);
    }

    static double easeOutQuad(double x) {
        return 1.0 - (1.0 - x) * (1.0 - x);
    }

    public static Identifier id(String path) {
        return Identifier.fromNamespaceAndPath(MOD_ID, path);
    }
}
