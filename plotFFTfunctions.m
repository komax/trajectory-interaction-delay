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

complexX = complex(x);
complexY = complex(y);
displacementApproxs = zeros(length(x),length(y));
alpha = 0.1;
for i = 1:length(x)
    for j = 1:length(y)
        displacementApproxs(i,j) = real(exp(log(complexY(j) + alpha) - log(complexX(i) + alpha)* i * pi / 2));
    end
end
mesh(x,y,displacementApproxs);
