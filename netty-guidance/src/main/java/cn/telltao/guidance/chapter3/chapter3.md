# Netty数据容器 -ByteBuf
~~~
本章节介绍的为 ButeBuf的用法
io.netty.buffer.ByteBuf
~~~
网络数据的基本单位总是字节。Java NIO 提供了 ByteBuffer 作为它 的字节容器，但是这个类使用起来过于复杂，而且也有些繁琐。

Netty 的 ByteBuffer 替代品是 ByteBuf，一个强大的实现，既解决了 JDK API 的局限性， 又为网络应用程序的开发者提供了更好的 API。
<br/>在本章中我们将会说明和 JDK 的 ByteBuffer 相比，ByteBuf 的卓越功能性和灵活性。

这也将有助于更好地理解 Netty 数据处理的一般方式，并为将在第四章中针对 ChannelPipeline 和 ChannelHandler 的讨论做好准备。
### ByteBuf ： Netty的数据容器
因为所有的网络通信都涉及字节序列的移动，所以高效易用的数据结构明显是必不可少的。 Netty 的 ByteBuf 实现满足并超越了这些需求。


零个或多个字节（八位字节）的随机和顺序可访问序列。 该接口为一个或多个原始字节数组 ( byte[] ) 和NIO 缓冲区提供了一个抽象视图。
#### 创建缓冲区
建议使用Unpooled的辅助方法创建新缓冲区，而不是调用单个实现的构造函数。
#### 随机访问索引
就像普通的原始字节数组一样， ByteBuf使用从零开始的索引 。 这意味着第一个字节的索引始终为0 ，最后一个字节的索引始终为capacity - 1 。 
<br/>例如，要迭代缓冲区的所有字节，无论其内部实现如何，您都可以执行以下操作：
````
   ByteBuf buffer = ...;
   for (int i = 0; i < buffer.capacity(); i ++) {
       byte b = buffer.getByte(i);
       System.out.println((char) b);
   }
````
#### 顺序访问索引
ByteBuf提供了两个指针变量来支持顺序读写操作readerIndex用于读操作和writerIndex用于写操作。 以下显示了缓冲区如何被两个指针分割成三个区域：
<pre>
   BEFORE discardReadBytes()
 
       +-------------------+------------------+------------------+
       | discardable bytes |  readable bytes  |  writable bytes  |
       +-------------------+------------------+------------------+
       |                   |                  |                  |
       0      <=      readerIndex   <=   writerIndex    <=    capacity
 
 
   AFTER discardReadBytes()
 
       +------------------+--------------------------------------+
       |  readable bytes  |    writable bytes (got more space)   |
       +------------------+--------------------------------------+
       |                  |                                      |
  readerIndex (0) <= writerIndex (decreased)        <=        capacity
  </pre>
  
##### 可读字节（实际内容）
1. 该段是存储实际数据的地方。 任何名称以read或skip开头的操作都会获取或跳过当前readerIndex处的数据，并将其增加读取字节数。 
2. 如果读操作的参数也是一个ByteBuf并且没有指定目标索引，则指定缓冲区的writerIndex一起增加。
3. 如果剩余的内容不足，则会引发IndexOutOfBoundsException 。 新分配、包装或复制的缓冲区的readerIndex的默认值为0 。
````
   // Iterates the readable bytes of a buffer.
   ByteBuf buffer = ...;
   while (buffer.isReadable()) {
       System.out.println(buffer.readByte());
   }
```` 
###### 可写字节
1. 该段是一个未定义的空间，需要填充。 任何名称以write开头的操作都将在当前writerIndex写入数据并增加写入字节数。 
2. 如果写操作的参数也是一个ByteBuf ，并且没有指定源索引，则指定缓冲区的readerIndex一起增加。 如果没有足够的可写字节，则会引发IndexOutOfBoundsException 。 
3. 新分配的缓冲区的writerIndex的默认值为0 。 包装或复制缓冲区的writerIndex的默认值是缓冲区的capacity 。
````
   // Fills the writable bytes of a buffer with random integers.
   ByteBuf buffer = ...;
   while (buffer.maxWritableBytes() >= 4) {
       buffer.writeInt(random.nextInt());
   }
````
###### 可丢弃字节
1. 该段包含读取操作已读取的字节。 最初，该段的大小为0 ，但随着读取操作的执行，其大小增加到writerIndex 。 
2. 可以通过调用discardReadBytes()回收未使用的区域来丢弃读取的字节，如下所示：

    BEFORE discardReadBytes()
  
        +-------------------+------------------+------------------+
        | discardable bytes |  readable bytes  |  writable bytes  |
        +-------------------+------------------+------------------+
        |                   |                  |                  |
        0      <=      readerIndex   <=   writerIndex    <=    capacity
  
  
    AFTER discardReadBytes()
  
        +------------------+--------------------------------------+
        |  readable bytes  |    writable bytes (got more space)   |
        +------------------+--------------------------------------+
        |                  |                                      |
   readerIndex (0) <= writerIndex (decreased)        <=        capacity
   
请注意，调用discardReadBytes()后无法保证可写字节的内容。 在大多数情况下，可写字节不会被移动，甚至可以填充完全不同的数据，具体取决于底层缓冲区实现。
###### 清除缓冲区索引
您可以通过调用clear()将readerIndex和writerIndex都设置为0 。 它不清除缓冲区内容（例如用0填充）而只是清除两个指针。 另请注意，此操作的语义与ByteBuffer.clear()不同。

    BEFORE clear()
  
        +-------------------+------------------+------------------+
        | discardable bytes |  readable bytes  |  writable bytes  |
        +-------------------+------------------+------------------+
        |                   |                  |                  |
        0      <=      readerIndex   <=   writerIndex    <=    capacity
  
  
    AFTER clear()
  
        +---------------------------------------------------------+
        |             writable bytes (got more space)             |
        +---------------------------------------------------------+
        |                                                         |
        0 = readerIndex = writerIndex            <=            capacity
   
###### 搜索操作
对于简单的单字节搜索，请使用indexOf(int, int, byte)和bytesBefore(int, int, byte) 。 bytesBefore(byte)在处理以NUL结尾的字符串时特别有用。 对于复杂的搜索，将forEachByte(int, int, ByteProcessor)与ByteProcessor实现一起使用。
###### 标记和重置
每个缓冲区中有两个标记索引。 一个用于存储readerIndex ，另一个用于存储writerIndex 。 您始终可以通过调用 reset 方法重新定位两个索引之一。 除了没有readlimit之外，它的工作方式与InputStream的 mark 和 reset 方法readlimit 。
