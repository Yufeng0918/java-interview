# J2SE Interview

## 1. Java Concurrent
#### volatile
+ before release lock, refresh shared variable value in main memory
+ before acquire lock, get the value from main memory
+ unlock/acquired same lock
+ Feature
    + visible
        + thread copy shared variable value from main memory into thread's local memory
        + thread direct modify the variable in local memory
        + thread refresh value of variable in main memory
        + other threads is able to see the change of variable
    + atomic
        + atomic in single read/write operation
        + not atomic in ++
            + getfield
            + iadd
            + putfield
        + normal long/double write operation may not be atomic since 32bit * 2
    + no re-order
        + compiler re-order, instruction parallel re-order, memory re-order, final execution re-order
        + consist in single thread: re-order only ensure single thread final result is same as execution result in order
        + no-consist in multiple thread: final result consist is not guarantee
        + CPU consider data dependency in re-order
            + thread1: x = a; b = 1;
            + thread2: y = b; a = 2
            + re-order: b = 1; y = b; a = 2, x = a, result: x = 2, y = 1
        + Memory Barrier
            + ensure specific operation execute in order
            + ensure some variable visible
            + insert memory barrier to disallow re-order and refresh variable in main memory
            + volatile write
                + StoreStore, volatile write, StoreLoad
                + StoreStore disallow re-order write | volatile write before volatile write
                + StoreLoad disallow re-order volatile write | volatile write && volatile read after volatile write
            + volatile read
                + volatile read, LoadLoad, LoadStore
                + LoadLoad disallow re-order volatile read | read after volatile read
                + LoadStore disallow re-order volatile read | all write after volatile read
             + instance == null && instance = new S03_SingletonDemo may re-order
```
 public static S03_SingletonDemo getInstance() {
    if (instance == null) {
        synchronized (S03_SingletonDemo.class) {
            if (instance == null) {
                instance = new S03_SingletonDemo();
            }
        }
    }
    return instance;
}
```

#### CAS
- compare the local variable in thread with variable in Heap
- similar to svn version control
- Unsafe
    + unsafe: from Unsafe.getUnsafe(), rt.jar
    + modify the memory as C pointer, call OS interface to modify the object
    + compareAndSwapInt(this, valueOffset, expect, update);
        + this: current object
        + valueOffset: memory offset to find object's memory address
        + CAS trigger assemble command via CPU, this is command for Hardware, native atomic
    + getAndAddInt(this, valueOffset, i)
        + continue loop to get value and compare set
        + do { var5 = this.getIntVolatile(var1, var2)} while (!this.compareAndSwapInt(var1, var2, var5 + var4))
    + unsafe.cpp
        + Atomic::cmpxchg(x, add, e)
        + x: new value
        + add: address
        + e: expected old value
    + cons
        + self-spin if compare failed
        + only one variable atomic operation
        + ABA
            + thread1 A update to B, B update back to A
            + thread2 pick up A to set the value
            + AtomicStampReference compare value + version
***

## 2. Collection
#### ArrayList
- java.util.ConcurrentModificationException
- ArrayList is not thread safe
- Solution
    + Vector is thread safe with synchronized
    + Collections.synchronizedList|Map|Set
    + CopyOnWriteArrayList
        + seperate read and write
        + lock list, copy current to new array and add element
        + assign new array to list, unlock list
    + CopyOnWriteArraySet
        + implemented by CopyOnWriteArrayList
        + HashSet
            + init capacity is 16 and load factor 0.75
            + implemented by HashMap
            + add by Entry(value, DummyObject())
    + ConcurrentHashMap    
```
IntStream.range(0, 30).forEach(i -> {
    new Thread(() -> {
        list.add(UUID.randomUUID().toString().substring(0, 8));
        System.out.println(list);
    }, String.valueOf(i)).start();
});
```
***

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

