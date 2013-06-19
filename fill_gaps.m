function [cells] = fill_gaps(cells)
[width, height] = size(cells);

for i = 1:height-1
    if any(cells(i,:) ~= Inf) && all(cells(i+1,:) == Inf)
        cells(i+1,:) = cells(i,:);
    end
end

for j = 1:width-1
    if any(cells(:,j) ~= Inf) && all(cells(:,j+1) == Inf)
        cells(:,j+1) = cells(:,j);
    end
end

% two pass
for j = width:-1:2
    if any(cells(:,j) ~= Inf) && all(cells(:,j-1) == Inf)
        cells(:,j-1) = cells(:,j);% + 0.05;
    end
end
for i = height:-1:2
    if any(cells(i,:) ~= Inf) && all(cells(i-1,:) == Inf)
        cells(i-1,:) = cells(i,:);% + 0.05;
    end

end

