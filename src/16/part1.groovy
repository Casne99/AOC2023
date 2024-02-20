import groovyjarjarantlr4.v4.runtime.misc.NotNull
import groovyjarjarantlr4.v4.runtime.misc.Tuple2

Maze maze = new Maze(gameMap())
maze.energize()
int ans = maze.countEnergizedCells()
print(ans)

static Map<Coordinate, String> gameMap() {
    Map<Coordinate, String> res = new HashMap()
    List<String> input = new File('input.txt').readLines()
    for (int i = 0; i < input.size(); i++) {
        String currLine = input[i]
        for (int j = 0; j < currLine.size(); j++)
            res.put(new Coordinate(j, input.size() - i - 1), currLine[j])
    }
    return res
}

class Maze {

    private final Map<Coordinate, String> gameMap

    public static final List<String> mirrors = ['-', '|', '/', '\\']

    private Set<Memo> alreadyStartedFrom = [] as Set<Memo>

    private Set<Coordinate> energizedMirrors = [] as Set<Coordinate>

    private final Map<Coordinate, String> mirrorPositions = [:]


    Maze(final Map<Coordinate, String> gameMap) {
        this.gameMap = gameMap.clone() as Map<Coordinate, String>
        gameMap.entrySet().each {
            def val = it.getValue()
            if (val in mirrors)
                mirrorPositions.put(it.getKey(), val)
        }
    }


    void energize() {
        int beamx = 0
        int beamy = gameMap.keySet().collect { value -> value.gety() }.max()
        final Set<Memo> startingPoints = new HashSet<>()
        startingPoints.add(new Memo(new Coordinate(beamx, beamy), Direction.RIGHT))

        while (!startingPoints.isEmpty()) {

            Memo start = pop(startingPoints)
            alreadyStartedFrom.add(start)
            addNewStartingPoints(start, startingPoints)
        }
    }

    private void addNewStartingPoints(Memo start, final Set<Memo> startingPoints) {
        List<Memo> newStartingPoints = beamFrom(start.coordinate, start.direction)
        for (Memo memo : newStartingPoints) {
            if (!alreadyStartedFrom.contains(memo) && gameMap.containsKey(memo.coordinate))
                startingPoints.add(memo)
        }
    }

    private List<Memo> beamFrom(final Coordinate start, final Direction direction) {
        def str = gameMap.get(start)
        def currCoordinate = start
        if (str in mirrors) {
            energizedMirrors.add(currCoordinate)
            return Mirror.make(str, currCoordinate).reflect(Direction.opposite(direction))
        }
        while ((!(gameMap[currCoordinate] in mirrors) && gameMap[currCoordinate] != null)) {
            if (gameMap[currCoordinate] == '.')
                gameMap[currCoordinate] = '#'
            currCoordinate = updateCurrCoordinate(currCoordinate, direction)
        }
        if (gameMap[currCoordinate] != null) {
            energizedMirrors.add(currCoordinate)
            return Mirror.make(gameMap.get(currCoordinate), currCoordinate).reflect(Direction.opposite(direction))
        }
        return []
    }

    static Coordinate updateCurrCoordinate(final Coordinate coordinate, final Direction direction) {

        int newx = coordinate.getx()
        int newy = coordinate.gety()

        switch (direction) {
            case Direction.RIGHT:
                newx++
                break

            case Direction.LEFT:
                newx--
                break

            case Direction.UP:
                newy++
                break

            case Direction.DOWN:
                newy--
                break
        }
        return new Coordinate(newx, newy)
    }

    int countEnergizedCells() {
        return gameMap.values().count { it == '#' } + energizedMirrors.size()
    }


    static Memo pop(final HashSet<Memo> startingPoints) {
        Memo startingPoint = startingPoints.first()
        startingPoints.remove(startingPoint)
        return startingPoint
    }
}


class Coordinate extends Tuple2<Integer, Integer> {

    Coordinate(Integer item1, Integer item2) {
        super(item1, item2)
    }

    int getx() {
        getItem1()
    }

    int gety() {
        getItem2()
    }

    static Coordinate make(final int x, final int y) {
        return new Coordinate(x, y)
    }
}


class Memo {

    @NotNull
    public final Coordinate coordinate

    @NotNull
    public final Direction direction

    Memo(final @NotNull Coordinate coordinate, final @NotNull Direction direction) {
        this.coordinate = coordinate
        this.direction = direction
    }

    static Memo make(final Coordinate coordinate, final Direction direction) {
        return new Memo(coordinate, direction)
    }

    @Override
    boolean equals(Object obj) {
        if (obj == null)
            return false
        if (!(obj instanceof Memo))
            return false
        Memo other = (Memo) obj
        return coordinate == other.coordinate && direction == other.direction
    }

    @Override
    int hashCode() {
        return Objects.hash(coordinate, direction)
    }
}

enum Direction {

    UP(0),
    RIGHT(1),
    DOWN(2),
    LEFT(3),

