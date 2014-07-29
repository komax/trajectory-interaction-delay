function [trajectoryA,trajectoryB] = generateArtificialData(dataType)
lengthTrajectories = 10;
trajectoryA = zeros(lengthTrajectories,2);
trajectoryB = zeros(lengthTrajectories,2);
switch dataType
    case 'zickzack'
        pointA = [1 0];
        pointB = [0 1];
        for i = 1:lengthTrajectories
            trajectoryA(i,:) = pointA;
            trajectoryB(i,:) = pointB;
            pointA = pointA + [1 1];
            pointB = pointB + [1 1];
        end
        trajectoryB(10,:) = trajectoryB(9,:);
        trajectoryB(9,:) = trajectoryB(8,:);
        trajectoryB(8,:) = trajectoryB(7,:);
        trajectoryB(7,:) = trajectoryB(6,:) + [-1 0];
    otherwise
        error('Cannot handle this choice');
end
