def ans = new File('input.txt').readLines()
    .collect { it ->
        String filtered = it.replaceAll('[^0-9]', '')
        (filtered[0] + filtered[-1]) as Integer
    }.sum()

println "Answer is ${ans}"