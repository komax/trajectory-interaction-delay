function bird = get_bird(i)
load traj16.mat;
bird = trajectories(trajectories(:,5) == i,:);
end
