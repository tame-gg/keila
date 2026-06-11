<div align="center">

# Keila

面向高负载 Paper 网络的 tame.gg Minecraft 服务端分支。

[![Build](https://img.shields.io/github/actions/workflow/status/tame-gg/keila/build.yml?style=for-the-badge&label=build&colorA=151a18&colorB=2e8b57)](https://github.com/tame-gg/keila/actions/workflows/build.yml)
[![Java 25](https://img.shields.io/badge/java-25-ef4444?style=for-the-badge&colorA=151a18)](https://adoptium.net/temurin/releases/?version=25)
[![Minecraft](https://img.shields.io/badge/minecraft-26.1.2-f59e0b?style=for-the-badge&colorA=151a18)](https://www.minecraft.net/)

[English](../../README.md) | **中文**

</div>

Keila 是 tame.gg 基于 [Purpur](https://github.com/PurpurMC/Purpur) 的分支，构建于 [Paper](https://papermc.io/) 26.1.2（Java 25）平台之上。它保持 Paper/Purpur 风格的服务器运维与插件模型，并在其之上加入 Keila 自有的运行时安全、发布与运维工具。

> [!NOTE]
> **Keila 现已是 Purpur 26.1.2 分支。** 基础为 [Purpur](https://github.com/PurpurMC/Purpur) `ver/26.1.2`。Keila 早期（基于 Leaf）的异步优化子系统与 `/keila` 运维命令**尚未包含在当前基础中**，它们保存在 git 历史中，待在具备构建能力的环境中移植，详见 [docs/upstream/26.1.2-purpur-rebase.md](../../docs/upstream/26.1.2-purpur-rebase.md)。

## 从源码构建

```bash
git clone https://github.com/tame-gg/keila.git
cd keila

./gradlew applyAllPatches
./gradlew build
```

构建需要 **Java 25** LTS。

## 项目标识

| 字段 | 值 |
| --- | --- |
| 项目 | `keila` |
| Maven group | `gg.tame.keila` |
| Minecraft 目标 | `26.1.2` |
| Java 运行时 | Temurin 25 LTS |

## 致谢

Keila 是 Purpur 的分支，并依托 Paper 分支生态多年的服务端工作：

- [Paper](https://papermc.io/)
- [Purpur](https://github.com/PurpurMC/Purpur)

许可详情请参阅 [LICENSE.md](../../LICENSE.md)。
