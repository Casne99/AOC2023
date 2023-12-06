import groovyjarjarantlr4.v4.runtime.misc.Tuple2

def input = new File('input.txt').readLines().collect { line ->
    line.split(':\\s+')[1].trim().findAll('\\d+').join()
}

long ans = waysToWin(new Tuple2<Long, Long>(input[0] as long, input[1] as long))
println(ans)

static int waysToWin(final Tuple2<Long, Long> race) {

    int ans = 0
    long time = race.getItem1()
    long distance = race.getItem2()

    for (int i = 1; i < time; i++) {
        if (i * (time - i) > distance)
            ans++
    }
    return ans
}