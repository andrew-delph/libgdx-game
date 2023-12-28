# Learning Game Engine & Game

## Description
This project is a game engine and game developed for educational purposes, inspired by the mechanics and style of games like Terraria and tower defense genres. The core gameplay revolves around exploring a procedurally generated world, acquiring resources, and constructing defenses to protect against relentless monsters. The longer you survive, the higher your score. The world is created using Perlin noise to ensure a unique experience each time, and the game supports multiplayer functionality. The AI utilizes the A* algorithm for pathfinding, making the enemy behavior more challenging and dynamic.

## Features
- **Procedurally Generated World**: Each playthrough offers a new world layout, thanks to Perlin noise.
- **Survival Mechanics**: Gather resources and build defenses to survive against monsters.
- **Score System**: The longer you live, the higher your score.
- **Multiplayer Support**: Team up with friends to explore and defend.
- **Advanced AI**: Enemies use A* pathfinding to navigate the world intelligently.

## Built With
- **Java**: The primary programming language used.
- **libGDX**: A powerful library that provides a well-established framework for game development.
- **gRPC**: A high-performance, open-source universal RPC framework.
- **Guice**: A lightweight dependency injection framework for Java.

## Controls

The game can be controlled using the following key bindings:

| Action        | Key         |
| ------------- | ----------- |
| Move          | A,S,W,D     |
| Dig           | Space bar + Move |
| Place Dirt    | Shift + Move |
| Spawn Enemy   | F           |

