int ans = new File('input.txt').readLines()
        .collect { line ->
            line.split(',')
                    .collect { instruction -> hash(instruction) }.sum()
        }[0] as int

println(ans)

static int hash(final String input) {

    int ans = 0
    for (int i = 0; i < input.length(); i++) {
        ans += (int) input.charAt(i)
        ans *= 17
        ans %= 256
    }
    return ans
}