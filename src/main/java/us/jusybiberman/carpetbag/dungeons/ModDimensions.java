package us.jusybiberman.carpetbag.dungeons;

import us.jusybiberman.carpetbag.Carpetbag;
import net.minecraft.world.DimensionType;
import net.minecraft.world.WorldServer;
import net.minecraftforge.common.DimensionManager;
import us.jusybiberman.carpetbag.config.ModConfiguration;

import java.util.ArrayList;
import java.util.List;

public class ModDimensions {

    public static DimensionType dungeonType;
    public static List<DimensionType> dungeonTypes = new ArrayList<>();

    public static void init() {
        // Get next id if provider id is not set
        int id = ModConfiguration.DIMENSION_PROVIDER_ID;
        if (id == -1) {
            for (DimensionType type : DimensionType.values())
                if (type.getId() > id)
                    id = type.getId();
            id++;
        }
        Carpetbag.getLogger().info("Registering carpetbag dungeon dimension type(s) at id " + id + " + " + ModConfiguration.DUNGEON_DIMENSION_AMOUNT);
        dungeonType = DimensionType.register("carpetbag_dungeon_dimension", "_cpbdungeon", id, Dungeon.class, false);
        /*for (int i = 0; i < ModConfiguration.DUNGEON_DIMENSION_AMOUNT; i++) {
            for (DimensionType type : DimensionType.values())
                if (type.getId() > id)
                    id = type.getId();
            id++;
            dungeonTypes.add(DimensionType.register("carpetbag_dungeon" + i + "_dimension", "_cpbdungeon", id, Dungeon.class, false));
        }*/

        //GameRegistry.registerWorldGenerator(new GenericWorldGenerator(), 1000);
    }

    public static void initDimensions() {
        WorldServer world = DimensionManager.getWorld(0);
        if (world != null) {
            DungeonManager dimensionManager = DungeonManager.getDimensionManager(world);
            if (dimensionManager != null) {
                dimensionManager.registerDimensions();
            }
        }
    }
}
