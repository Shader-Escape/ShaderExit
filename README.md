# ShaderExit

[简体中文](README.zh-CN.md)

## What is this

ShaderExit lets other mods' world overlays escape shader packs loaded by Iris. It uses [Emergency Exit](https://github.com/Shader-Escape/emergency-exit) to draw those overlays separately and merge them back after the shader pack finishes the frame.

Each supported mod has its own switch. You can keep MiniHUD's shapes out of the shader pack while leaving WorldEdit CUI's selection rendering as it was, for example.

## Supported mods

| Mod | What is routed |
| --- | --- |
| MiniHUD | World overlays drawn through MaLiLib's world-render callbacks, including shapes and boundaries |
| Tweakeroo | World overlays drawn through MaLiLib's world-render callbacks |
| Litematica | MaLiLib world-overlay callbacks only; **schematic terrain rendering is not covered** |
| TweakerMore | World rendering dispatched by `TweakerMoreRenderEvents` |
| WorldEdit CUI | Selection outlines, grids and other world rendering |
| MaLiLib | Other MaLiLib world renderers that are not identified above |
| Minecraft | Vanilla world debug renderers and gizmos |

The MaLiLib switch is for the remaining renderers. It does not override MiniHUD, Tweakeroo or Litematica's own switches. Only the world-render callbacks listed here are covered; a supported mod's menus and HUD are left alone.

## Installation

Install the ShaderExit jar for your Minecraft version on the **client**, along with Fabric API and the matching Emergency Exit jar. EXIT is a separate dependency at present. Install Iris and its required Sodium version to use shader packs.

Install whichever supported mods you use. They are optional, and the server does not need ShaderExit.

The project builds for Fabric on **1.21.1, 1.21.3, 1.21.4, 1.21.5, 1.21.8, 1.21.10, 1.21.11, 26.1.2, 26.2 and 26.3**.

## Per-mod switches

With [Mod Menu](https://modrinth.com/mod/modmenu) installed, select **ShaderExit → Configure** in the mod list. You can also open the same screen in a world with:

```text
/shaderexit
```

The screen lists each mod with its icon, installation status and bypass switch. Installed mods are bright; missing mods are dim and their switches are disabled. Icons come from the installed mods themselves, with initials as a fallback. Use the mouse wheel or the page buttons when the list does not fit.

All switches start **on**. Turn a switch off to use that mod's original rendering. It still draws its content. Changes apply and save immediately, without a restart. Hover a switch to see what it covers.

Settings are saved to `config/shader_exit.json`:

```json
{
  "bypass": {
    "minihud": true,
    "tweakeroo": false,
    "litematica": true,
    "tweakermore": true,
    "worldeditcui": true,
    "malilib": true,
    "minecraft": true
  }
}
```

An omitted entry defaults to on. Restart the game after editing the file by hand. Without an active shader pack, EXIT uses its normal fallback rendering.

## Building

Stonecutter manages the Minecraft versions. Use Java 21 for 1.21.x and Java 25 for 26.x.

```sh
# One version
bash gradlew :26.3:build

# All versions, collected into build/libs/0.1.0/
bash gradlew buildAndCollect
```

The EXIT dependency selects the newest release for each Minecraft version.

## License

CC0-1.0. See [LICENSE](LICENSE).
