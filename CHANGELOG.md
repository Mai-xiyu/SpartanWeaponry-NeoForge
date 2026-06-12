# Changelog

## [1.2.1] - 2026-06-12

### Fixed
- **Mixins never loaded**: The mixin config declared a wrong Java package and was never referenced from `neoforge.mods.toml`, so none of the 11 mixins (armor-piercing damage, arrow-to-quiver pickup, mob spawn weapons, skeleton longbow AI, oil-coating item models) had ever been applied. Both issues are fixed and all mixins now load and apply cleanly.
- **Mixin 从未加载**: mixin 配置声明了错误的 Java 包名，且从未在 `neoforge.mods.toml` 中引用，导致全部 11 个 mixin（破甲伤害、箭矢入箭袋、生物出生武器、骷髅长弓 AI、油涂层物品模型）从未生效。两处均已修复，全部 mixin 现可正常应用。
- **Standard oils zeroed damage**: `OilHandler.useEffect` returned `0` for any non-potion oil, so weapons coated with standard oils (undead, arthropod, etc.) dealt zero damage instead of bonus damage.
- **普通武器油清零伤害**: `OilHandler.useEffect` 对所有非药水油返回 `0`，导致涂抹普通油（亡灵、节肢等）的武器伤害归零而非增伤。
- **Sweep trait now works on 1.21**: sweep damage is applied through the vanilla `SWEEPING_DAMAGE_RATIO` attribute; the old 1.20-era hook targeted a method removed in 1.21.
- **横扫特性适配 1.21**: 横扫加成改为通过原版 `SWEEPING_DAMAGE_RATIO` 属性实现；旧的 1.20 注入点方法在 1.21 已被移除。
- **Quiver `isEmpty` logic was inverted**, breaking ammo auto-equip from quivers.
- **箭袋 `isEmpty` 逻辑反转**，导致箭袋自动装填判断错误。
- **Heavy crossbow trait attributes**: material trait attribute modifiers were built against the removed Forge 1.20 hook and silently dropped; they now use `getDefaultAttributeModifiers`.
- **重弩特性属性**: 材质特性属性加成挂在已移除的 Forge 1.20 接口上被静默丢弃；现改用 `getDefaultAttributeModifiers`。
- **Damage boosted/reduced particles never spawned** (compared against the already-updated event amount).
- **增伤/减伤粒子从不显示**（与已被覆盖的事件数值比较导致恒假）。
- **Copper arrow/bolt lightning**: operator precedence made tipped copper projectiles spawn lightning in any weather; now requires a thunderstorm for all copper projectiles.
- **铜箭/铜弩闪电**: 运算符优先级错误使药水铜弹药在任何天气都能引雷；现统一要求雷暴天气。
- **Melee-block damage filter**: operator precedence error let explosion/fire/projectile damage through the block check for player-sourced damage.
- **近战格挡判定**: 运算符优先级错误使玩家来源的爆炸/火焰/弹射物伤害绕过过滤条件。
- **Heavy crossbow aim crosshair** collapsed to zero size while aiming due to an integer cast applied before scaling.
- **重弩瞄准准星**因先取整后缩放而在瞄准时直接缩为零。
- **Quiver priority-slot packet hardening**: the server now validates the slot index and slot filter; malicious packets can no longer crash the server or bypass quiver slot validation. Stale priority-slot NBT is clamped on read everywhere.
- **箭袋优先槽位包加固**: 服务端现校验槽位索引与槽位过滤器；恶意包无法再使服务器崩溃或绕过箭袋槽位校验。所有读取处对过期优先槽位 NBT 做钳制。
- **Quiver priority button tooltip** now actually renders its hint line.
- **箭袋优先槽位按钮提示**现在能真正显示。
- `WeaponMaterial.colorRGB(byte, byte, byte)` no longer produces wrong colours for components above 127.
- `WeaponMaterial.colorRGB(byte, byte, byte)` 不再因有符号字节而算错大于 127 的颜色分量。
- Tier-based `WeaponMaterial`s now inherit the tier's `getIncorrectBlocksForDrops` tag instead of always mining at wooden level; addons can override via `setIncorrectBlocksForDrops` or the builder.
- 基于 Tier 的 `WeaponMaterial` 现继承 Tier 的 `getIncorrectBlocksForDrops` 标签，不再恒按木制等级判定挖掘掉落；附属可通过 `setIncorrectBlocksForDrops` 或 builder 覆盖。

### Changed
- Migrated JEI integration to the non-deprecated `ISubtypeInterpreter` API and re-enabled tipped arrow/bolt subtype registration.
- JEI 集成迁移到未弃用的 `ISubtypeInterpreter` API，并恢复药水箭/弩箭的 subtype 注册。
- Removed redundant deprecated `Advancement.Builder.parent` calls in recipe builders.
- 移除配方 builder 中冗余的弃用 `Advancement.Builder.parent` 调用。
- Large-scale dead code cleanup (commented-out legacy Forge blocks, unused fields, no-op statements) and de-duplication of throwing-weapon merge logic.
- 大规模清理死代码（注释掉的旧 Forge 代码块、未使用字段、无效语句），去重投掷武器合并逻辑。

### API
- New: `OilEffects.registry()` and `WeaponTraits.registry()` accessors; `WeaponTrait.getModId()/getQuality()`; `WeaponMaterial.colorRGB(int, int, int)`; `WeaponMaterial.Builder.incorrectBlocksForDrops(...)`; `IWeaponTraitContainer.getFirstWeaponTraitWithType` is annotated `@Nullable`.
- 新增: `OilEffects.registry()` 与 `WeaponTraits.registry()` 访问器；`WeaponTrait.getModId()/getQuality()`；`WeaponMaterial.colorRGB(int, int, int)`；`WeaponMaterial.Builder.incorrectBlocksForDrops(...)`；`IWeaponTraitContainer.getFirstWeaponTraitWithType` 标注 `@Nullable`。
- `SpartanWeaponryAPI.init` and `IInternalMethodHandler` are now marked `@ApiStatus.Internal`.
- `SpartanWeaponryAPI.init` 与 `IInternalMethodHandler` 标注为 `@ApiStatus.Internal`。

---

## [1.0.7] - 2026-03-17

### Fixed
- **Dimension Portal Crash**: Fixed `IllegalStateException: Cannot encode empty ItemStack` crash when any projectile entity (arrows, throwing weapons, bolts) enters a dimension portal (Nether, End, Aether, etc.). Root cause: `AbstractArrow.addAdditionalSaveData()` would call `save()` on an empty pickup ItemStack during dimension transitions.
- **维度传送门崩溃**: 修复了任何投射物实体（箭矢、投掷武器、弩箭）进入维度传送门（下界、末地、天境等）时 `IllegalStateException: Cannot encode empty ItemStack` 崩溃。根本原因：`AbstractArrow.addAdditionalSaveData()` 在维度转换时对空的 pickup ItemStack 调用 `save()`。
- **Bolt Persistence**: Fixed bolts losing their type (copper, diamond, netherite, etc.) after chunk unload/reload — `DATA_BOLT` was never saved to or loaded from NBT.
- **弩箭持久化**: 修复了弩箭在区块卸载/重载后丢失类型（铜、钻石、下界合金等）的问题 — `DATA_BOLT` 从未写入/读取 NBT。

---