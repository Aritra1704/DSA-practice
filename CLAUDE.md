# LeetCode Practice — Aritra Ranjan Pal

## Purpose
Daily LeetCode practice. One problem minimum per day.
Goal: FAANG interview readiness in 6 months.

## Structure

```
src/main/java/com/aritra/leetcode/
├── arrays/          — Arrays, Two Pointers, Sliding Window
├── linkedlist/      — Linked List problems
├── trees/           — Binary Trees, BST, Tries
├── graphs/          — BFS, DFS, Union Find, Topological Sort
├── dp/              — Dynamic Programming
├── heap/            — Priority Queue, Top K problems
├── binarysearch/    — Binary Search on arrays and answer space
├── slidingwindow/   — Variable and fixed window problems
├── stack/           — Stack, Monotonic Stack, LRU Cache
└── misc/            — Math, Bit Manipulation, String problems

src/test/java/com/aritra/leetcode/
└── (mirrors main — one test class per solution)

notes/
└── PROGRESS.md      — Daily log of problems solved
```

## File Naming Convention

```
Solution file: {ProblemNumber}_{ProblemName}.java
Test file:     {ProblemNumber}_{ProblemName}Test.java

Example:
  arrays/P001_TwoSum.java
  arrays/P001_TwoSumTest.java
```

## Solution Template

Every solution file must have:
1. Problem number and title
2. LeetCode URL
3. Difficulty
4. Pattern used
5. Time and space complexity
6. Approach explanation (2-3 lines)
7. The solution

See: arrays/P001_TwoSum.java as the reference example.

## Test Template

Every test file must have:
1. Basic test cases (from LeetCode examples)
2. Edge cases (empty input, single element, duplicates)
3. Run with: `mvn test`

## Daily Workflow

1. Pick problem from notes/PROGRESS.md (next unchecked)
2. Create solution file from template
3. Solve it (try 15 min before looking at hints)
4. Write tests
5. Run: `mvn test`
6. Update PROGRESS.md with ✅ and notes

## Commands

```bash
# Run all tests
mvn test

# Run tests for specific pattern
mvn test -Dtest="*Arrays*"

# Run single test
mvn test -Dtest="P001_TwoSumTest"

# Compile only
mvn compile
```

## Pattern Reference
See: /Users/aritrarpal/Documents/interview_prep/concepts/DSA_Patterns.md
