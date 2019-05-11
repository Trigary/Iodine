# Iodine

A Forge mod - Bukkit plugin pair that lets plugins show real user interfaces to players.
This is a successor to [NickAc's Lithium project](https://www.spigotmc.org/threads/lithium.274569/).
The use of a variation of [Tom's ChatMenuAPI](https://github.com/timtomtim7/ChatMenuAPI)
as a native backup method for those players who are not using the mod is planned.
An API for custom backup methods (eg. Inventory based menus) are also to be added.
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
 - **iodine-forge:** the Forge mod part of the project

To recap which modules depend on which:

 - **iodine-backend:** common
 - **iodine-bukkit:** backend + api (inherited: common)
 - **iodine-api:** common
 - **iodine-forge:** backend (inherited: common)

## Protocol

### General rules

If a client sends a straight-out invalid message (one that can't be deserialized or handled),
then all future packets will be ignored from that player.

### Handshake

The client sends a *ClientLoginPacket* as soon as possible when joining the server.
The client has an unlimited time to do it, but plugins can't send custom GUIs before this login.
The server responds with either a *ServerLoginSuccessPacket* or a *ServerLoginFailedPacket*.
The login fails if the client and the server are on different protocol versions.
The client receives a message stating whether it or the server is outdated.
