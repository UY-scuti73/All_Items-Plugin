package xyz.quazaros;

import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.MusicInstrumentMeta;
import org.bukkit.inventory.meta.PotionMeta;
import org.bukkit.potion.PotionType;
import xyz.quazaros.data.items.item;
import xyz.quazaros.data.meta.potions;

import java.lang.reflect.*;
import java.util.ArrayList;
import java.util.Collection;

public class version {
    public void setGlint(ItemMeta item_meta, boolean setGlint) {
        try {
            Method glintMethod = item_meta.getClass().getMethod("setEnchantmentGlintOverride", Boolean.class);
            glintMethod.setAccessible(true);
            glintMethod.invoke(item_meta, setGlint);
        } catch (NoSuchMethodException ignored) {} catch (Exception e) {e.printStackTrace();}
    }

    public void setPotionMeta(PotionMeta potion_meta, potions i) {
        try {
            Method setBasePotionTypeMethod;
            setBasePotionTypeMethod = potion_meta.getClass().getMethod("setBasePotionType", PotionType.class);
            setBasePotionTypeMethod.setAccessible(true);
            setBasePotionTypeMethod.invoke(potion_meta, i.effect.get(0));
        } catch (NoSuchMethodException e) {
            try {
                Method setBasePotionDataMethod;
                setBasePotionDataMethod = potion_meta.getClass().getMethod("setBasePotionData", Class.forName("org.bukkit.potion.PotionData"));
                setBasePotionDataMethod.setAccessible(true);

                Class<?> potionDataClass = Class.forName("org.bukkit.potion.PotionData");
                Constructor<?> potionDataConstructor = potionDataClass.getConstructor(PotionType.class);
                Object potionDataInstance = potionDataConstructor.newInstance(i.effect.get(0));

                setBasePotionDataMethod.invoke(potion_meta, potionDataInstance);
            } catch (NoSuchMethodException | ClassNotFoundException | InstantiationException | IllegalAccessException | InvocationTargetException ignored) {}
        } catch (IllegalAccessException | InvocationTargetException e) {e.printStackTrace();}
    }

    public PotionType getPotionMeta(PotionMeta potion_meta) {
        PotionType baseType = null;
        try {
            baseType = (PotionType) PotionMeta.class.getMethod("getBasePotionType").invoke(potion_meta);
        } catch (NoSuchMethodException e) {
            try {
                Object potionData = PotionMeta.class.getMethod("getBasePotionData").invoke(potion_meta);
                Method getTypeMethod = potionData.getClass().getMethod("getType");
                baseType = (PotionType) getTypeMethod.invoke(potionData);
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        } catch (Exception e) {
            e.printStackTrace();
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

    public void getHorn(ItemStack currentItem, ArrayList<item> items, ArrayList<Integer> index_list) {
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

    public boolean checkItem (Material tempMaterial, String tempString) {
        for (Material m : Material.values()) {
            if (m.toString().equalsIgnoreCase(tempString)) {
                if (tempMaterial.equals(m)) {
                    return true;
                } else {return false;}
            }
        }
        return false;
    }
}
