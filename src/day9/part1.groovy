def input = new File('input.txt').readLines().collect { it ->
    it.trim().split().collect { num -> num as int }
}
ans = input.collect { line ->
    steps(line).collect { list -> list.last() }.sum()
}.sum() as int

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

