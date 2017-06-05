Version 2.0.0
-------------

\+ Ability to configure which servers are included in the Global Chat.  
\+ Ability to give multiple permissions at once using parent permission.  
\+ Ability to save player data on a MySQL database.  
\+ Ability to register custom placeholders and filters via the API.  
\+ Added /setprefix back to allow overwriting of the user prefix supplied by the Permission Plugin.  
  
\* Rewritten the whole plugin.  
\* Improved performance.  
\* Improved the Chat Filter Systems (Anti-Advertise, Anti-Duplicate, Anti-Swear).  
\* Improved the Reloading feature.  
\* Improved the BungeeChat API.  
\* Improved the way of saving player data files.  
\* Improved the Version Check System.  
\* Improved Permission Plugin system. The plugin will be detected automatically.  
\* Improved ChatLock. It can be used either globally or locally.  

\- Removed Redis support for now. (Will be added back in a later version).  

**Technical details:**

\* Wildcards (\* and ?) can be used in domain names for the Anti-Advertising and Anti-Swear filters.  
\* Regexes (start them with R=) can be used in domain names for the Anti-Advertising and Anti-Swear filters.  
\* Anti-Swear can be made more aggressive by enabling "freeMatching", "leetSpeak", "ignoreSpaces" and "ignoreDuplicateLetters".  
\* Anti-Duplicate can now be configurered to keep track of more than one message per user. (TODO)  
\* Filtering (Anti-Advertising, Anti-Swear, Anti-Duplicate and ChatLock) can be enabled for private messages. (TODO)  
\* Improved chat logging! You can now save the log to a file and customize logging formats.  


Version 1.3.5
-------------

\* Anti-Swear Filter now works case-insensitive.  
\* Slight performance improvements.  


Version 1.3.4.2
---------------

\* Fixed a bug with loading data from External Permission Systems.  


Version 1.3.4.1
---------------

\+ Added a warning message if chosen permissions manager isn't in the server.  


Version 1.3.4
-------------

\+ A lot of love for BrainStone for fixing these bugs!  
\* Fixed the bug that prefixes weren't loaded correctly.  
\* Fixed the bug that the leave message wasn't always displayed.  
\* Updated the integrations with other plugins.  


Version 1.3.3
-------------

\+ Added PowerfulPerms support.  
\* Fixed LuckPerms prefix/suffix bug.  
\* Fixed configuration structure.  
\* Changed Permissions implementation. (Use NONE, BUNGEEPERMS, LUCKPERMS, POWERFULPERMS)  


Version 1.3.2
-------------

\* Fixed messenger bug that displayed the sender message to both he target and sender.  
\* Fixed the color-code bug for formats.  
\* Fixed the local chat bug.  
\* Fixed bug when trying to get LuckPerms prefixes that aren't Present.  
\* Fixed bug where the suffix is always the default BungeeChat suffix.  


Version 1.3.1
-------------

\+ Added a configuration version checker.  
\+ Added the Anti-Duplication feature.  
\+ Added an API function to get the configuration version.  
\* Fixed bug with global chat when using the symbol.  
\* Fixed color bugs with placeholders.  
\* Fixed target is sender bug in the RedisMessanger.  
\* Fixed Local Chat Bug.  


Version 1.3.0
-------------

\+ Added support for LuckPerms.  
\+ Added RedisBungee support for the Messanger Feature.  
\+ Added new placholders: %suffix%, %ping%, %displayname.  
\+ Added a load screen on plugin load.  
\+ Added custom aliases for all commands.  
\+ Added the Ignore feature.  
\* Changed the placholder format to %<placholder>%.  
\* Changed the %player like placholders to %name%.  
\* Changed the structure of the configuration file.  
\* Changed the ##BCHAT special command to the /bungeechat command.  
\* Changed the behavior of the ChatHandler.  
\* Updated the API features to support BungeeChat version 3.  
\* Updated the plugin features to Java 8.  
\* Fixed some typos in messages and variables.  
\* Fixed the bug when someone leaves the network.  
\* Fixed the bug when trying to get a version string logner than 7 chars.  
\* Fixed the typo in the helpop view permission.  
\* Fixed the bug when global chatting with a symbol other than '!'.  
\* Fixed userdata loading bugs.  
\- Removed the /setprefix command from BungeeChat.  
\- Removed all messages related to the /setprefix command.  
\- Removed support for Java 7.  
\- Removed support for old userdata files.  


Version 1.2.5
-------------

\+ Added new API features for developers.  
\* Fixed that features are not loading on older BungeeCord versions.  
\* Changed/Updated things for the upcoming RedisBungee support.  


Version 1.2.4.1
---------------

\* Fixed prefix saving when a player leaves the server.  
\* Fixed a typo.  


Version 1.2.4
-------------

\+ Update checker with the '##BCHAT -ver' command.  
\+ Support more 'strange' characters in messages & formats.  
\+ Added 800+ Top-level domains for the Anti-Advertise.  
\+ Added pre-functions for RedisBungee support.  
\- Removed useless code.  
\- Removed another debug message that I forgot to remove...  
\* Fixed that some features could get a Concurrent Modification Exception.  
\* Fixed bugs/errors when trying to get BungeePerms prefixes.  
\* Fixed possible memory leaks.  
\* Fixed typos in the config.  
\* Improved the Muting System.  


Version 1.2.3
-------------

\- Removed some debug messages which were still in the plugin.  
\* Fixed the Concurrent Modification Exception.  
\* Fixed some typos.  
\* Changed the Plugin Build System.  


