# Changelog

## Apotheosis Mod Compatibility Fixes

- **Fixed the issue where Battle Axes cannot be placed in the Reforging Table**: Added `SWORD_DIG` to the `ItemAbility` set of `BATTLEAXE`, allowing it to be recognized as a melee weapon by Apotheosis while retaining its original axe functionalities (stripping bark, removing copper patina, and de-waxing).

- **Fixed the issue where other weapons cannot be placed in the Reforging Table**: Added the `SWORD_DIG` ability to all melee weapons and thrown weapons, including:

    - Melee Weapons: Dagger, Parrying Dagger, Sword, Rapier, Saber, Katana, Greatsword, Club, Gauntlets, War Hammer, Lucerne Hammer, Spear, Halberd, Pike, Lance, Flail, Naginata, Quarterstaff, Scythe

    - Thrown Weapons: Throwing Knife, Battle Axe, Javelin, Boomerang

## Sweep Attack Mechanics Overhaul (Sweep)

- **Modified ** **`PlayerMixin.java`**: Restored and rewrote the interception logic for `EnchantmentHelper.getSweepingDamageRatio`.

- **New Calculation Formula**: Final Sweep Damage = Weapon Base Damage × Sweep Trait Multiplier (e.g., 8.5 × 0.75 = 6.375).

- **Updated Sweep Multiplier Definitions in ** **`WeaponTraits.java`**:

    - `SWEEP_1`: 25% (0.25f)

    - `SWEEP_2`: 50% (0.50f)

    - `SWEEP_3`: 75% (0.75f) - Corresponds to the original "Sweeping Edge III" effect.

## Attack Range (Reach) Adjustments

- **Added New Trait Levels in ** **`WeaponTraits.java`**:

    - `REACH_1_5`: +1.5 Blocks (Magnitude 6.5f)

    - `REACH_2_5`: +2.5 Blocks (Magnitude 7.5f)

- **Updated ** **`ModWeaponTraitTagsProvider.java`**: Adjusted attack range bonuses for various weapons (added to the original base values):

    - **+1.0 Block Increase**:

        - Scythe: 0 -> +1.0 (`REACH_1`)

        - Javelin: 0 -> +1.0 (`REACH_1`)

        - Quarterstaff: 0 -> +1.0 (`REACH_1`)

        - Lance: +1.0 -> +2.0 (`REACH_2`)

        - Spear: +1.0 -> +2.0 (`REACH_2`)

    - **+0.5 Block Increase**:

        - Pike: +2.0 -> +2.5 (`REACH_2_5`)

        - Halberd: +1.0 -> +1.5 (`REACH_1_5`)

        - Glaive: +1.0 -> +1.5 (`REACH_1_5`)



# 更新日志

## Apotheosis 模组兼容性修复

- **修复战斧无法放入重铸台的问题**：为 `BATTLEAXE` 的 `ItemAbility` 集合添加了 `SWORD_DIG`，使其能被 Apotheosis 识别为近战武器，同时保留原有的斧头功能（剥树皮、刮铜锈、去蜡）。

- **修复其他武器无法放入重铸台的问题**：为所有近战武器和投掷武器添加了 `SWORD_DIG` 能力，包括：

- 近战武器：匕首、格挡匕首、剑、刺剑、军刀、武士刀、巨剑、棍棒、拳套、战锤、锤矛、长矛、戟、长枪、骑枪、链枷、薙刀、棍棒、镰刀

- 投掷武器：飞刀、战斧、标枪、回旋镖

## 横扫攻击机制重构（Sweep）

- **`PlayerMixin.java`** **修改 **：恢复并重构了 `EnchantmentHelper.getSweepingDamageRatio` 的拦截逻辑。

- **新增计算公式**：最终横扫伤害 = 武器基础伤害 × 横扫特质倍率（示例：8.5 × 0.75 = 6.375）。

- **`WeaponTraits.java`** **更新  中的横扫倍率定义**：`SWEEP_1`: 25% (0.25f)

- `SWEEP_2`: 50% (0.50f)

- `SWEEP_3`: 75% (0.75f) - 对应原版“横扫之刃III”效果。

## 攻击范围（Reach）调整

- **`WeaponTraits.java`** **在  中新增特质等级**：
`REACH_1_5`: +1.5 格（量级 6.5f）

- `REACH_2_5`: +2.5 格（量级 7.5f）

**`ModWeaponTraitTagsProvider.java`** **更新 **：调整了各类武器的攻击范围加成（在原有基础值上增加）：
**+1.0 格提升**：
镰刀：0 → +1.0（`REACH_1`）

标枪：0 → +1.0（`REACH_1`）

棍棒：0 → +1.0（`REACH_1`）

骑枪：+1.0 → +2.0（`REACH_2`）

长矛：+1.0 → +2.0（`REACH_2`）

**+0.5 格提升**：
长枪：+2.0 → +2.5（`REACH_2_5`）

戟：+1.0 → +1.5（`REACH_1_5`）

薙刀：+1.0 → +1.5（`REACH_1_5`）
> （注：文档部分内容可能由 AI 生成）