int ans = 0

new File('input.txt').readLines()
        .each { it ->
            def gameData = it.split(':')[1].trim()
            def extractions = []
            gameData.split(';').each { extraction ->
                extractions.add(new Extraction(extraction as String))
            }
            ans += new Game(extractions).computePower()
        }

println "Answer is ${ans}"


class Extraction {

    private final Map<String, Integer> occurrences = [
            'red'  : 0,
            'green': 0,
            'blue' : 0
    ]

    Extraction(final String toParse) {
        toParse.split(',').each { extraction ->
            String[] toInsert = (extraction as String).trim().split(' ')

            String key = toInsert[1]
            Integer toAdd = toInsert[0] as Integer
            Integer alreadyPresent = occurrences[key]

            occurrences[key] = alreadyPresent + toAdd
        }
    }

    int getValue(final String color) {
        occurrences[color]
    }
}

class Game {

    private final List<Extraction> extractions = []

    Game(final List<Extraction> extractions) {
        extractions.each { it ->
            this.extractions.add(it)
        }
    }

    private int getMax(final String color) {
        extractions.collect(extraction ->
            extraction.getValue(color) as Integer
        ).max()
    }

    int computePower() {
        def mins = ['red', 'green', 'blue'].collect( it -> getMax(it))
        mins.inject(1) { product, next ->
            product * next
        }
    }
}