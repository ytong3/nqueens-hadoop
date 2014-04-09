# 4/9
Let the solver run iteratively.


# 4/7
Input files are in the following format:

    8|1,3,5,7,4,6....|T\n
    8|1,3,5,7,5,3....|F\n

where each line represents a kind of queens layout.

Character '|' divides each line into three parts, including the dimension, the
queen layout, and a descriptor.

The numbers in a line are comma-seperated. If number i is at the j-th slot of
a certain line, it says we place a queen at the i-th column of j-th row at the
board. The trailing descriptor, which takes value of F, T, or C, indicates whether the board layout is a dead end or
it search can continue, or the layout is already complete, respectively.

If a line has less number of elements than the dimension, then it is said to be incomplete.
Unless indicated as F, an incomplete board layout will continue another step of
search until either it reaches an dead end or the board becomes complete.
=============
Mapper:
A mapper should read each board layout indicated as T in the input file, and
continue the search with one more step and emits the result to the reducer.
For example: the mapper gets an input as '1,3,5|T', it outputs '1,3,5,7|T',
'1,3,5,8|T' ... Should '1,3,5' is an dead end after exhausting the search, the
mapper indicates the fact by change T to F, i.e. '1,3,5|F' such that following
searches will skip this branch.

==============
Reducer:
Upon receiving a incomplete layout from the mapper, the reducer also continues
the search one more step and write the results into the persistent memory to be
read by the mappers in the next iteration.

==============
Lessons:
Top-level design is not waste of time at all.
In a project, one should first define those top level, including message format,
program framework, etc.
Then we implement and realize the design.

# 4/3
+ created a repository on github for version control
+ imported previous logs
+ had a new idea of solving the problem using iterative MapReduce leveraging
  hadoop framework.

Main body complete.
Will begin testing and debug soon
========================

# 2/20/2014
+ Project kick off
+ Proposal submitted