Version 1.2.2
-------------

\+ Added tab-complete as a feature, which can be disabled or enabled in the config.  
\* Updated version check system.  
\* Updated API features.  


Version 1.2.1.1
-------------

\* Hotfix for the connection listeners.  


Version 1.2.1
-------------

\+ Added support for BungeePerms prefixes. (You can now use prefixes of zPerms, PEX and other bukkit plugins via this plugin).  
\* Remade the anti-advertise feature as some people got problems with it.  
\* Updated API features.  


Version 1.2.0
-------------

\+ Added chat cancelation check for compatibility with AutheMeBridge for BungeeCord.  
\* Fixed some little bugs.  
\* Cleared up some code.  


Version 1.2.0 (Beta 2)
----------------------

\* Fixed support for UTF-8.  
\* Fixed some problems in mute time saving.  
\* Fixed a little bug in the API.  


Version 1.2.0 (Beta 1)
----------------------

\+ Tempmute Command.
\+ Time Placeholders: %time, %day, %month  
\+ Whitelisted Domains for Anti-Advertise  
\* Fixed some little bugs and cleaned up some code.  


Version 1.1.5
-------------

\+ Added an anti-advertise feature. (Perm to bypass anti-advertise: "bungeechat.antiadd.bypass")  
\+ Added a version checker when the plugin is enabling.  
\* Bug fixes.  


Version 1.1.4.2
---------------

\* Fixed bug with server-switch message.  


Version 1.1.4.1
---------------

\* Fixed bug when replying to an offline player.  


Version 1.1.4
-------------

\+ Added an anti-swear feature. Words can be added in the config!  
\+ Prefixes now can be longer than 1 argument!  
\* Made Bungee Chat more compatible with BungeeChatBukkitBridge.  
\* Some little code cleanup.  


Version 1.1.3
-------------

\+ Added a reload (config) and version function in the plugin. These function are not like the default bukkit command format! (See spoiler bellow).  


Version 1.1.2
-------------

\+ Added a logger system, which logs all chat messages and commands a player sends. You can disable/enable this feature in the config file.  


Version 1.1.1
-------------

\+ Added an API for other developers.  
\* Fixed the bug that switch server didn't give the server name.  
\* Fixed the bug that when you disconnected because you are not whitlisted it doesn't get your server.  


Version 1.1.0
-------------

\+ Players now see thier own HelpOp messages.  
\* Fixed the bug with /r not giving the first word.  
\* Fixed the bug that a 'empty' prefix is not saved.  
\* Updated the placholders of some messages in the config. (%target was used in messages)  


Version 1.1.0 (Beta 2)
----------------------

\* Fixed error when disabeling the plugin.  
\* Fixed error when player is connecting to the server.  


Version 1.1.0 (Beta 1)
----------------------

\+ Added user files to store userdate.  
\+ Added UUID support for userdate.  
\+ Added a permission to allow a player to talk when the chat is locked (bungeechat.chatlock.bypass)  
\+ Added a placeholder to get the uuid of a user.  
\+ Added a server-switch message feature  
\- Removed the mutes.yml and prefixes.yml file.  
\* Recoded the whole plugin  
\* Tested and coded with Bungee Cord for minecraft 1.9  
\* Changed the placeholders  
\* Changed Muteall to ChatLock.  


Version 1.0.11
-------------

\+ Added a /muteall command, to disable chatting on your network.  
\- Removed the /t aliase for the /msg command (caused problems on towny servers)  
\* Updated and tested with the newest bungee version 1109  
\* Fixed bugs when using BungeePerms. (This is tested with the latest BungeePerms version)  


Version 1.0.10
-------------

\+ Added an option to set the global chat as default chat.  
\* The reply command now is a part of the messager feature.  
\* Fixed the cannot find yourself bug.  
\* Changed the format of the feature loading.  
\* Removed some unused code.  


Version 1.0.9.1
---------------

\* Updated and tested with the newest BungeeCord version (1.8 Build:1093)  
\* Some little code cleanup.  


Version 1.0.9
-------------

\+ Hide Mode (Vanish): This will hide you for tabcomplete and the /msg command.  
\+ Message Toggeling  
\+ Helpop Command  


Version 1.0.8
-------------

\+ Tabcomplete for usernames (This may also work on other bungee plugins)!  
\+ You may now customize all messages in the plugin!  
\* Fixed the bug that the color resets on a new line!  
\* Some little code cleanup.  


Version 1.0.7
-------------

\+ You now can toggle global chat mode (You will need the bungeechat.global.toggle perm for this!).  
\* Some little code cleanup  


Version 1.0.6
-------------

\+ Added join message.  
\+ Added leave message.  


Version 1.0.5
-------------

\+ There now is a prefix module with placeholder!  
\+ There now is a local chat manager feature witch allows you to customise you chat.  


Version 1.0.4
-------------

\+ You now can choose the commands you want to block when someone is muted!  
\- Removed some command aliases.  
\* Fixed that someone can't talk in staff chat when muted.  


Version 1.0.3
-------------

\+ Added a /mute command.  
\+ Added a /unmute command.  


Version 1.0.2
-------------

\+ Added custom formats for the chats (with placeholders!)  
\+ Added a Staff Chat feature.  


Version 1.0.1.1
-------------

\* Fixed the none playername bug in the global chat.  


Version 1.0.1
-------------

\+ You can now Enable or Disable features.  
\+ There now is a /global command  


Version 1.0.0
-------------

\+ Added /msg and /reply command.  
