#!/bin/bash -l
#SBATCH -J measure_sort
#SBATCH -A edu24.dd2443
#SBATCH --nodes=1
#SBATCH --ntasks=1
#SBATCH --cpus-per-task=96
#SBATCH -t 01:30:00
#SBATCH -p shared

module load java/17

# Compile Java files before running
javac *.java

ALGO=Default
OPERATIONS=100000

for SPLIT in 1:1:8 1:1:0; do
    # for THREADS in 1 2 4 8 16 32 48 64 96; do
    for THREADS in 1 2 4 8; do
        echo "java Main $THREADS $ALGO Normal 1000 $SPLIT $OPERATIONS 2 5"
        java Main $THREADS $ALGO Normal 1000 $SPLIT $OPERATIONS 2 5
    done
done