## 7. 消息队列
- 为什么使用消息队列？
- 消息队列有什么优点和缺点？
- Kafka、ActiveMQ、RabbitMQ、RocketMQ 都有什么区别，以及适合哪些场景？

## 面试官心理分析
其实面试官主要是想看看：

- **第一**，你知不知道你们系统里为什么要用消息队列这个东西？<br>
不少候选人，说自己项目里用了 Redis、MQ，但是其实他并不知道自己为什么要用这个东西。其实说白了，就是为了用而用，或者是别人设计的架构，他从头到尾都没思考过。<br>
没有对自己的架构问过为什么的人，一定是平时没有思考的人，面试官对这类候选人印象通常很不好。因为面试官担心你进了团队之后只会木头木脑的干呆活儿，不会自己思考。

- **第二**，你既然用了消息队列这个东西，你知不知道用了有什么好处&坏处？<br>
你要是没考虑过这个，那你盲目弄个 MQ 进系统里，后面出了问题你是不是就自己溜了给公司留坑？你要是没考虑过引入一个技术可能存在的弊端和风险，面试官把这类候选人招进来了，基本可能就是挖坑型选手。就怕你干 1 年挖一堆坑，自己跳槽了，给公司留下无穷后患。

- **第三**，既然你用了 MQ，可能是某一种 MQ，那么你当时做没做过调研？<br>
你别傻乎乎的自己拍脑袋看个人喜好就瞎用了一个 MQ，比如 Kafka，甚至都从没调研过业界流行的 MQ 到底有哪几种。每一个 MQ 的优点和缺点是什么。每一个 MQ **没有绝对的好坏**，但是就是看用在哪个场景可以**扬长避短，利用其优势，规避其劣势**。<br>
如果是一个不考虑技术选型的候选人招进了团队，leader 交给他一个任务，去设计个什么系统，他在里面用一些技术，可能都没考虑过选型，最后选的技术可能并不一定合适，一样是留坑。

### 为什么使用消息队列
其实就是问问你消息队列都有哪些使用场景，然后你项目里具体是什么场景，说说你在这个场景里用消息队列是什么？

面试官问你这个问题，**期望的一个回答**是说，你们公司有个什么**业务场景**，这个业务场景有个什么技术挑战，如果不用 MQ 可能会很麻烦，但是你现在用了 MQ 之后带给了你很多的好处。

先说一下消息队列常见的使用场景吧，其实场景有很多，但是比较核心的有 3 个：**解耦**、**异步**、**削峰**。

#### 解耦
看这么个场景。A 系统发送数据到 BCD 三个系统，通过接口调用发送。如果 E 系统也要这个数据呢？那如果 C 系统现在不需要了呢？A 系统负责人几乎崩溃......

![mq-1](/images/mq-1.png)

在这个场景中，A 系统跟其它各种乱七八糟的系统严重耦合，A 系统产生一条比较关键的数据，很多系统都需要 A 系统将这个数据发送过来。A 系统要时时刻刻考虑 BCDE 四个系统如果挂了该咋办？要不要重发，要不要把消息存起来？头发都白了啊！

如果使用 MQ，A 系统产生一条数据，发送到 MQ 里面去，哪个系统需要数据自己去 MQ 里面消费。如果新系统需要数据，直接从 MQ 里消费即可；如果某个系统不需要这条数据了，就取消对 MQ 消息的消费即可。这样下来，A 系统压根儿不需要去考虑要给谁发送数据，不需要维护这个代码，也不需要考虑人家是否调用成功、失败超时等情况。

![mq-2](/images/mq-2.png)

**总结**：通过一个 MQ，Pub/Sub 发布订阅消息这么一个模型，A 系统就跟其它系统彻底解耦了。

**面试技巧**：你需要去考虑一下你负责的系统中是否有类似的场景，就是一个系统或者一个模块，调用了多个系统或者模块，互相之间的调用很复杂，维护起来很麻烦。但是其实这个调用是不需要直接同步调用接口的，如果用 MQ 给它异步化解耦，也是可以的，你就需要去考虑在你的项目里，是不是可以运用这个 MQ 去进行系统的解耦。在简历中体现出来这块东西，用 MQ 作解耦。

