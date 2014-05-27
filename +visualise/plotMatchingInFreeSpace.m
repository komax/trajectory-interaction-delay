function [delayPlot] = plotDistanceTerrain(distanceTerrain, javaMatchingObj)
% Plots a heated body map of the distance terrain of the trajectories with the
% LCF matching.
figure;
hold on;
distanceTerrain = flipdim(distanceTerrain,1);
imagesc(distanceTerrain);
heatedColorMap = colormap(hot);
myHeatedColorMap = halfColorSpectrum(halfColorSpectrum(halfColorSpectrum(heatedColorMap)));
colormap(myHeatedColorMap);
set(gca,'position',[0 0 1 1],'units','normalized');
axis tight;
axis off;
delayPlot = plot(javaMatchingObj.j,javaMatchingObj.i,'g');
end

