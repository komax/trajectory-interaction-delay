function velocities = average_velocities(varargin)
%%% DEPRECATED
%%% TODO should I smooth then average or average then smooth?
num_birds = nargin;
birds = {};
dX = [];
for arg = varargin
    bird_id = arg{1}(1);
    birds{bird_id} = get_bird(bird_id + 16);
    [dx, dy, dz, frame_id] = smoothing.flight_velocities(birds{bird_id});

    dX = [dX; smoothing.apply_butterworth([dx,dy,dz,frame_id],2,0.1)];
end

start_frame = min(dX(:,4));
stop_frame = max(dX(:,4));

velocities = [repmat([NaN],stop_frame - start_frame + 1, 3) (start_frame:stop_frame)'];

%%%for frame = start_frame:stop_frame
%%%    velocities(
%%%end

for row = 1:size(dX,1)
    frame = dX(row,4);
    idx = find(velocities(:,4) == frame);
    if isnan(velocities(idx,1))
        velocities(idx,1:3) = dX(row,1:3);
    else
        velocities(idx,1:3) = velocities(idx,1:3) + dX(row,1:3);
    end
end
for frame = start_frame:stop_frame
    count = sum(dX(:,4) == frame);
    if count > 0
        idx = find(velocities(:,4) == frame);
        if any(isnan(velocities(idx,1:3)))
            velocities(idx,1:3)
        end
        velocities(idx,1:3) = velocities(idx, 1:3) ./ count;
    end
end

end
