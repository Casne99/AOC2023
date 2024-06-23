String pattern = '(?=(one|two|three|four|five|six|seven|eight|nine|\\d))'
def substitutions = [
        'one'  : 1,
        'two'  : 2,
        'three': 3,
        'four' : 4,
        'five' : 5,
        'six'  : 6,
        'seven': 7,
        'eight': 8,
        'nine' : 9
]

def ans = new File('input.txt').readLines()
        .collect { line ->
            (line =~ pattern)[[0, -1]].collect { match ->
                String captured = (match as List)[1]
                substitutions[captured] ?: captured
            }.join() as int
        }.sum()

println "Answer is ${ans}"
