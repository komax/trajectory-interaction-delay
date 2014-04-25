function [cells] = compute_distance_terrain(t1, t2, varargin)
%%%function [cells] = compute_distance_terrain(t1, t2, 'square')
%%% Given two trajectories, builds the matrix which plots
%%% the distance between the two for all pairs of points on
%%% the two trajectories. If either trajectory is missing 
%%% a location fix for either frame, the distance is set to inf.
%%% This is the "square" version, i.e. all points from both
%%% trajectories.
%%%function [cells] = compute_distance_terrain(t1, t2)
%%% cells is a n x m grid where n is length of t1 and m is length of t2
if nargin == 2 
    cells = zeros(length(t1),length(t2));
    for i = 1:size(t1,1)
        for j = 1:size(t2,1)
            p = t1(i,1:3);
            q = t2(j,1:3);
            % Compute distance between p and q using the Euclidean
            % distance.
            d = norm(p - q,2);
            cells(i,j) = d;
        end
    end
    cells = flipdim(cells,1);
    return;
    % TODO Unclear when the following branch applies.
elseif nargin >= 3 && strcmp(varargin{1},'square')
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
    % TODO flips matrix mxn matrix
    cells = flipdim(cells,1);
else
    fprintf(2,'Invalid call to matching.compute_distance_terrain\n');
end % end if
end % end function

function [f] = get_frame(traj, i)
% FIXME Specific to the bird data?
% Return that frame that has i as in the fourth column.
    f = traj(traj(:,4) == i,:);
end
