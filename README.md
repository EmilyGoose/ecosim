# ECOsim
ICS3U6 project

A simple ecosystem simulation built for the OOP unit.

Thanks to @AdamMc331 for helping with efficiency and style feedback. You can see his suggestions [here](https://github.com/AdamMc331/ecosim).

#Documentation for constants
All constants that can be tweaked are at the beginning of EcoSim.java

###GRID_SIZE
Integer determining the size of one side of the square world grid. 10-100 recommended

###ITERATIONS
Integer determining the number of ticks the simulation runs for

###TICK_LENGTH
Integer determining the number of milliseconds between each tick

###WORLD_FILL_DENSITY
Percentage of world to be filled at the start. Expressed as a double between 0 and 1.

###WOLF_DENSITY
Percentage of filled world to spawn as wolves. Expressed as a double between 0 and 1.

###SHEEP_DENSITY
Percentage of filled world to spawn as sheep. Expressed as a double between 0 and 1.

###GROWTH_RATE
Chance a new plant will spawn that turn. Values greater than 1 guarantee a certain number of plants.

Example: 20.4 guarantees 20 plants per turn, plus 40% chance of a 21st plant

###MAX_WOLF_HEALTH
Integer determining the maximum health for a wolf

###MAX_SHEEP_HEALTH
Integer determining the maximum health for a sheep

###MIN_MATE_HEALTH_SHEEP
Integer determining minimum health for a sheep to be eligible to mate

###MIN_MATE_HEALTH_WOLF
Integer determining minimum health for a wolf to be eligible to mate

###BABY_HEALTH_SHEEP
Integer determining the health of a newly spawned baby sheep. Damage penalty to parents is also calculated from this.

###BABY_HEALTH_WOLF
Integer determining the health of a newly spawned baby wolf. Damage penalty to parents is also calculated from this.

###STRUGGLE_CHANCE
Chance a wolf will be unable to attack a weaker sheep. Expressed as a double between 0 and 1.

###FIGHT_DAMAGE_LOSER
Double determining the damage multiplier dealt to a losing wolf. Multiplied by health of the winner to determine damage

###FIGHT_DAMAGE_WINNER
Double determining the damage multiplier dealt to a winning wolf. Multiplied by health of the loser to determine damage

