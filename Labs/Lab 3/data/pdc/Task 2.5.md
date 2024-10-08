java Main 1 GlobalLog Normal 100000 1:1:8 1000000 2 5
Warmup time: 771016370
Warmup discrepancy: 0
Warmup time: 831348851
Warmup discrepancy: 0
Measurement time: 815921461
Measurement discrepancy: 0
Measurement time: 783376721
Measurement discrepancy: 0
Measurement time: 700101237
Measurement discrepancy: 0
Measurement time: 694948116
Measurement discrepancy: 0
Measurement time: 902669324
Measurement discrepancy: 0
java Main 2 GlobalLog Normal 100000 1:1:8 1000000 2 5
Warmup time: 1367555789
Warmup discrepancy: 0
Warmup time: 1596932583
Warmup discrepancy: 0
Measurement time: 1621297573
Measurement discrepancy: 0
Measurement time: 1485245748
Measurement discrepancy: 0
Measurement time: 1708167038
Measurement discrepancy: 0
Measurement time: 1624643587
Measurement discrepancy: 0
Measurement time: 1803978281
Measurement discrepancy: 0
java Main 4 GlobalLog Normal 100000 1:1:8 1000000 2 5
Warmup time: 2503106748
Warmup discrepancy: 1
Warmup time: 2674054823
Warmup discrepancy: 2
Measurement time: 1734811009
Measurement discrepancy: 2
Measurement time: 2478118019
Measurement discrepancy: 2
Measurement time: 2115687156
Measurement discrepancy: 2
Measurement time: 2177298446
Measurement discrepancy: 3
Measurement time: 2054379841
Measurement discrepancy: 4
java Main 8 GlobalLog Normal 100000 1:1:8 1000000 2 5
Warmup time: 6494068711
Warmup discrepancy: 1
Warmup time: 6007572918
Warmup discrepancy: 3
Measurement time: 4770270903
Measurement discrepancy: 5
Measurement time: 6228582491
Measurement discrepancy: 5
Measurement time: 5869070843
Measurement discrepancy: 7
Measurement time: 4854424986
Measurement discrepancy: 8
Measurement time: 4641981650
Measurement discrepancy: 10
java Main 16 GlobalLog Normal 100000 1:1:8 1000000 2 5
Warmup time: 16660397235
Warmup discrepancy: 3
Warmup time: 15163388743
Warmup discrepancy: 5
Measurement time: 13979540133
Measurement discrepancy: 6
Measurement time: 14158708250
Measurement discrepancy: 9
Measurement time: 13816059792
Measurement discrepancy: 11
Measurement time: 15692393460
Measurement discrepancy: 12
Measurement time: 13803905086
Measurement discrepancy: 14
java Main 32 GlobalLog Normal 100000 1:1:8 1000000 2 5
Warmup time: 37705473693
Warmup discrepancy: 6
Warmup time: 32873972985
Warmup discrepancy: 7
Measurement time: 37296309735
Measurement discrepancy: 9
Measurement time: 35106123417
Measurement discrepancy: 12
Measurement time: 36064545966
Measurement discrepancy: 16
Measurement time: 24089938319
Measurement discrepancy: 20
Measurement time: 25720433747
Measurement discrepancy: 23
java Main 48 GlobalLog Normal 100000 1:1:8 1000000 2 5
Warmup time: 58239891005
Warmup discrepancy: 6
Warmup time: 67811180137
Warmup discrepancy: 11
Measurement time: 58966168542
Measurement discrepancy: 15
Measurement time: 36082539225
Measurement discrepancy: 19
Measurement time: 38732340130
Measurement discrepancy: 25
Measurement time: 67848084664
Measurement discrepancy: 32
Exception in thread "main" java.lang.OutOfMemoryError: Java heap space
        at java.base/java.lang.reflect.Array.newArray(Native Method)
        at java.base/java.lang.reflect.Array.newInstance(Array.java:78)
        at java.base/java.util.Arrays.copyOf(Arrays.java:3513)
        at java.base/java.util.Arrays.copyOf(Arrays.java:3481)
        at java.base/java.util.concurrent.ConcurrentLinkedQueue.toArrayInternal(ConcurrentLinkedQueue.java:642)
        at java.base/java.util.concurrent.ConcurrentLinkedQueue.toArray(ConcurrentLinkedQueue.java:716)
        at LockFreeSkipListGlobal.getLog(LockFreeSkipListGlobal.java:207)
        at Main.main(Main.java:65)
