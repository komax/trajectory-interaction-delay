function [x, y, z, frame_id] = velocities(trajectory)
%%%function [dx, dy, dz, frame_id] = velocities(trajectory)
%%% velocities, measured in metres per frame
num_frames = size(trajectory, 1);
direction = zeros(num_frames - 1, 4);
for i = 2:num_frames
    previous = trajectory(i - 1, 1:3);
    current = trajectory(i, 1:3);
    direction(i - 1,:) = [current - previous, trajectory(i-1,4)];
end
x = direction(:,1);
y = direction(:,2);
z = direction(:,3);
frame_id = direction(:,4);

end
