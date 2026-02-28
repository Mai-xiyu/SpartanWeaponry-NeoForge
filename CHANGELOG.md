# Changelog

## [1.0.4] - 2026-02-08

### Fixed
- **Loyalty Enchantment Bug**: Fixed throwing weapons (and throwable melee weapons) losing ALL enchantments after being returned via the Loyalty enchantment. Root cause: `removeEnchantments()` in `ThrowingWeaponEntity` was stripping all enchantments on block hit, pickup, and item drop. The method has been removed entirely; duplication is prevented by the existing AmmoUsed merge system.
- **忠诚附魔 Bug**: 修复了可投掷武器（及带有投掷特性的近战武器）在通过忠诚附魔回收后丢失所有附魔效果的问题。根本原因：`ThrowingWeaponEntity` 中的 `removeEnchantments()` 会在命中方块、拾取和掉落时清除所有附魔。该方法已被完全移除，物品复制由现有的 AmmoUsed 合并系统防止。
- **Entity Persistence**: ThrowingWeaponEntity now correctly saves/loads weapon data (`DATA_WEAPON`) and loyalty return level (`DATA_RETURN`) in NBT, preventing weapon loss after chunk unload/reload.
- **实体持久化**: ThrowingWeaponEntity 现在正确地在 NBT 中保存/加载武器数据和忠诚返回等级，防止区块卸载/重载后武器丢失。
- **Non-merge Pickup Path**: Fixed `attemptCatch()` incorrectly adding `NBT_AMMO_USED` tag to non-ThrowingWeaponItem weapons (e.g., melee weapons with Throwable trait), which would cause enchantment loss on subsequent throws.
- **非合并拾取路径**: 修复了 `attemptCatch()` 错误地向非 ThrowingWeaponItem 武器（如带投掷特性的近战武器）添加 `NBT_AMMO_USED` 标签的问题，该问题会导致后续投掷时附魔丢失。

### Added
- **Config Screen**: Added in-game configuration screen for client settings (crosshair options, HUD alignment, integration toggles). Accessible via NeoForge mod list → Config button.
- **配置界面**: 添加了客户端设置的游戏内配置界面（准星选项、HUD 对齐、联动开关）。可通过 NeoForge 模组列表 → 配置按钮访问。

---
