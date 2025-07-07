package fuzs.skeletonaifix.config;

import fuzs.puzzleslib.api.config.v3.Config;
import fuzs.puzzleslib.api.config.v3.ConfigCore;

public class ServerConfig implements ConfigCore {
    @Config(
            description = {
                    "A multiplier for the time between ranged skeleton attacks, i.e. how long it takes the skeleton to reload.",
                    "The default attack interval in vanilla is between 1-2 seconds depending on difficulty."
            }
    )
    @Config.DoubleRange(min = 0.0, max = 4.0)
    public double attackIntervalMultiplier = 1.0;
    @Config(
            description = {
                    "The fraction of the attack interval that can be scaled by distance.",
                    "Setting this to e.g. 0.25 means 25% of the total attack interval time will scale depending on how far a target is away from the skeleton, while the remaining 75% are fixed."
            }
    )
    @Config.DoubleRange(min = 0.0, max = 1.0)
    public double attackIntervalDistanceScale = 0.5;
}
