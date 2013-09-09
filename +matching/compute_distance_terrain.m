function [cells] = compute_distance_terrain(t1, t2)
%%%function [cells] = compute_distance_terrain(t1, t2)
%%% Given two trajectories, builds the matrix which plots
%%% the distance between the two for all pairs of points on
%%% the two trajectories. If either trajectory is missing 
%%% a location fix for either frame, the distance is set to inf.
%%% This is the "square" version, i.e. all points from both
%%% trajectories.
start_frame = min([t1(:,4); t2(:,4)]);
stop_frame = max([t1(:,4); t2(:,4)]);
size_union = stop_frame - start_frame + 1;
cells = inf(size_union, size_union);

for f1 = start_frame:stop_frame
    t1_f1 = get_frame(t1,f1);
    for f2 = start_frame:stop_frame
%%%        fprintf('(%d, %d)\n',f1, f2);
        t2_f2 = get_frame(t2,f2);
        if length(t1_f1) && length(t2_f2)
            p1 = t1_f1(1:3);
            p2 = t2_f2(1:3);
            d = norm(p1 - p2, 2); % euclidean distance
            idx1 = f1 - start_frame;
            idx2 = f2 - start_frame;
            if d == 0
                fprintf('error while computing the square distance terrain\n');
                fprintf('found a zero\n');
            end
            % check bounds
            if (size_union - idx1 > size_union) || (idx2 + 1 > size_union)
                fprintf('Matrix index out of bounds\n');
                return
            end
            cells(size_union - idx1,  idx2 + 1) = d;
        end
    end
end
end % end function

function [f] = get_frame(traj, i)
    f = traj(traj(:,4) == i,:);
end
