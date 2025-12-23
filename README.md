
# Command Rod MOD

A Mod for Minecraft 1.21.10

# Introduction

クリックでコマンドを実行できるアイテム「コマンドロッド」を追加します。  
マップ制作者をクリック検知の苦悩から解放します。

# Environment

* Minecraft 1.21.10
* NeoForge 21.10.64

# How to use

以下のコマンドを実行すると、「使用すると"tp 0 64 0"を実行するコマンドロッド」を入手できます。

```declarative
give @s commandrod:command_rod[commandrod:command="tp 0 64 0"]
```
```declarative
give @s commandrod:command_rod[commandrod:command={right:"tp 0 64 0"}]
```

どちらでも結果は同じです。  
左クリックでコマンドを実行したい場合は、以下のコマンドを実行してください。　　

```declarative
give @s commandrod:command_rod[commandrod:command={left:"tp 0 64 0"}]
```

leftの値を指定すると、このアイテムを持った状態での左クリックによる破壊、攻撃はできなくなります。

また、この場合、右クリックではコマンドは実行されません。  
以下のコマンドを実行すれば、右クリックと左クリックで別々のコマンドを実行するコマンドロッドを入手できます。

```declarative
give @s commandrod:command_rod[commandrod:command={right:"tp 0 64 0",left:"xp add @s 100"}]
```

もちろん、同じコマンドを設定すれば、右クリックでも左クリックでも同じコマンドを実行するアイテムになります。

バニラのアイテムモデル機能を使えば、個別に見た目を変えられます！

## Licenses

Code: All Rights Reserved

* このModは、自由にMod packに同梱することができます。
* このModを導入したゲームプレイ映像については、収益化を含めて自由に公開することができます。

## 免責事項

このModの使用その他によって生じる、いかなる損害についても、作者は一切の責任を負いません。  
自己責任で導入してください。