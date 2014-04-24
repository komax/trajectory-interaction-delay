function plotDistanceTerrain(distanceTerrain, javaMatchingObj)
% Plots a heated body map of the distance terrain of the trajectories.
figure;
hold on;
imagesc(distanceTerrain);
colormap(hot);
plot(javaMatchingObj.i,javaMatchingObj.j,'g');
axis tight;
end