#### 异步
再来看一个场景，A 系统接收一个请求，需要在自己本地写库，还需要在 BCD 三个系统写库，自己本地写库要 3ms，BCD 三个系统分别写库要 300ms、450ms、200ms。最终请求总延时是 3 + 300 + 450 + 200 = 953ms，接近 1s，用户感觉搞个什么东西，慢死了慢死了。用户通过浏览器发起请求，等待个 1s，这几乎是不可接受的。

![mq-3](/images/mq-3.png)

一般互联网类的企业，对于用户直接的操作，一般要求是每个请求都必须在 200 ms 以内完成，对用户几乎是无感知的。

如果**使用 MQ**，那么 A 系统连续发送 3 条消息到 MQ 队列中，假如耗时 5ms，A 系统从接受一个请求到返回响应给用户，总时长是 3 + 5 = 8ms，对于用户而言，其实感觉上就是点个按钮，8ms 以后就直接返回了，爽！网站做得真好，真快！

![mq-4](/images/mq-4.png)

#### 削峰
每天 0:00 到 12:00，A 系统风平浪静，每秒并发请求数量就 50 个。结果每次一到 12:00 ~ 13:00 ，每秒并发请求数量突然会暴增到 5k+ 条。但是系统是直接基于 MySQL 的，大量的请求涌入 MySQL，每秒钟对 MySQL 执行约 5k 条 SQL。

一般的 MySQL，扛到每秒 2k 个请求就差不多了，如果每秒请求到 5k 的话，可能就直接把 MySQL 给打死了，导致系统崩溃，用户也就没法再使用系统了。

但是高峰期一过，到了下午的时候，就成了低峰期，可能也就 1w 的用户同时在网站上操作，每秒中的请求数量可能也就 50 个请求，对整个系统几乎没有任何的压力。

![mq-5](/images/mq-5.png)

如果使用 MQ，每秒 5k 个请求写入 MQ，A 系统每秒钟最多处理 2k 个请求，因为 MySQL 每秒钟最多处理 2k 个。A 系统从 MQ 中慢慢拉取请求，每秒钟就拉取 2k 个请求，不要超过自己每秒能处理的最大请求数量就 ok，这样下来，哪怕是高峰期的时候，A 系统也绝对不会挂掉。而 MQ 每秒钟 5k 个请求进来，就 2k 个请求出去，结果就导致在中午高峰期（1 个小时），可能有几十万甚至几百万的请求积压在 MQ 中。

![mq-6](/images/mq-6.png)

这个短暂的高峰期积压是 ok 的，因为高峰期过了之后，每秒钟就 50 个请求进 MQ，但是 A 系统依然会按照每秒 2k 个请求的速度在处理。所以说，只要高峰期一过，A 系统就会快速将积压的消息给解决掉。

### 消息队列有什么优缺点
优点上面已经说了，就是**在特殊场景下有其对应的好处**，**解耦**、**异步**、**削峰**。

缺点有以下几个：

- 系统可用性降低<br>
系统引入的外部依赖越多，越容易挂掉。本来你就是 A 系统调用 BCD 三个系统的接口就好了，人 ABCD 四个系统好好的，没啥问题，你偏加个 MQ 进来，万一 MQ 挂了咋整，MQ 一挂，整套系统崩溃的，你不就完了？如何保证消息队列的高可用，可以[点击这里查看](/docs/high-concurrency/how-to-ensure-high-availability-of-message-queues.md)。

- 系统复杂度提高<br>
硬生生加个 MQ 进来，你怎么[保证消息没有重复消费](/docs/high-concurrency/how-to-ensure-that-messages-are-not-repeatedly-consumed.md)？怎么[处理消息丢失的情况](/docs/high-concurrency/how-to-ensure-the-reliable-transmission-of-messages.md)？怎么保证消息传递的顺序性？头大头大，问题一大堆，痛苦不已。

