def ans = new File('input.txt').readLines()
    .collect { it ->
        it.findAll('\\d')[[0, -1]].join() as int
    }.sum()

println "Answer is ${ans}"