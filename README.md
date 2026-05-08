Are YOU looking for a plugin that can easily and conveniently track your progression for any or ALL items in the game? Well you've come to the right place!

This plugin allows a server to collect any set of items or kill any set of mobs. The default list, and the main intent of this plugin is to allow players to collect every obtainable item/mob in Minecraft, but there is also customizability to allow for more creative ways to use this plugin. The main functionalities are the ability to send the items that you collect, and the ability to list your progress. You can use this plugin for everyone with the main list, or have personal lists for every player on the server (or both!).

Versions: (1.17 - 1.21.11)
(Tested More on Current Versions)

A full tutorial on how to use this plugin, as well as community provided item/mob lists are available at this discord: https://discord.gg/zHzFgWX8KW

# **PlaceholderAPI Supported (but not required):**
You can record information about global or personal lists such as the progress, players score, and check if an item has been found or not! For info on the placeholders scroll to the 'Placeholders' section of this description or look in the discord (link right above).

[PlaceholderAPI Link](https://www.spigotmc.org/resources/placeholderapi.6245/)

# **Extensive Config File:**
You are able to change everything about this plugin. Feel free to toggle off commands, global/personal lists, and even the mob/item sections of the plugin. The oportunities are endless.

Some common uses of the config file is to enable auto collect, so players don't need to run a command to submit an item. Also subtraction mode so that the items disappear once submitted.

# **Lang File Support:**
Don't want the plugin in english? No problem! Use the lang file to customize every text output on your server

# **Commands:**
Plugin commands are "/aitem", "/amob", "/atimer", "/areset", and "/ahelp". The item command can execute all of the item related sub-commands. The mob command can execute all of the mob related sub-commands. The timer command can execute all of the timer related sub-commands. The reset command resets the data of the plugin (like turning it off and on again). The help command will display a help message for the plugin overall.

Here is a brief description of all of the commands in the plugin:

- list: Lists the items obtained
- self: Lists your personal item list

- send: Sends the item in your hand

- prog: Displays the total item progress
- player: Displays the item score of the player listed
- check: Displays whether or not the item listed was found or not

- help: Displays the item help message
- settings: Lists the item settings of the plugin

(ADMIN)

- reset: Resets the itemdata

- submit: Submits the item listed as the player listed
- unsubmit: Unsubmits the item listed

- placeholders: Help menu for placeholder setup

Timer also has sub-commands, like start, stop, help, etc.

# Placeholder Info

The '<>' are NOT needed for PLAYERS, if your name is 'B0WSER', input 'B0WSER' in place of <player> not '<B0WSER>'

The '{}' ARE needed for ITEMS/MOBS

### Progress
- %allitems_itemprog% - Fetches the global item progress
- %allitems_mobprog% - Fetches the global mob progress

- %allitems_itemprogpersonal\_<player>% - Fetches the progress of a players personal item list
- %allitems_mobprogpersonal\_<player>% - Fetches the progress of a players personal mob list

- %allitems_itemprogself% - Fetches the item progress of the player running the command
- %allitems_mobprogself% - Fetches the mob progress of the player running the command

### Score
- %allitems_itemscore\_<player>% - Fetches the score of a player in the global item list
- %allitems_mobscore\_<player>% - Fetches the score of a player in the global mob list

- %allitems_itemscoreself% - Fetches the item score of the player running the command
- %allitems_mobprogself% - Fetches the mob score of the player running the command

### Check
- %allitems_itemcheck\_{item}% - Fetches whether or not a specific item has been found on the global item list
- %allitems_mobcheck\_{mob}% - Fetches whether or not a specific mob has been killed on the global mob list

- %allitems_itemcheckpersonal\_{item}\_<player>% - Fetches whether or not a specific item has been found on the personal item list
- %allitems_mobcheckpersonal\_{mob}\_<player>% - Fetches whether or not a specific mob has been killed on the personal mob list

- %allitems_itemcheckself\_{item}% - Fetches whether or not a specific item has been found on the player running the command's item list
- %allitems_mobcheckself\_{mob}% - Fetches whether or not a specific item has been found on the player running the command's mob list

### Completed
- %allitems_itemcompleted% - Fetches whether or not the main item list has been completed
- %allitems_mobcompleted% - Fetches whether or not the main mob list has been completed

- %allitems_itemcompletedpersonal\_<player>% - Fetches whether or not the personal item list has been completed
- %allitems_mobcompletedpersonal\_<player>% - Fetches whether or not the personal mob list has been completed

- %allitems_itemcompletedself% - Fetches whether or not the player running the command's item list has been completed
- %allitems_mobcompletedself% - Fetches whether or not the player running the command's mob list has been completed

### Leaderboard
- %allitems_itemleaderboardplayer\_<place>% - Fetches the player at the specified place for the main item leaderboard
- %allitems_mobleaderboardplayer\_<place>% - Fetches the player at the specified place for the main mob leaderboard

- %allitems_itemleaderboardscore\_<place>% - Fetches the score of the player at the specified place for the main item leaderboard
- %allitems_mobleaderboardscore\_<place>% - Fetches the score of the player at the specified place for the main mob leaderboard

- %allitems_itemleaderboardplayerpersonal\_<place>% - Fetches the player at the specified place for the personal item leaderboard
- %allitems_mobleaderboardplayerpersonal\_<place>% - Fetches the player at the specified place for the personal mob leaderboard

- %allitems_itemleaderboardscorepersonal\_<place>% - Fetches the score of the player at the specified place for the personal item leaderboard
- %allitems_mobleaderboardscorepersonal\_<place>% - Fetches the score of the player at the specified place for the personal mob leaderboard

### Timer
- %allitems_timertime% - Fetches the time of the timer
- %allitems_timeracctive% - Fetches the activity of the timer

# Lite Verions
All Items/Mobs lite versions are available for a better out of the box experience.

[All Items Lite](https://www.spigotmc.org/resources/all-items-lite.118585/)

[All Mobs Lite](https://www.spigotmc.org/resources/all-mobs-lite.130871/)

[All Items Random](https://www.spigotmc.org/resources/all-items-random.130872/)

# **Enjoy!**