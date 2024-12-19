package me.salmonmoses.days.twentyfour

import me.salmonmoses.days.Day
import me.salmonmoses.days.DayTask
import me.salmonmoses.days.TaskSpec
import me.salmonmoses.utils.*
import org.koin.core.annotation.Single

enum class MapEntityType { Wall, Box, Floor }
class MapEntity(
    val type: MapEntityType,
    var position: Vector,
    val size: Vector
)

class RobotMap(val width: Int, val height: Int) {
    val grid = MutableGrid<MapEntity?>(width, height) { null }

    fun placeEntity(entity: MapEntity) {
        (0..<entity.size.y).forEach { dy ->
            (0..<entity.size.x).forEach { dx ->
                val offset = Vector(dx, dy)
                grid[entity.position + offset] = entity
            }
        }
    }

    fun removeEntity(position: Vector) {
        grid[position]?.let { entity ->
            (0..<entity.size.y).forEach { dy ->
                (0..<entity.size.x).forEach { dx ->
                    val offset = Vector(dx, dy)
                    grid[position + offset] = null
                }
            }
        }
    }

    private fun checkMoveOneEntity(start: Vector, direction: Vector): List<MapEntity> {
        val entityToMove = grid[start]
        if (entityToMove == null) {
            return listOf()
        } else if (entityToMove.type == MapEntityType.Wall) {
            return listOf(entityToMove)
        }
        val obstacles = mutableListOf<MapEntity>()
        (0..<entityToMove.size.y).forEach { dy ->
            (0..<entityToMove.size.x).forEach { dx ->
                val offset = Vector(dx, dy)
                val obstacleOnNewPlace = grid[entityToMove.position + offset + direction]
                if (obstacleOnNewPlace != null && obstacleOnNewPlace !== entityToMove) {
                    obstacles.add(obstacleOnNewPlace)
                }
            }
        }
        return obstacles
    }

    fun tryMove(start: Vector, direction: Vector): Boolean {
        val entityOnStart = grid[start] ?: return true
        val entitiesToCheck = ArrayDeque<MapEntity>()
        val entitiesToMove = ArrayDeque<MapEntity>()
        entitiesToCheck.addLast(entityOnStart)
        for (entity in DequeIterator(entitiesToCheck)) {
            if (entity.type == MapEntityType.Wall) {
                return false
            }
            entitiesToMove.add(entity)
            entitiesToCheck.addAll(checkMoveOneEntity(entity.position, direction))
        }
        for (entity in DequeIterator(entitiesToMove, IteratorDirection.BACKWARD)) {
            removeEntity(entity.position)
            entity.position += direction
            placeEntity(entity)
        }
        return true
    }

    fun printMap(robot: Vector) {
        for (y in 0..<height) {
            for (x in 0..<width) {
                if (x == robot.x && y == robot.y) {
                    print('@')
                    continue
                }
                val char = when (grid[x, y]?.type) {
                    MapEntityType.Wall -> '#'
                    MapEntityType.Box -> 'O'
                    else -> '.'
                }
                print(char)
            }
            println()
        }
    }

    fun entities() = iterator<MapEntity> {
        val visited = mutableSetOf<MapEntity>()
        (0..<height).forEach { y ->
            (0..<width).forEach { x ->
                val entity = grid[x, y]
                if (entity != null && !visited.contains(entity)) {
                    yield(entity)
                    visited.add(entity)
                }
            }
        }
    }
}

