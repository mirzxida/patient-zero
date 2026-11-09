# Assignment 4. Patient Zero

Student ID: 254224
Name: Khilolakhon Mirzabekova
Group: [впиши свою группу]

This file is the report. Fill it in as you go. How to start: `START_HERE.md`. The task: `docs/Assignment4.pdf`.

## Program output

The 9 lines your program prints on your data (`java -cp out Main`):

    Mutations: 106
    Shared core: 845
    Marker hits: 4
    First hit: 6427
    Border length: 4
    Shared words: 210
    Tests needed: 47
    Cheapest testing: 183
    Longest chain: 32

My outbreak report (a screenshot of `output/outbreak.html`):

![outbreak](output/outbreak.png)

## Questions

Answer in your own words, two or three sentences each.

**Mission 1. What does the cell d[i][j] of your table mean, and how is it computed from other cells?**
The cell `d[i][j]` stores the minimum number of single-letter changes (edit distance) required to transform the first `i` characters of strain A into the first `j` characters of strain B[cite: 3]. If the current characters match, it takes the value from the diagonal cell `d[i-1][j-1]`; if they differ, it takes the minimum of the top, left, and diagonal cells plus 1, representing insertion, deletion, or replacement costs.

**Mission 2. How is the longest common subsequence different from the longest common substring? My Shared core is bigger than the number of letters that are the same at the same place: why?**
A longest common subsequence consists of characters that appear in the same order in both strings but do not have to be contiguous[cite: 3, 4], whereas a substring requires the characters to be strictly adjacent. The Shared core value is larger because mutations (like insertions or deletions) shift the alignment of the genome; checking characters at the exact same index misses matching characters that have simply been pushed to different positions.

**Mission 3. What does the prefix function tell you, and why does the pointer in the text never go back in KMP? What is the Border length of my marker and what does it mean?**
The prefix function (`pi[i]`) stores the length of the longest proper prefix of the pattern `p[0..i]` that is also a suffix[cite: 3, 4]. The text pointer in KMP never goes back because when a mismatch occurs, the prefix array tells us exactly where the next potential match can continue[cite: 3, 4], allowing us to skip unnecessary re-evaluations. The Border length of my marker is 4, meaning the first 4 characters of the marker perfectly match its last 4 characters.

**Mission 4. Why must you compare the letters after the hashes are equal? What happens to the cost of Rabin-Karp if many windows have the same hash?**
You must compare the actual letters because hash functions can produce collisions, meaning two completely different character sequences might accidentally compute to the same numeric hash value[cite: 4]. If there are too many hash collisions, the Rabin-Karp algorithm degrades from $O(N)$ time to $O(N \times K)$, because it is forced to do a slow character-by-character comparison for almost every window[cite: 4].

**Mission 5. What are the two states of a person in your DP, and how do you get the answer for a person from its children? Why does a greedy "test the person with most links first" not always give the cheapest answer?**
The two states are the minimum cost if the person IS tested, and the minimum cost if the person is NOT tested. If the person is tested, we safely take the minimum of either state for each child; if not tested, we must strictly add the "IS tested" cost of all children to guarantee coverage. A greedy approach fails because picking a central hub might have an extremely high individual test cost, whereas testing its multiple cheaper neighbors instead would result in a lower total expenditure.

## AI use

The syllabus requires you to say which AI tools you used and for what. Write the tool (ChatGPT, Copilot, other) and what you asked it: explain an idea, find an error, review code. Write "none" if you did not use any. If you took code or text from a book or a website, give the source here.

*   **Tool:** Gemini AI
*   **Usage:** Used as a coding assistant to understand the mathematical transition formulas for Edit Distance and LCS, to implement the KMP prefix function without using forbidden String methods[cite: 4], to structure the Rolling Hash updates in $O(1)$[cite: 3, 4], and to formulate the tree DP states for the minimum vertex cover logic.

## Timing (Mission 7)

Milliseconds, average of 3 runs, from `java -cp out Bench`.

| test | size | time |
|---|---|---|
| naive search | 20 000 / 40 000 / 80 000 | 103.4 ms / 186.6 ms / 328.4 ms |
| KMP | 20 000 / 40 000 / 80 000 | 4.5 ms / 3.0 ms / 0.7 ms |
| edit distance | 500 / 1000 / 2000 / 4000 | 8.8 ms / 22.0 ms / 41.5 ms / 156.3 ms |

**What do the numbers say about how the time grows? When you double the size, by how much does each time grow? Two or three sentences.**
The numbers show that the naive search time roughly doubles as the text size doubles ($O(N)$ behavior on worst-case data), while KMP handles the search near-instantly regardless of text size due to avoiding backtracking. The edit distance time roughly quadruples when the size doubles (e.g., from 41.5 ms to 156.3 ms), clearly demonstrating its $O(N \times M)$ quadratic time complexity.

## Complexity

n is the length of the text (or of a strain), m the length of the pattern, k the length of the window, v the number of people. Fill in every algorithm you wrote.

| algorithm | time | space | assumptions |
|---|---|---|---|
| Edit distance | $O(n \times m)$ | $O(n \times m)$ | Uses a 2D table to store states |
| LCS | $O(n \times m)$ | $O(n \times m)$ | Uses a 2D table to store states |
| Prefix function and KMP | $O(n + m)$ | $O(m)$ | Space is used for the `pi` array |
| Rabin-Karp (shared words) | $O(n)$ average | $O(n)$ | Assumes few hash collisions[cite: 4] |
| Tree DP | $O(v)$ | $O(v)$ | Uses recursive DFS stack and state arrays |

## Before you submit

- [x] The four files in `data` and the screenshot `output/outbreak.png` are in the repository.
- [x] `java tools/Check.java` shows all examples right.
- [x] This README is filled in, the program output and the section AI use included.
- [x] The work is pushed to main (at least one commit per mission) and the repository is public.
- [x] The link is attached in Moodle.