import java.util.regex.Matcher
import java.util.regex.Pattern

String pattern = 'one|two|three|four|five|six|seven|eight|nine'


def ans = new File('input.txt').readLines()
        .collect { it ->
            RecursiveReplacer replacer = new RecursiveReplacer(substitutes)
            String filtered = replacer.replace(it).replaceAll('[^0-9]', '')
            (filtered[0] + filtered[-1]) as Integer
        }.sum()

println "Answer is ${ans}"


class RecursiveReplacer {

    private final Map<String, String> substitutions

    private final Pattern pattern


    RecursiveReplacer(final Map substitutions) {
        this.substitutions = substitutions
        pattern = Pattern.compile(substitutions.keySet().join('|'))
    }

    String replace(final String input) {

        Matcher matcher = pattern.matcher(input)

        if (matcher.find()) {

            int startIndex = matcher.start()
            int endIndex = matcher.end()

            String matchedSubstring = input.substring(startIndex, endIndex)
            return replace(input.replaceFirst(matchedSubstring,
                    substitutions[matchedSubstring]))
        }
        return input // Base case!
    }
}

