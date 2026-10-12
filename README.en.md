# Tom's Storage Category Filters

A **client side** addon for [Tom's Simple Storage Mod](https://www.curseforge.com/minecraft/mc-mods/toms-storage) that adds category buttons to the **Storage Terminal** and the **Crafting Terminal**. One click filters your stored items by type, so you don't have to type anything in the search bar.

Portuguese version: [README.md](README.md)

Source code: [github.com/dantas42/Tom-s-Storage-Category-Filters](https://github.com/dantas42/Tom-s-Storage-Category-Filters/)
Author: dantas42

## Categories

The buttons sit on the right side of the terminal, in a column. Hover over a button to see its name and description.

| Button | Shows |
|---|---|
| **All** | Every item (no category filter) |
| **Weapons** | Swords, axes, bows, crossbows, tridents, maces and other weapons with attack damage |
| **Armor** | Helmets, chestplates, leggings, boots and shields |
| **Tools** | Pickaxes, shovels, hoes, shears, fishing rods and similar tools |
| **Food** | Edible and drinkable food |
| **Blocks** | Items that place a block in the world |
| **Materials** | Ingots, gems, dusts, plates and other resources, plus items that don't fall into any other category |
| **Ores** | Ore blocks and raw ores |
| **Wood** | Logs and planks |
| **Accessories** | Items that Curios accepts in an accessory slot (necklaces, rings, belts and more, from any mod) |
| **Apotheosis** | Equipment with Apotheosis affixes or gem sockets |

When **Apotheosis** is installed, a second column appears next to it with sub-filters:

- **Affixes**: items with affixes.
- **Sockets**: equipment with at least one gem socket, empty or filled.
- **Socketed gems**: equipment with a gem installed.
- **Gems**: loose gems (the gem items themselves).
- **Enchanting**: blocks that give Quanta, Arcana or Eterna to the enchanting table. Shown when **Apothic Enchanting** is installed.

An item can appear in more than one category. A diamond axe, for example, appears in both **Weapons** and **Tools**.

## How to use

1. Open the Storage Terminal or the Crafting Terminal.
2. Click a category to filter. The selected category gets a golden outline.
3. Click **All** to remove the filter.

The filter works together with the text search. For example, with **Weapons** selected and `diamond` in the search bar, only weapons matching "diamond" are shown. The text you typed stays when you switch categories.

## Safety

The addon only changes how the item list is displayed. It doesn't move, remove or duplicate items, doesn't change quantities, and doesn't touch affixes, gems or enchantments. Storage works exactly as before.

## Requirements

| Mod | Version | Required |
|---|---|---|
| Minecraft | 1.21.1 | Yes |
| NeoForge | 21.1.235 or newer | Yes |
| Tom's Simple Storage Mod | 2.4.2 or newer | Yes |
| Apotheosis | 8.9.0 or newer | No (enables the Apotheosis sub-filters) |
| Apothic Enchanting | 1.6.2 or newer | No (enables the Enchanting sub-filter) |
| Curios | 9.5.1 or newer | No (enables the Accessories category) |

The addon is **client side only**: it doesn't need to be installed on the server.

## Installation

1. Install NeoForge 1.21.1 and Tom's Simple Storage Mod.
2. Put `toms_storage_category_filters-1.0.0.jar` in your instance's `mods` folder.
3. Launch the game.

## Known limitations

- The integration relies on internal parts of Tom's Simple Storage 2.4.2's terminal screen. If a major Tom's update changes that screen, the addon may need an update.
- Categories are based on item tags and components. Mods that don't follow those conventions may land in a different category than expected.
- The **Enchanting** category covers only blocks listed in Apothic Enchanting's data. Blocks that provide these values only through code don't show up.

## Credits

- Addon by dantas42.
- Tom's Simple Storage Mod by tom5454.
- Apotheosis by Shadows_of_Fire.

---

## Development

To build from source:

```powershell
.\gradlew.bat clean build
```

Requires **JDK 21**. The JAR is written to `build/libs/`. Unit tests for the categories run with `.\gradlew.bat test`.

How each category identifies items is documented in [`docs/CATEGORIAS.md`](docs/CATEGORIAS.md).

License: All Rights Reserved.
