# Panama Off-heap vs Primitve on-heap allocation on primitve types

The Allocation Benchmarks program is designed to measure the performance in terms of execution times when allocating arrays of various sizes through the JVM as primitive types on the heap and off-heap using the Panama API.

## Prerequisites

- JDK 21 or later (with Panama API support)
- Java compiler (`javac`)
- Java Virtual Machine (`java`)

## Compilation

Compile the program using the following command:

```bash
javac --enable-preview -source 17 Alloc.java
```            

## Run


```bash
java -Xms20G -Xmx20G --enable-preview Alloc
```


* -Xms20G sets the initial heap size to 20 gigabytes.
* -Xmx20G sets the maximum heap size to 20 gigabytes.
* --enable-preview enables the use of preview features.



## Initial findings
Comparing a 1MB, 5MB, 50MB, 500MB, 1GB allocation on `floats` with various interactions to enable JIT.

| Array Size (MB)  |            1 |            1 |            1 |            1 |            5 |            5 |            5 |            5 |           50 |           50 |           50 |          50 |         500 |         500 |         500 |         500 |        1000 |        1000 |        1000 |        1000 |
|------------------|-------------:|-------------:|-------------:|-------------:|-------------:|-------------:|-------------:|-------------:|-------------:|-------------:|-------------:|------------:|------------:|------------:|------------:|------------:|------------:|------------:|------------:|------------:|
| Iterations       |           10 |           50 |          100 |          200 |           10 |           50 |          100 |          200 |           10 |           50 |          100 |         200 |          10 |          50 |         100 |         200 |          10 |          50 |         100 |         200 |
| Avg time speedup |  2.071693376 | 0.9238784382 | 0.9241373665 |  0.920333627 | 0.9064481624 | 0.8967471617 |  1.318597052 |  3.250146357 | 0.8969166443 | 0.8888306992 |  1.039505154 | 3.830908353 | 2.351019899 |  3.64290878 | 3.949166718 | 4.068314664 | 3.875968705 | 3.989839216 | 4.051047113 | 4.056515941 |
| Min time speedup | 0.9379019828 | 0.9078389786 | 0.9198776394 | 0.9011427344 | 0.9057631333 |  0.890349245 |  3.972939398 |  1.140403268 | 0.9020588523 |  0.888013458 |  1.741676137 | 1.168228418 | 2.354992253 |  3.98727977 | 3.954026129 | 3.976271466 | 3.946497014 | 3.936328649 | 3.947106214 | 3.944837903 |
| Max time speedup |  11.41957527 | 0.9171548906 |  1.016562305 |  1.849172599 | 0.8929949124 | 0.9429990073 | 0.9545698934 |  1.842416666 | 0.8900596716 | 0.8656451165 |  1.745200867 | 2.257952691 | 2.395516707 |   3.2101775 |  4.00318672 | 6.118421661 | 3.806771273 | 4.689236852 | 5.248539901 | 4.504534929 |
| Std time speedup |  88.97749329 |  0.994443398 |  1.489087657 |   2.73152834 |  0.501646588 |  1.573177946 | 0.2627654394 | 0.8816630975 | 0.4052955684 | 0.6797531897 | 0.5618390944 | 1.740666187 | 2.318859456 | 1.484543106 | 6.082537889 | 28.52842901 | 2.512906066 | 10.04699821 | 26.67635983 | 15.21583165 |


## Experimental Setup 