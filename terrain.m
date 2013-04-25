function [cells, time_diff] = free_space(trajectory1, trajectory2)
    start = min([trajectory1(:,4); trajectory2(:,4)]);
    stop = max([trajectory1(:,4); trajectory2(:,4)]);
    s = stop - start + 1;
    t1 = zeros(s, size(trajectory1,2));
    t2 = zeros(s, size(trajectory2,2));
    for t = start:stop
        if find(trajectory1(:,4) == t)
             
            t1(t - s,:) = trajectory1(find(trajectory1(:,4) == t),:);
        end
        if find(trajectory2(:,4) == t)
            t2(t - s,:) = trajectory2(find(trajectory2(:,4) == t),:);
        end

    end
    [cells, time_diff] = compute(t1, t2);
%%%    colormap([1 1 1; 0 0 0]);
    image(cells)
end

function [cells, time_diff] = compute(trajectory1, trajectory2)

n1 = length(trajectory1);
n2 = length(trajectory2);
cells = zeros(n1,n2);
time_diff = zeros(n1,n2);
for i = 1:n1
    p1 = trajectory1(i, 1:3);
    for j = 1:n2

        p2 = trajectory2(j, 1:3);
        d = norm(p1 - p2,2); % euclidean distance
        frame_dist = trajectory1(i,4) - trajectory2(j,4);
        if ~ (trajectory1(i,4) & trajectory2(i,4))
            d = 9000;
        end
        cells(n1 - i + 1,j) = d;  % (0,0) to bottom left corner of matrix



    end
end
for i = 1:n1
    cells(n1 - i + 1,i) = 8000;
end


end


