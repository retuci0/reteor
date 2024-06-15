# Reteor

Shittiest Meteor addon you'll find out there. Just took a 
bunch of missing features and pasted them into here.

## How to use

> To build:
- `git clone https://github.com/retuci0/reteor.git`
- `cd ./reteor/`
- `gradlew build`

> To use:
- Of course make sure to have Meteor Client installed, as well as the [Fabric API](https://www.curseforge.com/minecraft/mc-mods/fabric-api).
- Move the jar to your mods folder (probably `%appdata%/.minecraft/mods`)

## What it adds

### Modules

- **AntiInvis:** Reveals invisible entities.
- **AntiSpectator:** Reveals players in spectator mode.
- **AnvilFont:** Lets you use special characters to imitate some font 
styles while renaming an item on an anvil.
- **AutoTorch:** Automatically uses torches when there is too little light.
- **Dolphin:** Makes you swim superfast, imitating a dolphin's movement.
- **EntityDesync:** Removes the entity you're riding client-side, hence desyncing 
what happens client-side and server-side.
- **EntityNametags:** Renders entities' nametag labels no matter whether you're 
hovering it or not. Idea by edlska.
- **GhostMode:** Makes you a ghost by skipping the death screen and letting you 
move client-side.
- **HitboxDesync:** Desyncs you from your hitbox by moving slightly aside. Useful to 
avoid taking knockback.
- **IceSpeed:** Makes you move a lot faster in ice by changing its slipperiness. 
Very high and very low values will make you move superfast, while medium values 
will make you move slower.
- **JumpDelay:** Lets you customize the amount of jumping cooldown the player has.
- **PortalInvincibility:** Makes you unable to take damage while standing on portals 
by cancelling teleportation packets. Note that you won't be able to move or look around either.
- **ProjectileExploit:** Sends multiple movement packets per tick to make the next
projectile you shoot deal a lot of damage.
- **Racist:** Uh it's not what it looks like I can explain...
- **StrongholdFinder:** Tries to calculate the approximate coordinates of the nearest
Stronghold. It is done by "tracing" two ender eyes' paths and getting the intersection.
To get started just throw an ender eye while facing downwards, then move a hundred blocks
or so and throw another one. The coordinates will appear on chat. Please note that the
coordinates might be up to 200 or so blocks off, so maybe don't dig straight down.

### Other

- **DeathHUD:** Shows your last death's position, including X Y Z and dimension.
