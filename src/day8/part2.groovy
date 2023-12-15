import groovyjarjarantlr4.v4.runtime.misc.Tuple2

def input = new File('input.txt').readLines()
String instructions = input[0]
Map<String, Tuple2<String, String>> mappings = new HashMap<>()
for (int i = 2; i < input.size(); i++) {
    String[] params = input[i].split(' = ').collect(it -> it.trim())
    String[] subNodes = params[1].replaceAll('[()]', '').split(', ')
    mappings.put(params[0], new Tuple2<String, String>(subNodes[0], subNodes[1]))
}

Set<Loop> loops = mappings.keySet().findAll { node ->
    node.endsWith('A')
}.collect { loop -> get(mappings, instructions, loop) }

println(lcm(loops.collect{ it -> it.length() + 1}.toArray() as int[]))

class Loop {

    private final List<String> nodes = []

    Loop(final List<String> nodes) {
        nodes.each { node ->
            this.nodes.add(node)
        }
    }

    int length() {
        return nodes.size()
    }

    @Override
    String toString() {
        return nodes.toString()
    }
}

static Loop get(Map<String, Tuple2<String, String>> mappings, String instructions, String start) {

    int currentInstruction = 0

    String elem = getNext(mappings, instructions[currentInstruction++], start)
    List<String> res = new ArrayList<>()
    while (!elem.endsWith('Z')) {
        res.add(elem)
        elem = getNext(mappings, instructions[currentInstruction], elem)
        currentInstruction = (currentInstruction + 1) % instructions.length()
    }
    return new Loop(res)
}

static String getNext(Map<String, Tuple2<String, String>> mappings, String instruction, String key) {
    return switch (instruction) {
        case 'R' -> mappings.get(key).getItem2()
        default -> mappings.get(key).getItem1()
    }
}

static long lcm(int[] element_array)
{
    long lcm_of_array_elements = 1
    int divisor = 2

    while (true) {
        int counter = 0
        boolean divisible = false

        for (int i = 0; i < element_array.length; i++) {

            if (element_array[i] == 0) {
                return 0
            }
            else if (element_array[i] < 0) {
                element_array[i] = element_array[i] * (-1)
            }
            if (element_array[i] == 1) {
                counter++
            }

            if (element_array[i] % divisor == 0) {
                divisible = true
                element_array[i] = element_array[i].intdiv(divisor)
            }
        }

        if (divisible) {
            lcm_of_array_elements = lcm_of_array_elements * divisor
        }
        else {
            divisor++
        }

        // Check if all element_array is 1 indicate
        // we found all factors and terminate while loop.
        if (counter == element_array.length) {
            return lcm_of_array_elements
        }
    }
}

