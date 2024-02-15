import groovyjarjarantlr4.v4.runtime.misc.NotNull
import groovyjarjarantlr4.v4.runtime.misc.Nullable

def input = new File('input.txt').readLines()
Map<Integer, List<Lens>> map = initMap()

for (final String line : input) {
    for (final String str : line.split(',')) {
        Lens lens = new Lens(str)
        List<Lens> alreadyPresent = map.get(lens.getHash())
        final int index = alreadyPresent.findIndexOf { it -> it.key == lens.key }
        if (lens.operation == '-') {
            removeFrom(alreadyPresent, index)
        } else {
            addTo(index, alreadyPresent, lens)
        }
    }
}

int ans = getAnswer(map)
println(ans)

private static void addTo(int index, List<Lens> alreadyPresent, Lens lens) {
    if (index == -1)
        alreadyPresent.add(lens)
    else
        alreadyPresent[index] = lens
}


static int getAnswer(final Map<Integer, List<Lens>> input) {
    input.entrySet().collect { entry ->
        int t = 0
        entry.getValue().eachWithIndex { lens, i ->
            t += (entry.getKey() + 1) * (i + 1) * lens.focalLength
        }
        t
    }.sum() as int
}

private static void removeFrom(List<Lens> alreadyPresent, final int index) {
    if (index > -1)
        alreadyPresent.remove(index)
}

static Map<Integer, List<Lens>> initMap() {

    def res = [:] as Map<Integer, List<Lens>>
    0.upto(255) { key ->
        res.put(key as int, [])
    }
    return res
}


class Lens {

    private static final validOperations = ['-', '=']

    @NotNull
    public final String key

    @NotNull
    public final String operation

    @Nullable
    public final Integer focalLength

    Lens(final @NotNull String inputString) {

        String operation = getOperationChar(inputString)
        this.operation = operation
        def params = inputString.split(operation)
        key = params[0]
        focalLength = inputString[-1] == operation ? null : params[1] as int
    }

    private static String getOperationChar(final String inputString) {

        if (!validOperations.any { inputString.contains(it) })
            throw new IllegalArgumentException("Cannot build a lens from string: ${inputString}")

        for (final String operation : validOperations) {
            if (inputString.contains(operation))
                return operation
        }

        throw new IllegalStateException("Should not happen")
    }

    int getHash() {
        return hash(key)
    }


    private static int hash(final String input) {

        int ans = 0
        for (int i = 0; i < input.length(); i++) {
            ans += (int) input.charAt(i)
            ans *= 17
            ans %= 256
        }
        return ans
    }
}