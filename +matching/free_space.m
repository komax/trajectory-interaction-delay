function cells = free_space(trajectory1, trajectory2, delta)
    cells = compute(trajectory1, trajectory2, delta);
    colormap([1 1 1; 0 0 0]);
    imagesc(cells)
end

function cells = compute(trajectory1, trajectory2, delta)

n1 = length(trajectory1);
n2 = length(trajectory2);
cells = zeros(n1,n2);
for i = 1:n1
    p1 = trajectory1(i, 1:3);
    for j = 1:n2

        p2 = trajectory2(j, 1:3);
        d = norm(p1 - p2,2); % euclidean distance
        if d > delta
            cells(n1 - i + 1,j) = 1;  % (0,0) to bottom left corner of matrix
        end


    end
end


end


