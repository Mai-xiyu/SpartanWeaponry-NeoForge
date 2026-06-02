# Maintainer Documentation

This directory contains local maintainer and addon-author notes for Spartan Weaponry Unofficial.
The public documentation site is maintained in `ProJect_Docs`; these files are kept close to the
code for review and release checks.

## Files

- `addon-authors.md`: stable addon-facing API notes, dependency setup, and compatibility rules.
- `tags.md`: item tag paths for weapon type, material, namespace, and legacy compatibility.
- `datagen.md`: addon data generation helper usage for item models, recipes, and tags.
- `manual-test-checklist.md`: behavior-preserving regression checklist for weapon runtime changes.
- `mixin-risk.md`: mixin purpose and compatibility risk notes for maintainers.

## Maintenance Rules

- Keep source behavior, registry names, tags, recipes, language keys, and resource paths stable
  unless a change explicitly targets them.
- Update the public docs when public API, tag paths, config semantics, or addon workflows change.
- Run `.\gradlew.bat build --console=plain --no-daemon` and
  `.\gradlew.bat runData --console=plain --no-daemon` after code or datagen-facing changes.
- Treat unexpected `src/generated/resources` diffs as a regression until explained.