java Main 64 GlobalLog Normal 100000 1:1:8 1000000 2 5
Warmup time: 71080030375
Warmup discrepancy: 12
Warmup time: 52289899336
Warmup discrepancy: 19
Measurement time: 70739836525
Measurement discrepancy: 30
Measurement time: 82145641206
Measurement discrepancy: 36
Exception in thread "main" java.lang.OutOfMemoryError: Java heap space
        at java.base/java.lang.reflect.Array.newArray(Native Method)
        at java.base/java.lang.reflect.Array.newInstance(Array.java:78)
        at java.base/java.util.Arrays.copyOf(Arrays.java:3513)
        at java.base/java.util.Arrays.copyOf(Arrays.java:3481)
        at java.base/java.util.concurrent.ConcurrentLinkedQueue.toArrayInternal(ConcurrentLinkedQueue.java:642)
        at java.base/java.util.concurrent.ConcurrentLinkedQueue.toArray(ConcurrentLinkedQueue.java:716)
        at LockFreeSkipListGlobal.getLog(LockFreeSkipListGlobal.java:207)
        at Main.main(Main.java:65)
java Main 96 GlobalLog Normal 100000 1:1:8 1000000 2 5
Warmup time: 145033191233
Warmup discrepancy: 30
Warmup time: 147732115133
Warmup discrepancy: 41
Measurement time: 150354000929
Measurement discrepancy: 51
Exception in thread "main" java.lang.OutOfMemoryError: Java heap space
        at java.base/java.lang.reflect.Array.newArray(Native Method)
        at java.base/java.lang.reflect.Array.newInstance(Array.java:78)
        at java.base/java.util.Arrays.copyOf(Arrays.java:3513)
        at java.base/java.util.Arrays.copyOf(Arrays.java:3481)
        at java.base/java.util.concurrent.ConcurrentLinkedQueue.toArrayInternal(ConcurrentLinkedQueue.java:642)
        at java.base/java.util.concurrent.ConcurrentLinkedQueue.toArray(ConcurrentLinkedQueue.java:716)
        at LockFreeSkipListGlobal.getLog(LockFreeSkipListGlobal.java:207)
        at Main.main(Main.java:65)
