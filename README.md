[![Build Status](https://travis-ci.org/CSC480-18S/Red-Team.svg?branch=dev)](https://travis-ci.org/CSC480-18S/Red-Team)
[![Waffle.io - Columns and their card count](https://badge.waffle.io/CSC480-18S/Red-Team.svg?columns=all)](https://waffle.io/CSC480-18S/Red-Team)

# Red-Team
Read Team Repository

# How to run

### Follow these steps in this order to get things up and running!

1. Make sure you are in the proper directory (this one)
    1a. All of these commands expect you start from the root of the directory
    1b. These all need to be running in their own terminal windows
2. `cd Database/Dictionary`
    2a. `./mvnw spring-boot:run`
    2b. This can be run concurrently with #3
3. `cd Database/GameDB`
    2a. `./mvnw spring-boot:run`
4. `cd Server\ Backend`
    4a. `npm run start`
5. `cd Server\ Frontend`
    5a. `./gradlew desktop:dist`
    5b. Move the `words.txt` files from the `core/assets` folder at root of the `Server Frontend` folder to the `desktop/build/libs` folder
    5c. `cd desktop/build/libs`
    5d. `java -jar desktop-1.3-beta1.jar -debug -ai`
6. From step 4 of #5 (if you have to, repeat the steps above), `java -jar desktop-1.3-beta1.jar -debug -stats`