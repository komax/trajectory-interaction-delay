function bird = get_bird(i)
load data_from_anael/traj16.mat;
bird = trajectories(trajectories(:,5) == i,:);
end
