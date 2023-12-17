import groovyjarjarantlr4.v4.runtime.misc.Tuple2

List<String> input = new File('input.txt').readLines()
long multiplier = 999_999 // Already counting one row/column when calculating manhattan distance
long ans = 0

Set<Tuple2<Integer, Integer>> coords = getCoordsSet(input)
List<Integer> rowIndexesToExpand = getRowIndexes(input)
List<Integer> columnIndexesToExpand = getColumnIndexes(input)

while (!coords.isEmpty()) {
    Tuple2<Integer, Integer> curr = coords.first()
    for (int i = 0; i < coords.size(); i++) {
        Tuple2<Integer, Integer> coord = coords[i]
        ans += manhattanDistance(curr, coord)
        Number rowMultiplier = rowIndexesToExpand.count { index ->
            curr.getItem1() < index && coord.getItem1() > index ||
                    coord.getItem1() < index && curr.getItem1() > index
        }
        Number columnMultiplier = columnIndexesToExpand.count { index ->
            curr.getItem2() < index && coord.getItem2() > index ||
                    coord.getItem2() < index && curr.getItem2() > index
        }
        ans += multiplier * (columnMultiplier + rowMultiplier)
    }
    coords.remove(curr)
}

println(ans)


static List<Integer> getRowIndexes(final List<String> input) {
    List<Integer> result = []
    for (int i = 0; i < input.size(); i++)
        if (input.get(i).matches('\\.+'))
            result.add(i)
    return result
}

static List<Integer> getColumnIndexes(final List<String> input) {
    List<Integer> result = []
    String t = input.get(0)
    for (int i = 0; i < t.length(); i++)
        if (getColumn(input, i).matches('\\.+'))
            result.add(i)
    return result
}

static Set<Tuple2<Integer, Integer>> getCoordsSet(final List<String> input) {
    Set<Tuple2<Integer, Integer>> result = new HashSet<>()
    for (int i = 0; i < input.size(); i++) {
        String line = input.get(i)
        for (int j = 0; j < line.length(); j++) {
            if (line[j] == '#')
                result.add(new Tuple2<Integer, Integer>(i, j))
        }
    }
    return result
}

static int manhattanDistance(final Tuple2<Integer, Integer> a, final Tuple2<Integer, Integer> b) {

    int xdiff = Math.abs(a.getItem1() - b.getItem1())
    int ydiff = Math.abs(a.getItem2() - b.getItem2())
    return xdiff + ydiff
}

static String getColumn(final List<String> input, final int index) {
    return input.collect { String line -> line[index] }.join()
}
