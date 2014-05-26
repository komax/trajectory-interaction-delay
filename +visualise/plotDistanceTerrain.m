function plotDistanceTerrain(distanceTerrain)
% Plots a heated body map of the distance terrain of the trajectories.
figure;
hold on;
% FIXME Refactoring the usages of dimension flipping
distanceTerrain = flipdim(distanceTerrain,1);
imagesc(distanceTerrain);
colormap(hot);
set(gca,'position',[0 0 1 1],'units','normalized');
axis tight;
axis off;
end

