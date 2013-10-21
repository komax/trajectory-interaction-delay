function blah(traj, k)

load results/frechet_distances_overlap.mat;

max_bird = max(traj(:,5));
max_bird = 18; % TEMP TODO FIX

for curr = 1:max_bird
    curr;
    neighbours = k_nearest_neighbour(k, curr, traj);
    counts = [histc(neighbours, 1:max_bird); 1:max_bird]';
    ranks = sortrows(counts, [-1, 2]);
    top_nn = ranks(1,2);
    
    top_fd = find_nearest_pair(results,curr,max_bird);
    if size(top_fd,2) > 1
        fprintf('ties for smallest FD?\n');
    end

    if top_nn == top_fd
        fprintf(1, 'Bird i = %d: (nn: %d), (fd: %d) SAME\n',curr,top_nn, top_fd);
    else
        fprintf(1, 'Bird i = %d: (nn: %d), (fd: %d) DIFFERENT\n',curr,top_nn, top_fd);
%%%        fprintf(1, 'second: %d\n',ranks(2,2));
%%%        if ranks(2,2) == top_fd
%%%            fprintf(2,'agree now\n');
%%%        end
    end
end

end


