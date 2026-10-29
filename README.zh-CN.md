# ShaderExit

[English](README.md)

## 这是什么

ShaderExit 帮助其他模组的世界覆盖层逃离由 Iris 加载的光影包。它使用 [Emergency Exit（应急出口）](https://github.com/Shader-Escape/emergency-exit)，把覆盖层单独画出来，再在光影包完成这一帧之后合回画面。

你可以按模组开关光影绕过。比如，你可以让 MiniHUD 的形状绕过光影包，同时让 WorldEdit CUI 的选区保持原来的渲染方式。

## 支持的模组

| 模组 | 绕过哪些内容 |
| --- | --- |
| MiniHUD | 通过 MaLiLib 世界渲染回调绘制的形状、边界等覆盖层 |
| Tweakeroo | 通过 MaLiLib 世界渲染回调绘制的覆盖层 |
| Litematica | 仅 MaLiLib 世界覆盖层回调；**不包含投影区块／原理图地形渲染** |
| TweakerMore | `TweakerMoreRenderEvents` 分发的世界渲染 |
| WorldEdit CUI | 选区线框、网格等世界渲染 |
| Carpet | 共用的形状渲染器，包括 scarpet 图形、TIS 微时序标记；共用一个绕过开关 |
| MaLiLib | 上面没有单独识别的其他 MaLiLib 世界渲染器 |
| Minecraft 原版 | 世界调试渲染器和 Gizmo 图形 |

**Carpet** 这一项同时支持 Carpet 本体和 Carpet TIS Addition。两者的图形共用 Carpet 的渲染器与网络协议，客户端无法可靠区分图形来源，因此合并为一个开关。

MaLiLib 这一项只管剩下的渲染器，不会覆盖 MiniHUD、Tweakeroo、Litematica 各自的开关。这里只接管表中列出的世界渲染回调，模组的菜单和 HUD 照常工作。

## 安装

在**客户端**安装对应 Minecraft 版本的 ShaderExit、Fabric API 和 Emergency Exit。目前 EXIT 需要单独安装。使用光影包还需要 Iris 及其要求的 Sodium 版本。

按需安装你使用的受支持模组，不用全部装上。服务器不用安装 ShaderExit。

项目提供以下 Fabric 版本：**1.21.1、1.21.3、1.21.4、1.21.5、1.21.8、1.21.10、1.21.11、26.1.2、26.2、26.3**。

## 每个模组单独开关

安装 [Mod Menu](https://modrinth.com/mod/modmenu) 后，在模组列表中选择 **ShaderExit → 配置**。进入世界后，也可以输入客户端命令打开同一个界面：

```text
/shaderexit
```

界面列出每个模组的图标、安装状态和绕过开关。已安装的模组用亮色，未安装的用暗色，未安装的项目不能操作。图标从已安装的模组中读取，没有图标时用缩写代替。列表放不下时，用滚轮或底部按钮翻页。

所有开关默认**开启**。关闭某一项后，该模组恢复原来的渲染方式，内容仍然会画。修改后立即保存并生效，不用重启。把鼠标放在开关上，可以查看这一项具体管哪些内容。

设置保存在 `config/shader_exit.json`：

```json
{
  "bypass": {
    "minihud": true,
    "tweakeroo": false,
    "litematica": true,
    "tweakermore": true,
    "worldeditcui": true,
    "carpet": true,
    "malilib": true,
    "minecraft": true
  }
}
```

没写出来的项目默认开启。手动修改文件后需要重启游戏。没有开启光影包时，EXIT 按其正常的回退方式绘制。

## 构建

项目用 Stonecutter 管理 Minecraft 版本。1.21.x 使用 Java 21，26.x 使用 Java 25。

```sh
# 单个版本
bash gradlew :26.3:build

# 所有版本，产物收集到 build/libs/0.1.0/
bash gradlew buildAndCollect
```

EXIT 依赖会在对应 Minecraft 版本的发布版本中选择最新版本。

## 许可

CC0-1.0，见 [LICENSE](LICENSE)。
