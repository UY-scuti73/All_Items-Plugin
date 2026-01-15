package xyz.quazaros.sprites;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class spriteExceptions {
    Map<String, String> sprite_exception_map;
    ArrayList<String> path_change_map;

    public spriteExceptions() {
        sprite_exception_map = new HashMap<>();
        path_change_map = new ArrayList<>();

        ArrayList<String> wood_types = new ArrayList<>();
        wood_types.add("oak");
        wood_types.add("birch");
        wood_types.add("spruce");
        wood_types.add("jungle");
        wood_types.add("acacia");
        wood_types.add("dark_oak");
        wood_types.add("pale_oak");
        wood_types.add("cherry");
        wood_types.add("mangrove");

        ArrayList<String> nether_wood_types = new ArrayList<>();
        nether_wood_types.add("crimson");
        nether_wood_types.add("warped");

        ArrayList<String> other_wood_types = new ArrayList<>();
        other_wood_types.add("bamboo");

        ArrayList<String> stones = new ArrayList<>();
        stones.add("stone");
        stones.add("cobblestone");
        stones.add("mossy_cobblestone");
        stones.add("granite");
        stones.add("polished_granite");
        stones.add("diorite");
        stones.add("polished_diorite");
        stones.add("andesite");
        stones.add("polished_andesite");
        stones.add("cobbled_deepslate");
        stones.add("polished_deepslate");
        stones.add("tuff");
        stones.add("polished_tuff");
        stones.add("sandstone");
        stones.add("smooth_sandstone");
        stones.add("cut_sandstone");
        stones.add("red_sandstone");
        stones.add("smooth_red_sandstone");
        stones.add("cut_red_sandstone");
        stones.add("prismarine");
        stones.add("dark_prismarine");
        stones.add("blackstone");
        stones.add("polished_blackstone");

        ArrayList<String> bricks = new ArrayList<>();
        bricks.add("deepslate_tile");
        bricks.add("stone_brick");
        bricks.add("mossy_stone_brick");
        bricks.add("deepslate_brick");
        bricks.add("tuff_brick");
        bricks.add("brick");
        bricks.add("mud_brick");
        bricks.add("resin_brick");
        bricks.add("prismarine_brick");
        bricks.add("nether_brick");
        bricks.add("red_nether_brick");
        bricks.add("polished_blackstone_brick");
        bricks.add("end_stone_brick");

        ArrayList<String> oddBlocks = new ArrayList<>();
        oddBlocks.add("purpur");

        ArrayList<String> colors = new ArrayList<>();
        colors.add("red");
        colors.add("orange");
        colors.add("yellow");
        colors.add("lime");
        colors.add("green");
        colors.add("cyan");
        colors.add("light_blue");
        colors.add("blue");
        colors.add("purple");
        colors.add("magenta");
        colors.add("pink");
        colors.add("white");
        colors.add("light_gray");
        colors.add("gray");
        colors.add("black");
        colors.add("brown");

        ArrayList<String> copper = new ArrayList<>();
        copper.add("");
        copper.add("exposed_");
        copper.add("weathered_");
        copper.add("oxidized_");

        ArrayList<String> infested = new ArrayList<>();
        infested.add("cobblestone");
        infested.add("deepslate");
        infested.add("stone");
        infested.add("stone_bricks");
        infested.add("mossy_stone_bricks");
        infested.add("cracked_stone_bricks");
        infested.add("chiseled_stone_bricks");

        ArrayList<String> side_sprites = new ArrayList<>();
        side_sprites.add("ancient_debris");
        side_sprites.add("azalea");
        side_sprites.add("flowering_azalea");
        side_sprites.add("basalt");
        side_sprites.add("barrel");
        side_sprites.add("bee_nest");
        side_sprites.add("beehive");
        side_sprites.add("blast_furnace");
        side_sprites.add("furnace");
        side_sprites.add("smoker");
        side_sprites.add("bone_block");
        side_sprites.add("cactus");
        side_sprites.add("cake");
        side_sprites.add("cauldron");
        side_sprites.add("command_block");
        side_sprites.add("chain_command_block");
        side_sprites.add("repeating_command_block");
        side_sprites.add("chiseled_bookshelf");
        side_sprites.add("composter");
        side_sprites.add("crafting_table");
        side_sprites.add("dirt_path");
        side_sprites.add("enchanting_table");
        side_sprites.add("end_portal_frame");
        side_sprites.add("fletching_table");
        side_sprites.add("grass_block");
        side_sprites.add("hay_block");
        side_sprites.add("honey_block");
        side_sprites.add("jigsaw");
        side_sprites.add("jukebox");
        side_sprites.add("lodestone");
        side_sprites.add("mangrove_roots");
        side_sprites.add("melon");
        side_sprites.add("pumpkin");
        side_sprites.add("muddy_mangrove_roots");
        side_sprites.add("mycelium");
        side_sprites.add("ochre_froglight");
        side_sprites.add("pearlescent_froglight");
        side_sprites.add("podzol");
        side_sprites.add("polished_basalt");
        side_sprites.add("quartz_block");
        side_sprites.add("reinforced_deepslate");
        side_sprites.add("sculk_catalyst");
        side_sprites.add("sculk_shrieker");
        side_sprites.add("smithing_table");
        side_sprites.add("smooth_stone_slab");
        side_sprites.add("stonecutter");
        side_sprites.add("target");
        side_sprites.add("tnt");
        side_sprites.add("verdant_froglight");

        for (String i : side_sprites) {
            sprite_exception_map.put(i, i + "_side");
        }

        for (String i : wood_types) {
            sprite_exception_map.put(i + "_wood", i + "_log");
            sprite_exception_map.put("stripped_" + i + "_wood", "stripped_" + i + "_log");
            sprite_exception_map.put(i + "_stairs", i + "_planks");
            sprite_exception_map.put(i + "_slab", i + "_planks");
            sprite_exception_map.put(i + "_pressure_plate", i + "_planks");
            sprite_exception_map.put(i + "_button", i + "_planks");
            sprite_exception_map.put(i + "_fence", i + "_planks");
            sprite_exception_map.put(i + "_fence_gate", i + "_planks");
            sprite_exception_map.put(i + "_shelf", i + "_planks");
            path_change_map.add(i + "_door");
            path_change_map.add(i + "_sign");
            path_change_map.add(i + "_hanging_sign");
        }

        for (String i : nether_wood_types) {
            sprite_exception_map.put(i + "_hyphae", i + "_stem");
            sprite_exception_map.put("stripped_" + i + "_hyphae", "stripped_" + i + "_stem");
            sprite_exception_map.put(i + "_stairs", i + "_planks");
            sprite_exception_map.put(i + "_slab", i + "_planks");
            sprite_exception_map.put(i + "_pressure_plate", i + "_planks");
            sprite_exception_map.put(i + "_button", i + "_planks");
            sprite_exception_map.put(i + "_fence", i + "_planks");
            sprite_exception_map.put(i + "_fence_gate", i + "_planks");
            sprite_exception_map.put(i + "_shelf", i + "_planks");
            path_change_map.add(i + "_door");
            path_change_map.add(i + "_sign");
            path_change_map.add(i + "_hanging_sign");
        }

        for (String i : other_wood_types) {
            sprite_exception_map.put(i + "_stairs", i + "_planks");
            sprite_exception_map.put(i + "_slab", i + "_planks");
            sprite_exception_map.put(i + "_pressure_plate", i + "_planks");
            sprite_exception_map.put(i + "_button", i + "_planks");
            sprite_exception_map.put(i + "_fence", i + "_planks");
            sprite_exception_map.put(i + "_shelf", i + "_planks");
            sprite_exception_map.put(i + "_mosaic_stairs", i + "_mosaic");
            sprite_exception_map.put(i + "_mosaic_slab", i + "_mosaic");
            path_change_map.add(i + "_door");
            path_change_map.add(i + "_sign");
            path_change_map.add(i + "_hanging_sign");
        }

        for (String i : stones) {
            sprite_exception_map.put(i + "_stairs", i);
            sprite_exception_map.put(i + "_slab", i);
            sprite_exception_map.put(i + "_wall", i);
        }

        for (String i : bricks) {
            sprite_exception_map.put(i + "_stairs", i + "s");
            sprite_exception_map.put(i + "_slab", i + "s");
            sprite_exception_map.put(i + "_wall", i + "s");
        }

        for (String i : oddBlocks) {
            sprite_exception_map.put(i + "_stairs", i + "_block");
            sprite_exception_map.put(i + "_slab", i + "_block");
        }

        for (String i : colors) {
            sprite_exception_map.put(i + "_carpet", i + "_wool");
            sprite_exception_map.put(i + "_banner", i + "_wool");
            sprite_exception_map.put(i + "_stained_glass_pane", i + "_stained_glass");
            sprite_exception_map.put(i + "_bed", i + "_wool");
            path_change_map.add(i + "_candle");
        }

        for (String i : copper) {
            sprite_exception_map.put(i + "cut_copper_stairs", i + "cut_copper");
            sprite_exception_map.put(i + "cut_copper_slab", i + "cut_copper");
            sprite_exception_map.put(i + "copper_chest", i + "cut_copper");
            sprite_exception_map.put(i + "copper_golem_statue", i + "cut_copper");
            path_change_map.add(i + "copper_door");
            path_change_map.add(i + "copper_lantern");
            path_change_map.add(i + "copper_chain");

            sprite_exception_map.put("waxed_" + i + "chiseled_copper", i + "chiseled_copper");
            sprite_exception_map.put("waxed_" + i + "copper_grate", i + "copper_grate");
            sprite_exception_map.put("waxed_" + i + "cut_copper", i + "cut_copper");
            sprite_exception_map.put("waxed_" + i + "cut_copper_stairs", i + "cut_copper");
            sprite_exception_map.put("waxed_" + i + "cut_copper_slab", i + "cut_copper");
            sprite_exception_map.put("waxed_" + i + "copper_chest", i + "cut_copper");
            sprite_exception_map.put("waxed_" + i + "copper_golem_statue", i + "cut_copper");
            sprite_exception_map.put("waxed_" + i + "copper_bulb", i + "copper_bulb");
            sprite_exception_map.put("waxed_" + i + "copper_trapdoor", i + "copper_trapdoor");
            sprite_exception_map.put("waxed_" + i + "copper_door", i + "copper_door");
            sprite_exception_map.put("waxed_" + i + "lightning_rod", i + "lightning_rod");
            sprite_exception_map.put("waxed_" + i + "copper_bars", i + "copper_bars");
            sprite_exception_map.put("waxed_" + i + "copper_chains", i + "copper_chains");
            sprite_exception_map.put("waxed_" + i + "cut_copper_stairs", i + "cut_copper");
            sprite_exception_map.put("waxed_" + i + "copper_lantern", i + "copper_lantern");
            sprite_exception_map.put("waxed_" + i + "copper_chain", i + "copper_chain");
            path_change_map.add("waxed_" + i + "copper_door");
            path_change_map.add("waxed_" + i + "copper_lantern");
            path_change_map.add("waxed_" + i + "copper_chain");
        }

        for (String i : infested) {
            sprite_exception_map.put("infested_" + i, i);
        }

        sprite_exception_map.put("clock", "clock_00");
        sprite_exception_map.put("compass", "compass_00");
        sprite_exception_map.put("recovery_compass", "recovery_compass_00");
        sprite_exception_map.put("enchanted_golden_apple", "golden_apple");
        sprite_exception_map.put("quartz_stairs", "quartz_block_side");
        sprite_exception_map.put("quartz_slab", "quartz_block_side");
        sprite_exception_map.put("smooth_quartz", "quartz_block_bottom");
        sprite_exception_map.put("smooth_quartz_stairs", "quartz_block_bottom");
        sprite_exception_map.put("smooth_quartz_slab", "quartz_block_bottom");
        sprite_exception_map.put("smooth_sandstone", "sandstone_top");
        sprite_exception_map.put("smooth_sandstone_stairs", "sandstone_top");
        sprite_exception_map.put("smooth_sandstone_slab", "sandstone_top");
        sprite_exception_map.put("smooth_red_sandstone", "red_sandstone_top");
        sprite_exception_map.put("smooth_red_sandstone_stairs", "red_sandstone_top");
        sprite_exception_map.put("smooth_red_sandstone_slab", "red_sandstone_top");
        sprite_exception_map.put("waxed_copper_block", "copper_block");
        sprite_exception_map.put("waxed_exposed_copper", "exposed_copper");
        sprite_exception_map.put("waxed_weathered_copper", "weathered_copper");
        sprite_exception_map.put("waxed_oxidized_copper", "oxidized_copper");
        sprite_exception_map.put("light_weighted_pressure_plate", "gold_block");
        sprite_exception_map.put("heavy_weighted_pressure_plate", "iron_block");
        sprite_exception_map.put("nether_brick_fence", "nether_bricks");
        sprite_exception_map.put("stone_button", "stone");
        sprite_exception_map.put("stone_pressure_plate", "stone");
        sprite_exception_map.put("polished_blackstone_button", "polished_blackstone");
        sprite_exception_map.put("polished_blackstone_pressure_plate", "polished_blackstone");
        sprite_exception_map.put("glass_pane", "glass");
        sprite_exception_map.put("snow_block", "snow");
        sprite_exception_map.put("moss_carpet", "moss_block");
        sprite_exception_map.put("pale_moss_carpet", "pale_moss_block");
        sprite_exception_map.put("tall_grass", "tall_grass_top");
        sprite_exception_map.put("large_fern", "large_fern_top");
        sprite_exception_map.put("sunflower", "sunflower_front");
        sprite_exception_map.put("lilac", "lilac_top");
        sprite_exception_map.put("rose_bush", "rose_bush_top");
        sprite_exception_map.put("peony", "peony_top");
        sprite_exception_map.put("dried_ghast", "happy_ghast_spawn_egg");
        sprite_exception_map.put("dried_kelp_block", "dried_kelp_top");
        sprite_exception_map.put("weeping_vines", "weeping_vines_plant");
        sprite_exception_map.put("twisting_vines", "twisting_vines_plant");
        sprite_exception_map.put("big_dripleaf", "big_dripleaf_top");
        sprite_exception_map.put("small_dripleaf", "big_dripleaf_stem");
        sprite_exception_map.put("calibrated_sculk_sensor", "calibrated_sculk_sensor_input_side");
        sprite_exception_map.put("sculk_sensor", "sculk_sensor_side");
        sprite_exception_map.put("loom", "loom_front");
        sprite_exception_map.put("cartography_table", "cartography_table_top");
        sprite_exception_map.put("crafter", "crafter_top");
        sprite_exception_map.put("anvil", "anvil_top");
        sprite_exception_map.put("chipped_anvil", "chipped_anvil_top");
        sprite_exception_map.put("damaged_anvil", "damaged_anvil_top");
        sprite_exception_map.put("piston", "piston_top");
        sprite_exception_map.put("sticky_piston", "piston_top_sticky");
        sprite_exception_map.put("chest", "oak_planks");
        sprite_exception_map.put("trapped_chest", "oak_planks");
        sprite_exception_map.put("ender_chest", "obsidian");
        sprite_exception_map.put("daylight_detector", "daylight_detector_top");
        sprite_exception_map.put("grindstone", "grindstone_side");
        sprite_exception_map.put("dispenser", "dispenser_front");
        sprite_exception_map.put("dropper", "dropper_front");
        sprite_exception_map.put("observer", "observer_front");
        sprite_exception_map.put("respawn_anchor", "respawn_anchor_side4");
        sprite_exception_map.put("lectern", "lectern_top");
        sprite_exception_map.put("scaffolding", "scaffolding_top");
        sprite_exception_map.put("sprite", "sand_top");
        sprite_exception_map.put("suspicious_gravel", "suspicious_gravel_2");
        sprite_exception_map.put("suspicious_sand", "suspicious_sand_2");
        sprite_exception_map.put("heavy_core", "mace");
        sprite_exception_map.put("tipped_arrow", "arrow");
        sprite_exception_map.put("shield", "spruce_planks");
        sprite_exception_map.put("debug_stick", "stick");
        sprite_exception_map.put("light", "light_15");
        sprite_exception_map.put("test_block", "test_block_start");
        sprite_exception_map.put("trial_spawner", "trial_spawner_side_active");
        sprite_exception_map.put("vault", "vault_front_on");
        sprite_exception_map.put("petrified_oak_slab", "oak_planks");
        sprite_exception_map.put("player_head", "oak_planks");
        sprite_exception_map.put("zombie_head", "oak_planks");
        sprite_exception_map.put("creeper_head", "oak_planks");
        sprite_exception_map.put("skeleton_skull", "oak_planks");
        sprite_exception_map.put("wither_skeleton_skull", "oak_planks");
        sprite_exception_map.put("piglin_head", "oak_planks");
        sprite_exception_map.put("dragon_head", "oak_planks");
        sprite_exception_map.put("crossbow", "crossbow_arrow");

        path_change_map.add("iron_door");
        path_change_map.add("lantern");
        path_change_map.add("soul_lantern");
        path_change_map.add("pointed_dripstone");
        path_change_map.add("candle");
        path_change_map.add("seagrass");
        path_change_map.add("sea_pickle");
        path_change_map.add("kelp");
        path_change_map.add("sugar_cane");
        path_change_map.add("bamboo");
        path_change_map.add("nether_wart");
        path_change_map.add("turtle_egg");
        path_change_map.add("sniffer_egg");
        path_change_map.add("dried_ghast");
        path_change_map.add("pitcher_plant");
        path_change_map.add("repeater");
        path_change_map.add("comparator");
        path_change_map.add("campfire");
        path_change_map.add("soul_campfire");
        path_change_map.add("hopper");
        path_change_map.add("bell");
        path_change_map.add("flower_pot");
        path_change_map.add("wheat");
        path_change_map.add("heavy_core");
        path_change_map.add("shield");
        path_change_map.add("barrier");
        path_change_map.add("structure_void");
        path_change_map.add("light");
    }
}
