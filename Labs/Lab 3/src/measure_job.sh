#!/bin/bash -l
#SBATCH -J measure_sort
#SBATCH -A edu24.dd2443
#SBATCH --nodes=1
#SBATCH --ntasks=1
#SBATCH --cpus-per-task=96
#SBATCH -t 01:30:00
#SBATCH -p shared

module load java/17

ALGO=Default

for SPLIT in 1:1:8 1:1:0; do
    for THREADS in 2 4 8 16 32 48 64 96; do
        echo "java Main $THREADS $ALGO Normal 100000 $SPLIT 1000000 2 5"
        java Main $THREADS $ALGO Normal 100000 $SPLIT 1000000 2 5
    done
done
