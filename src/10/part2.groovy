Map<Coordinate, String> gameMap = gameMap()
def startCoordinates = gameMap.find { key, value ->
    value == 'S'
}?.key

Finder finder = new Finder(gameMap, getStartingCoordinates(gameMap, startCoordinates))
Finder finder1 = new Finder(gameMap, getStartingCoordinates(gameMap, startCoordinates))
List<Coordinate> vertices = finder.findVertices()
int boundaryPoints = finder1.countBoundaryPoints()
int area = shoelace(vertices)
println("Area: ${area}")
println("Bpoints: ${boundaryPoints}")
println("ANS: ${area - (boundaryPoints / 2) + 1}")

static int shoelace(final List<Coordinate> vertices) {

    List<Integer> xs = vertices.collect { it -> it.getx() }
    List<Integer> ys = vertices.collect { it -> it.gety() }

    int term1 = 0
    int term2 = 0

    for (int i = 0; i < xs.size() - 1; i++) {
        int j = i + 1
        term1 += xs[i] * ys[j]
    }

    for (int i = 1; i < xs.size(); i++) {
        int j = i - 1
        term2 += xs[i] * ys[j]
    }

    def t = term1 - term2
    return t > 0 ? t / 2 : -t / 2
}


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

    List<Coordinate> findVertices() {
        def res = [sPosition]
        def prev = new Coordinate(sPosition.getx(), sPosition.gety())
        def cursor = startingCoordinates[0]
        while (cursor != sPosition) {
            updateCursor(cursor, prev, gameMap.get(cursors[0]))
            if (gameMap.get(cursor) in ['7', 'L', 'F', 'J', 'S'])
                res.add(0, new Coordinate(cursor))
        }
        return res
    }

    int countBoundaryPoints() {
        int ans = 1
        def prevs = [new Coordinate(sPosition.getx(), sPosition.gety())]

        while (cursors[0] != sPosition) {
            updateCursor(cursors[0], prevs[0], gameMap.get(cursors[0]))
            ans++
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

    Coordinate(Coordinate coordinate) {
        this.x = coordinate.x
        this.y = coordinate.y
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