- 一致性问题<br>
A 系统处理完了直接返回成功了，人都以为你这个请求就成功了；但是问题是，要是 BCD 三个系统那里，BD 两个系统写库成功了，结果 C 系统写库失败了，咋整？你这数据就不一致了。

所以消息队列实际是一种非常复杂的架构，你引入它有很多好处，但是也得针对它带来的坏处做各种额外的技术方案和架构来规避掉，做好之后，你会发现，妈呀，系统复杂度提升了一个数量级，也许是复杂了 10 倍。但是关键时刻，用，还是得用的。

### Kafka、ActiveMQ、RabbitMQ、RocketMQ 有什么优缺点？

| 特性 | ActiveMQ | RabbitMQ | RocketMQ | Kafka |
|---|---|---|---|---|
| 单机吞吐量 | 万级，比 RocketMQ、Kafka 低一个数量级 | 同 ActiveMQ | 10 万级，支撑高吞吐 | 10 万级，高吞吐，一般配合大数据类的系统来进行实时数据计算、日志采集等场景 |
| topic 数量对吞吐量的影响 | | | topic 可以达到几百/几千的级别，吞吐量会有较小幅度的下降，这是 RocketMQ 的一大优势，在同等机器下，可以支撑大量的 topic | topic 从几十到几百个时候，吞吐量会大幅度下降，在同等机器下，Kafka 尽量保证 topic 数量不要过多，如果要支撑大规模的 topic，需要增加更多的机器资源 |
| 时效性 | ms 级 | 微秒级，这是 RabbitMQ 的一大特点，延迟最低 | ms 级 | 延迟在 ms 级以内 |
| 可用性 | 高，基于主从架构实现高可用 | 同 ActiveMQ | 非常高，分布式架构 | 非常高，分布式，一个数据多个副本，少数机器宕机，不会丢失数据，不会导致不可用 |
| 消息可靠性 | 有较低的概率丢失数据 | 基本不丢 | 经过参数优化配置，可以做到 0 丢失 | 同 RocketMQ |
| 功能支持 | MQ 领域的功能极其完备 | 基于 erlang 开发，并发能力很强，性能极好，延时很低 | MQ 功能较为完善，还是分布式的，扩展性好 | 功能较为简单，主要支持简单的 MQ 功能，在大数据领域的实时计算以及日志采集被大规模使用 |


综上，各种对比之后，有如下建议：

一般的业务系统要引入 MQ，最早大家都用 ActiveMQ，但是现在确实大家用的不多了，没经过大规模吞吐量场景的验证，社区也不是很活跃，所以大家还是算了吧，我个人不推荐用这个了；

后来大家开始用 RabbitMQ，但是确实 erlang 语言阻止了大量的 Java 工程师去深入研究和掌控它，对公司而言，几乎处于不可控的状态，但是确实人家是开源的，比较稳定的支持，活跃度也高；

不过现在确实越来越多的公司会去用 RocketMQ，确实很不错，毕竟是阿里出品，但社区可能有突然黄掉的风险（目前 RocketMQ 已捐给 [Apache](https://github.com/apache/rocketmq)，但 GitHub 上的活跃度其实不算高）对自己公司技术实力有绝对自信的，推荐用 RocketMQ，否则回去老老实实用 RabbitMQ 吧，人家有活跃的开源社区，绝对不会黄。

所以**中小型公司**，技术实力较为一般，技术挑战不是特别高，用 RabbitMQ 是不错的选择；**大型公司**，基础架构研发实力较强，用 RocketMQ 是很好的选择。

如果是**大数据领域**的实时计算、日志采集等场景，用 Kafka 是业内标准的，绝对没问题，社区活跃度很高，绝对不会黄，何况几乎是全世界这个领域的事实性规范。