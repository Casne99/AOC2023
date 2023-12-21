import groovyjarjarantlr4.v4.runtime.misc.Tuple2

def input = new File('input.txt').readLines()
String instructions = input[0]
Map<String, Tuple2<String, String>> mappings = [:]
for (int i = 2; i < input.size(); i++) {
    String[] params = input[i].split(' = ').collect(it -> it.trim())
    String[] subNodes = params[1].replaceAll('[()]', '').split(', ')
    mappings.put(params[0], new Tuple2<String, String>(subNodes[0], subNodes[1]))
}

int ans = 0
int currentInstruction = 0
String currentNode = 'AAA'
while ('ZZZ' != currentNode) {
    ans++
    Tuple2<String, String> possibles = mappings.get(currentNode)
    currentNode = switch (instructions[currentInstruction]) {
        case 'R' -> possibles.getItem2()
        default  -> possibles.getItem1()
    }
    currentInstruction = (currentInstruction + 1) % instructions.length()
}

println(ans)
