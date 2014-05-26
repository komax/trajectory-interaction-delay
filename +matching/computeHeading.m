function [headingMatrix] = computeHeading(traject1,traject2)
headingMatrix = zeros(length(traject1),length(traject2));
for i = 1:length(traject1)
    for j = 1:length(traject2)
        angleI = headingOnTrajectory(traject1, i);
        angleJ = headingOnTrajectory(traject2, j);
        headingMatrix(i,j) = attraction(angleI, angleJ);
    end
end
headingMatrix = flipdim(headingMatrix, 1);
end

function [angle] = computeAngle(vectorA, vectorB)
angle = atan2(norm(vectorA,vectorB), dot(vectorA,vectorB));
end

function [headingValue] = headingAngle(p, successorP)
projectedPoint = [p(1), successorP(2:)];
% TODO compute heading value among these points
headingValue = 0;
end

function [headingValue] = headingOnTrajectory(traject, timestamp)
if timestamp == length(traject)
    headingValue = inf;
else
    headingValue = headingAngle(traject(timestamp), traject(timestamp));
end
end

function [attractionValue] = attraction(headingA, headingB)
if headingA == inf
    if headingB == inf
        attractionValue = 1;
    else
        attractionValue = 0;
    end
elseif headingB == inf
    attractionValue = 0;
else
    attractionValue = cos(headingA - headingB);
end