@Single
@Day(15)
class Day15 : DayTask {
    override val spec1: TaskSpec
        get() = TaskSpec(
            "##########\n" +
                    "#..O..O.O#\n" +
                    "#......O.#\n" +
                    "#.OO..O.O#\n" +
                    "#..O@..O.#\n" +
                    "#O#..O...#\n" +
                    "#O..O..O.#\n" +
                    "#.OO.O.OO#\n" +
                    "#....O...#\n" +
                    "##########\n" +
                    "\n" +
                    "<vv>^<v^>v>^vv^v>v<>v^v<v<^vv<<<^><<><>>v<vvv<>^v^>^<<<><<v<<<v^vv^v>^\n" +
                    "vvv<<^>^v^^><<>>><>^<<><^vv^^<>vvv<>><^^v>^>vv<>v<<<<v<^v>^<^^>>>^<v<v\n" +
                    "><>vv>v^v^<>><>>>><^^>vv>v<^^^>>v^v^<^^>v^^>v^<^v>v<>>v^v^<v>v^^<^^vv<\n" +
                    "<<v<^>>^^^^>>>v^<>vvv^><v<<<>^^^vv^<vvv>^>v<^^^^v<>^>vvvv><>>v^<<^^^^^\n" +
                    "^><^><>>><>^^<<^^v>>><^<v>^<vv>>v>>>^v><>^v><<<<v>>v<v<v>vvv>^<><<>^><\n" +
                    "^>><>^v<><^vvv<^^<><v<<<<<><^v<<<><<<^^<v<^^^><^>>^<v^><<<^>>^v<v^v<v^\n" +
                    ">^>>^v>vv>^<<^v<>><<><<v<<v><>v<^vv<<<>^^v^>^^>>><<^v>>v^v><^^>>^<>vv^\n" +
                    "<><^^>^^^<><vvvvv^v<v<<>^v<v>v<<^><<><<><<<^^<<<^<<>><<><^^^>^^<>^>v<>\n" +
                    "^^>vv<^v^v<vv>^<><v<^v>^^^>>>^^vvv^>vvv<>>>^<^>>>>>^<<^v>^vvv<>^<><<v>\n" +
                    "v^^>>><<^^<>>^v^<v^vv<>v^<<>^<^v^v><^<<<><<^<v><v<>vv>>v><v^<vv<>v^<<^", "10092"
        )
    override val spec2: TaskSpec?
        get() = TaskSpec(
            "##########\n" +
                    "#..O..O.O#\n" +
                    "#......O.#\n" +
                    "#.OO..O.O#\n" +
                    "#..O@..O.#\n" +
                    "#O#..O...#\n" +
                    "#O..O..O.#\n" +
                    "#.OO.O.OO#\n" +
                    "#....O...#\n" +
                    "##########\n" +
                    "\n" +
                    "<vv>^<v^>v>^vv^v>v<>v^v<v<^vv<<<^><<><>>v<vvv<>^v^>^<<<><<v<<<v^vv^v>^\n" +
                    "vvv<<^>^v^^><<>>><>^<<><^vv^^<>vvv<>><^^v>^>vv<>v<<<<v<^v>^<^^>>>^<v<v\n" +
                    "><>vv>v^v^<>><>>>><^^>vv>v<^^^>>v^v^<^^>v^^>v^<^v>v<>>v^v^<v>v^^<^^vv<\n" +
                    "<<v<^>>^^^^>>>v^<>vvv^><v<<<>^^^vv^<vvv>^>v<^^^^v<>^>vvvv><>>v^<<^^^^^\n" +
                    "^><^><>>><>^^<<^^v>>><^<v>^<vv>>v>>>^v><>^v><<<<v>>v<v<v>vvv>^<><<>^><\n" +
                    "^>><>^v<><^vvv<^^<><v<<<<<><^v<<<><<<^^<v<^^^><^>>^<v^><<<^>>^v<v^v<v^\n" +
                    ">^>>^v>vv>^<<^v<>><<><<v<<v><>v<^vv<<<>^^v^>^^>>><<^v>>v^v><^^>>^<>vv^\n" +
                    "<><^^>^^^<><vvvvv^v<v<<>^v<v>v<<^><<><<><<<^^<<<^<<>><<><^^^>^^<>^>v<>\n" +
                    "^^>vv<^v^v<vv>^<><v<^v>^^^>>>^^vvv^>vvv<>>>^<^>>>>>^<<^v>^vvv<>^<><<v>\n" +
                    "v^^>>><<^^<>>^v^<v^vv<>v^<<>^<^v^v><^<<<><<^<v><v<>vv>>v><v^<vv<>v^<<^", "9021"
        )

