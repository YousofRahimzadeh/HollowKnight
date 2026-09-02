<div align="center">

# Hollow Knight

<img src="docs/images/HollowKnight.png" alt="Hollow Knight" width="600"/>

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

## ⚔️ Knight & Enemies

The Knight has **16 states** (idle, run, jump, dash, shadow dash, pogo, attacks, spells, wall-slide, and more). Enemies range from simple ground patrollers to the multi-phase **False Knight** boss (10 states across 2 phases).

<!-- 🎬 Add animated previews here, e.g.: -->
<!-- ![Knight Combat](docs/gifs/knight_combat.gif) -->

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

<!-- 🖼️ Map screenshots below in the Screenshots section -->

---

## 💎 Charm System

| Charm | Effect |
|---|---|
| ⚔️ **Quick Slash** | Attack speed ×2 |
| 🔨 **Heavy Blow** | Knockback force +5 |
| 💪 **Unbreakable Strength** | Damage +5 per hit |
| 🌑 **Sharp Shadow** | Dash → Shadow Dash (damages enemies, passes through) |
| 💚 **Quick Focus** | Heal speed ×2 |
| 🖤 **Void Heart** | Soul spells → Shadow spells (1.5× damage) |
| 🦅 **Dashmaster** | Dash cooldown 1s → 0.1s |

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

| Crystal Peaks | City of Tears | Boss Arena |
|---|---|---|
| ![Crystal Peaks](docs/screenshots/crystal_peaks.png) | ![City of Tears](docs/screenshots/city_of_tears.png) | ![Boss Arena](docs/screenshots/boss_arena.png) |

| Main Menu | Inventory | Pause Menu |
|---|---|---|
| ![Main Menu](docs/screenshots/main_menu.png) | ![Inventory](docs/screenshots/inventory.png) | ![Pause Menu](docs/screenshots/pause_menu.png) |

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
