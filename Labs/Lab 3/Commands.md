ssh dardel.pdc.kth.se

scp -r "/Users/casperkristiansson/programming/Parallel and Distributed Computing - DD2443/Labs/Lab 3/src" casperkr@dardel.pdc.kth.se:/cfs/klemming/home/c/casperkr/Public/lab3/src

sbatch ./measure_job.sh
squeue -u casperkr
