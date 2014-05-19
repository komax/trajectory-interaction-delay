function [reducedColorMap] = halfColorSpectrum(oldColorMap)
lengthOfOldColorMap = length(oldColorMap);
reducedColorMap = zeros(lengthOfOldColorMap/2,3);
reducedColorMap(1, :) = oldColorMap(1, :);
for row = 2:lengthOfOldColorMap
    if mod(row,2)
        newIndex = (row-1) / 2 + 1;
        reducedColorMap(newIndex, :) = oldColorMap(row, :);
    end
end


