function [headingMatrix] = computeHeading(traject1,traject2)
heading = headingAngle([1., 2., 0.],[2., 4., 0.]);
heading
headingMatrix = zeros(length(traject1),length(traject2));
for i = 1:length(traject1)
    for j = 1:length(traject2)
        angleI = headingOnTrajectory(traject1, i);
        angleJ = headingOnTrajectory(traject2, j);
        headingMatrix(i,j) = attraction(angleI, angleJ);
    end
end
headingMatrix = flipdim(headingMatrix, 1);
max(max(headingMatrix))
min(min(headingMatrix))
end

function [angle] = computeAngle(vectorA, vectorB)
angle = atan2(norm(cross(vectorA,vectorB)), dot(vectorA,vectorB));
end

function [headingValue] = headingAngle(p, successorP)
projectedPoint = [successorP(1), p(2:end)];
vectorA = projectedPoint - p;
vectorB = successorP - p;
headingValue = computeAngle(vectorA,vectorB);
end

function [headingValue] = headingOnTrajectory(traject, timestamp)
if timestamp == length(traject)
    headingValue = inf;
else
    headingValue = headingAngle(traject(timestamp, :), traject(timestamp + 1, :));
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
end