java Main 1 GlobalLog Normal 100000 1:1:0 1000000 2 5
Warmup time: 1250443908
Warmup discrepancy: 0
Warmup time: 1232598251
Warmup discrepancy: 0
Measurement time: 1110443851
Measurement discrepancy: 0
Measurement time: 1210897718
Measurement discrepancy: 0
Measurement time: 1252149729
Measurement discrepancy: 0
Measurement time: 1214693229
Measurement discrepancy: 0
Measurement time: 1025027312
Measurement discrepancy: 0
java Main 2 GlobalLog Normal 100000 1:1:0 1000000 2 5
Warmup time: 1658680439
Warmup discrepancy: 0
Warmup time: 1432314450
Warmup discrepancy: 0
Measurement time: 1490153572
Measurement discrepancy: 0
Measurement time: 2146371380
Measurement discrepancy: 0
Measurement time: 1597969213
Measurement discrepancy: 0
Measurement time: 1678302943
Measurement discrepancy: 0
Measurement time: 1390108408
Measurement discrepancy: 0
java Main 4 GlobalLog Normal 100000 1:1:0 1000000 2 5
Warmup time: 2863986123
Warmup discrepancy: 0
Warmup time: 2554102751
Warmup discrepancy: 0
Measurement time: 2551326371
Measurement discrepancy: 0
Measurement time: 2132567058
Measurement discrepancy: 0
Measurement time: 2105085217
Measurement discrepancy: 0
Measurement time: 1831309887
Measurement discrepancy: 0
Measurement time: 1752615571
Measurement discrepancy: 0
java Main 8 GlobalLog Normal 100000 1:1:0 1000000 2 5
Warmup time: 4769983870
Warmup discrepancy: 0
Warmup time: 4278135715
Warmup discrepancy: 0
Measurement time: 3745295168
Measurement discrepancy: 0
Measurement time: 3118589157
Measurement discrepancy: 0
Measurement time: 3537709133
Measurement discrepancy: 0
Measurement time: 2647659354
Measurement discrepancy: 0
Measurement time: 3372557647
Measurement discrepancy: 0
java Main 16 GlobalLog Normal 100000 1:1:0 1000000 2 5
Warmup time: 8319577323
Warmup discrepancy: 0
Warmup time: 7250853680
Warmup discrepancy: 0
Measurement time: 7775084916
Measurement discrepancy: 2
Measurement time: 7588075208
Measurement discrepancy: 4
Measurement time: 6082075115
Measurement discrepancy: 4
Measurement time: 6309875171
Measurement discrepancy: 4
Measurement time: 5240642729
Measurement discrepancy: 4
java Main 32 GlobalLog Normal 100000 1:1:0 1000000 2 5
Warmup time: 23935433239
Warmup discrepancy: 0
Warmup time: 18778912509
Warmup discrepancy: 0
Measurement time: 23398774003
Measurement discrepancy: 0
Measurement time: 22157318708
Measurement discrepancy: 0
Measurement time: 23869698689
Measurement discrepancy: 0
Measurement time: 21280831347
Measurement discrepancy: 2
Measurement time: 22829080509
Measurement discrepancy: 2
java Main 48 GlobalLog Normal 100000 1:1:0 1000000 2 5
Warmup time: 37733153384
Warmup discrepancy: 0
Warmup time: 37628421441
Warmup discrepancy: 2
Measurement time: 38451538502
Measurement discrepancy: 2
Measurement time: 34637763813
Measurement discrepancy: 2
Measurement time: 34413329648
Measurement discrepancy: 2
Measurement time: 36601524327
Measurement discrepancy: 2
Measurement time: 36234330222
Measurement discrepancy: 2
java Main 64 GlobalLog Normal 100000 1:1:0 1000000 2 5
Warmup time: 53335626336
Warmup discrepancy: 0
Warmup time: 53160718722
Warmup discrepancy: 0
Measurement time: 53461026750
Measurement discrepancy: 0
Measurement time: 47130947705
Measurement discrepancy: 0
Measurement time: 48536074397
Measurement discrepancy: 0
Measurement time: 48978577140
Measurement discrepancy: 4
Measurement time: 48014040240
Measurement discrepancy: 4
java Main 96 GlobalLog Normal 100000 1:1:0 1000000 2 5
Warmup time: 75538086846
Warmup discrepancy: 4
Warmup time: 72950683329
Warmup discrepancy: 6
Measurement time: 68341506270
Measurement discrepancy: 12
Measurement time: 69657084071
Measurement discrepancy: 24
Measurement time: 72498305310
Measurement discrepancy: 40
Exception in thread "main" java.lang.OutOfMemoryError: Java heap space
        at java.base/java.lang.reflect.Array.newArray(Native Method)
        at java.base/java.lang.reflect.Array.newInstance(Array.java:78)
        at java.base/java.util.Arrays.copyOf(Arrays.java:3513)
        at java.base/java.util.Arrays.copyOf(Arrays.java:3481)
        at java.base/java.util.concurrent.ConcurrentLinkedQueue.toArrayInternal(ConcurrentLinkedQueue.java:642)
        at java.base/java.util.concurrent.ConcurrentLinkedQueue.toArray(ConcurrentLinkedQueue.java:716)
        at LockFreeSkipListGlobal.getLog(LockFreeSkipListGlobal.java:207)
        at Main.main(Main.java:65)