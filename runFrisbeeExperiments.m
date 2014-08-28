delaySpaces = {'normal';'directionalDistance';'dynamicInteraction';'dynamicDistance'};
normes = [1 2 Inf];

for i = 1:length(delaySpaces)
    currentDelaySpace = delaySpaces{i};
    for j = 1:length(normes)
        currentNorm = normes(j);
        runFrisbeeExperiment(currentDelaySpace,currentNorm);
    end
end