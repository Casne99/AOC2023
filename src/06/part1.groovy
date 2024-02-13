import groovyjarjarantlr4.v4.runtime.misc.Tuple2

def input = new File('input.txt').readLines().collect { line ->
    line.split(':\\s+')[1].trim().split().collect { it -> it as int }
}

List<Tuple2<Integer, Integer>> races = new ArrayList<>()
int numOfRaces = input[0].size()
for (int i = 0; i < numOfRaces; i++)
    races.add(new Tuple2<Integer, Integer>(input[0][i], input[1][i]))

int ans = races.collect { waysToWin(it) }
        .inject(1) { acc, next -> acc * next }

println(ans)

static int waysToWin(final Tuple2<Integer, Integer> race) {

    int ans = 0
    int time = race.getItem1()
    int distance = race.getItem2()

    for (int i = 1; i < time; i++) {
        if (i * (time - i) > distance)
            ans++
    }
    return ans
}