    final int value

    Direction(int value) {
        this.value = value
    }

    static Direction opposite(final Direction direction) {
        switch (direction) {
            case UP: return DOWN
            case DOWN: return UP
            case LEFT: return RIGHT
            case RIGHT: return LEFT
        }
    }


}


abstract class Mirror {

    String whichMirror

    Coordinate mirrorPosition

    abstract List<Memo> reflect(Direction comingFrom)

    static Mirror make(final String str, Coordinate coordinate) {
        switch (str) {

            case '-':
                return new FlatMirror(str, coordinate)

            case '|':
                return new VerticalMirror(str, coordinate)

            case '\\':
                return new BackMirror(str, coordinate)

            case '/':
                return new ForthMirror(str, coordinate)

            default:
                throw new IllegalArgumentException("Cannot build a mirror from string: ${str}")
        }
    }

}

class FlatMirror extends Mirror {

    FlatMirror(@NotNull String str, Coordinate coordinate) {
        if (str != '-')
            throw new IllegalArgumentException()
        this.whichMirror = str
        this.mirrorPosition = coordinate
    }

    @Override
    List<Memo> reflect(Direction comingFrom) {
        if (comingFrom in [Direction.DOWN, Direction.UP])
            return [Memo.make(Coordinate.make(mirrorPosition.getx() - 1, mirrorPosition.gety()), Direction.LEFT),
                    Memo.make(Coordinate.make(mirrorPosition.getx() + 1, mirrorPosition.gety()), Direction.RIGHT)]
        if (comingFrom == Direction.LEFT)
            return [Memo.make(Coordinate.make(mirrorPosition.getx() + 1, mirrorPosition.gety()), Direction.RIGHT)]
        return [Memo.make(Coordinate.make(mirrorPosition.getx() - 1, mirrorPosition.gety()), Direction.LEFT)]
    }
}

class VerticalMirror extends Mirror {

    VerticalMirror(String str, Coordinate coordinate) {
        if (str != '|')
            throw new IllegalArgumentException()
        this.whichMirror = str
        this.mirrorPosition = coordinate
    }

    @Override
    List<Memo> reflect(Direction comingFrom) {
        if (comingFrom in [Direction.RIGHT, Direction.LEFT])
            return [Memo.make(Coordinate.make(mirrorPosition.getx(), mirrorPosition.gety() + 1), Direction.UP),
                    Memo.make(Coordinate.make(mirrorPosition.getx(), mirrorPosition.gety() - 1), Direction.DOWN)]
        if (comingFrom == Direction.DOWN)
            return [Memo.make(Coordinate.make(mirrorPosition.getx(), mirrorPosition.gety() + 1), Direction.UP)]
        return [Memo.make(Coordinate.make(mirrorPosition.getx(), mirrorPosition.gety() - 1), Direction.DOWN)]
    }
}

class BackMirror extends Mirror {

    BackMirror(String str, Coordinate coordinate) {
        if (str != '\\')
            throw new IllegalArgumentException()
        this.whichMirror = str
        this.mirrorPosition = coordinate
    }

    @Override
    List<Memo> reflect(Direction comingFrom) {
        switch (comingFrom) {
            case Direction.UP:
                return [Memo.make(Coordinate.make(mirrorPosition.getx() + 1, mirrorPosition.gety()), Direction.RIGHT)]

            case Direction.DOWN:
                return [Memo.make(Coordinate.make(mirrorPosition.getx() - 1, mirrorPosition.gety()), Direction.LEFT)]

            case Direction.RIGHT:
                return [Memo.make(Coordinate.make(mirrorPosition.getx(), mirrorPosition.gety() + 1), Direction.UP)]

            case Direction.LEFT:
                return [Memo.make(Coordinate.make(mirrorPosition.getx(), mirrorPosition.gety() - 1), Direction.DOWN)]
        }
    }
}

class ForthMirror extends Mirror {

    ForthMirror(String str, Coordinate coordinate) {
        if (str != '/')
            throw new IllegalArgumentException()
        this.whichMirror = str
        this.mirrorPosition = coordinate
    }

    @Override
    List<Memo> reflect(Direction comingFrom) {
        switch (comingFrom) {
            case Direction.UP:
                return [Memo.make(Coordinate.make(mirrorPosition.getx() - 1, mirrorPosition.gety()), Direction.LEFT)]

            case Direction.DOWN:
                return [Memo.make(Coordinate.make(mirrorPosition.getx() + 1, mirrorPosition.gety()), Direction.RIGHT)]

            case Direction.RIGHT:
                return [Memo.make(Coordinate.make(mirrorPosition.getx(), mirrorPosition.gety() - 1), Direction.DOWN)]

            case Direction.LEFT:
                return [Memo.make(Coordinate.make(mirrorPosition.getx(), mirrorPosition.gety() + 1), Direction.UP)]
        }
    }
}
