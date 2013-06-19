function plot_matching(t1,t2, matching)
figure
hold on;
plot3(t1(:,1),t1(:,2),t1(:,3),'k');
plot3(t2(:,1),t2(:,2),t2(:,3),'k');

for n = 1:length(matching)
    i = matching(n,1) + 1;
    j = matching(n,2) + 1;
    plot3([t1(i,1); t2(i,1)],[t1(i,2); t2(i,2)],[t1(i,3); t2(i,3)],'-')
end

scatter3(t1(:,1),t1(:,2),t1(:,3),30,t1(:,4),'filled');
scatter3(t2(:,1),t2(:,2),t2(:,3),30,t2(:,4),'filled');
