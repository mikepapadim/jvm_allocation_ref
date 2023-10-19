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

| Array Size (MB) | 1 | 1 | 1 | 1 | 5 | 5 | 5 | 5 | 50 | 50 | 50 | 50 | 500 | 500 | 500 | 500 | 1000 | 1000 | 1000 | 1000 |
|---|---:|---:|---:|---:|---:|---:|---:|---:|---:|---:|---:|---:|---:|---:|---:|---:|---:|---:|---:|---:|
| Iterations | 10 | 50 | 100 | 200 | 10 | 50 | 100 | 200 | 10 | 50 | 100 | 200 | 10 | 50 | 100 | 200 | 10 | 50 | 100 | 200 |
| Avg time speedup | 2.07 | 0.92 | 0.92 | 0.92 | 0.91 | 0.90 | 1.32 | 3.25 | 0.90 | 0.89 | 1.04 | 3.83 | 2.35 | 3.64 | 3.95 | 4.07 | 3.88 | 3.99 | 4.05 | 4.06 |
| Min time speedup | 0.94 | 0.91 | 0.92 | 0.90 | 0.91 | 0.89 | 3.97 | 1.14 | 0.90 | 0.89 | 1.74 | 1.17 | 2.35 | 3.99 | 3.95 | 3.98 | 3.95 | 3.94 | 3.95 | 3.94 |
| Max time speedup | 11.42 | 0.92 | 1.02 | 1.85 | 0.89 | 0.94 | 0.95 | 1.84 | 0.89 | 0.87 | 1.75 | 2.26 | 2.40 | 3.21 | 4.00 | 6.12 | 3.81 | 4.69 | 5.25 | 4.50 |

## Experimental Setup 