Map<Integer, Integer> cardsToMatches = fillMap()
Set<Integer> cardsIds = cardsToMatches.keySet()
Map<Integer, Integer> cardsToQuantity = [:]

(cardsIds.min()..cardsIds.max()).each { id ->
    cardsToQuantity[id] = 1
}

for (final Integer id : cardsIds) {
    for (int i = 0; i < cardsToQuantity[id]; i++)
        for (int j = id + 1; j < id + cardsToMatches[id] + 1; j++)
            cardsToQuantity[j] = cardsToQuantity[j] + 1
}

int ans = cardsToQuantity.values().sum() as int
println(ans)

private static Map<Integer, Integer> fillMap() {
    String regex = '\\b\\d+\\b'
    Map<Integer, Integer> res = [:]
    new File('input.txt').readLines().each { it ->
        int matches = 0
        int cardId = it.split(': ')[0].split('\\s+')[1] as int
        String[] cardNumbers = it.split(': ')[1].trim().split(' \\| ')
        for (final String wNumber : cardNumbers[0].findAll(regex).collect { it.toInteger() }) {
            String[] myNumbers = cardNumbers[1].findAll(regex).collect { it.toInteger() }
            if (wNumber in myNumbers)
                matches++
        }
        res[cardId] = matches
    }
    return res
}