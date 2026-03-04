# Changelog

## [1.0.5] - 2026-03-04

### Fixed
- **Dedicated Server Crash**: Fixed a critical crash on dedicated servers caused by client-only class references (`Screen`, `ConfigurationScreen`, `IConfigScreenFactory`) in the common mod entry point `ModSpartanWeaponry`. These classes are stripped by `RuntimeDistCleaner` on servers, causing `NoClassDefFoundError`.
- **专用服务器崩溃**: 修复了因主模组入口 `ModSpartanWeaponry` 中引用了客户端专属类（`Screen`、`ConfigurationScreen`、`IConfigScreenFactory`）而导致专用服务器崩溃的严重问题。这些类在服务器端被 `RuntimeDistCleaner` 移除，导致 `NoClassDefFoundError`。
- **CuriosHelper.Client on Server**: Added `FMLEnvironment.dist.isClient()` guard to prevent `CuriosHelper.Client::registerReloadListener` from being called on dedicated servers.
- **CuriosHelper.Client 服务端问题**: 添加了 `FMLEnvironment.dist.isClient()` 检查，防止在专用服务器上调用 `CuriosHelper.Client::registerReloadListener`。

### Changed
- **Config Screen Registration**: Moved config screen extension point registration from `ModSpartanWeaponry` to `ClientHelper.registerConfigScreen()`, properly guarded behind a dist check.
- **配置界面注册**: 将配置界面扩展点注册从 `ModSpartanWeaponry` 移至 `ClientHelper.registerConfigScreen()`，并用物理端检查正确保护。

---