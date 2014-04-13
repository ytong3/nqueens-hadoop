#!/bin/bash

hadoop jar Nqueens.jar nqueens.Nqueens -Dnqueens.board.dimension=15 nqueens/initial nqueens/$1 15
