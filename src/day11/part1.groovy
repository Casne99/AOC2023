import groovyjarjarantlr4.v4.runtime.misc.Tuple2

List<String> input = new File('input.txt').readLines()
int ans = 0

Set<Tuple2<Integer, Integer>> coords = getCoordsSet(newInput(input))
while (!coords.isEmpty()) {
    Tuple2<Integer, Integer> curr = coords.first()
    ans += coords.collect { coord ->
        manhattanDistance(curr, coord)
    }       .sum()
    coords.remove(curr)
}

println(ans)

static List<String> newInput(final List<String> originalInput) {
    List<String> result = []
    List<Integer> indexes = []
    for (int i = 0; i < originalInput.size(); i++) {
        String line = originalInput.get(i)
        result.add(line)
        if (line.matches('\\.+'))
            result.add(line)
    }
    String t = result.get(0)
    for (int i = 0; i < t.length(); i++)
        if (getColumn(result, i).matches('\\.+'))
            indexes.add(i)

    addColumns(result, indexes)
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

static void addColumns(final List<String> input, final List<Integer> indexes) {
    for (int i = 0; i < indexes.size(); i++) {
        for (int j = 0; j < input.size(); j++) {
            input[j] = addColumn(input[j], indexes.reverse()[i])
        }
    }
}

static String addColumn(final String input, final int index) {
    return input.substring(0, index) + '.' + input.substring(index, input.length())
}
