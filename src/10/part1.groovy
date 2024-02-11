Map<Coordinate, String> gameMap = gameMap()
def startCoordinates = gameMap.find { key, value ->
    value == 'S'
}?.key

Finder finder = new Finder(gameMap, getStartingCoordinates(gameMap, startCoordinates))
println(finder.search())


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

static List<Coordinate> getStartingCoordinates(Map<Coordinate, String> gameMap, Coordinate start) {
    def res = []
    def xstart = start.getx()
    def ystart = start.gety()

    def up = gameMap.get(new Coordinate(xstart, ystart + 1))
    def bottom = gameMap.get(new Coordinate(xstart, ystart - 1))
    def left = gameMap.get(new Coordinate(xstart - 1, ystart))
    def right = gameMap.get(new Coordinate(xstart + 1, ystart))

    if (up == '|' || up == '7' || up == 'F')
        res.add(new Coordinate(xstart, ystart + 1))

    if (bottom == '|' || bottom == 'J' || bottom == 'L')
        res.add(new Coordinate(xstart, ystart - 1))

    if (left == '-' || left == 'F' || left == 'L')
        res.add(new Coordinate(xstart - 1, ystart))

    if (right == '-' || right == 'J' || right == '7')
        res.add(new Coordinate(xstart + 1, ystart))

    return res
}

class Finder {

    private final Map<Coordinate, String> gameMap

    private final List<Coordinate> startingCoordinates

    private final List<Coordinate> cursors

    private final Coordinate sPosition

    Finder(final Map<Coordinate, String> gameMap, final List<Coordinate> startingCoordinates) {
        this.gameMap = gameMap.clone() as Map<Coordinate, String>
        this.startingCoordinates = startingCoordinates.clone() as List<Coordinate>
        cursors = [startingCoordinates[0], startingCoordinates[1]]
        this.sPosition = gameMap.find { key, value ->
            value == 'S'
        }?.key as Coordinate
    }

    int search() {
        int ans = 0
        def prevs = [new Coordinate(sPosition.getx(), sPosition.gety()),
                     new Coordinate(sPosition.getx(), sPosition.gety())]

        for (int i = 0; i < 4; i++) {
            println(gameMap.get(cursors[0]))
            updateCursor(cursors[0], prevs[0], gameMap.get(cursors[0]))
        }
        return ans
    }

    static void updateCursor(Coordinate cursor, Coordinate prevPosition, String str) {
        if (str == '|') {
            if (prevPosition.gety() < cursor.gety()) {
                cursor.incrementY()
                prevPosition.incrementY()
            } else {
                cursor.decrementY()
                prevPosition.decrementY()
            }
        } else if (str == '-') {
            if (prevPosition.getx() < cursor.getx()) {
                cursor.incrementX()
                prevPosition.incrementX()
            } else {
                cursor.decrementX()
                prevPosition.decrementX()
            }
        } else if (str == 'J') {
            if (prevPosition.getx() < cursor.getx()) {
                cursor.incrementY()
                prevPosition.incrementX()
            } else {
                cursor.decrementX()
                prevPosition.decrementY()
            }
        } else if (str == '7') {
            if (prevPosition.getx() < cursor.getx()) {
                cursor.decrementY()
                prevPosition.incrementX()
            } else {
                cursor.decrementX()
                prevPosition.incrementY()
            }
        } else if (str == 'F') {
            if (prevPosition.gety() < cursor.gety()) {
                cursor.incrementX()
                prevPosition.incrementY()
            } else {
                cursor.decrementY()
                prevPosition.decrementX()
            }
        } else if (str == 'L') {
            if (prevPosition.getx() > cursor.getx()) {
                cursor.incrementY()
                prevPosition.decrementX()
            } else {
                cursor.incrementX()
                prevPosition.decrementY()
            }
        }
    }
}

class Coordinate {

    private int x
    private int y

    Coordinate(Integer x, Integer y) {
        this.x = x
        this.y = y
    }

    void incrementX() {
        x++
    }

    void incrementY() {
        y++
    }

    void decrementX() {
        x--
    }

    void decrementY() {
        y--
    }

    int getx() {
        return x
    }

    int gety() {
        return y
    }

    @Override
    boolean equals(Object obj) {
        if (obj == null)
            return false
        if (!(obj instanceof Coordinate))
            return false
        Coordinate other = obj as Coordinate
        return this.x == other.x && this.y == other.y
    }

    @Override
    int hashCode() {
        x + y
    }

    @Override
    String toString() {
        return x + " " + y
    }
}
