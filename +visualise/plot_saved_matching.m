function [distances, delay] = plot_matching(i,j)
t1 = get_bird(i);
t2 = get_bird(j);
load(['precomputed/filtered_matching',int2str(i),'-',int2str(j),'.mat']);
figure
hold on;
plot3(t1(:,1),t1(:,2),t1(:,3),'k');
plot3(t2(:,1),t2(:,2),t2(:,3),'k');
distances = [];
delay = [];
for n = 1:length(matching)
    i = matching(n,1) + 1;
    j = matching(n,2) + 1;
    plot3([t1(i,1); t2(j,1)],[t1(i,2); t2(j,2)],[t1(i,3); t2(j,3)],'-')
    p1 = t1(i,1:3);
    p2 = t2(j,1:3);
    d = norm(p1 - p2, 2);
    if size(t1, 2) >= 4 && size(t2, 2) >= 4
        delay(n) = t1(i,4) - t2(j,4);
    end
    distances(n) = d;
end
if size(t1,2) >= 4 && size(t2,2) >=4
    scatter3(t1(:,1),t1(:,2),t1(:,3),30,t1(:,4),'filled');
    scatter3(t2(:,1),t2(:,2),t2(:,3),30,t2(:,4),'filled');
end