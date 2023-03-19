package com.rickyslash.backgroundthreadapp

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
    }
}

// Background Thread / Async Task : 'process' that runs 'separately' from 'main thread' / 'UI thread'
// Thread: is a 'group of instructions' that could be 'executed side-by-side' with other thread
// The 'mechanism' for 'thread' in action is:
// - time slice: when a CPU perform a 'move between one thread to another')
// - multiprocess: when a 'thread is being run' by 'another CPU' inside the 'same system'

// 2 rules for great UX regarding about Thread:
// - 'Don't block' 'UI Thread' / 'Main Thread', means we 'need to make a background thread' / 'async task' for 'processes'
// - 'Don't call' 'UI widget component' (TextView, Button, ImageView, etc) in 'background thread' / 'async task'

// example:
/*fun onClick(v: View) {
    Thread(Runnable {
        val txt = loadStringFromNetwork("http://example.com/string.json")
        textView.post(Runnable() { // '.post' will return to 'main thread'
            textView.setText(txt)
        }
    }).start()
    }*/

// Handler: component to 'send' & 'process' message or other 'runnable' 'object' that's connected to a thread

// example:
/*private val handler = Handler(Looper.getMainLooper()) { // instantiate Handler. 'Looper.getMainLooper' handles 'message loop' for 'main thread'
  fun handleMessage(msg:Message) {
    val message = msg.obj as String // typecast the 'msg.obj' to 'String'
    textView.text = message // 'update' the 'textView'
  }
}

fun onClick(v:View) {
  Thread(Runnable {
    public override fun run() {
      val txt = loadStringFromNetwork("http://example.com/string.json") // 'get data' from 'remote server'
      val msg = Message.obtain() // obtain 'Message object' that's needed for 'handleMessage()'
      msg.obj = txt // sets 'obj' field of 'msg object' as the 'txt' variable
      msg.setTarget(handler) // set it to the 'handler' variable 'which is a Handler'
      msg.sendToTarget() // 'send' 'msg' object to 'handler'
    }
  }).start()
}*/

// Executor: component to manage multiple thread at once, including process's order, scheduling, or parallel-process

//example:
/*val executor:Executor = anExecutor()
executor.execute(RunnableTask1())
executor.execute(RunnableTask2())
...*/

// type of executor:
// - newSingleThreadExecutor: if only wanna make '1 thread'. It makes 'multiple request being pending', 'waiting 1st thread to be finished'
// - newFixedThreadPool(nThreads): if wanna 'make multiple thread' with 'amount' of 'nThreads'. 'Request will be distributed' to those thread in 'parallel'
// - newCachedThreadPool: 'thread' will be 'created on-demand'. It could also use 'old thread' that 'hasn't been destroyed'. 'Thread will be destroyed' after '1 minute' from 'cache'

// ExecutorService: 'derivative' of executor. it could 'monitor running process' using its 'submit function' that produce 'Future'
// - 'Future' is an object returned from 'ExecutorService' that 'checks whether a task is complete' & 'retrieve result'
// - There's also 'shutdown function' to 'refuse new task' and 'cancel' to 'abort task'

// example:
/*val executor: ExecutorService = Executors.newSingleThreadExecutor()
val handler = Handler(Looper.getMainLooper())
executor.execute {
    val txt: String = loadStringFromNetwork("http://example.com/string.json")
    handler.post {
        textView.setText(txt)
    }
}*/

// Coroutine: 'Kotlin features' to 'handle concurrency'
// - Concurrency: 'processes' that 'could be done in parallel' / in no order
// - 'Coroutine' & 'Threads' 'works the same way', but it's 'more lightweight & efficient'

// 2 ways to do 'coroutine':
// - Launch: do process 'without' 'return value'

// example:
/*fun main(args: Array<String>) = runBlocking {
    launch {
        delay(1000)
        println("Coroutines!")
    }
    println("Hello")
}*/ //

// printed -> Hello
// Coroutines!

// - Async: do process with 'waiting' for 'deferred' 'return'
// --- 'deferred' is a value that may not be available yet, but will be, at some point

// example:
/*fun main(args: Array<String>) = runBlocking {
    val first = async { getNumber() }
    val result = first.await() // get the result with 'await'
    println(result)
}
suspend fun getNumber(): Int { // state the function as 'suspend'
    delay(1000)
    return 3*2
}*/

// Dispatcher: to know 'where' the 'process' of 'Coroutine' is 'running'
// - Dispatchers.Default: the 'default'. good for handling process that 'needs high CPU process' (such parsing 100 data)
// - Dispatchers.IO: good for run function that contains 'read-write' data to 'Network/Disk'
// - Dispatchers.Main: to run function in 'Main Thread' (such updating UI)

// Using 'Coroutines' could reduce the potential of 'memory leaks', because it 'needs to be run on the scope' that builds 'structured concurrency'
// this 'scope' will 'control' 'how long Coroutine run its task'. Example:
// - CoroutineScope: Common scope to 'track' Coroutine that runs inside. Coroutine could be 'aborted' by 'cancel()'
// - LifecycleScope: Special scope that's being used on 'Activity' or 'Fragment'. By using this scope, Coroutine will 'automatically' 'abort' 'Coroutine' when 'Lifecycle goes dead' (onDestroy())
// - ViewModelScope: Special scope to use in 'ViewModel'. By using this scope, Coroutine will be 'automatically aborted' when 'ViewModel' is 'cleared' (onCleared)