    private val directions = mapOf(
        '>' to Vector(1, 0),
        '^' to Vector(0, -1),
        '<' to Vector(-1, 0),
        'v' to Vector(0, 1),
    )

    private fun printMap(map: BaseGrid<MapEntityType>, robot: Vector) {
        for (y in 0..<map.height) {
            for (x in 0..<map.width) {
                if (x == robot.x && y == robot.y) {
                    print('@')
                    continue
                }
                val char = when (map[x, y]) {
                    MapEntityType.Wall -> '#'
                    MapEntityType.Box -> 'O'
                    MapEntityType.Floor -> '.'
                }
                print(char)
            }
            println()
        }
    }

    override fun task1(input: List<String>): String {
        val inputIterator = input.iterator().withIndex()
        val rows = mutableListOf<MutableList<MapEntityType>>()
        var robotPosition = Vector(0, 0)
        while (inputIterator.hasNext()) {
            val line = inputIterator.next()
            if (line.value.isEmpty()) {
                break
            }
            rows.add(line.value.trim().mapIndexed { x, char ->
                when (char) {
                    '#' -> MapEntityType.Wall
                    'O' -> MapEntityType.Box
                    '@' -> {
                        robotPosition = Vector(x, line.index)
                        MapEntityType.Floor
                    }

                    else -> MapEntityType.Floor
                }
            }.toMutableList())
        }
        val map = MutableGrid(rows)
        while (inputIterator.hasNext()) {
            val cmdLine = inputIterator.next().value.trim()
            for (cmd in cmdLine) {
                val direction = directions[cmd]!!
                var nextPosition = robotPosition + direction
                val boxesToMove = ArrayDeque<Vector>()
                var shouldMove = true
                while (map.isValid(nextPosition)) {
                    if (map[nextPosition] == MapEntityType.Wall) {
                        shouldMove = false
                        break
                    } else if (map[nextPosition] == MapEntityType.Box) {
                        boxesToMove.addLast(nextPosition)
                    } else if (map[nextPosition] == MapEntityType.Floor) {
                        break
                    }
                    nextPosition += direction
                }
                if (shouldMove) {
                    for (box in DequeIterator(boxesToMove, IteratorDirection.BACKWARD)) {
                        map[box + direction] = MapEntityType.Box
                        map[box] = MapEntityType.Floor
                    }
                    robotPosition += direction
                }
            }
        }
        return map.filter { map[it] == MapEntityType.Box }.sumOf { it.y * 100 + it.x }.toString()
    }

    override fun task2(input: List<String>): String {
        val inputIterator = input.iterator().withIndex()
        val entities = mutableListOf<MapEntity>()
        var robotPosition = Vector(0, 0)
        var width = 0
        var height = 0
        while (inputIterator.hasNext()) {
            val line = inputIterator.next()
            if (line.value.isEmpty()) {
                break
            }
            width = line.value.length * 2
            height = line.index + 1
            entities.addAll(line.value.trim().mapIndexed { x, char ->
                when (char) {
                    '#' -> MapEntity(MapEntityType.Wall, Vector(x * 2, line.index), Vector(2, 1))
                    'O' -> MapEntity(MapEntityType.Box, Vector(x * 2, line.index), Vector(2, 1))
                    '@' -> {
                        robotPosition = Vector(x * 2, line.index)
                        null
                    }

                    else -> null
                }
            }.filterNotNull())
        }
        val map = RobotMap(width, height)
        entities.forEach {
            map.placeEntity(it)
        }
        while (inputIterator.hasNext()) {
            val cmdLine = inputIterator.next().value.trim()
            for (cmd in cmdLine) {
                val direction = directions[cmd]!!
                val nextPosition = robotPosition + direction
                if (map.tryMove(nextPosition, direction)) {
                    robotPosition += direction
                }
            }
        }
        map.printMap(robotPosition)
        return map.entities()
            .asSequence()
            .filter { it.type == MapEntityType.Box }
            .sumOf { it.position.y * 100 + it.position.x }.toString()
    }
}