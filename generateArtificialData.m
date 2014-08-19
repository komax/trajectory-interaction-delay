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
    case 'crossing'
        pointA = [1 0];
        pointB = [0 1];
        for i = 1:lengthTrajectories
            trajectoryA(i,:) = pointA;
            trajectoryB(i,:) = pointB;
            pointA = pointA + [1 1];
            pointB = pointB + [1 1];
        end
        for i = 7:10
            trajectoryB(i,:) = trajectoryA(i,:) + [3 -3];
        end
    case 'separating'
        pointA = [1 0];
        pointB = [0 1];
        for i = 1:lengthTrajectories
            trajectoryA(i,:) = pointA;
            trajectoryB(i,:) = pointB;
            pointA = pointA + [1 1];
            pointB = pointB + [1 1];
        end
        trajectoryA(6,:) = trajectoryA(5,:) + [1 0];
        trajectoryB(5,:) = trajectoryB(4,:) + [-1 0];
        pointA = trajectoryA(6,:) + [2 0];
        pointB = trajectoryB(5,:) + [-2 0];
        for i = 7:10
            trajectoryA(i,:) = pointA;
            trajectoryB(i-1,:) = pointB;
            pointA = pointA + [1 1];
            pointB = pointB + [1 1];
        end
    case 'smalldistance'
        pointA = [1 0];
        pointB = [0 1];
        for i = 1:lengthTrajectories
            trajectoryA(i,:) = pointA;
            trajectoryB(i,:) = pointB;
            pointA = pointA + [1 1];
            pointB = pointB + [1 1];
        end
        commonPoint = trajectoryB(5,:);
        trajectoryB(5,:) = commonPoint + [-0.1 0];
        trajectoryA(5,:) = commonPoint + [+0.1 0];
        trajectoryA(6,:) = commonPoint + [3 0];
        trajectoryB(6,:) = commonPoint + [-10 5];
        pointA = trajectoryA(6,:);
        pointB = trajectoryB(6,:);
        for i = 6:10
            trajectoryA(i,:) = pointA;
            trajectoryB(i,:) = pointB;
            pointA = pointA + [1 1];
            pointB = pointB + [1 1];
        end
    otherwise
        error('Cannot handle this choice');
end
