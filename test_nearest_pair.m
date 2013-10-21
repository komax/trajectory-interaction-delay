function do_stuff()

m = [  0 2 4 5; 0 0 5 2; 0 0 0 1;0 0 0 0;];

idx = find_nearest_pair(m, 1, 4);

if idx ~= 2
    fprintf(2, 'failed test\n');
end
if find_nearest_pair(m, 2, 4) ~= 4
    fprintf(2, 'failed test 2\n');
end
if find_nearest_pair(m, 3, 4) ~= 4
    fprintf(2, 'failed test 3\n');
end
if find_nearest_pair(m, 4, 4) ~= 3
    fprintf(2, 'failed test 4\n');
end

load('results/frechet_distances_overlap.mat');
if find_nearest_pair(results,2,18) ~= 15
    fprintf(2, 'failed test\n')
end

if find_nearest_pair(results,1,18) ~= 8
    fprintf(2, 'failed test\n')
end
