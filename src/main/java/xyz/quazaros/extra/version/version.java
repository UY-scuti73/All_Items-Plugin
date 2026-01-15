package xyz.quazaros.extra.version;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.MusicInstrumentMeta;
import org.bukkit.inventory.meta.PotionMeta;
import org.bukkit.potion.PotionType;
import xyz.quazaros.structures.items.item;

import java.lang.reflect.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class version {

    public double mc_version;

    ArrayList<ItemFlag> itemFlags;

    public version() {
        mc_version = get_version();
        setDefaultFlags();
    }

    //Gets the version of minecraft
    public double get_version() {
        String versionInfo = Bukkit.getVersion();

        Pattern pattern = Pattern.compile("\\(MC: ([\\d.]+)\\)");
        Matcher matcher = pattern.matcher(versionInfo);

        double ver = 1.0;
        if (matcher.find()) {
            String mcVersion = matcher.group(1);
            String[] parts = mcVersion.split("\\.");

            if (parts.length >= 2) {
                String major = parts[1]; // "21"
                String minor = parts.length >= 3 ? parts[2] : "0";
                ver = Double.parseDouble(major + "." + minor);
            }
        }

        return ver;
    }

    public boolean readyForSprites() {
        return isGreater(21.9);
    }

    public boolean isGreater(double a) {
        String verStr = Double.toString(mc_version);
        String aStr = Double.toString(a);

        if (verStr.length() > aStr.length()) {
            return true;
        } else if (verStr.length() < aStr.length()) {
            return false;
        } else {
            return mc_version >= a;
        }
    }

    public void setGlint(ItemMeta item_meta, boolean setGlint) {
        if (isGreater(20.5)) {
            item_meta.setEnchantmentGlintOverride(setGlint);
        }
    }

    public void setPotionMeta(PotionMeta potion_meta, PotionType i) {
        if (isGreater(20.5)) {
            potion_meta.setBasePotionType(i);
        } else {
            try {
                Method setBasePotionDataMethod;
                setBasePotionDataMethod = potion_meta.getClass().getMethod("setBasePotionData", Class.forName("org.bukkit.potion.PotionData"));
                setBasePotionDataMethod.setAccessible(true);

                Class<?> potionDataClass = Class.forName("org.bukkit.potion.PotionData");
                Constructor<?> potionDataConstructor = potionDataClass.getConstructor(PotionType.class);
                Object potionDataInstance = potionDataConstructor.newInstance(i);

                setBasePotionDataMethod.invoke(potion_meta, potionDataInstance);
            } catch (NoSuchMethodException | ClassNotFoundException | InstantiationException | IllegalAccessException | InvocationTargetException ignored) {}
        }
    }

    public PotionType getPotionMeta(PotionMeta potion_meta) {
        PotionType baseType = null;
        if (isGreater(20.5)) {
            baseType = potion_meta.getBasePotionType();
        } else {
            try {
                Object potionData = PotionMeta.class.getMethod("getBasePotionData").invoke(potion_meta);
                Method getTypeMethod = potionData.getClass().getMethod("getType");
                baseType = (PotionType) getTypeMethod.invoke(potionData);
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
        return baseType;
    }

    public Material tryMaterials(String a, String b) {
        Material matA = null, matB = null;
        for (Material m : Material.values()) {
            if (m.toString().equalsIgnoreCase(a)) {matA = m;}
            if (m.toString().equalsIgnoreCase(b)) {matB = m;}
        }
        return matA != null ? matA : matB;
    }

    public void initializeHorns(ArrayList<item> items) {
        if (isGreater(20.0)) {
            try {
                Class<?> musicInstrumentClass = Class.forName("org.bukkit.MusicInstrument");
                Method valuesMethod = musicInstrumentClass.getMethod("values");
                Object collectionObj = valuesMethod.invoke(null);

                if (!(collectionObj instanceof Collection<?>)) {
                    throw new IllegalStateException("Expected Collection from values(), got " + collectionObj.getClass());
                }

                Collection<?> instruments = (Collection<?>) collectionObj;

                Class<?> musicInstrumentMetaClass = Class.forName("org.bukkit.inventory.meta.MusicInstrumentMeta");

                for (Object instrument : instruments) {
                    Method getKey = instrument.getClass().getMethod("getKey");
                    Object keyObj = getKey.invoke(instrument);
                    Method getKeyStr = keyObj.getClass().getMethod("getKey");
                    String str = (String) getKeyStr.invoke(keyObj);
                    str = str.substring(0, str.length() - 10);

                    item temp = new item("goat_horn");
                    temp.set_name(str);

                    Object instrumentMeta = temp.item_meta;

                    if (musicInstrumentMetaClass.isInstance(instrumentMeta)) {
                        Method setInstrument = musicInstrumentMetaClass.getMethod("setInstrument", musicInstrumentClass);
                        setInstrument.setAccessible(true);
                        setInstrument.invoke(instrumentMeta, instrument);

                        Method setItemMetaMethod = temp.item_stack.getClass().getMethod("setItemMeta", Class.forName("org.bukkit.inventory.meta.ItemMeta"));
                        setItemMetaMethod.invoke(temp.item_stack, instrumentMeta);

                        temp.item_meta = (ItemMeta) instrumentMeta;
                    }

                    items.add(temp);
                }
            } catch (ClassNotFoundException e) {} catch (Exception e) {e.printStackTrace();}
        }
    }

    public void getHorn(ItemStack currentItem, ArrayList<item> items, ArrayList<Integer> index_list) {
        if (isGreater(20.0)) {
            try {
                Class<?> instrumentClass = Class.forName("org.bukkit.MusicInstrument");
                Class<?> keyedClass = Class.forName("org.bukkit.NamespacedKey");

                MusicInstrumentMeta currentItemMeta = (MusicInstrumentMeta) currentItem.getItemMeta();
                Object currentInstrument = currentItemMeta.getInstrument();

                Method getKeyMethod = instrumentClass.getMethod("getKey");
                Method getKeyStrMethod = keyedClass.getMethod("getKey");

                Method valuesMethod = instrumentClass.getMethod("values");
                Object valuesResult = valuesMethod.invoke(null);

                if (!(valuesResult instanceof Collection<?> instruments)) {
                    throw new IllegalStateException("Expected Collection from MusicInstrument.values()");
                }

                for (int i = 0; i < items.size(); i++) {
                    for (Object o : instruments) {
                        Object keyObj = getKeyMethod.invoke(o);
                        String keyStr = (String) getKeyStrMethod.invoke(keyObj);
                        String trimmedKey = keyStr.substring(0, keyStr.length() - 10);

                        if (currentInstrument.equals(o) && items.get(i).item_name.equals(trimmedKey)) {
                            index_list.add(i);
                        }
                    }
                }
            } catch (ClassNotFoundException e) {} catch (ReflectiveOperationException | ClassCastException e) {e.printStackTrace();}
        }
    }

    public boolean checkItem(Material tempMaterial, String tempString) {
        for (Material m : Material.values()) {
            if (m.toString().equalsIgnoreCase(tempString)) {
                if (tempMaterial.equals(m)) {
                    return true;
                } else {return false;}
            }
        }
        return false;
    }

    public Material checkItemExists(String itemName) {
        for (Material m : Material.values()) {
            if (m.toString().equalsIgnoreCase(itemName)) {
                return m;
            }
        }
        return null;
    }

    private void setDefaultFlags() {
        itemFlags = new ArrayList<>();
        ArrayList<String> itemFlagNames = new ArrayList<>(Arrays.asList("HIDE_ADDITIONAL_TOOLTIP", "HIDE_ATTRIBUTES", "HIDE_INSTRUMENT", "HIDE_TOOLTIP_DISPLAY", "HIDE_BUNDLE_CONTENTS", "HIDE_OMINOUS_BOTTLE_AMPLIFIER"));
        for (ItemFlag flag : ItemFlag.values()) {
            if (itemFlagNames.contains(flag.name())) {
                itemFlags.add(flag);
            }
        }
    }

    public void addFlags(ItemMeta meta) {
        meta.addItemFlags(itemFlags.toArray(new ItemFlag[itemFlags.size()]));
    }
}
