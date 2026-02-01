# Changelog

## Sweep Attack Mechanics Overhaul (Sweep)

- **Modified `PlayerMixin.java`**: Restored and rewrote the interception logic for `EnchantmentHelper.getSweepingDamageRatio`.
- **New Calculation Formula**: Final Sweep Damage = Weapon Base Damage × Sweep Trait Multiplier (e.g., 8.5 × 0.75 = 6.375).
- **Updated Sweep Multiplier Definitions in `WeaponTraits.java`**:
  - `SWEEP_1`: 25% (0.25f)
  - `SWEEP_2`: 50% (0.50f)
  - `SWEEP_3`: 75% (0.75f) - Corresponds to the original "Sweeping Edge III" effect.

## Attack Range (Reach) Adjustments

- **Added New Trait Levels in `WeaponTraits.java`**:
  - `REACH_1_5`: +1.5 Blocks (Magnitude 6.5f)
  - `REACH_2_5`: +2.5 Blocks (Magnitude 7.5f)

- **Updated `ModWeaponTraitTagsProvider.java`**: Adjusted attack range bonuses for various weapons (adding to the original base values):
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
