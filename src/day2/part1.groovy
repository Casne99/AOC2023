int ans = 0

def given = [
        'red'  : 12,
        'green': 13,
        'blue' : 14
]

new File('input.txt').readLines()
        .each { it ->
            Integer gameId = it.split(':')[0].split(' ')[1] as Integer
            def gameData = it.split(':')[1]?.trim()
            def extractions = []
            gameData.split(';').each { extraction ->
                extractions.add(new Extraction(extraction as String))
            }
            if (new Game(extractions).compliantWith(given))
                ans += gameId
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

    boolean compliantWith(final Map<String, Integer> given) {
        return extractions.every { extraction ->
            extraction.getValue('red') <= given['red'] &&
                    extraction.getValue('green') <= given['green'] &&
                    extraction.getValue('blue') <= given['blue']
        }
    }
}