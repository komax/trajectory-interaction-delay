function [dx, dy, dz, frame_id] = velocities(trajectory)
%%%function [dx, dy, dz, frame_id] = velocities(trajectory)
%%% velocities, measured in metres per frame
num_frames = size(trajectory, 1);
dX1 = trajectory(2:end-1,1:3) - trajectory(1:end-2,1:3);
dX2 = trajectory(3:end,1:3) - trajectory(2:end-1,1:3);
dX = dX1 + dX2;
dx = dX(:,1);
dy = dX(:,2);
dz = dX(:,3);
frame_id = trajectory(2:end-1,4);
end
