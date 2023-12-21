List<String> fileInput = new File('input.txt').readLines()
def seeds = fileInput[0].findAll('\\d+').collect{ it -> it as long}
List<List<Mapping>> conversionSteps = []
List<Mapping> conversionStep = null

println(seeds)

for (int i = 2; i < fileInput.size(); i++) {
    String currentLine = fileInput[i]
    if (fileInput[i].endsWith('map:'))
        conversionStep = new ArrayList<>()
    else if (!currentLine.isBlank()) {
        conversionStep.add(new Mapping(currentLine.trim()))
        if (i == fileInput.size() - 1)
            conversionSteps.add(conversionStep)
    } else
        conversionSteps.add(conversionStep)
}

Converter converter = new Converter(conversionSteps)
long ans = seeds.collect { seed -> converter.convert(seed) }.min()
println(ans)


class Mapping {

    private final long to

    private final long from

    private final long values

    Mapping(final String toParse) {
        long[] parameters = toParse.trim().split().collect { it -> it as long }
        this.to = parameters[0]
        this.from = parameters[1]
        this.values = parameters[2]
    }

    boolean containsKey(final long key) {
        return from <= key && key < from + values
    }

    long mappedTo(final long key) {
        return to + (key - from)
    }

    @Override
    String toString() {
        return "[${from} - ${to} - ${values}]"
    }
}

class Converter {

    private final List<List<Mapping>> conversionSteps = [[]]

    Converter(final List<List<Mapping>> conversionSteps) {
        conversionSteps.each { conversionStep ->
            this.conversionSteps.add(conversionStep)
        }
    }

    long convert(final long input) {
        long result = input
        Iterator<List<Mapping>> iterator = conversionSteps.iterator()
        while (iterator.hasNext()) {
            List<Mapping> conversionStep = iterator.next()
            Optional<Mapping> mappingOpt = Optional.ofNullable(conversionStep.find { mapping ->
                mapping.containsKey(result)
            })
            result = mappingOpt.isPresent() ? mappingOpt.get().mappedTo(result) : result
        }
        return result
    }
}