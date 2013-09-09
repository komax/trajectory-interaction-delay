function [dx, dy, dz, frame_id] = velocities(trajectory)
%%%function [dx, dy, dz, frame_id] = velocities(trajectory)
%%% velocities, measured in metres per frame
num_frames = size(trajectory, 1);
dX = trajectory(2:end,1:3) - trajectory(1:end-1,1:3);
dx = dX(:,1);
dy = dX(:,2);
dz = dX(:,3);
frame_id = trajectory(2:end,4);
end
