function plotMatching(javaMatchingObj)
% Plots the computed frechet matching in a swing GUI
if isempty(javaclasspath('-dynamic'))
    javaaddpath bin;
end
visualization.VisualizationLauncher.launchMatchingPlot(javaMatchingObj);
end

