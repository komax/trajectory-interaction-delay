x = linspace(0.0,1.0);
y = linspace(0.0,1.0);

alpha = 3;
displacements = zeros(length(x),length(y));
for i = 1:length(x)
    for j = 1:length(y)
        displacements(i,j) = 1. - (abs(x(i)-y(j))/(x(i)+y(j)))^alpha;
    end
end
mesh(x,y,displacements);

displacementApproxs = zeros(length(x),length(y));
