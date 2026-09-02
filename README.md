<div align="center">

<img src="docs/images/HollowKnight.png" alt="Hollow Knight" width="800"/>

### A faithful recreation of Team Cherry's Hollow Knight, built from scratch in Java

[![Java](https://img.shields.io/badge/Java-21-E76F00?style=for-the-badge&logo=openjdk&logoColor=white)](https://openjdk.org/projects/jdk/21/)
[![libGDX](https://img.shields.io/badge/libGDX-1.14.2-4CAF50?style=for-the-badge)](https://libgdx.com/)
[![Box2D](https://img.shields.io/badge/Box2D-Physics-2196F3?style=for-the-badge)](https://libgdx.com/wiki/extensions/box2d)
[![Gradle](https://img.shields.io/badge/Gradle-8.x-02303A?style=for-the-badge&logo=gradle&logoColor=white)](https://gradle.org/)
[![License](https://img.shields.io/badge/License-MIT-yellow?style=for-the-badge)](LICENSE)

**[🎬 Video Demo](#)** · **[📥 Download](#-installation)** · **[📖 Docs](#-architecture)** · **[🗺️ Maps](#-maps)**

<!-- 🎬 Replace with a real gameplay clip once you upload it -->
<!-- ![Gameplay Demo](docs/gifs/gameplay_demo.gif) -->

</div>

---

## 📋 Table of Contents

- [Overview](#-overview)
- [Key Features](#-key-features)
- [Tech Stack](#-tech-stack)
- [Architecture](#-architecture)
- [Design Patterns](#-design-patterns)
- [Knight & Enemies](#-knight--enemies)
- [Maps](#-maps)
- [Charm System](#-charm-system)
- [HUD & Save System](#-hud--save-system)
- [Installation](#-installation)
- [Controls](#-controls)
- [Screenshots](#-screenshots)
- [Credits](#-credits)

---

## 🌟 Overview

A **ground-up recreation** of Hollow Knight's core gameplay in **Java + libGDX** — full Box2D physics, state-machine AI for multiple enemies and a boss, Tiled map integration, a charm system, save/load, English/French localization, and a polished HUD with animated health and soul meters.

The codebase applies **11+ design patterns** in a clean MVC architecture with a modular entity system.

---

## 🎯 Key Features

| Feature | Details |
|---|---|
| **Physics Engine** | Full Box2D — gravity, collisions, sensors, bitmask filtering |
| **State Machine AI** | 60+ states across Knight, 5 enemy types, Boss, NPC, Camera |
| **Boss Fight** | Phase-based False Knight — dynamic attacks, armor removal, stun |
| **Charm System** | 6 equippable charms (Quick Slash, Heavy Blow, Sharp Shadow, etc.) |
| **Spells** | Vengeful Spirit, Howling Wraiths (AOE), Focus (Heal) |
| **Movement** | Pogo bouncing, wall sliding, wall jumping |
| **3 Unique Maps** | Crystal Peaks, City of Tears, Boss Arena |
| **Save System** | JSON save slots with full state serialization |
| **Localization** | English & French, runtime switching |
| **Extras** | Re-bindable keys, achievements, dev cheat console |

---

## 🛠️ Tech Stack

| Layer | Technology |
|---|---|
| Language | Java 21 |
| Framework | libGDX 1.14.2 |
| Physics | Box2D (gdx-box2d) |
| Backend | LWJGL3 3.4.1 |
| Fonts | gdx-freetype |
| UI | TenPatch 5.2.3 |
| Level Design | Tiled (8×8 tiles) |
| Build | Gradle + construo / GraalVM |

---

## 🏗️ Architecture

```
Lwjgl3Launcher → Main (Game) → MainController (Assets)
                     │
        ┌────────────┴────────────┐
    MainScreen                GameScreen
    (Menu/UI)          (GameController · GameSession · GameHUD)
                                    │
                    ┌───────────────┼──────────────┐
                 Knight           Enemies       Projectiles
              (16 states)        (5 types)     (Vengeful, Wave)
                    │               │               │
                    └───────────────┼───────────────┘
                                    │
                              Box2D World
                 (GameContactListener, 9 sub-listeners)
```

**Class Hierarchy**
```
Entitie (Abstract)
├── Knight               — Player (16 states)
├── Enemy (Abstract)
│   ├── GroundEnemy       — Crawlid, Tiktik, CrystalCrawler
│   ├── HuskHornheadEnemy — Aggressive ground enemy
│   ├── WingedSentry      — Flying charger
│   ├── CrystalGuardian   — Ranged laser enemy
│   ├── FalseKnightEnemy  — Boss (2 phases)
│   └── Zote              — NPC with dialogue
└── Projectile (Abstract)
    ├── VengefulProjectile
    └── WaveProjectile
```

---

## 🎨 Design Patterns

11+ patterns, each solving a specific architectural challenge — **Singleton** (GameSession, AudioManager…), **Factory** (EnemyFactory, ProjectileFactory), **State** (60+ states across 8 machines), **Observer** (9-listener contact system), **Template Method** (state base classes), **Decorator** (Knockback wrapping), **Strategy** (charm effects), **Composite** (listener/UI stacks), **Command** (cheat & input handling), **Registry** (enum-based asset/map registries), and a lightweight **Abstract Factory** for UI modals.

> 📄 Full pattern-by-pattern breakdown with file references lives in [`docs/ARCHITECTURE.md`](docs/ARCHITECTURE.md) — split it out to keep this README readable.

---

## ⚔️ Knight — Movements & Abilities

The Knight has **16 distinct states** covering movement, combat, spells, and special abilities. Each state is fully animated with directional variants.

<div align="center">

### Movement & Traversal

| # | Movement | GIF | Description |
|---|---|---|---|
| 1 | **Run** | <img src="docs/gifs/Run.gif" width="120"/> | Horizontal movement with configurable speed |
| 2 | **Double Jump** | <img src="docs/gifs/DoubleJump.gif" width="120"/> | Second jump in mid-air resets vertical velocity |
| 3 | **Wall Slide** | <img src="docs/gifs/WallSide.gif" width="120"/> | Slows descent when touching a wall (80% velocity retention) |

### Combat — Melee

| # | Attack | GIF | Description |
|---|---|---|---|
| 4 | **Horizontal Slash** | <img src="docs/gifs/Slash.gif" width="120"/> | Forward melee attack (left/right sensor detection) |
| 5 | **Up Slash** | <img src="docs/gifs/UpSlash.gif" width="120"/> | Upward melee attack (overhead sensor detection) |
| 6 | **Down Slash (Pogo)** | <img src="docs/gifs/DownSlash.gif" width="120"/> | Downward attack that bounces off enemies/spikes, resets dash & double jump |

### Combat — Dash

| # | Ability | GIF | Description |
|---|---|---|---|
| 7 | **Dash** | <img src="docs/gifs/DashNormal.gif" width="120"/> | Quick horizontal burst (impulse ±12), locks Y velocity |
| 8 | **Dash (Dashmaster)** | <img src="docs/gifs/DashMaster.gif" width="120"/> | Dash cooldown 1s → 0.1s with the Dashmaster charm |

### Combat — Spells

| # | Spell | GIF | Description |
|---|---|---|---|
| 9 | **Vengeful Spirit** | <img src="docs/gifs/Vengeful.gif" width="120"/> | Horizontal fireball projectile (33 soul) |
| 10 | **Vengeful Spirit (Shadow)** | <img src="docs/gifs/VengefulSpecial.gif" width="120"/> | Void Heart → Shadow version (8 damage, 1.5×) |
| 11 | **Howling Wraiths** | <img src="docs/gifs/ScreamNormal.gif" width="120"/> | AOE scream damaging all nearby enemies (33 soul) |
| 12 | **Howling Wraiths (Shadow)** | <img src="docs/gifs/ScreamSpecial.gif" width="120"/> | Void Heart → 1.5× damage |

### NPC

| Character | GIF | Description |
|---|---|---|
| **Zote** | <img src="docs/gifs/Zote.gif" width="120"/> | Talkative NPC with dialogue, killable (20 HP) |

</div>

**False Knight — Phase Transition**: at HP ≤ 50, armor breaks off, speed doubles, and `ChargeMaceSlam` unlocks.

### Enemy Animation Showcase

| Enemy | Idle | Walk | Attack/Charge | Death |
|---|---|---|---|---|
| **Crawlid** | ![Crawlid Idle](docs/gifs/enemies/crawlid_idle.gif) | ![Crawlid Walk](docs/gifs/enemies/crawlid_walk.gif) | — | ![Crawlid Death](docs/gifs/enemies/crawlid_death.gif) |
| **Husk Hornhead** | ![HH Idle](docs/gifs/enemies/husk_idle.gif) | ![HH Walk](docs/gifs/enemies/husk_walk.gif) | ![HH Attack](docs/gifs/enemies/husk_attack.gif) | ![HH Death](docs/gifs/enemies/husk_death.gif) |
| **Winged Sentry** | ![WS Idle](docs/gifs/enemies/winged_idle.gif) | — | ![WS Charge](docs/gifs/enemies/winged_charge.gif) | ![WS Death](docs/gifs/enemies/winged_death.gif) |
| **Crystal Guardian** | ![CG Idle](docs/gifs/enemies/crystal_guardian_idle.gif) | — | ![CG Laser](docs/gifs/enemies/crystal_guardian_laser.gif) | ![CG Death](docs/gifs/enemies/crystal_guardian_death.gif) |
| **False Knight** | ![FK Idle](docs/gifs/enemies/false_knight_idle.gif) | — | ![FK Slam](docs/gifs/enemies/false_knight_slam.gif) | ![FK Death](docs/gifs/enemies/false_knight_death.gif) |

> Full animation set (idle/walk/turn/knockback/death per enemy) is in [`docs/ENEMY_ANIMATIONS.md`](docs/ENEMY_ANIMATIONS.md).

---

## 🗺️ Maps

| Map | Theme | Enemies | Music |
|---|---|---|---|
| **Crystal Peaks** | Crystalline caverns | Crawlid, Tiktik, CrystalCrawler, Crystal Guardian | `CrystalPeaks.mp3` |
| **City of Tears** | Gothic rain city | Husk Hornhead, Winged Sentry | `CityOfTears.mp3` |
| **Boss Arena** | Combat arena | False Knight (Boss) | `BossFite.mp3` |

Built with **Tiled** (orthogonal, 8×8 tiles), loaded via `TmxMapLoader`, 7+ layers per map. Teleport sensors trigger seamless map transitions with state preservation.

<div align="center">

| Crystal Peaks | City of Tears | Boss Arena |
|---|---|---|
| ![Crystal Peaks](docs/images/CrystalPeaks.png) | ![City of Tears](docs/images/CityOfTears.png) | ![Boss Fight](docs/images/BossFight.png) |

</div>

<!-- 🖼️ Map screenshots below in the Screenshots section -->

---

## 💎 Charm System

<div align="center">

| Charm | Effect |
|---|---|
| <img src="assets/animations/Atlas/charms/quick_slash.png" width="64"/> ⚔️ **Quick Slash** | Attack speed ×2 |
| <img src="assets/animations/Atlas/charms/heavy_blow.png" width="64"/> 🔨 **Heavy Blow** | Knockback force +5 |
| <img src="assets/animations/Atlas/charms/strength.png" width="64"/> 💪 **Unbreakable Strength** | Damage +5 per hit |
| <img src="assets/animations/Atlas/charms/sharp_shadow.png" width="64"/> 🌑 **Sharp Shadow** | Dash → Shadow Dash (damages enemies, passes through) |
| <img src="assets/animations/Atlas/charms/quick_focus.png" width="64"/> 💚 **Quick Focus** | Heal speed ×2 |
| <img src="assets/animations/Atlas/charms/Void_Heart.png" width="64"/> 🖤 **Void Heart** | Soul spells → Shadow spells (1.5× damage) |
| <img src="assets/animations/Atlas/charms/dashmaster.png" width="64"/> 🦅 **Dashmaster** | Dash cooldown 1s → 0.1s |
| <img src="assets/animations/Atlas/charms/soul_catcher.png" width="64"/> 💧 **Soul Catcher** | +10 max soul capacity |

</div>

---

## ❤️ HUD & Save System

- **Heart Icons** — 4-state animation (`FILLED → BREAKING → EMPTY → REFILLING`)
- **Soul Meter** — FrameBuffer-based liquid fill
- **Soul** — +1 per hit, +11 per kill (cap 99); spells cost 33 soul
- **Save** — JSON via `SaveManager` → `GameData` → `saves/slotN.json`, 3 slots, auto-save on map transitions

---

## 🛠️ Installation

**Prerequisites:** Java 21 (JDK), Gradle (wrapper included)

```bash
git clone https://github.com/YousofRahimzadeh/HollowKnight.git
cd HollowKnight

./gradlew lwjgl3:run        # Run the game
./gradlew lwjgl3:dist        # Build distributable JAR
./gradlew lwjgl3:construo    # Package with construo
```

**Native image (GraalVM):**
```bash
./gradlew lwjgl3:nativeImage
./build/construo/lwjgl3/result/hollow-knight
```

---

## 🎮 Controls

| Action | Key |
|---|---|
| Move | A/D or ←/→ |
| Jump | Space |
| Attack | J |
| Look Up/Down | W/S or ↑/↓ |
| Dash | K |
| Vengeful Spirit | L |
| Howling Wraiths | L (while looking down) |
| Focus (Heal) | Hold Shift |
| Pause / Inventory | Esc / I |

All keys are re-bindable in Settings.

---

## 🖼️ Screenshots

<div align="center">

| Main Menu | Select Game | Settings | Guide |
|---|---|---|---|
| ![Main Menu](docs/images/MainMenu.png) | ![Select Game](docs/images/SelectGameMenu.png) | ![Settings](docs/images/SettingMenu.png) | ![Guide](docs/images/GuideMenu.png) |

</div>

---

## 👥 Credits

- **Developer**: [Yousof Rahimzadeh](https://github.com/YousofRahimzadeh)
- **Original Game**: [Hollow Knight](https://www.hollowknight.com/) by Team Cherry
- **Framework**: [libGDX](https://libgdx.com/) · **Physics**: [Box2D](https://box2d.org/)

---

<div align="center">

### ⭐ Star this repo if you found it impressive!

**Built with ❤️ using Java & libGDX**

</div>
