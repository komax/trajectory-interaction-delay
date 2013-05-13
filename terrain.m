function [cells, time_diff] = free_space(trajectory1, trajectory2)
    start = min([trajectory1(:,4); trajectory2(:,4)]);
    stop = max([trajectory1(:,4); trajectory2(:,4)]);
    s = stop - start + 1;
    t1 = zeros(s, size(trajectory1,2));
    t2 = zeros(s, size(trajectory2,2));
    for t = start:stop
        if find(trajectory1(:,4) == t)
            disp('hi');
            s
            t
             find(trajectory1(:,4) == t)
             size(trajectory1)
            t1(s - t,:) = trajectory1(find(trajectory1(:,4) == t),:);
        end
        if find(trajectory2(:,4) == t)
            t2(s - t,:) = trajectory2(find(trajectory2(:,4) == t),:);
        end

    end
    [cells, time_diff] = compute_distance_terrain(t1, t2);
%%%    colormap([1 1 1; 0 0 0]);
    image(cells)
end



