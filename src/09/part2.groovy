def input = new File('input.txt').readLines().collect { it ->
    it.trim().split().collect { num -> num as int }
}

int ans = input.collect { backtrack(steps(it)) }.sum() as int
println(ans)

static List<Integer> step(final List<Integer> input) {
    List<Integer> res = []
    for (int i = 0; i < input.size() - 1; i++) {
        def difference = input[i + 1] - input[i]
        res.add(difference)
    }
    return res
}

static List<List<Integer>> steps(List<Integer> input) {
    List<List<Integer>> sequences = []
    while (input.any { it -> it != 0 }) {
        sequences.add(input)
        input = step(input)
    }
    return sequences
}

static int backtrack(final List<List<Integer>> input) {
    input.last().add(0, input.last().get(0))
    for (int i = input.size() - 2; i > -1; i--) {
        List<Integer> t = input.get(i)
        t.add(0, t.first() - input.get(i + 1).first())
    }
    return input.first().first()
}
