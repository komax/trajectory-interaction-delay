load traj16.mat;

bird8 = trajectories(trajectories(:,5) == 8,:);
bird9 = trajectories(trajectories(:,5) == 9,:);

[terrain] = compute_distance_terrain(bird8,bird9);

for cutoff = 1:5
    fprintf('Testing DFS for cutoff %f: \n',cutoff);
    if depth_first_search(terrain, cutoff)
        fprintf('path found\n');
    else
        fprintf('no path found\n');
    end
end
for cutoff = 3.5:0.1:4.0
    fprintf('Testing DFS for cutoff %f: \n',cutoff);
    if depth_first_search(terrain, cutoff)
        fprintf('path found\n');
    else
        fprintf('no path found\n');
    end
end
for cutoff = 3.7:0.01:3.8
    fprintf('Testing DFS for cutoff %f: \n',cutoff);
    if depth_first_search(terrain, cutoff)
        fprintf('path found\n');
    else
        fprintf('no path found\n');
    end
end

