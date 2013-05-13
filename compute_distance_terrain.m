function [cells] = compute(t1, t2)
start_frame = min([t1(:,4); t2(:,4)]);
stop_frame = max([t1(:,4); t2(:,4)]);
size_union = stop_frame - start_frame;
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
            cells(size_union - idx1 + 1,  idx2 + 1) = d;
        end
    end
end
end % end function

%%%n1 = length(trajectory1);
%%%n2 = length(trajectory2);
%%%cells = zeros(n1,n2);
%%%for i = 1:n1
%%%    p1 = trajectory1(i, 1:3);
%%%    for j = 1:n2

%%%        p2 = trajectory2(j, 1:3);
%%%        d = norm(p1 - p2,2); % euclidean distance
%%%        if ~ (trajectory1(i,4) & trajectory2(i,4)) % TODO uhoh that looks wrong
%%%            d = Inf;
%%%        end
%%%        cells(n1 - i + 1,j) = d;  % (0,0) to bottom left corner of matrix



%%%    end
%%%end


function [f] = get_frame(traj, i)
    f = traj(traj(:,4) == i,:);
end
