# Iodine

A Minecraft mod - server plugin pair that adds custom GUIs and overlays.
No more inventory and chat menus!
This is a successor to [NickAc's Lithium project](https://www.spigotmc.org/threads/lithium.274569/).

This project is under heavy development currently.
It is not in a usable state, everything is expected to change, etc.
Helping hands are welcome.

Currently a Forge based client-side and a Bukkit based server-side is being worked on.
Adding Fabric or especially Sponge support shouldn't be much trouble at all thanks
to how the project is structured: Forge and Bukkit are abstracted away.

What this project offers:

 - GUIs that can be viewed and interacted with by multiple players simulatenously
 - Clickable buttons, sliders, checkboxes, linear and grid layouts, etc.
 - Overlays which can be made into minimaps or custom status bars
 - An easy-to-use API for plugin developers to harness this all
 - Security: no remote code execution is done, the bandwidth is minimized as well
 - All this while keeping the Minecraftian feeling!

## Showcase

images, GIFs: to be added

```java
//the code behind one of the samples
```

## Modules

This project consists of multiple subprojects, each with their own role:

 - **iodine-common:** contains code that both *iodine-api* and *iodine-backend* require
 - **iodine-api:** the API that plugin developers use, included in *iodine-server*
 - **iodine-backend:** contains code that both *iodine-server* and *iodine-client* require
 - **iodine-server:** contains code that is used by multiple server-side plugin projects
 - **iodine-client:** contains code that is used by multiple client-side mod projects
 - **iodine-bukkit:** the Bukkit plugin part of the project
 - **iodine-forge-VERSION:** the Forge mod part of the project, for the specified version

The *iodine-forge-VERSION* projects are not be real subprojects due to
ForgeGradle issues, they are actually considered separate projects by Gradle.

To recap which projects depend on which:

 - **iodine-api:** common
 - **iodine-backend:** common
 - **iodine-server:** backend + api (inherited: common)
 - **iodine-client:** backend (inherited: common)
 - **iodine-bukkit:** server (inherited: api, backend, common)
 - **iodine-forge-VERSION:** client (inherited: backend, common)
