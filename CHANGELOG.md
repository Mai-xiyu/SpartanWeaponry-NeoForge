# Changelog

## [1.0.2] - 2026-02-07

### Changed
- **Mod ID Update**: Changed from `spartanweaponryunofficial` to `spartan_weaponry_unofficial` (using underscores) to comply with Minecraft modding naming conventions.
- **Version Number**: Updated to 1.0.2.

### Added
- **Strengthened Weapon Naming**: Longbows and heavy crossbows created through the addon API now automatically have the `_strengthened` suffix:
  - Longbow: `longbow_{material}_strengthened` (e.g., `longbow_diamond_strengthened`)
  - Heavy Crossbow: `heavy_crossbow_{material}_strengthened` (e.g., `heavy_crossbow_iron_strengthened`)
- **Localization Updates**: Updated all 7 language translation files to support the new naming format.

### Fixed
- **Model Generation System**: Fixed and improved texture path conversion logic in `ModelGenerator.java`, supporting automatic conversion for all 18 material types.
- **Data Generation**: Resolved data generation failures caused by mismatches between weapon registration names and texture file names.
- **Data Namespace References**: Updated 506 namespace references in data and assets JSON files from old to new Mod ID format.

### Technical Changes
- Refactored the `WeaponItemsRanged` constructor in `ModItems.java` to add strengthened weapon suffix logic.
- Extended the material list in `ModelGenerator.java` to cover all vanilla and mod-added material types.
- Renamed resource directory structure to match the new Mod ID (`assets/spartan_weaponry_unofficial` and `data/spartan_weaponry_unofficial`).
- Updated API documentation and resource pack development guide to reflect the naming changes.
- Batch updated all namespace references in:
  - 489 data JSON files (tags, enchantments, recipes, loot tables, advancements)
  - 17 assets JSON files (models)

### Breaking Changes
⚠️ **Incompatible with Old Save Files**: Due to the Mod ID change, items from previous versions will be lost after updating. It is recommended to use this version in new worlds.

---

## [1.0.2] - 2026-02-07

### Changed
- **Mod ID 更新**: 从 `spartanweaponryunofficial` 更改为 `spartan_weaponry_unofficial` (使用下划线)，以符合 Minecraft 模组命名规范。
- **版本号**: 更新到 1.0.2。

### Added
- **强化武器命名**: 通过附加 API 创建的长弓和重型十字弩现在自动添加 `_strengthened` 后缀：
  - 长弓: `longbow_{material}_strengthened` (例如: `longbow_diamond_strengthened`)
  - 重型十字弓: `heavy_crossbow_{material}_strengthened` (例如: `heavy_crossbow_iron_strengthened`)
- **本地化更新**: 更新了所有 7 种语言的翻译文件以支持新的命名格式。

### Fixed
- **模型生成系统**: 修复并改进了 `ModelGenerator.java` 中的纹理路径转换逻辑，支持所有 18 种材料的自动转换。
- **数据生成**: 解决了武器注册名与纹理文件名不匹配导致的数据生成失败问题。
- **数据命名空间引用**: 更新了 data 和 assets JSON 文件中的 506 处命名空间引用，从旧格式更新为新的 Mod ID 格式。

### Technical Changes
- 重构了 `ModItems.java` 中的 `WeaponItemsRanged` 构造函数，添加了强化武器后缀逻辑。
- 扩展了 `ModelGenerator.java` 中的材料列表，覆盖原版和模组添加的所有材料类型。
- 重命名了资源目录结构以匹配新的 Mod ID (`assets/spartan_weaponry_unofficial` 和 `data/spartan_weaponry_unofficial`)。
- 更新了 API 文档和资源包开发指南以反映命名变更。
- 批量更新了所有命名空间引用：
  - 489 个 data JSON 文件（tags、enchantments、recipes、loot tables、advancements）
  - 17 个 assets JSON 文件（models）

### Breaking Changes
⚠️ **不兼容旧版本存档**: 由于 Mod ID 变更，旧版本的物品在更新后会丢失。建议在新世界中使用此版本。
