List<String> fileInput = new File('input.txt').readLines()
List<Long> ranges = fileInput[0].findAll('\\d+').collect { it -> it as long }
List<Set<Mapping>> conversionSteps = []
Set<Mapping> conversionStep = null
long ans = 0


for (int i = 2; i < fileInput.size(); i++) {
    String currentLine = fileInput[i]
    if (fileInput[i].endsWith('map:'))
        conversionStep = new HashSet<>()
    else if (!currentLine.isBlank()) {
        conversionStep.add(new Mapping(currentLine.trim()))
        if (i == fileInput.size() - 1)
            conversionSteps.add(conversionStep)
    } else
        conversionSteps.add(conversionStep)
}

Converter converter = new Converter(conversionSteps)
ans = converter.convert(ranges[0])
Set<Long> toCheckLater = new HashSet<>()
long prev = 0

println(ranges.size())
for (long i = 0; i < ranges.size() - 1; i += 2) {
    for (long j = ranges[i]; j < ranges[i] + ranges[i + 1]; j = Math.min(j + 3000, ranges[i] + ranges[i + 1])) {
        ans = Math.min(ans, converter.convert(j))
        if (ans != prev){
            for (long t = j; t > j - 3000 && t > 0; t--) {
                toCheckLater.add(t)
            }
        }
        prev = ans
    }
}

ans = Math.min(ans, toCheckLater.collect( it -> converter.convert(it)).min())
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
    int hashCode() {
        return Objects.hash(from, to, values)
    }

    @Override
    String toString() {
        return "[${from} - ${to} - ${values}]"
    }
}

class Converter {

    private final List<Set<Mapping>> conversionSteps = []

    Converter(final List<Set<Mapping>> conversionSteps) {
        conversionSteps.each { conversionStep ->
            this.conversionSteps.add(conversionStep)
        }
    }

    long convert(final long input) {
        long result = input
        Iterator<Set<Mapping>> iterator = conversionSteps.iterator()
        while (iterator.hasNext()) {
            Set<Mapping> conversionStep = iterator.next()
            Optional<Mapping> mappingOpt = Optional.ofNullable(conversionStep.find { mapping ->
                mapping.containsKey(result)
            })
            result = mappingOpt.isPresent() ? mappingOpt.get().mappedTo(result) : result
        }
        return result
    }
}