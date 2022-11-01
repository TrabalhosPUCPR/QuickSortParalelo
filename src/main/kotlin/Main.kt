import java.util.Random
import kotlin.system.measureTimeMillis

fun main(args : Array<String>){
    val size = 10_000
    val array = IntArray(size) { Random().nextInt(size) }
    var sortedArray = IntArray(0)
    val parallelQuickSortTime = measureTimeMillis {
        QuickSort(array, 100){
            sortedArray = it
        }
    }
    val normalQuickSortTime = measureTimeMillis {
        quickSort(array)
    }
    print("Parallel sorted array:")
    for(i in sortedArray){
        print("$i ")
    }
    println("\nTime needed to sort: ${parallelQuickSortTime / 1000.0}")
    print("Normally sorted array:")
    for(i in array){
        print("$i ")
    }
    println("\nTime needed to sort: ${normalQuickSortTime / 1000.0}")
}

fun quickSort(array : IntArray){
    sort(0, array.size, array)
}

fun sort(leftMost: Int, rightMost: Int, array: IntArray){
    if(leftMost < rightMost){
        val pivot = partition(array, leftMost, rightMost)
        sort(leftMost, pivot, array)
        sort(pivot + 1, rightMost, array)
    }
}

fun partition(array: IntArray, leftMost: Int, rightMost: Int): Int {
    var lowest = leftMost
    val pivot = array[rightMost - 1]
    for (i in leftMost until rightMost - 1){
        if(array[i] < pivot){
            val temp = array[i]
            array[i] = array[lowest]
            array[lowest] = temp
            lowest++
        }
    }
    val temp = array[rightMost - 1]
    array[rightMost - 1] = array[lowest]
    array[lowest] = temp
    return lowest
}