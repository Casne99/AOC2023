def input = new File('input.txt').readLines().collect { line ->
    line.split(':\\s+')[1].trim().split().collect { it -> it as int }
}

List<Race> races = new ArrayList<>()
int numOfRaces = input[0].size()
for (int i = 0; i < numOfRaces; i++)
    races.add(new Race(input[0][i], input[1][i]))

int ans = races.collect { waysToWin(it) }
        .inject(1) { acc, next -> acc * next }

println(ans)

class Race {

    final int time

    final int recordDistance

    Race(final int time, final int recordDistance) {
        this.time = time
        this.recordDistance = recordDistance
    }

    @Override
    String toString() {
        return "Time: ${time}\nDistance: ${recordDistance}\n"
    }
}

static int waysToWin(final Race race) {

    int ans = 0

    for (int i = 1; i < race.time; i++) {
        if (i * (race.time - i) > race.recordDistance)
            ans++
    }
    return ans
}