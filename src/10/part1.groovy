import groovyjarjarantlr4.v4.runtime.misc.Tuple2

Map<Tuple2<Integer, Integer>, String> gameMap = gameMap()
def startCoordinates = gameMap.find { key, value ->
    value == 'S'
}?.key
println(getStartingCoordinates(gameMap, startCoordinates))


static Map<Tuple2<Integer, Integer>, String> gameMap() {
    Map<Tuple2<Integer, Integer>, String> res = new HashMap()
    List<String> input = new File('input.txt').readLines()
    for (int i = 0; i < input.size(); i++) {
        String currLine = input[i]
        for (int j = 0; j < currLine.size(); j++)
            res.put(new Tuple2<Integer, Integer>(j, input.size() - i - 1), currLine[j])
    }
    return res
}

static List<Tuple2<Integer, Integer>> getStartingCoordinates(Map<Tuple2<Integer, Integer>, String> gameMap, Tuple2<Integer, Integer> start) {
    def res = []
    def xstart = start.getItem1()
    def ystart = start.getItem2()

    def up = gameMap.get(new Tuple2(xstart, ystart + 1))
    def bottom = gameMap.get(new Tuple2(xstart, ystart - 1))
    def left = gameMap.get(new Tuple2(xstart - 1, ystart))
    def right = gameMap.get(new Tuple2(xstart + 1, ystart))

    if (up == '|' || up == '7' || up == 'F')
        res.add(new Tuple2(xstart, ystart + 1))

    if (bottom == '|' || bottom == 'J' || bottom == 'L')
        res.add(new Tuple2(xstart, ystart - 1))

    if (left == '-' || left == 'F' || left == 'L')
        res.add(new Tuple2(xstart - 1, ystart))

    if (right == '-' || right == 'J' || right == '/')
        res.add(new Tuple2(xstart + 1, ystart))

    return res
}
