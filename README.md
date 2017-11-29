# ECOsim
ICS3U6 project

A simple ecosystem simulation built for the OOP unit.

Thanks to @AdamMc331 for helping with efficiency and style feedback. You can see his suggestions [here](https://github.com/AdamMc331/ecosim).

# Features

### All animals
 * Have gender randomly determined at birth
 * Can mate, child spawns next to parent
    * If there's no room, the child won't spawn
 * Find mates and food nearby
    * Animals do not see the whole board at once
 * Have a maximum health
 * Do not eat when full

### Sheep
 * Prioritize mating over eating nearby plants
 
### Wolves
 * Fight other males nearby
 * Prioritize mating over fighting over eating
 
### Plants
 * Live for a maximum of 10 ticks
 * Have a parabolic function to determine health

### Display
 * Layered images for animal and gender images
 * Displays number of wolves, sheep, and plants

# Documentation for constants
All constants that can be tweaked are at the beginning of EcoSim.java

The default constants are the ones found to be the most balanced

### GRID_SIZE
Integer determining the size of one side of the square world grid. 10-100 recommended

### ITERATIONS
Integer determining the number of ticks the simulation runs for

### TICK_LENGTH
Integer determining the number of milliseconds between each tick

### WORLD_FILL_DENSITY
Percentage of world to be filled at the start. Expressed as a double between 0 and 1.

### WOLF_DENSITY
Percentage of filled world to spawn as wolves. Expressed as a double between 0 and 1.

### SHEEP_DENSITY
Percentage of filled world to spawn as sheep. Expressed as a double between 0 and 1.

### GROWTH_RATE
Chance that any given empty spot will sprout a new plant any given tick. Expressed as a double between 0 and 1.

### MAX_WOLF_HEALTH
Integer determining the maximum health for a wolf

### MAX_SHEEP_HEALTH
Integer determining the maximum health for a sheep

### MIN_MATE_HEALTH_SHEEP
Integer determining minimum health for a sheep to be eligible to mate

### MIN_MATE_HEALTH_WOLF
Integer determining minimum health for a wolf to be eligible to mate

### BABY_HEALTH_SHEEP
Integer determining the health of a newly spawned baby sheep. Damage penalty to parents is also calculated from this.

### BABY_HEALTH_WOLF
Integer determining the health of a newly spawned baby wolf. Damage penalty to parents is also calculated from this.

### STRUGGLE_CHANCE
Chance a wolf will be unable to attack a stronger sheep. Expressed as a double between 0 and 1.

### EATING_HEALTH_RATIO
Double determining how much of an eaten sheep's health the wolf absorbs. It is recommended this stay well below 1.

### FIGHT_DAMAGE_LOSER
Double determining the damage multiplier dealt to a losing wolf. Multiplied by health of the winner to determine damage

### FIGHT_DAMAGE_WINNER
Double determining the damage multiplier dealt to a winning wolf. Multiplied by health of the loser to determine damage

