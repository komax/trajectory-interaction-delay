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
%mesh(x,y,displacementsBigAlpha);
contourf(x,y,displacementsBigAlpha);
xlabel('d_p');
ylabel('d_q');
axis square;

% This function does not fit nicely. Drop it
complexX = complex(x);
complexY = complex(y);
displacementApproxs = zeros(length(x),length(y));
beta = 0.15;
for k = 1:length(x)
    for j = 1:length(y)
        fx(k) = 1;
        fy(j) = 1;
        gx(k) = log(complexX(k) + beta);
        gy(j) = log(complexY(j) + beta);
        displacementApproxs(k,j) = real(fx(k)*fy(j)*exp((gy(j)-gx(k))*i*pi/2));
        %displacementApproxs(k,j) = real(exp(log((complexY(j) + alpha) - log(complexX(k) + alpha))* i * pi / 2));
    end
end
figure;
% Use a different color map.
colormap(summer);
%mesh(x,y,displacementApproxs);
contourf(x,y,displacementApproxs);
xlabel('d_p');
ylabel('d_q');
axis square;

displacementApproxs = zeros(length(x),length(y));
for k = 1:length(x)
    for j = 1:length(y)
        fx(k) = complexX(k)^0.1;
        fy(j) = complexY(j)^0.1;
        gx(k) = log(complexX(k)^2 + beta);
        gy(j) = log(complexY(j)^2 + beta);
        displacementApproxs(k,j) = real(fx(k)*fy(j)*exp((gy(j)-gx(k))*i*pi/2));
        %displacementApproxs(k,j) = real(complexY(j)^0.1 * complexX(k)^0.1 * exp((log(complexY(j) + alpha) - log(complexX(k) + alpha)) * i * pi / 2));
    end
end
figure;
% Use a different color map.
colormap(summer);
%mesh(x,y,displacementApproxs);
contourf(x,y,displacementApproxs);
xlabel('d_p');
ylabel('d_q');
axis square;
