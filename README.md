[![](https://jitpack.io/v/sakura-ryoko/malilib.svg)](https://jitpack.io/#sakura-ryoko/malilib)

malilib
==============
malilib is a library mod used by masa's LiteLoader mods. It contains some common code previously
duplicated in most of the mods, such as multi-key capable keybinds, configuration GUIs etc.

Multiloader (Fabric / NeoForge)
===============================
このリポジトリは Sakura-Ryoko 版 malilib を Multiloader 化したもの。
共通コードは `common`、Fabric 用は `fabric`、NeoForge 用は `neoforge` に分かれてる。
追加クラスは `com.sayuki.malilib` パッケージに置いて、既存の再利用クラスは `fi.dy.masa.malilib` のままにしてる。

Compiling
=========
* Clone the repository
* Open a command prompt/terminal to the repository directory
* run 'gradlew build'  (common / fabric / neoforge 全部ビルドされる)
* Fabric jar は `fabric/build/libs/`、NeoForge jar は `neoforge/build/libs/`、Common jar は `common/build/libs/` に出る

Structure
=========
* `common` - ローダー非依存の共通コード、Platform abstraction は `com.sayuki.malilib.platform` にある
* `fabric` - Fabric 用エントリーポイント (`com.sayuki.malilib.fabric.MaLiLibFabric`) と ModMenu 連携
* `neoforge` - NeoForge 用エントリーポイント (`com.sayuki.malilib.neoforge.MaLiLibNeoForge`) と mods.toml
