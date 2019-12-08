Long version of this 3 hours [gamejam project](https://diocletian.itch.io/particulitis). Keeping the same gameplay idea


Written in Kotlin. It's centered around an ECS:

- Entities: A bag of components
- Components: That's where the data are
- Systems: That's where the logic is. Which system will act on which entities is defined based on the components present on the entity.

Here is a more comprehensive view of the different SYSTEMS

Arrows would roughly represent a case where order might be important. That's not an inheritance

An entity will only be picked up by the relevant systems. So it might skip quite a few

~~~mermaid
classDiagram

GLOBAL --> Tag
Tag : Entity by Tag()

GLOBAL --> Time
Time : Enemy time()
Time : Game time()
Time : Player time()

ENTITY --> Control
Control : Action
Control : Control
Control : Add action if keyPressed()

Control --> Char Movement
Char Movement: Action
Char Movement: Space
Char Movement: IsPlayer
Char Movement: Execute stored action()

ENTITY --> Target acquisition
Target acquisition: Target
Target acquisition: Follow
Target acquisition: Update target pos if follow()

Target acquisition --> Target seek
Target seek: Target
Target seek: Space
Target seek: Dir
Target seek: Update dir with target()


Target seek --> Dir Movement
Dir Movement: Space
Dir Movement: Dir
Dir Movement: IsPlayer
Dir Movement: Move according to dir()

ENTITY --> Shooter
Shooter: shoot
Shooter: IsPlayer
Shooter: Invoke Shooting Func()

ENTITY --> Ttl
Ttl: Ttl
Ttl: IsPlayer
Ttl: decrease ttl()
Ttl: Invoke end method()

GLOBAL --> Collider
Collider: Space
Collider: Collide
Collider: Resolve collision()

Collider --> Damage
Damage: Collide
Damage: Hp
Damage: Decrease Hp Rule()

DirMovement --> ClampPos
CharMovement --> ClampPos
ClampPos: Space
ClampPos: Keep in bounds()

ClampPos --> LightTrack
LightTrack: Light
LightTrack: Space
LightTrack: Update Light Pos()

LightTrack --> Drawer
Damage --> Drawer
LightTrack --> Drawer
Shooter --> Drawer
Drawer: Draw component
Drawer: Shaders
Drawer: Draw()

Drawer --> UiDisplay
UiDisplay: Text
UiDisplay: Space
UiDisplay: Draw()

Drawer --> ClearActions
ClearActions: clear()

Drawer --> Dead
Dead: Hp
Dead: Collect the dead()

Dead --> Score
Score: Score
Score: Compute score()

Dead --> Spawner
Spawner: Space
Spawner: new enemies avoiding space()
~~~