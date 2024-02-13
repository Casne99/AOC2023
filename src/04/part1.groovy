int ans = 0
def regex = '\\b\\d+\\b'

new File('input.txt').readLines().each { it ->
    int matches = 0
    String[] cardNumbers = it.split(': ')[1].trim().split(' \\| ')
    for (final String wNumber : cardNumbers[0].findAll(regex).collect { it.toInteger() }) {
        String[] myNumbers = cardNumbers[1].findAll(regex).collect { it.toInteger() }
        if (wNumber in myNumbers)
            matches++
    }
    if (matches != 0)
        ans += pow(2, matches - 1)
}

println("Answer is ${ans}")

static int pow(final int b, final int x) {
    int res = b

    if (x == 0)
        return 1

    for (int i = 1; i < x; i++)
        res *= 2

    return res
}

