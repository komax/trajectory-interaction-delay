delaySpaces = {'normal';'directionalDistance';'dynamicInteraction';'dynamicDistance';'heading'};
normes = [1 2 Inf];

for i = 1:length(delaySpaces)
    currentDelaySpace = delaySpaces{i};
    for j = 1:length(normes)
        currentNorm = normes(j);
        runBatsExperiment(currentDelaySpace,currentNorm);
    end
end
