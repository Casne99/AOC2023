import java.util.regex.Matcher
import java.util.regex.Pattern

int ans = 0

List<String> lines = new File('input.txt').readLines()

for (int i = 0, prev = i - 1, next = i + 1; i < lines.size(); i++, prev++, next++) {
    String currentLine = lines.get(i)
    Matcher matcher = Pattern.compile('\\*').matcher(currentLine)
    while (matcher.find()) {
        int gearPos = matcher.start()
        List<String> candidates = [currentLine, safeGet(prev, lines), safeGet(next, lines)]
        def found = computeAdjacency(candidates, gearPos)
        if (found.size() == 2)
            ans += found.collect { it -> it as int }
                    .inject(1) { acc, num ->
                        acc * num
                    }
    }
}

println(ans)

static List<String> computeAdjacency(final List<String> strings, final int pos) {
    def res = []
    Pattern pattern = Pattern.compile('\\d+')
    strings.each { it ->
        Matcher matcher = pattern.matcher(it)
        while (matcher.find()) {
            int candidateStart = matcher.start()
            int candidateEnd = matcher.end()
            String candidate = it.substring(candidateStart, candidateEnd)
            if (candidateStart - 1 <= pos && pos < candidateEnd + 1)
                res.add(candidate)
        }
    }
    res
}

static String safeGet(final int index, final List<String> lines) {
    return index < 0 || index >= lines.size() ? '' : lines[index]
}
