package net.examplemod;

import com.google.common.base.Suppliers;
import com.mojang.serialization.Codec;
import dev.architectury.registry.CreativeTabRegistry;
import dev.architectury.registry.registries.DeferredRegister;
import dev.architectury.registry.registries.Registries;
import dev.architectury.registry.registries.RegistrySupplier;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.levelgen.Heightmap;
import net.minecraft.world.level.levelgen.structure.Structure;
import net.minecraft.world.level.levelgen.structure.StructureType;
import net.minecraft.world.level.levelgen.structure.pieces.StructurePiecesBuilder;
import net.minecraft.world.level.levelgen.structure.structures.SwampHutPiece;
import net.minecraft.world.level.levelgen.structure.structures.SwampHutStructure;

import java.util.Optional;
import java.util.function.Supplier;

public class ExampleMod {
    public static final String MOD_ID = "examplemod";

    public static final DeferredRegister<StructureType<?>> STRUCTURE_TYPES = DeferredRegister.create(MOD_ID, Registry.STRUCTURE_TYPE_REGISTRY);

    public static final RegistrySupplier<StructureType<TestStructure>> TEST_STRUCTURE = STRUCTURE_TYPES.register(new ResourceLocation(MOD_ID, "test_structure"), ()->()->TestStructure.CODEC);

    public static void init() {
        STRUCTURE_TYPES.register();
    }

    private static class TestStructure extends Structure
    {
        public static final Codec<TestStructure> CODEC = Structure.simpleCodec(TestStructure::new);

        public TestStructure(Structure.StructureSettings structureSettings) {
            super(structureSettings);
        }

        public Optional<Structure.GenerationStub> findGenerationPoint(Structure.GenerationContext generationContext) {
            return onTopOfChunkCenter(generationContext, Heightmap.Types.WORLD_SURFACE_WG, (structurePiecesBuilder) -> {
                generatePieces(structurePiecesBuilder, generationContext);
            });
        }

        private static void generatePieces(StructurePiecesBuilder structurePiecesBuilder, Structure.GenerationContext generationContext) {
            structurePiecesBuilder.addPiece(new SwampHutPiece(generationContext.random(), generationContext.chunkPos().getMinBlockX(), generationContext.chunkPos().getMinBlockZ()));
        }

        public StructureType<?> type() {
            return TEST_STRUCTURE.get();
        }
    }
}
