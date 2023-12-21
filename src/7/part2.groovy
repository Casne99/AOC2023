Map<CardStack, Integer> handToBid = new HashMap<>()
new File('input.txt').readLines().each { line ->
    String[] splitted = line.trim().split()
    handToBid.put(new CardStack(splitted[0]), splitted[1] as int)
}

List<CardStack> sorted = handToBid.keySet().asList().sort()

int ans = 0
for (int i = 0; i < sorted.size(); i++) {
    ans += handToBid[sorted[i]] * (sorted.indexOf(sorted[i]) + 1)
}
println(ans)

enum Hand {

    FIVE_OF_A_KIND,
    FOUR_OF_A_KIND,
    FULL_HOUSE,
    THREE_OF_A_KIND,
    TWO_PAIR,
    PAIR,
    HIGH_CARD
}


class HandEvaluator {

    static Hand evaluate(final String cards) {
        return FiveOfAKindEvaluator.evaluate(cards)
    }
}

class FiveOfAKindEvaluator {

    static Hand evaluate(final String cards) {
        return isFiveOfAKind(cards) ? Hand.FIVE_OF_A_KIND :
                FourOfAKindEvaluator.evaluate(cards)
    }

    private static boolean isFiveOfAKind(final String cards) {
        Set<String> uniques = new HashSet<>()
        for (String card : cards)
            uniques.add(card)
        return uniques.size() == 1
    }
}

class FourOfAKindEvaluator {

    static Hand evaluate(final String cards) {
        return isFourOfAKind(cards) ? Hand.FOUR_OF_A_KIND :
                FullHouseEvaluator.evaluate(cards)
    }

    private static boolean isFourOfAKind(final String cards) {
        Map<String, Integer> occurrences = new HashMap<>()
        for (String card : cards)
            occurrences.put(card, occurrences.containsKey(card) ? occurrences[card] + 1 : 1)
        return occurrences.values().contains(4)
    }
}

class FullHouseEvaluator {

    static Hand evaluate(final String cards) {
        return isFullHouse(cards) ? Hand.FULL_HOUSE :
                ThreeOfAKindEvaluator.evaluate(cards)
    }

    private static boolean isFullHouse(final String cards) {
        Map<String, Integer> occurrences = new HashMap<>()
        for (String card : cards)
            occurrences.put(card, occurrences.containsKey(card) ? occurrences[card] + 1 : 1)
        Set<Integer> values = occurrences.values()
        return values.contains(2) && values.contains(3)
    }
}

class ThreeOfAKindEvaluator {

    static Hand evaluate(final String cards) {
        return isThreeOfAKind(cards) ? Hand.THREE_OF_A_KIND :
                TwoPairEvaluator.evaluate(cards)
    }

    private static boolean isThreeOfAKind(final String cards) {
        Map<String, Integer> occurrences = new HashMap<>()
        for (String card : cards)
            occurrences.put(card, occurrences.containsKey(card) ? occurrences[card] + 1 : 1)
        Set<Integer> values = occurrences.values()
        return values.contains(3)
    }
}

class TwoPairEvaluator {

    static Hand evaluate(final String cards) {
        return isTwoPair(cards) ? Hand.TWO_PAIR :
                PairEvaluator.evaluate(cards)
    }

    private static boolean isTwoPair(final String cards) {
        Map<String, Integer> occurrences = new HashMap<>()
        for (String card : cards)
            occurrences.put(card, occurrences.containsKey(card) ? occurrences[card] + 1 : 1)
        Collection<Integer> values = occurrences.values().toList()
        return values.count { it -> it == 2 } == 2
    }
}

class PairEvaluator {

    static Hand evaluate(final String cards) {
        return isPair(cards) ? Hand.PAIR :
                Hand.HIGH_CARD
    }

    private static boolean isPair(final String cards) {
        Map<String, Integer> occurrences = new HashMap<>()
        for (String card : cards)
            occurrences.put(card, occurrences.containsKey(card) ? occurrences[card] + 1 : 1)
        Collection<Integer> values = occurrences.values()
        return values.count { it -> it == 2 } == 1
    }
}

class CardStack implements Comparable<CardStack> {

    public final String cards
    public final Hand hand

    static final List<String> order = [
            'A', 'K', 'Q', 'T', '9', '8', '7', '6', '5', '4', '3', '2', 'J'
    ]

    CardStack(final String cards) {
        this.cards = cards
        this.hand = jokerGoesWild(cards)
    }

    @Override
    int compareTo(final CardStack o) {
        Hand myHand = hand
        Hand otherHand = o.hand
        int comparedRanks = Integer.compare(otherHand.ordinal(), myHand.ordinal())
        if (comparedRanks != 0)
            return comparedRanks
        for (int i = 0; i < cards.length(); i++) {
            int comparedCard = Integer.compare(order.indexOf(o.cards[i]),
                    order.indexOf(cards[i]))
            if (comparedCard != 0)
                return comparedCard
        }
        return 0
    }

    @Override
    String toString() {
        return cards.toString()
    }

    private static Hand jokerGoesWild(final String cards) {
        int jokers = cards.count('J')
        if (cards.contains('J')) {
            switch (HandEvaluator.evaluate(cards)) {
                case Hand.FIVE_OF_A_KIND:
                    return Hand.FIVE_OF_A_KIND
                    break
                case Hand.FOUR_OF_A_KIND:
                    return Hand.FIVE_OF_A_KIND
                    break
                case Hand.FULL_HOUSE:
                    return Hand.FIVE_OF_A_KIND
                    break
                case Hand.THREE_OF_A_KIND:
                    if (jokers == 1 || jokers == 3)
                        return Hand.FOUR_OF_A_KIND
                    if (jokers == 2)
                        return Hand.FIVE_OF_A_KIND
                    break
                case Hand.TWO_PAIR:
                    if (jokers == 1)
                        return Hand.FULL_HOUSE
                    if (jokers == 2)
                        return Hand.FOUR_OF_A_KIND
                    break
                case Hand.PAIR:
                    if (jokers == 1 || jokers == 2)
                        return Hand.THREE_OF_A_KIND
                    break
                case Hand.HIGH_CARD:
                    return Hand.PAIR
                break

                default:
                    return HandEvaluator.evaluate(cards)
            }
        }
        return HandEvaluator.evaluate(cards)
    }
}


