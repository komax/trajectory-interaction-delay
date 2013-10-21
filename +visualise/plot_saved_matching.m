function [distances, delay] = plot_matching(i,j)
load(['precomputed/matching',int2str(i),'_',int2str(j),'.mat']);
% loads results, i, j, smoothed_i, smoothed_j
t1 = smoothed_i;
t2 = smoothed_j;
figure
grid on;
axis equal;
hold on;

plot3(t1(:,1),t1(:,2),t1(:,3),'k');
plot3(t2(:,1),t2(:,2),t2(:,3),'k');
distances = [];
delay = [];
for n = 1:length(results)
    plot3([results(n,1); results(n,6)],[results(n,2); results(n,7)],[results(n,3); results(n,8)],'-')
    p1 = results(n, 1:3);
    p2 = results(n, 6:8);
    d = norm(p1 - p2, 2);
    delay(n) = results(n,4) - results(n,9);
    distances(n) = d;
end
if size(t1,2) >= 4 && size(t2,2) >=4
    scatter3(results(:,1),results(:,2),results(:,3),30,results(:,4),'filled');
    scatter3(results(:,6),results(:,7),results(:,8),30,results(:,9),'filled');
end
