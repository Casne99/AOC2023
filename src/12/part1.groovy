int ans = 0
List<String> input = new File('input.txt').readLines()
input.each { line ->
    def splitted = line.split()
    def collapsed = collapse(splitted[0]).findAll('\\?+')
    ans += countMatches(collapsed, splitted[1].split(',').collect(it -> it as int))
}

// println(combinations('???'))
//println("Answer is ${ans}")
//def a = ['.', '.', '.', '#', '#', '#'].subsequences().findAll(it -> it.size() == 3).collect { it.join() }
//def b = ['.', '#', '.', '#', '.', '#'].subsequences().findAll(it -> it.size() == 3).collect { it.join() }
//a.addAll(b)
//println(a as Set<String>)
println('#'.repeat(3).collect(it -> it))



static int countMatches(final List<String> unknowns, final List<Integer> broken) {
    return 0
}

static String collapse(final String str) {
    return str.replaceAll('\\.+', '.')
}

static Set<String> combinations(final String input) {

    int len = positionsOf('?', input).size()

    def a = '.'.repeat(len).collect(it -> it)
            .subsequences().findAll(it -> it.size() == len)
            .collect { it.join() }

    def b = '#'.repeat(len).collect(it -> it)
            .subsequences().findAll(it -> it.size() == len)
            .collect { it.join() }

    a.addAll(b)

    def c = '.#'.repeat(len).collect { it -> it}
            .subsequences().findAll(it -> it.size() == len)
            .collect { it.join() }

    a.addAll(c)
    return a
}


static List<Integer> positionsOf(final String candidate, final String input) {
    return input.findIndexValues { it -> it == candidate }
}

static String replaceQuestionMarks(final String input, final List<Integer> positions) {

    def res = ''
    int currMark = 0
    for (int i = 0; i < input.length(); i++) {
        if (input[i] == '?')
            res += positions[currMark++]
        else
            res += input[i]
    }
    return res
}