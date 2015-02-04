x = linspace(0.0,1.0);
y = linspace(0.0,1.0);

alpha = 2;
displacements = zeros(length(x),length(y));
for k = 1:length(x)
    for j = 1:length(y)
        displacementsBigAlpha(k,j) = 1. - (abs(x(k)-y(j))/(x(k)+y(j)))^alpha;
    end
end
figure;
% Use a different color map.
colormap(summer);
mesh(x,y,displacementsBigAlpha);

% This function does not fit nicely. Drop it
complexX = complex(x);
complexY = complex(y);
displacementApproxs = zeros(length(x),length(y));
for k = 1:length(x)
    for j = 1:length(y)
        fx(k) = 1;
        fy(j) = 1;
        gx(k) = log(complexX(k) + alpha);
        gy(j) = log(complexY(j) + alpha);
        displacementApproxs(k,j) = real(fx(k)*fy(j)*exp((gy(j)-gx(k))*i*pi/2));
        %displacementApproxs(k,j) = real(exp(log((complexY(j) + alpha) - log(complexX(k) + alpha))* i * pi / 2));
    end
end
figure;
mesh(x,y,displacementApproxs);

displacementApproxs = zeros(length(x),length(y));
for k = 1:length(x)
    for j = 1:length(y)
        fx(k) = complexX(k)^0.1;
        fy(j) = complexY(j)^0.1;
        gx(k) = log(complexX(k)^2 + alpha);
        gy(j) = log(complexY(j)^2 + alpha);
        displacementApproxs(k,j) = real(fx(k)*fy(j)*exp((gy(j)-gx(k))*i*pi/2));
        %displacementApproxs(k,j) = real(complexY(j)^0.1 * complexX(k)^0.1 * exp((log(complexY(j) + alpha) - log(complexX(k) + alpha)) * i * pi / 2));
    end
end
figure;
% Use a different color map.
colormap(summer);
mesh(x,y,displacementApproxs);
