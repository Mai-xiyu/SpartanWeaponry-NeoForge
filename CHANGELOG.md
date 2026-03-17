# Changelog

## [1.0.7] - 2026-03-17

### Fixed
- **Dimension Portal Crash**: Fixed `IllegalStateException: Cannot encode empty ItemStack` crash when any projectile entity (arrows, throwing weapons, bolts) enters a dimension portal (Nether, End, Aether, etc.). Root cause: `AbstractArrow.addAdditionalSaveData()` would call `save()` on an empty pickup ItemStack during dimension transitions.
- **维度传送门崩溃**: 修复了任何投射物实体（箭矢、投掷武器、弩箭）进入维度传送门（下界、末地、天境等）时 `IllegalStateException: Cannot encode empty ItemStack` 崩溃。根本原因：`AbstractArrow.addAdditionalSaveData()` 在维度转换时对空的 pickup ItemStack 调用 `save()`。
- **Bolt Persistence**: Fixed bolts losing their type (copper, diamond, netherite, etc.) after chunk unload/reload — `DATA_BOLT` was never saved to or loaded from NBT.
- **弩箭持久化**: 修复了弩箭在区块卸载/重载后丢失类型（铜、钻石、下界合金等）的问题 — `DATA_BOLT` 从未写入/读取 NBT。

---