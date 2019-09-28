package io.github.vampirestudios.raa.generation.surface;

import com.mojang.datafixers.Dynamic;
import net.minecraft.block.BlockState;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.chunk.Chunk;
import net.minecraft.world.gen.GenerationStep;
import net.minecraft.world.gen.feature.Feature;
import net.minecraft.world.gen.surfacebuilder.SurfaceBuilder;
import net.minecraft.world.gen.surfacebuilder.TernarySurfaceConfig;

import java.util.Random;
import java.util.function.Function;

public class CustomDimensionSurfaceBuilder extends SurfaceBuilder<TernarySurfaceConfig> {
    public CustomDimensionSurfaceBuilder(Function<Dynamic<?>, ? extends TernarySurfaceConfig> function_1) {
        super(function_1);
    }

    @Override
    public void generate(Random rand, Chunk chunk, Biome biome, int x, int height, int z, double noise, BlockState state, BlockState state2, int int1, long long1, TernarySurfaceConfig config) {

        SurfaceBuilder.DEFAULT.generate(rand, chunk, biome, x, height, z, noise, state, state2, int1, long1, config);
    }
}