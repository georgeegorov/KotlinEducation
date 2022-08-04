fun main () {
    var count=0
    for (primeNumber in 3 until 5000) {
        if (testPrimeNumber(primeNumber)) { println("primeNumber $primeNumber is good"); count++ }
    }
    println(count)
}

fun testPrimeNumber(primeNumber: Int) :Boolean {
    for (anyNumber in 1 until primeNumber-1) {
        var result = 1
        for (i in 1 until primeNumber) {
            result = (result * anyNumber) % primeNumber
        }
        if (result != 1) return false
    }
    return true;
}


