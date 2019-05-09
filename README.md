# Iodine

A Forge mod - Bukkit plugin pair that lets plugins show real user interfaces to players.
This is a successor to [NickAc's Lithium project](https://www.spigotmc.org/threads/lithium.277556/).

## Modules

This project consists of multiple subprojects, each with their own roles:

 - **iodine-common:** contains code that both *iodine-bukkit* and *iodine-forge* requires
 - **iodine-bukkit:** the Bukkit plugin part of the project
 - **iodine-api:** the API that plugin developers use, included in *iodine-bukkit*
 - **iodine-forge:** the Forge mod part of the project

## Protocol

### General rules

If a client sends a straight-out invalid message (one that can't be deserialized or one that is
inappropriate, eg. multiple *LoginPacket*s), then all future packets will be ignored from that player.

### Handshake

The client sends a *LoginPacket* as soon as possible when joining as server.
The client has an unlimited time to do it, but plugins can't send custom UIs before the login.
The server respond with either a *LoginSuccessPacket* or a *LoginFailedPacket*.
The login fails if the client and the server are on a different protocol version.
The client receives a message stating whether him or the server is outdated.
