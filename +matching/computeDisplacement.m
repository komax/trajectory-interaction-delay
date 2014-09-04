function [displacementMatrix] = computeDisplacement(traject1,traject2,normNumber,alpha)
displacementMatrix = zeros(length(traject1),length(traject2));
for i = 1:length(traject1)
    for j = 1:length(traject2)
        distanceI = computeDistance(traject1,i,normNumber);
        distanceJ = computeDistance(traject2,j,normNumber);
        displacementMatrix(i,j) = displacement(distanceI,distanceJ,alpha);
    end
end
displacementMatrix = flipdim(displacementMatrix, 1);
end

function [distanceValue] = computeDistance(trajectory,timestamp,normNumber)
if timestamp == length(trajectory)
    distanceValue = 0.0;
else
    point = trajectory(timestamp, :);
    successorPoint = trajectory(timestamp + 1, :);
    distanceValue = norm(point - successorPoint, normNumber);
end
end

function [displacementValue] = displacement(distanceA,distanceB,alpha)
summedDistance = distanceA + distanceB;
epsilon = 0.000001;
if summedDistance < epsilon
    displacementValue = 1.0;
else
    numerator = abs(distanceA - distanceB);
    fraction = numerator / summedDistance;
    displacementValue = 1 - fraction^alpha;
end
end
