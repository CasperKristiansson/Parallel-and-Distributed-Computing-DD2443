#!/usr/bin/env bash

# Do not run this script directly on PDC.

# This stops the script when a command's exit code is non-zero (i.e., error).
set -e

ALGO=JavaSort
SIZE=1000000
WARMUP=10
MEASURE=1000
SEED=42

javac MeasureMain.java

for THREADS in 2 4 8 16 32 48 64 96; do
	java MeasureMain $ALGO $THREADS $SIZE $WARMUP $MEASURE $SEED
done
