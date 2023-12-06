def input = new File('input.txt').readLines().collect { line ->
    line.split(':\\s+')[1].trim().findAll('\\d+').join()
}

long ans = waysToWin(new Race(input[0] as long, input[1] as long))
println(ans)

class Race {

    final long time

    final long recordDistance

    Race(final long time, final long recordDistance) {
        this.time = time
        this.recordDistance = recordDistance
    }

    @Override
    String toString() {
        return "Time: ${time}\nDistance: ${recordDistance}\n"
    }
}

static long waysToWin(final Race race) {

    long ans = 0

    for (long i = 1; i < race.time; i++) {
        if (i * (race.time - i) > race.recordDistance)
            ans++
    }
    return ans
}