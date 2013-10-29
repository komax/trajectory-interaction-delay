function [velocities, frame_ids] = velocities(trajectory)
%%%function [dx, dy, dz, frame_id] = velocities(trajectory)
%%% velocities, measured in metres per frame
A = trajectory(1:end-1, 1:3);
B = trajectory(2:end, 1:3);
%%%dX1 = pointwise_distances(A, B);
%%%dX2 = pointwise_distances(B, C);

dT = trajectory(2:end, 4) - trajectory(1:end-1, 4);
%%%velocities = (dX1 + dX2) ./ dT;
velocities = (B - A) ./ repmat(dT, 1, 3);
frame_ids = trajectory(2:end-1,4);

end

function distances = pointwise_distances(a,b)
    distances = sqrt(sum(power(b - a,2),2));
end

