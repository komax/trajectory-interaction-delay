function plotDistanceTerrain(distanceTerrain, javaMatchingObj)
% Plots a heated body map of the distance terrain of the trajectories with the
% LCF matching.
figure;
hold on;
distanceTerrain = flipdim(distanceTerrain,1);
imagesc(distanceTerrain);
heatedColorMap = colormap(hot);
heatedColorMap
myHeatedColorMap = zeros(length(heatedColorMap)/2,3);
myHeatedColorMap(1) = heatedColorMap(1);
for row = 2:length(heatedColorMap)
    if mod(row,2)
        mappedIndex = (row-1) / 2  + 1;
        myHeatedColorMap(mappedIndex, :) = heatedColorMap(row, :);
    end
end
myHeatedColorMap
colormap(myHeatedColorMap);
plot(javaMatchingObj.j,javaMatchingObj.i,'g');
set(gca,'position',[0 0 1 1],'units','normalized');
axis square;
axis tight;
axis off;
end

