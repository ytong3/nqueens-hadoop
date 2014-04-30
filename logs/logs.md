# 4/28
Cleaned up the codes. Added necessary comments for future review. Removed unnecessary
codes for debugging purpose.

Codes ready to submit.

# 4/26

The problem seems to be that reducers have to process much and much more entries
than mappers do. For example, in a 15-by-15 board. For each layout processed by
mappers, reducers need to process as many as 10 layout derived from that layout.
This means when mappers complete 10%, reducers completion is bound to 1%. When mappers
complete all the tasks, reducers must finish less than 10% of all the reducer
tasks.
So when mappers can quickly finish their task in case where there are not many input,
reducers cannot follow the same speed, which gives us the impression that reducers
begin to work after all mappers finishes. In fact, they do start to work right after
mappers finish 10% or 5%, but they sometimes cannot even finish 1% by the time mappers
complete their tasks.

# 4/25

Somehow, reducers do not start working until mappers complete 91%, although the
respective parameter `mapred.reduce.slowstart.completed.maps` is already set to
0.05， which means mappers start to work as soon as 5% of mappers complete
mapping.


# 4/20

Adding more slave workers into the hadoop framework.
With 4 slave workers and 4 reducers, time: 8 minutes 29 seconds
With 5 slave workers and 4 reducers, time: 7 minutes 48 seconds
With 6 slave workers and 4 reducers, time: 7 minutes 3  seconds.

# 4/18

Wrote several shell scripts to facilitate compiling and debugging.
+ all.sh
+ cleanup.sh
+ compile.sh
+ make_jar.sh
+ run_program.sh

Especially`gen_initial.sh` generates the initial inputs.
For example, gen_initial.sh 8 generates the following input in a text file.

    8|0|T
    8|1|T
    8|2|T
    8|3|T
    8|4|T
    8|5|T
    8|6|T
    8|7|T

This input serves as the initial step of a 8-queen problem.

# 4/17

## Bug3
By examining the log, the last iteration is useless.
All entries in the result of the second last iteration are already marked as 'C' (complete).
This is because there is already one step provided by the initial step, so the initial
value of dfsStep should be 1 and the termination condition for DFS search should be
'while(dfsStep<=boardDim)'

This bug will not lead to program crash but it undermines the performance.

# 4/15
Tried to increase the number of reducers from the 1, which is the default value, to 3.
This can be done by adding an argument `-Dmapred.reduce.tasks=3` to hadoop core.

After increasing reducers, performance was obviously improved.
For a 15-by-15 board, it now takes only 6 minutes and 38 seconds, which is significantly less than 20 mintues.


# 4/12
Tested the the 15-by-15 board, the result proved to be right. There are 2279184 solutions. It takes about 20 minutes 16:23:19 - 16:53:00.

However, there are a lot of space to improve.
First off, there are only 1 reducer working in each job, which easily became the bottleneck, as it can be seen from the log.
Secondly, the stdout/stderr were not turned off. For millions of records, that could be a huge burden.
Thirdly, the increasing the number of slave worker is expected to speed up the search
Fourthly, refactor the code. Optimize the code, remove redundancy and unnecessary operations.
Fourthly, it remains unknown if change the number of duplication can save the run time, if the dependabilities of machiens are expected to be good enough.
Lastly, explore other more efficient algorithms than the brute-force approach.

=========================
## Bug1
using str.split("|") would not split a "|" separated str, nor would str.split("\|").
The reason is that what is supplied to split are supposed to be a regular expression.
So, we need to do str.split("[|]") to get the job done.
Honestly, I did not quite get why str.split("\|") failed in this case. Need to find it out later.

## Bug2
I used a poor naming convention to confuse myself.
At the class Nqueens, what should be specificed should be variable newLayout,
but I put layout, which happened to be another variable.
To avoid this issue, I need to come up with a good, clear, and error-proof naming convention.
Maybe it is a good idea to  consult Google C++/Java style guide.

## stdout/stderr in hadoop
One can dig into the log of jobtracker of MapReduce to find the stdout/stderr output.


# 4/9
Let the solver run iteratively.
The trick is to let the mapper of N-th iteration takes as input the output of (N-1)-th iteration.
Respective code follows.

    if (dfsStep==0)
      FileInputFormat.setInputPaths(conf, new Path(args[0]));
    else
      FileInputFormat.setInputPaths(conf, new Path(args[1]+(numIteration-1)));

    FileOutputFormat.setOutputPath(conf, new Path(args[1]+(numIteration)));


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
