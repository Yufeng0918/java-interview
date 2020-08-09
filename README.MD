# J2SE Interview

## 3. Lock
#### FairSync vs NonFairSync
- fairSync 
    + ReentrantLock
    + FIFO
- nonfairSync
    + ReentrantLock(true)
    + allow thread acquire last to get the lock first
    + high throughput
    + synchronized is nonfairSync
#### ReentrantLock as RecursiveLock
- recursive get the same lock
- access sync block which lock by the lock owe by current thread
- prevent deadlock
#### Spin Lock
- thread is not block, loop to get the lock, less context switch but more CPU consuption
- AtomicReference point to self as get lock
#### Shared & Exclusive Lock
- shared: read can share
- exclusive: write
```
lock.writeLock().lock();
try { } finally { lock.writeLock().unlock();}

lock.readLock().lock();
try { } finally { lock.readLock().unlock();}
```
***

## 4. Utils
#### CountDownLotch
- decrease count to release
- sub thread: countDown()
- main thread: await()
#### CyclicBarrier
- increase count to release
#### Semaphore
- avaliable slots
- acquire() and release()
#### BlockingQueue
- Take() block if queue is empty
- Put() block if queue is full
- Implementation
    + ArrayBlockingQueue: array limit queue
    + LinkedBlockingQueue: limit size in integer.MAX_VALUE
    + PriorityBlockingQueue: support priority queue
    + DelayQueue
    + SynchronousQueue: no storage, only store one item
 - Method
    + Exception
        + if queue is full or empty, add() or remove will throw exception
    + Value
        + insert: true or false
        + remove: return element or null
    + Block
        + put() block if queue is full
        + take() block is queue is empty      

| Method | Throw Exception | Special Value | Block | Timeout |
|------------|------------|------------|------------|--------|
| Insert | add(e) | offer(e) | put(e) | offer(e, time, unit) |
| Remove | remove() | poll() | take() | poll(e, time, unit) |
| Check | element() | peek() | N/A | N/A |

#### Synchronized vs Lock
- JVM vs API
    + synchronized is JVM, monitorenter and monitorexist
    + Lock is in java.utils.concurrent.lock
- Useage
    + synchronized acquire or release lock automatically
    + Lock to lock/release mannully
- Interrupted
    + synchronized is not interrupted
    + lockInterruptibly()
- fair
    + synchronized is unfair
    + lock default is unfair lock, can be fair
- condition
    + synchronized notify or notify all
    + lock can signal specific thread
#### Callable
- FutureTask implements RunnableFuture, RunnableFuture implments Runnable
- FutureTask(Callable), FutureTask(Runnable)
***

## 5. ThreadPool
#### Feature
- multiple thread on multiple core to avoid context switch
- pron
    + control running thread 
    + reuse thread
    + reduce cost to create/destroy thread
- Layers
    + Executor
        + ExecutorService
            + AbstractExecutorSerivce
                + ThreadPoolExecutor
            + ScheduledExecutorSerivce
                + ThreadPoolExecutor
- Implementation
    + Executor.newScheduledThreadPool()
    + Executor.newFixedThreadPool()
        + ThreadPoolExecutor(n, n, 0L, TimeUnit.MILLISECONDS, LinkedBlockingQueue)
    + Executor.newSingleThreadPool()
        + ThreadPoolExecutor(1, 1, 0L, TimeUnit.MILLISECONDS, LinkedBlockingQueue)
    + Executor.newCachedThreadPool()
        + ThreadPoolExecutor(0, Integer.MAX_VALUE, 60L, TimeUnit.MILLISECONDS, SynchronousQueue)
        + aync and small task
    + newFixedThreadPool and newSingleThreadPool allow blockingQueue is MAX_VALUE, may result OOM
    + newCachedThreadPool and newScheduledThreadPool allow max number of thread, may result OOM
    + ThreadPoolExecutor to create the thread pool
