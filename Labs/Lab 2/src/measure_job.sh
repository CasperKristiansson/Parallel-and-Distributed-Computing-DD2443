#!/bin/bash -l
#SBATCH -J measure_sort
#SBATCH -A edu24.dd2443
#SBATCH --nodes=1
#SBATCH --ntasks=1
#SBATCH --cpus-per-task=96
#SBATCH -t 00:30:00
#SBATCH -p shared

module load java/17

ALGO=JavaSort
SIZE=1000000
WARMUP=5
MEASURE=100
SEED=42

for THREADS in 2 4 8 16 32 48 64 96; do
    java MeasureMain $ALGO $THREADS $SIZE $WARMUP $MEASURE $SEED
done
