def input = new File('input.txt').readLines()
int ans = 0

for (final String line : input) {
    ans += line.split(',').collect { it ->
        hash(it)
    }.sum()
}

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