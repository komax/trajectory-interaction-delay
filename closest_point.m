function [x, y] = closest_point(trajectory1, trajectory2) 

n1 = size(trajectory1,1);
n2 = size(trajectory2,1);
y = zeros(n1,1);
x = 1:n1;
for t = 1:n1
    d = trajectory2(:,1:3) - repmat(trajectory1(t,1:3),n2,1);
    distances = sqrt(sum(d .^ 2, 2));
    idx = find(distances == min(distances));
    y(t) = trajectory1(t,4) - trajectory2(idx,4);


end
