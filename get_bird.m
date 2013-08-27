function bird = get_bird(i, varargin)
%%% function bird = get_bird(i, varargin)
%%%  Fetch a matrix corresponding to either bird i from
%%%  the first data file (of 16 birds) or the second.
%%%  matrix has five columns: x, y, z, frame_id, bird_id
if nargin == 1
    load data_from_anael/traj16.mat;
elseif varargin(1,1) == 1
    load data_from_anael/traj16.mat;
elseif varargin(1,1) == 2
    load data_from_anael/traj18.mat;
end

bird = trajectories(trajectories(:,5) == i,:);
end
