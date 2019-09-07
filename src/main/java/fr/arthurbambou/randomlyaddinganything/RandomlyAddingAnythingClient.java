package fr.arthurbambou.randomlyaddinganything;

import com.swordglowsblue.artifice.api.Artifice;
import fr.arthurbambou.randomlyaddinganything.api.enums.OreTypes;
import fr.arthurbambou.randomlyaddinganything.client.OreBackedModel;
import fr.arthurbambou.randomlyaddinganything.items.RAABlockItem;
import fr.arthurbambou.randomlyaddinganything.materials.Material;
import fr.arthurbambou.randomlyaddinganything.registries.Materials;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.model.ModelLoadingRegistry;
import net.fabricmc.fabric.api.event.client.ClientSpriteRegistryCallback;
import net.minecraft.client.render.model.BakedModel;
import net.minecraft.client.render.model.ModelBakeSettings;
import net.minecraft.client.render.model.ModelLoader;
import net.minecraft.client.render.model.UnbakedModel;
import net.minecraft.client.texture.Sprite;
import net.minecraft.client.texture.SpriteAtlasTexture;
import net.minecraft.util.Identifier;
import org.apache.commons.lang3.text.WordUtils;

import java.util.*;
import java.util.function.Function;

public class RandomlyAddingAnythingClient implements ClientModInitializer {

    private static final Map<Identifier, Map.Entry<Material, RAABlockItem.BlockType>> IDENTIFIERS = new HashMap<>();

    @Override
    public void onInitializeClient() {
        while (!Materials.isIsReady()) {
            System.out.println("Not Ready");
        }
        ClientSpriteRegistryCallback.event(SpriteAtlasTexture.BLOCK_ATLAS_TEX)
                .register((spriteAtlasTexture, registry) -> {
                    registry.register(new Identifier("block/stone"));
                    registry.register(new Identifier("block/dirt"));
                    registry.register(new Identifier("block/sand"));
                    registry.register(new Identifier("block/red_sand"));
                    registry.register(new Identifier("block/sandstone"));
                    registry.register(new Identifier("block/red_sandstone"));
                    for (Material material : Materials.MATERIAL_LIST) {
                        registry.register(material.getOverlayTexture());
                        registry.register(material.getResourceItemTexture());
                    }
                });
        Artifice.registerAssets(new Identifier(RandomlyAddingAnything.MOD_ID, "pack"), clientResourcePackBuilder -> {
            for (Material material : Materials.MATERIAL_LIST) {
                for (RAABlockItem.BlockType blockType : RAABlockItem.BlockType.values()) {
                    Identifier id = new Identifier(RandomlyAddingAnything.MOD_ID, material.getName().toLowerCase() + blockType.getString());
                    clientResourcePackBuilder.addBlockState(id, blockStateBuilder -> blockStateBuilder.variant("", variant -> {
                        variant.model(new Identifier(id.getNamespace(), "block/" + id.getPath()));
                    }));
                    clientResourcePackBuilder.addBlockModel(id, modelBuilder -> {
                        modelBuilder.parent(new Identifier("block/cube_all"));
                        modelBuilder.texture("all", new Identifier(id.getNamespace(), "block/" + id.getPath()));
                    });
                    clientResourcePackBuilder.addItemModel(id, modelBuilder -> {
                        modelBuilder.parent(new Identifier(id.getNamespace(), "block/" + id.getPath()));
                    });
                    clientResourcePackBuilder.addTranslations(id, translationBuilder ->
                            translationBuilder.entry("block." + id.getNamespace() + "." + id.getPath(), WordUtils.capitalizeFully(id.getNamespace() + "." + id.getPath())));
                    Map<Material, RAABlockItem.BlockType> map = new HashMap<>();
                    map.put(material, blockType);
                    IDENTIFIERS.put(id, (Map.Entry<Material, RAABlockItem.BlockType>) map.entrySet().toArray()[0]);
                }
                if (material.getOreType() == OreTypes.GEM) {
                    clientResourcePackBuilder.addItemModel(new Identifier(RandomlyAddingAnything.MOD_ID, material.getName().toLowerCase() + "_gem"), modelBuilder -> {
                        modelBuilder.parent(new Identifier("item/generated"));
//                        modelBuilder.texture("layer0", new Identifier(id.getNamespace(), "item/" + id.getPath() + "_gem"));
                        modelBuilder.texture("layer0", new Identifier(material.getResourceItemTexture().getNamespace(), material.getResourceItemTexture().getPath().toLowerCase()));
                    });
                }
                if (material.getOreType() == OreTypes.METAL) {
                    clientResourcePackBuilder.addItemModel(new Identifier(RandomlyAddingAnything.MOD_ID, material.getName().toLowerCase() + "_ingot"), modelBuilder -> {
                        modelBuilder.parent(new Identifier("item/generated"));
//                        modelBuilder.texture("layer0", new Identifier(id.getNamespace(), "item/" + id.getPath() + "_ingot"));
                        modelBuilder.texture("layer0", new Identifier(material.getResourceItemTexture().getNamespace(), material.getResourceItemTexture().getPath().toLowerCase()));
                    });
                    clientResourcePackBuilder.addItemModel(new Identifier(RandomlyAddingAnything.MOD_ID, material.getName().toLowerCase() + "_nugget"), modelBuilder -> {
                        modelBuilder.parent(new Identifier("item/generated"));
//                        modelBuilder.texture("layer0", new Identifier(id.getNamespace(), "item/" + id.getPath() + "_nugget"));
                        modelBuilder.texture("layer0", new Identifier(material.getResourceItemTexture().getNamespace(), material.getResourceItemTexture().getPath().toLowerCase()));
                    });
                }
            }
        });

        ModelLoadingRegistry.INSTANCE.registerVariantProvider(resourceManager -> (modelIdentifier, modelProviderContext) -> {
            if(!modelIdentifier.getNamespace().equals(RandomlyAddingAnything.MOD_ID)){
                return null;
            }
            Identifier identifier = new Identifier(modelIdentifier.getNamespace(), modelIdentifier.getPath());
            if (!(IDENTIFIERS.containsKey(identifier))) return null;
            System.out.println(modelIdentifier.toString());
            return new UnbakedModel() {
                @Override
                public Collection<Identifier> getModelDependencies() {
                    return Collections.emptyList();
                }

                @Override
                public Collection<Identifier> getTextureDependencies(Function<Identifier, UnbakedModel> var1, Set<String> var2) {
                    return Collections.emptyList();
                }

                @Override
                public BakedModel bake(ModelLoader var1, Function<Identifier, Sprite> var2, ModelBakeSettings var3) {
                    return new OreBackedModel(IDENTIFIERS.get(identifier).getKey());
                }
            };

        });
    }
}
