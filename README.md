# sorting-algorithms-showcase

Performance comparison of merge sort and quicksort with a variable insertion sort cutoff in Java.

---

## About

Written in Java, this project benchmarks merge sort and quicksort on randomly generated long integer arrays. Both algorithms fall back to insertion sort below a configurable cutoff threshold. Tests are averaged over 5 runs for each cutoff value from 0 to 30. Results are printed in a format suitable for spreadsheet import.

## Usage

Pass three command-line arguments: integer count, upper limit for values, and random seed. A second fixed test (10,000,000 integers, upper limit 4,000,000,000, seed 30) always runs automatically.

```
java se.kth.Main <integerCount> <upperLimit> <seed>
```

Example:
```
java se.kth.Main 1000000 2000000000 42
```

## Getting Started

### Prerequisites

- Java 11 or later

### Building

**Unix**
```
javac -d out src/se/kth/*.java
```

**Windows**
```
javac -d out src\se\kth\*.java
```

### Running

**Unix**
```
java -cp out se.kth.Main 1000000 2000000000 42
```

**Windows**
```
java -cp out se.kth.Main 1000000 2000000000 42
```

## Configuration

| Parameter | Description |
|-----------|-------------|
| `integerCount` | Number of integers in the test array |
| `upperLimit` | Maximum value for generated integers |
| `seed` | Seed for the random number generator |

---

MIT License - see [LICENSE](LICENSE)
