function plotDistanceTerrain(distanceTerrain)
% Plots a heated body map of the distance terrain of the trajectories.
figure;
hold on;
imagesc(distanceTerrain);
colormap(hot);
axis tight;
end

