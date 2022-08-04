fun main() {
    val inputList = readln().split(" ").map { it -> it.toInt() }

    val itemList = mutableListOf<WashItem>()
    for (i in 0 until inputList.size - 1) {
        itemList.add(WashItem(inputList.elementAt(i)))
    }

    val numWashMachine = inputList.last();
    val washMachines = Array(numWashMachine) { _ -> WashMachine() }

    var hours: Int = 0

    do {
        // put into washing machines
        var minRemainingHours : Int = Int.MAX_VALUE
        for (i in 0 until numWashMachine) {

            if (itemList.isNotEmpty() && washMachines[i].isEmpty()) {
                washMachines[i].placeWashItem(itemList.first())
                itemList.removeAt(0)
            }
            if (!washMachines[i].isEmpty()){
                    if (washMachines[i].remainingWashHours() < minRemainingHours) minRemainingHours = washMachines[i].remainingWashHours()
            }
        }

        // wash for
        for (i in 0 until numWashMachine) {
            if (!washMachines[i].isEmpty()) washMachines[i].wash(minRemainingHours)
        }
        hours+= minRemainingHours

    } while (itemList.isNotEmpty() || washMachines.any{!it.isEmpty()})

    println("Hours spent $hours")

}

class WashItem (washHours: Int){
    private var remainingHours:Int = washHours;

    fun isWashed(): Boolean {
        return remainingHours == 0
    }

    fun getRemainingHours():Int {
        return remainingHours
    }

    fun wash(hours:Int) {
        if (remainingHours>=0) remainingHours-=hours
    }
}

class WashMachine {
    private var washItem: WashItem? = null

    fun placeWashItem (washItem: WashItem) {
        this.washItem = washItem
    }

    fun removeWashItem (washItem: WashItem) {
        this.washItem = null
    }

    fun isEmpty() : Boolean {
        return if (washItem == null)
            true
        else {
            washItem!!.isWashed()
        }
    }

    fun remainingWashHours() :Int {
        if (washItem == null) return 0
        else return washItem!!.getRemainingHours()
    }

    fun wash(hours:Int) {
        washItem?.wash(hours)
    }

}