- ThreadPool Parameters
    + int corePoolSize: thread live in thread pool
    + int maximumPoolSize: max number of thread in thread pool
    + int keepLiveTime: idle time for live thread (maximumPoolSize - corePoolSize) stay in thread pool
    + TimeUnit unit: time unit
    + BlockingQueue workQueue: pending task queue 
    + threadFactory: factory to create thread
    + RejectExecutionHandler: reject task if thread pool hit the maximum pool size and workQueue is full
        + AbortPolicy: throw rejectedExecutionException
        + CallerRunsPolicy: A handler for rejected tasks that runs the rejected task in invoker
        + DiscardOldestPolicy: throw oldest task
        + DiscardPolicy: rejected tasks that silently
    + Setting
        + CPU bounded: core size + 1
        + IO bounded:
            + core size * 2
            + core size / 1 - block factor, e.g. block factor: [0.8, 0.9]
 - Flow
    + thread pool is free, task assign to corePool thread
    + new task is waiting in workQueue
    + if workQueue is full, thread pool extend worker thread to maximum pool size
    + if maximum pool size hits, workQueue is full, reject the task
***    

## 6. GC
#### Mark Garbage
- Tracing
    + apply to copying, mark-sweep, mark-compact
    + trace if object is reachable, unreachable is mark as death
    + traverse from GC Roots to trace object
    + GC Roots
        + Stack local variable
        + method space static reference object
        + method space constant reference object
        + native reference object        
- JVM parameter
    + standard
        + -version
        + -help
    + X parameters
        + change java mode
        + -Xint: interepter to run
        + -Xcomp: compile to local machine code
        + -Xmixed: mixed mode
    + XX parameters
        + Boolean
            + -XX:[+|-]JVM_Properties
            + Jinfo -flag <VM_Flag> $pid, Jinfo -flags $pid
            + -XX:+PrintGCDetails
        + Key-Value
           + -XX:MetaspaceSize=128m, default 21MB
           + -XX:InitailHeapSize = -Xms, alias, is total memory/64
           + -XX:MaxlHeapSize = -Xmx, alias, is total memory/4
           + -XX:MetaSpace: metaspace not in JVM, use native memory, default is 21MB
           + -Xmn
    + Default Value
        + -XX:+PrintFlagsInital, java -XX:+PrintFlagsInitial -version, initial value
        + -XX:+PrintFlagsFinal, java -XX:+PrintFlagsFinal -version, final value modified by JVM
        + -XX:+PrintCommandLineFlags
        + -XX:ThreadStackSize
            + default is 512k to 1024k
            + 0 is means default value
        + -XX:PrintGCDetails
        + -XX:SurvivoRatio
            + 8:1:1 = eden:s0:s1
        + -XX:NewRaio=2
            + Yong:Old = 2:1
        + -XX:MaxTenuringThreshold=15
            + value must between 0 and 15
        + "=" is fixed const
        + ":=" modified by users or JVM
        + young generation is 1/3, old generation 2/3
```
[PSYoungGen: 0K->0K(2560K)] 378K->378K(9728K), 0.0003440 secs] [Times: user=0.00 sys=0.00, real=0.00 secs] 
PSYoungGen: 496K->0K(2560K): memory space before and after GC in young generation, total is 2560k
378K->378K(9728K): jvm heap size to heap size , total is 9728k

[PSYoungGen: 0K->0K(2560K)] [ParOldGen: 378K->360K(7168K)] 378K->360K(9728K), [Metaspace: 3063K->3063K(1056768K)], 0.0061373 secs] [Times: user=0.00 sys=0.00, real=0.00 secs] 
378K->360K(9728K): JVM before and after, total is 9728k
```
#### Reference
- Structure
    + Object
        + ReferenceObject
        + Reference
            + SoftReference
            + WeakReference
            + PhantomReference
- Strong Reference
    + String string = new String(); default
    + Never GC for one is strong reference
    + May result OOM
- Soft Reference
    + if memory is sufficient, weak reference will not GC
    + if memory is insufficient, weak reference will be GC    
    + applied to cache
- Weak Reference
    + always GC
    + WeakHashMap: cleared all content after GC
- Phantom Reference
    + do not decide object lifecycle
    + get() return null
    + after GC, Phantom Reference put into ReferenceQueue
    + ReferenceQueue
        + Weak/Phantom Reference is put into ReferenceQueue
        + perform work after GC
#### OutOfMemoryError and StackOverflowError
- Throwable
    + Exception
        + RuntimeException
    + Error
        + StackOverflowError
        + OutOfMemoryError
- java.lang.StackOverflowError
- java.lang.OutOfMemoryError: java heap space
- java.lang.OutOfMemoryError: GC overhead limit exceeded, GC can not free up the space efficiently
- java.lang.OutOfMemoryError: Direct buffer memory
    + ByteBuffer in Channel or Buffer
    + allocate native memory as DirectByteBuffer via allocateDirect
    + throws Direct buffer memory Error if no space in native memory
    + sun.misc.VM.maxDirectMemory() is the direct memory that JVM able to allocate
    + -XX:MaxDirectMemorySize=5m to set size
