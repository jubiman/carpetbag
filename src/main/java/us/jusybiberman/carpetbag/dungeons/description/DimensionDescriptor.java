package us.jusybiberman.carpetbag.dungeons.description;

import net.minecraft.nbt.NBTTagCompound;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.tuple.Pair;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

/**
 * A unique descriptor of a dimension.
 */
public class DimensionDescriptor {
    private final String descriptionString;
    private final long forcedSeed;

    public DimensionDescriptor(List<String> descriptors, long forcedSeed) {
        this.forcedSeed = forcedSeed;

        StringBuilder s = new StringBuilder();
        s.append("Dungeon");
        s.append(descriptors);

        // List of all non-modifier dimlets with all associated modifiers.
        //List<Pair<DimletKey,List<DimletKey>>> dimlets = new ArrayList<>();

        // A list of all current modifier that haven't been fitted into a type yet.
        //List<DimletKey> currentModifiers = new ArrayList<>();

        //groupDimletsAndModifiers(descriptors, dimlets, currentModifiers);
        //constructDescriptionStringNew(s, dimlets, currentModifiers);

        descriptionString = s.toString();
    }

    private void constructDescriptionStringNew(StringBuilder s) {

    }

    private void groupDimletsAndModifiers() {

    }

    public DimensionDescriptor(NBTTagCompound tagCompound) {
        descriptionString = tagCompound.getString("descriptionString");
        forcedSeed = tagCompound.getLong("forcedSeed");
    }


    public static List<String > parseDescriptionString(String descriptionString) {

        return null;
    }

    public long calculateSeed(long seed) {
        return seed;
    }

    public String getDescriptionString() {
        return descriptionString;
    }

    public long getForcedSeed() {
        return forcedSeed;
    }

    public void writeToNBT(NBTTagCompound tagCompound) {
        tagCompound.setString("descriptionString", descriptionString);
        tagCompound.setLong("forcedSeed", forcedSeed);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        DimensionDescriptor that = (DimensionDescriptor) o;

        if (!descriptionString.equals(that.descriptionString)) return false;

        return true;
    }

    @Override
    public int hashCode() {
        return descriptionString.hashCode();
    }
}
