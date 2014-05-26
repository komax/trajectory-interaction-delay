function [headingMatrix] = computeHeading(traject1,traject2)
headingMatrix = zeros(length(traject1),length(traject2));

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

