import java.util.concurrent.Semaphore

class QuickSort(arr: IntArray, maxThreads: Int, result: (resultado: IntArray)->Unit) {
    companion object ThreadValues{
        var threadsInUse = 1
        lateinit var array : IntArray
    }
    init {
        val semaphore = Semaphore(1)
        val sorter = Sort(0, arr.size, maxThreads, semaphore)
        array = arr
        sorter.start()
        sorter.join()
        result(array)
    }

    private class Sort(private var leftMost : Int, private var rightMost : Int, private val maxThreads: Int, private val semaphore: Semaphore) : Thread(){
        override fun run() {
            sortArray(leftMost, rightMost)
        }

        fun sortArray(left: Int, right: Int){
            if(left < right){
                val pivot = partition(left, right)
                var threadsRunning : Array<Thread> = arrayOf()
                if(threadsInUse < maxThreads){
                    threadsInUse++
                    threadsRunning += Sort(left, pivot, maxThreads, semaphore)
                    threadsRunning[threadsRunning.size-1].start()
                }else{
                    sortArray(left, pivot)
                }
                if(threadsInUse < maxThreads){
                    threadsInUse++
                    threadsRunning += Sort(pivot+1, right, maxThreads, semaphore)
                    threadsRunning[threadsRunning.size-1].start()
                }else{
                    sortArray(pivot+1, right)
                }

                for(i in threadsRunning){
                    i.join()
                    threadsInUse--
                }
            }
            threadsInUse--
        }

        fun partition(leftMost: Int, rightMost: Int) : Int{
            var lowest = leftMost
            val pivot = array[rightMost - 1]
            for (i in leftMost until rightMost - 1){
                if(array[i] < pivot){
                    val temp = array[i]
                    semaphore.acquire()
                    array[i] = array[lowest]
                    array[lowest] = temp
                    semaphore.release()
                    lowest++
                }
            }
            val temp = array[rightMost - 1]
            semaphore.acquire()
            array[rightMost - 1] = array[lowest]
            array[lowest] = temp
            semaphore.release()
            return lowest
        }
    }
}