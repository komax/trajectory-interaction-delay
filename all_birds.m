function stuff(birds)
colors = 'bgrcmykw'
subplot(1,2,2);
for i = 1:16
    m = birds{i};
    s = colors(mod(i,8) + 1);
    hold on
    plot3(m(:,1), m(:,2), m(:,3),s)
end

end

