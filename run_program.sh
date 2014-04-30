#!/bin/bash

hadoop jar Nqueens.jar nqueens.Nqueens -Dnqueens.board.dimension=$2 -Dmapred.reduce.tasks=3 nqueens/initial nqueens/$1 $2
