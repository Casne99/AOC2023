import java.util.regex.Matcher
import java.util.regex.Pattern

import static java.lang.Math.max
import static java.lang.Math.min

int ans = 0
List<String> lines = new File('input.txt').readLines()


for (int i = 0, prev = i - 1, next = i + 1; i < lines.size(); i++, prev++, next++) {
    String currentLine = lines.get(i)
    Matcher matcher = Pattern.compile('\\d+').matcher(currentLine)
    int lineSize = currentLine.size()
    while (matcher.find()) {
        int matchStart = matcher.start()
        int matchEnd = matcher.end()
        List<String> candidates = [currentLine, safeGet(prev, lines), safeGet(next, lines)]
        String adjacency = computeAdjacency(candidates, max(0, matchStart - 1), min(matchEnd + 1, lineSize))
        if (adjacency.any { ch ->
            !ch.isNumber() && ch != '.'
        }) ans += currentLine.substring(matchStart, matchEnd) as int
    }
}

println ans


static String computeAdjacency(final List<String> strings, final int start, final int end) {
    return strings.collect { string ->
        string.isEmpty() ? '' : string.substring(start, end)
    }.join()
}

static String safeGet(final int index, final List<String> lines) {
    return index < 0 || index >= lines.size() ? '' : lines[index]
}
