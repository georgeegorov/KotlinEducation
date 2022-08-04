fun main () {
    val limit: Int = 10000;
    val evenSet = mutableSetOf<Int>();
    val unevenSet = mutableSetOf<Int>();

    for (a in 1 until limit) {
        for (b in 1 until a) {
            val result = a*a - b*b;
            if (result < 10000) {
                if (result % 2 == 0) evenSet.add(result) else unevenSet.add(result)
            }
        }
    }
    println("even count is  =" + evenSet.size)
    println("uneven count is = " + unevenSet.size)
}