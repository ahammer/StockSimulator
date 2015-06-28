# StockSimulator
A simple Stock Market simulator for Java and Android

# Usage

Build and install with gradle

    ./gradlew installDebug 

Click Stock Simulator on your Android Device and Enjoy.

Work in Progress. Alpha


TODO List - (Notify me at adamhammer2 (@) gmail.com if you'd like to tackle one of these)

-   Stock name/attribute generator + Improved Event System
    Each Market Item has a "personality" volatility, minimum price, initial price, name.
    Right now they are hard-coded. Preferably I'd like the names to be generated "NTI, LRD, TMC, PHE"
    Ideally, I want them to be generated randomly for each seed, and possibly as a setup option
    to the main menu.

    The event system provides randomly generated events, mainly supply up/demand up events
    to make the market bounce around.

    The names are hard-coded and will need to be made dynamically. The event data model could be updated as well.
    Instead of 3 arrays of int and supply/demand offsets, we can switch to one list of "MarketImpacts" which contains name/supply/demand offsets.

-   Serialization
    High in my priority list. I want to be able to write all the entities to disk, load them from disk.
    I also want to be able to save specifically end of game Player entities for top scores.

-   Icons or Art for the Open source edition
    I'm going to do my own branding, but the open source version could use a skin. If you would like
    to help, please start by doing graphical mockups.

