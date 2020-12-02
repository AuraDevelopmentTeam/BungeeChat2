# [Bungee Chat](https://www.spigotmc.org/resources/bungee-chat.12592/)

[![Current Version](https://badge.fury.io/gh/AuraDevelopmentTeam%2FBungeeChat2.svg)](https://www.spigotmc.org/resources/bungee-chat.12592/history)
[![Build Status](https://gitlab.aura-dev.team/AuraDev/BungeeChat2/badges/master/pipeline.svg)](https://gitlab.aura-dev.team/AuraDev/BungeeChat2/pipelines)
[![Coverage Report](https://gitlab.aura-dev.team/AuraDev/BungeeChat2/badges/master/coverage.svg)](https://gitlab.aura-dev.team/AuraDev/BungeeChat2/pipelines)
[![Codacy Badge](https://api.codacy.com/project/badge/Grade/581b40c0c5ca4ca5a08c83f7076b6d63)](https://www.codacy.com/app/AuraDevelopmentTeam/BungeeChat2?utm_source=github.com&amp;utm_medium=referral&amp;utm_content=AuraDevelopmentTeam/BungeeChat2&amp;utm_campaign=Badge_Grade)
[![License](https://img.shields.io/github/license/AuraDevelopmentTeam/BungeeChat2.svg)](https://github.com/AuraDevelopmentTeam/BungeeChat2/blob/master/LICENSE)
[![Supports Minecraft 1.7 - 1.16](https://img.shields.io/badge/supports_minecraft-1.7_--_1.16-brightgreen.svg)](https://www.spigotmc.org/threads/392/)

[![Bungee Chat Thread](https://proxy.spigotmc.org/e0e5c022701e9b3f55bdfee19e0a5d226f5c53ab?url=http%3A%2F%2Fi.imgur.com%2F9hHniVP.png)](https://www.spigotmc.org/resources/bungee-chat.12592/)

A new way to send private, global or help messages and to do much more with chatting network wide!

## Table of Contents

- [Downloads](#downloads)
- [Wiki](#wiki)
- [Issue Reporting](#issue-reporting)
- [Feature Requests](#feature-requests)
- [Developing with our Plugin](#developing-with-our-plugin)
- [Setting up a Workspace/Compiling from Source](#setting-up-a-workspacecompiling-from-source)
- [Development builds](#development-builds)
- [PGP Signing](#pgp-signing)
- [License](#license)
- [Support](#support)
	- [Getting Support](#getting-support)
	- [Supporting us!](#supporting-us)

## Downloads

You can download all builds from:

- SpigotMC: https://www.spigotmc.org/resources/bungee-chat.12592/
- Maven:
  - Releases: https://maven.aura-dev.team/repository/auradev-releases/
  - Snapshots: https://maven.aura-dev.team/repository/auradev-snapshots/

## Wiki

We have created a wiki for this plugin to help you with various issues and problems. Check
[it](https://github.com/AuraDevelopmentTeam/BungeeChat2/wiki) out!

## [Issue Reporting](https://github.com/AuraDevelopmentTeam/BungeeChat2/issues)

If you found a bug or are even experiencing a crash please report it, so we can fix it. Please first check if a bug report for the issue already
[exists](https://github.com/AuraDevelopmentTeam/BungeeChat2/issues). If not just create a
[new issue](https://github.com/AuraDevelopmentTeam/BungeeChat2/issues/new) and fill out the form.

Please include the following:

* Minecraft version
* Bungee Chat version ("latest" is not a version number)!
* Versions of any plugins potentially related to the issue
* Any relevant screenshots are greatly appreciated.
* For crashes:
  * Steps to reproduce
  * proxy.log.1 from the root folder of the server

*(When creating a new issue, please follow the template)*

## [Feature Requests](https://github.com/AuraDevelopmentTeam/BungeeChat2/issues)

If you want a new feature added, go ahead an open a [new issue](https://github.com/AuraDevelopmentTeam/BungeeChat2/issues/new), remove the existing form and
describe your feature the best you can. The more details you provide the easier, it will be implementing it.  
You can also talk to us on [Discord](https://discord.me/bungeechat).

## Developing with our Plugin

So you want to add support or even develop an add-on for our plugin? You can easily add our API to your development environment, read the
[API documentation](https://github.com/AuraDevelopmentTeam/BungeeChat2/blob/master/BungeeChatApi/README.md).

## Setting up a Workspace/Compiling from Source

* Clone:  
  Clone the repository like this: `git clone --recursive https://github.com/AuraDevelopmentTeam/BungeeChat2.git`
* IDE-Setup:  
  Run [gradle] in the repository root: `./gradlew installLombok <eclipse|idea>`
* Build:  
  Run [gradle] in the repository root: `./gradlew build`. The build will be in `build/libs`
* If obscure Gradle issues are found, try running `./gradlew cleanCache clean`

## Development builds

Between each official release, there are several bleeding edge development builds, which you can also use. But be aware that they might contain unfinished features and therefore won't work properly.

You can find the builds here: https://gitlab.aura-dev.team/AuraDev/BungeeChat2/pipelines

On the right is a download symbol, click that, a dropdown menu will open. Select "build". Then, you'll download a zip file containing all artifacts including the plugin jar.

## PGP Signing

All files will be signed with PGP.  
The public key to verify it can be found in `misc/publicKey.asc`. The signatures of the files will also be found in the maven.

## License

Bungee Chat 2 is licensed under the [GNU General Public License v3.0](https://www.gnu.org/licenses/gpl-3.0.html)

## Support

### Getting Support

If you need help with anything, want to discuss issues or suggestions, or just want to say hi, you can visit our
[Discord Server (https://discord.me/bungeechat)](https://discord.me/bungeechat).

### Supporting us!

Thanks for scrolling down so far!  
We are developing this plugin (and others!) as well as providing constant support for all our plugins free of charge so that as many people as possible can use
it! Since it is still a lot of work we would really appreciate it if you could support us on [Patreon](https://www.patreon.com/AuraDev)!

<p align="center"><a href="https://www.patreon.com/bePatron?u=6416598"><img alt="Become a Patreon" src="https://c5.patreon.com/external/logo/become_a_patron_button.png"></a></p>

## Random Quote

> In software, we rarely have meaningful requirements. Even if we do, the only measure of success that matters is whether our solution solves the customer’s
> shifting idea of what their problem is.
>
> — <cite>Jeff Atwood</cite>