- java.lang.OutOfMemoryError: unable to create native thread
    + too many thread created in platform
    + linux allow process create 1024 thread, config need to be update
- java.lang.OutOfMemoryError: unable to create native thread
    + -XX:MetaspaceSize=8m
    + -XX:MaxMetaspaceSize=8m
#### Type of GC
- Serial
    + single thread stop world to do the GC, not suitable server application
- Parallel
    + parallel thread do the GC, stop user threads
- CMS
    + Concurrent Mark Sweep
    + GC thread, user thread may execute at same time
    + may not block user thread
- G1
    + split into different area to do the GC
#### Garbage Collector
- UseSerialGC: serial gc
    + only in one core
    + yong generation, copying algo
    + old generation use SerialOld
- UseParNewGC: parallel new generation
    + young generation: Parallel, copying algo
    + old generation
        + default SerialOld, deprecated
        + recommended CMS
- UseParallelGC
    + PSYongGen, Parallel Scavenage default gc in jdk8
    + young generation: Parallel, copying algo 
    + old generation: Parallel
- UserSerialOldGC: serial old generation, mark-compact algo
- UseParalledOldGC: parallel old generation, mark-compact algo
- UserConMarkSweepGC
    + use on server/client, shorten response
    + auto open UseParNewGC in young generation
    + Initial Mark(Stop), Concurrent Mark, Final Remark(Stop), Concurrent Sweep
        + Concurrent Mark, trace object from GC Root
        + Remark: re-confirm
    + SerialOld is back GC for CMS
    + algo mark-sweep, after multiple time mark-sweep will apply mark-sweep
- UseG1GC
    + garbage first target for multi-processor server, available jdk7 update 4
        + gc concurrent with user thread
        + shorten sweep time
        + more time to predicate GC stoo time
        + less memory fragment
        + default G1 after jdk9
        + divdie Eden, s0, s1, Tenured into region, but region belong to young or old generation but region is not continue
        + -XX:G1HeapRegionSize=n, n is 1 - 32
        + Humongous: G1 has continue region for big object
    + Process
        + Eden and Survivor copy the data to new region as new Survivor
        + Survivor's old object copy to old generation
        + Initial Mark(Stop), Concurrent Mark, Final Remark(Stop), Concurrent Sweep
    + cross young and old generation
    + overall mark-compact
    + partial copying
    + parameters
        + -XX:+UseG1GC
        + -XX:G1HeapRegionSize=n
        + -XX:MaxGCPauseMillis=n
        + -XX:InitiatingHeapOccupancyPercent=n, default is 45
        + -XX:ConGCThreads=n
     + G1 vs CMS    
        + precious in pause
        + no memory fragment
- Youg: Serial, ParNew, Parellel Scavenge, G1
- Old: Serial Old, Parellel Old, CMS, G1
#### Server/Client
- 32bit windows : Client
- 32bit, 2G and 2cpu will be Server
- 64bit, server
#### Performance
- Overview env: top
    + load average: 1min 5min 15
    + if sum of (3 value) / 3 * 100% is greater than 60%, overload
- CPU: vmstat
    + vmstat -n period times 
        + us: user 
        + sy: system
        + id: idle time
        + us + sy is greater than 80%, cpu not sufficient
    + mpstat -P ALL 2
        + all cpu info
    + pidstat -u 1 -p $pid
- Memory: free
    + free -m
    + pidstat -p $pid -r period
- Disk: df
    + df -h
- DiskIO: iostat
    + iostat  -xdk 2 3
    + rkB/s: data read per second in KB
    + wkB/s: data write per second in KB
    + svctm: I/O serve time in milli sec
    + await: I/O await time in milli sec
        + if svctm and await value close, means no I/O waiting
        + if await value much higher than svctm, mean I/O waiting long
    + util: I/O utilization, close 100% is bad
- NetworkIO: ifstat
    + ifstat 1
- Drill into Thread
    + ps -mp $pid -o THREAD,tid,time
        + -m: display all thread
        + -p: pid process cpu time
        + -o: format
    + convert thread id into hex
    + jstack $pid | grep $tid[id in hex of lower case]
***
