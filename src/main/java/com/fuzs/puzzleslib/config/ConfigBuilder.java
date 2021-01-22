package com.fuzs.puzzleslib.config;

import net.minecraftforge.common.ForgeConfigSpec;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.config.ModConfig;

import javax.annotation.Nullable;
import java.io.File;
import java.util.EnumMap;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@SuppressWarnings("unused")
public class ConfigBuilder {

    /**
     * enum map of config type entries for storing and managing builders, specs and file names for every type
     */
    private final EnumMap<ModConfig.Type, ConfigTypeEntry> configTypeEntries = Stream.of(ModConfig.Type.values())
            .collect(Collectors.toMap(Function.identity(), ConfigTypeEntry::new, (key1, key2) -> key1, () -> new EnumMap<>(ModConfig.Type.class)));

    /**
     * config type of category currently being built
     */
    private ModConfig.Type activeType;

    /**
     * get spec, build from builder if absent
     * @param type type to get spec for
     * @return config spec for type
     */
    @Nullable
    public ForgeConfigSpec getSpec(ModConfig.Type type) {

        return this.configTypeEntries.get(type).getSpec();
    }

    /**
     * has the spec for this type been built yet (has {@link ConfigTypeEntry#getSpec} been called)
     * @param type type to check
     * @return has spec not been built
     */
    public boolean isSpecNotBuilt(ModConfig.Type type) {

        return this.configTypeEntries.get(type).isSpecBuilt();
    }

    /**
     * has the spec for this type been built yet and has it been loaded by Forge
     * @param type type to check
     * @return has spec been built and loaded
     */
    @SuppressWarnings("ConstantConditions")
    public boolean isSpecNotValid(ModConfig.Type type) {

        return this.isSpecNotBuilt(type) || !this.configTypeEntries.get(type).getSpec().isLoaded();
    }

    /**
     * add a spec when building config manually
     * @param spec spec to add
     * @param type type to add
     * @return was adding successful (spec not present yet)
     */
    public boolean addSpec(ModConfig.Type type, ForgeConfigSpec spec) {

        return this.configTypeEntries.get(type).addSpec(spec);
    }

    /**
     * wrap creation of a new category
     * @param category name of new category
     * @param options builder for category
     * @param type config type this category is for
     * @param comments comments to add to category
     */
    public void create(String category, Consumer<ForgeConfigSpec.Builder> options, ModConfig.Type type, String... comments) {

        this.activeType = type;

        ForgeConfigSpec.Builder builder = this.configTypeEntries.get(type).getBuilder();
        if (comments.length != 0) {

            builder.comment(comments);
        }

        builder.push(category);
        options.accept(builder);
        builder.pop();

        this.activeType = null;
    }

    /**
     * register all configs from non-empty builders
     * @param context active mod container context
     */
    public void registerConfigs(ModLoadingContext context) {

        for (ModConfig.Type type : ModConfig.Type.values()) {

            ConfigTypeEntry typeEntry = this.configTypeEntries.get(type);
            if (typeEntry.isSpecBuilt()) {

                context.registerConfig(type, typeEntry.getSpec(), typeEntry.getName(context));
            }
        }
    }

    /**
     * make sure folders have actually been created
     * @param folderName folder structure to place config files into
     */
    public void moveToFolder(String... folderName) {

        String prefix = String.join(File.separator, folderName);
        this.configTypeEntries.values().forEach(typeEntry -> typeEntry.setPrefix(prefix));
    }

    /**
     * @return type of category currently being built
     */
    public ModConfig.Type getActiveType() {

        return this.activeType;
    }

    /**
     * internal storage for builders, specs and file names
     */
    private static class ConfigTypeEntry {

        /**
         * type extension for config name
         */
        private final String type;
        /**
         * file path, only specified when config is in a separate folder
         */
        private String path = "";
        /**
         * spec for this type
         */
        private ForgeConfigSpec spec;
        /**
         * builder for this type
         */
        private ForgeConfigSpec.Builder builder;

        /**
         * @param type type of config for file name
         */
        ConfigTypeEntry(ModConfig.Type type) {

            this.type = type.extension();
        }

        /**
         * @return has builder not been created yet
         */
        boolean isBuilderMissing() {

            return this.builder == null;
        }

        /**
         * has the spec for this type been built yet (has {@link #getSpec} been called)
         * @return has spec been built
         */
        public boolean isSpecBuilt() {

            return this.spec == null;
        }

        /**
         * add a spec when building config manually
         * @param spec spec to add
         * @return was adding successful (spec not present yet)
         */
        boolean addSpec(ForgeConfigSpec spec) {

            if (!this.isSpecBuilt()) {

                this.spec = spec;
                return true;
            }

            return false;
        }

        /**
         * get spec, build from builder if absent
         * @return config spec for type
         */
        @Nullable
        ForgeConfigSpec getSpec() {

            if (!this.isSpecBuilt()) {

                if (this.isBuilderMissing()) {

                    return null;
                }

                this.spec = this.builder.build();
            }

            return spec;
        }

        /**
         * @return builder for type of this entry
         */
        ForgeConfigSpec.Builder getBuilder() {

            if (this.isBuilderMissing()) {

                this.builder = new ForgeConfigSpec.Builder();
            }

            return builder;
        }

        /**
         * @param prefix path for this config
         */
        void setPrefix(String prefix) {

            this.path = prefix;
        }

        /**
         * get file name for config
         * @param context context for supplying modid
         * @return full config name with path as prefix
         */
        String getName(ModLoadingContext context) {

            String modId = context.getActiveContainer().getModId();
            return this.path + File.separator + String.format("%s-%s.toml", modId, this.type);
        }
    }

}
