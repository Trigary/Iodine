# Iodine

A Forge mod - Bukkit plugin pair that lets plugins show real user interfaces to players.
This is a successor to [NickAc's Lithium project](https://www.spigotmc.org/threads/lithium.274569/).
An API for custom backup methods (eg. inventory based menus) are also to be added.
A single GUI can be viewed by multiple players simultaneously.

This project is under heavy development currently.
It is not in a usable state, everything is expected to change, etc.
Helping hands are welcome.

## Modules

This project consists of multiple subprojects, each with their own roles:

 - **iodine-common:** contains code that both *iodine-api* and *iodine-backend* require
 - **iodine-backend:** contains code that both *iodine-bukkit* and *iodine-forge* require
 - **iodine-bukkit:** the Bukkit plugin part of the project
 - **iodine-api:** the API that plugin developers use, included in *iodine-bukkit*
 - **iodine-client:** contains code that is used by multiple client-side mod projects
 - **iodine-forge-VERSION:** the Forge mod part of the project, for the specified version

The *iodine-forge-VERSION* projects might not be real subprojects:
Forge doesn't support multiple Forge subprojects,
therefore each of these "subprojects" might be a separate project.

To recap which modules depend on which:

 - **iodine-backend:** common
 - **iodine-bukkit:** backend + api (inherited: common)
 - **iodine-api:** common
 - **iodine-client:** backend (inherited: common)
 - **iodine-forge-VERSION:** forge (inherited: common, backend)
